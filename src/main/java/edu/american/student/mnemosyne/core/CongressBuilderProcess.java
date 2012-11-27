package edu.american.student.mnemosyne.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import org.apache.accumulo.core.client.mapreduce.AccumuloInputFormat;
import org.apache.accumulo.core.client.mapreduce.AccumuloOutputFormat;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.util.Pair;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

import edu.american.student.mnemosyne.conf.CongressNetworkConf;
import edu.american.student.mnemosyne.conf.HadoopJobConfiguration;
import edu.american.student.mnemosyne.core.exception.ProcessException;
import edu.american.student.mnemosyne.core.exception.RepositoryException;
import edu.american.student.mnemosyne.core.exception.StopMapperException;
import edu.american.student.mnemosyne.core.framework.MnemosyneProcess;
import edu.american.student.mnemosyne.core.framework.NNProcessor;
import edu.american.student.mnemosyne.core.model.Artifact;
import edu.american.student.mnemosyne.core.model.NNMetadata;
import edu.american.student.mnemosyne.core.util.BinaryUtils;
import edu.american.student.mnemosyne.core.util.factory.NNProcessorFactory;
import edu.american.student.mnemosyne.core.util.foreman.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.foreman.HadoopForeman;

public class CongressBuilderProcess implements MnemosyneProcess
{

	public void process() throws ProcessException
	{
		List<Artifact> artifacts = artifactForeman.returnArtifacts();
		for (Artifact artifact : artifacts)
		{
			HadoopForeman hForeman = new HadoopForeman();
			HadoopJobConfiguration conf = new HadoopJobConfiguration();
			conf.setJobName(HadoopJobConfiguration.buildJobName(CongressBuilderProcess.class));
			conf.setMapperClass(CongressBuilderMapper.class);
			conf.overrideDefaultTable(AccumuloForeman.getArtifactRepositoryName());
			Collection<Pair<Text, Text>> cfPairs = new ArrayList<Pair<Text, Text>>();
			cfPairs.add(new Pair<Text, Text>(new Text(artifact.getArtifactId()), null));
			conf.setFetchColumns(cfPairs);
			conf.setInputFormatClass(AccumuloInputFormat.class);
			conf.setOutputFormatClass(AccumuloOutputFormat.class);
			hForeman.runJob(conf);
		}

	}

	public void setup() throws ProcessException
	{
		artifactForeman.connect();

	}

	public static class CongressBuilderMapper extends Mapper<Key, Value, Writable, Writable>
	{
		@Override
		public void map(Key ik, Value iv, Context context)
		{
			NNMetadata metadata = NNMetadata.inflate(iv.toString(), ik.getRow().toString());
			int inputNeuronCount = BinaryUtils.toBinary(metadata.getInputMax(),new double[]{ metadata.getInputMax()},true).length;
			int outputNeuronCount = BinaryUtils.toBinary(metadata.getOutputMax(),new double[]{ metadata.getOutputMax() },false).length;
			int categories = metadata.getOutputNameFields().size();
			
			//create a couple hundred neurons
			CongressNetworkConf conf = new CongressNetworkConf();
			try
			{
				NNProcessor processor = NNProcessorFactory.getProcessorBean(conf);
				processor.constructNetworks(metadata.getArtifactId());
			}
			catch (RepositoryException e)
			{
				String gripe = "Access to the Repository Services died";
//				log.log(Level.SEVERE,gripe,e);
				throw new StopMapperException(gripe,e);
			}
		}
	}

}
