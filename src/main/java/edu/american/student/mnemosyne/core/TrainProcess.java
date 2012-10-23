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
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import edu.american.student.mnemosyne.conf.ClassificationNetworkConf;
import edu.american.student.mnemosyne.conf.HadoopJobConfiguration;
import edu.american.student.mnemosyne.core.exception.DataspaceException;
import edu.american.student.mnemosyne.core.framework.MnemosyneProcess;
import edu.american.student.mnemosyne.core.model.Artifact;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.ClassificationNetwork;
import edu.american.student.mnemosyne.core.util.HadoopForeman;
import edu.american.student.mnemosyne.core.util.NNInput;
import edu.american.student.mnemosyne.core.util.NNOutput;

public class TrainProcess implements MnemosyneProcess
{

	private static int round = 0;

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
			try
			{
				aForeman.connect();
			}
			catch (DataspaceException e3)
			{
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			System.out.println("Grabbing the base network...");
			BasicNetwork base = null;
			ClassificationNetworkConf baseConf = null;
			double error = 1;
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
				System.out.println("Training ...");
				long start = System.currentTimeMillis();
				long timeout = 1000 * 60;
				double[] input = NNInput.inflate(iv.toString());
				double[] output = NNOutput.inflate(iv.toString());
				MLDataSet trainingSet = null;

				try
				{
					trainingSet = constructTrainingSet(ik.getRow().toString(), input, output);
					BasicNetwork modified = ClassificationNetwork.addLayerToNetwork(base, baseConf, new BasicLayer(new ActivationSigmoid(), true, baseConf.getNumberOfCategories()));
					final ResilientPropagation train = new ResilientPropagation(modified, trainingSet);
					int epoch = 1;

					error = aForeman.getBaseNetworkError(ik.getRow().toString());

					long elapsed = System.currentTimeMillis() - start;
					do
					{
						train.iteration();
						elapsed = System.currentTimeMillis() - start;
						System.out.println("Round:" + round + " Epoch #" + epoch + " Error:" + train.getError() + " acceptable error:" + error + " Elapsed:" + elapsed + " Timeout:" + (elapsed > timeout));
						epoch++;
					}
					while (train.getError() > error && (elapsed < timeout));
					round++;
					start = System.currentTimeMillis();

					aForeman.assertBaseNetwork(modified, ik.getRow().toString(), baseConf);
					aForeman.assertBaseNetworkError(train.getError(), ik.getRow().toString());
				}
				catch (DataspaceException e2)
				{
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

			}

		}

		private MLDataSet constructTrainingSet(String artifactId, double[] input, double[] output) throws DataspaceException
		{

			ArrayList<double[][]> formerIn = aForeman.getPastTrainingInput(artifactId);
			ArrayList<double[][]> formerOut = aForeman.getPastTrainingOutput(artifactId);
			aForeman.addTrainingData(artifactId, new double[][]
			{ input }, new double[][]
			{ output });
			double[][] newIn = new double[formerIn.size() + 1][input.length];
			double[][] newOut = new double[formerOut.size() + 1][output.length];
			for (double[][] in : formerIn)
			{
				for (int i = 0; i < in.length; i++)
				{
					newIn[i] = in[i];
				}
			}
			newIn[newIn.length - 1] = input;

			for (double[][] out : formerOut)
			{
				for (int i = 0; i < out.length; i++)
				{
					newOut[i] = out[i];
				}
			}
			newOut[newOut.length - 1] = output;
			// System.out.println(input.length+" "+newIn.length);
			// fill in new arrays
			MLDataSet trainingSet = new BasicMLDataSet(newIn, newOut);
			return trainingSet;
		}
	}

	static double aerror;

	public void setup() throws Exception
	{
		aerror = .000005;

	}

}
