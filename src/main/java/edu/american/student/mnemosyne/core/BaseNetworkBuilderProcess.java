package edu.american.student.mnemosyne.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.accumulo.core.client.TableNotFoundException;
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
import edu.american.student.mnemosyne.core.framework.MnemosyneProcess;
import edu.american.student.mnemosyne.core.framework.NNProcessor;
import edu.american.student.mnemosyne.core.model.Artifact;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.HadoopForeman;
import edu.american.student.mnemosyne.core.util.NNMetadata;
import edu.american.student.mnemosyne.core.util.NNProcessorFactory;

public class BaseNetworkBuilderProcess implements MnemosyneProcess
{

	public void process() throws Exception
	{
		List<Artifact> artifacts = artifactForeman.returnArtifacts();
		for(Artifact artifact: artifacts)
		{
			HadoopForeman hForeman = new HadoopForeman();
			HadoopJobConfiguration conf = new HadoopJobConfiguration();
			conf.setJobName(HadoopJobConfiguration.buildJobName(BaseNetworkBuilderProcess.class));
			conf.setMapperClass(BaseNetworkBuilderMapper.class);
			conf.overrideDefaultTable(AccumuloForeman.getArtifactRepositoryName());
			Collection<Pair<Text,Text>> cfPairs = new ArrayList<Pair<Text,Text>>();
			cfPairs.add(new Pair<Text,Text>(new Text(artifact.getArtifactId()),null));
			conf.fetchColumns(cfPairs);
			conf.setInputFormatClass(AccumuloInputFormat.class);
			conf.setOutputFormatClass(AccumuloOutputFormat.class);
			hForeman.runJob(conf);
		}
		
	}

	public void setup() throws Exception
	{
		artifactForeman.connect();
		
	}
	
	public static class BaseNetworkBuilderMapper extends Mapper<Key, Value, Writable, Writable>
	{
		@Override
		public void map(Key ik, Value iv, Context context)
		{
			NNMetadata metadata = NNMetadata.inflate(iv.toString(),ik.getRow().toString());
			int inputNeuronCount =metadata.getInputNameFields().size();
			int num = metadata.getOutputNameFields().size();
			
			ClassificationNetworkConf conf = new ClassificationNetworkConf();
			conf.setInputActivation(null);
			conf.setInputBias(true);
			conf.setInputNeuronCount(inputNeuronCount);
			
			conf.setHiddenActiviation(new ActivationSigmoid());
			conf.setHiddenBias(true);
			conf.setHiddenNeuronCount(2^num);
			
			conf.setOutputActivation(new ActivationSigmoid());
			conf.setOutputNeuronCount(num);
			
			conf.setNumberOfCategories(num);
			NNProcessor processor = NNProcessorFactory.getProcessorBean(conf);
			try
			{
				processor.constructNetworks(metadata.getArtifactId());
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (TableNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
