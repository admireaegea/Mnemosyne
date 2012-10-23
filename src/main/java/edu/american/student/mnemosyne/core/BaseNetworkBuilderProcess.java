package edu.american.student.mnemosyne.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.accumulo.core.client.mapreduce.AccumuloInputFormat;
import org.apache.accumulo.core.client.mapreduce.AccumuloOutputFormat;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.util.Pair;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.encog.engine.network.activation.ActivationSigmoid;

import edu.american.student.mnemosyne.conf.ClassificationNetworkConf;
import edu.american.student.mnemosyne.conf.HadoopJobConfiguration;
import edu.american.student.mnemosyne.core.exception.ArtifactException;
import edu.american.student.mnemosyne.core.exception.ProcessException;
import edu.american.student.mnemosyne.core.exception.RepositoryException;
import edu.american.student.mnemosyne.core.framework.MnemosyneProcess;
import edu.american.student.mnemosyne.core.framework.NNProcessor;
import edu.american.student.mnemosyne.core.model.Artifact;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.BinaryUtils;
import edu.american.student.mnemosyne.core.util.HadoopForeman;
import edu.american.student.mnemosyne.core.util.NNMetadata;
import edu.american.student.mnemosyne.core.util.NNProcessorFactory;

public class BaseNetworkBuilderProcess implements MnemosyneProcess
{

	public void process() throws ProcessException
	{
		List<Artifact> artifacts = artifactForeman.returnArtifacts();
		for (Artifact artifact : artifacts)
		{
			HadoopForeman hForeman = new HadoopForeman();
			HadoopJobConfiguration conf = new HadoopJobConfiguration();
			conf.setJobName(HadoopJobConfiguration.buildJobName(BaseNetworkBuilderProcess.class));
			conf.setMapperClass(BaseNetworkBuilderMapper.class);
			conf.overrideDefaultTable(AccumuloForeman.getArtifactRepositoryName());
			Collection<Pair<Text, Text>> cfPairs = new ArrayList<Pair<Text, Text>>();
			cfPairs.add(new Pair<Text, Text>(new Text(artifact.getArtifactId()), null));
			conf.fetchColumns(cfPairs);
			conf.setInputFormatClass(AccumuloInputFormat.class);
			conf.setOutputFormatClass(AccumuloOutputFormat.class);
			hForeman.runJob(conf);
		}

	}

	public void setup() throws ArtifactException
	{
		artifactForeman.connect();

	}

	public static class BaseNetworkBuilderMapper extends Mapper<Key, Value, Writable, Writable>
	{
		@Override
		public void map(Key ik, Value iv, Context context)
		{
			NNMetadata metadata = NNMetadata.inflate(iv.toString(), ik.getRow().toString());
			int inputNeuronCount = BinaryUtils.toBinary(new double[]
			{ metadata.getInputMax() }).length;
			int num = BinaryUtils.toBinary(new double[]
			{ metadata.getOutputMax() }).length;

			ClassificationNetworkConf conf = new ClassificationNetworkConf();
			conf.setInputActivation(null);
			conf.setInputBias(true);
			conf.setInputNeuronCount(inputNeuronCount);

			conf.setHiddenActiviation(new ActivationSigmoid());
			conf.setHiddenBias(true);
			conf.setHiddenNeuronCount(2 ^ num);

			conf.setOutputActivation(new ActivationSigmoid());
			conf.setOutputNeuronCount(num);

			conf.setNumberOfCategories(num);
			conf.setBasicMLInput(getRandomArray(inputNeuronCount));
			conf.setBasicIdealMLOutput(getRandomArray(inputNeuronCount));

			try
			{
				NNProcessor processor = NNProcessorFactory.getProcessorBean(conf);
				processor.constructNetworks(metadata.getArtifactId());
			}
			catch (RepositoryException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		private double[][] getRandomArray(int inputNeuronCount)
		{
			double[][] toReturn = new double[1][inputNeuronCount];
			for (int i = 0; i < inputNeuronCount; i++)
			{
				toReturn[0][i] = new Random().nextDouble();
			}
			return toReturn;
		}

	}

}
