package edu.american.student.mnemosyne.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.accumulo.core.client.mapreduce.AccumuloInputFormat;
import org.apache.accumulo.core.client.mapreduce.AccumuloOutputFormat;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.util.Pair;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.ContainsFlat;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import edu.american.student.mnemosyne.conf.ClassificationNetworkConf;
import edu.american.student.mnemosyne.conf.HadoopJobConfiguration;
import edu.american.student.mnemosyne.core.framework.MnemosyneProcess;
import edu.american.student.mnemosyne.core.model.Artifact;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.ClassificationNetwork;
import edu.american.student.mnemosyne.core.util.HadoopForeman;
import edu.american.student.mnemosyne.core.util.NNInput;
import edu.american.student.mnemosyne.core.util.NNOutput;

public class TrainProcess implements MnemosyneProcess
{

	public void process() throws Exception
	{
		artifactForeman.connect();
		List<Artifact> artifacts = artifactForeman.returnArtifacts();
		for (Artifact artifact : artifacts)
		{
			HadoopForeman hForeman = new HadoopForeman();
			HadoopJobConfiguration conf = new HadoopJobConfiguration();
			conf.setJobName(HadoopJobConfiguration.buildJobName(this.getClass()));
			conf.setMapperClass(NNTrainMapper.class);
			conf.overrideDefaultTable(AccumuloForeman.getArtifactRepositoryName());
			Collection<Pair<Text, Text>> cfPairs = new ArrayList<Pair<Text, Text>>();
			cfPairs.add(new Pair<Text, Text>(new Text(artifact.getArtifactId() + ":FIELD"), null));
			conf.fetchColumns(cfPairs);
			conf.setInputFormatClass(AccumuloInputFormat.class);
			conf.setOutputFormatClass(AccumuloOutputFormat.class);
			hForeman.runJob(conf);
		}
	}

	public static class NNTrainMapper extends Mapper<Key, Value, Writable, Writable>
	{
		private AccumuloForeman aForeman = new AccumuloForeman();

		@Override
		public void map(Key ik, Value iv, Context context)
		{
			aForeman.connect();
			System.out.println("Grabbing the base network...");
			BasicNetwork base = null;
			ClassificationNetworkConf baseConf = null;
			long error = 1;
			try
			{
				base = aForeman.getBaseNetwork(ik.getRow().toString());
				baseConf = aForeman.getBaseNetworkConf(ik.getRow().toString());
				error = aForeman.getBaseNetworkError(ik.getRow().toString());
			}
			catch (Exception e)
			{
			}
			if (base != null)
			{
				// train shit
				System.out.println("Training ...");
				double[] input = NNInput.inflate(iv.toString());
				double[] output = NNOutput.inflate(iv.toString());
				MLDataSet trainingSet = new BasicMLDataSet(new double[][]{ input }, new double[][]{ output });
				final ResilientPropagation train = new ResilientPropagation(ClassificationNetwork.addLayerToNetwork(base,baseConf, new BasicLayer(new ActivationSigmoid(),true,baseConf.getNumberOfCategories() )), trainingSet);
				int epoch = 1;
				try
				{

					do
					{
						train.iteration();
						System.out.println("Epoch #" + epoch + " Error:" + train.getError());
						epoch++;
					}
					while (train.getError() > error * .000000000000000000001);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}

		}
	}

	public void setup() throws Exception
	{
		// TODO Auto-generated method stub

	}

}
