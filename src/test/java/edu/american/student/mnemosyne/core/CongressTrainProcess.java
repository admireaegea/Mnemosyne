package edu.american.student.mnemosyne.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.accumulo.core.client.mapreduce.AccumuloInputFormat;
import org.apache.accumulo.core.client.mapreduce.AccumuloOutputFormat;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.util.Pair;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;

import edu.american.student.mnemosyne.conf.HadoopJobConfiguration;
import edu.american.student.mnemosyne.core.exception.ProcessException;
import edu.american.student.mnemosyne.core.framework.MnemosyneProcess;
import edu.american.student.mnemosyne.core.model.Artifact;
import edu.american.student.mnemosyne.core.model.InputSet;
import edu.american.student.mnemosyne.core.model.Neuron;
import edu.american.student.mnemosyne.core.model.OutputSet;
import edu.american.student.mnemosyne.core.util.foreman.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.foreman.HadoopForeman;
import edu.american.student.mnemosyne.core.util.foreman.TrainForeman;

public class CongressTrainProcess implements MnemosyneProcess
{

	public void process() throws ProcessException
	{
		artifactForeman.connect();
		List<Artifact> artifacts = artifactForeman.returnArtifacts();
		for (Artifact artifact : artifacts)
		{
			HadoopForeman hForeman = new HadoopForeman();
			HadoopJobConfiguration conf = new HadoopJobConfiguration();
			conf.setJobName(HadoopJobConfiguration.buildJobName(this.getClass()));
			conf.setMapperClass(CongressTrainMapper.class);
			conf.overrideDefaultTable(AccumuloForeman.getArtifactRepositoryName());
			Collection<Pair<Text, Text>> cfPairs = new ArrayList<Pair<Text, Text>>();
			cfPairs.add(new Pair<Text, Text>(new Text(artifact.getArtifactId() + ":FIELD"), null));
			conf.setFetchColumns(cfPairs);
			conf.setInputFormatClass(AccumuloInputFormat.class);
			conf.setOutputFormatClass(AccumuloOutputFormat.class);
			hForeman.runJob(conf);
		}
	}

	public void setup() throws ProcessException
	{
		// TODO Auto-generated method stub

	}

	
	public static class CongressTrainMapper extends Mapper<Key, Value, Writable, Writable>
	{
		private static final Logger log = Logger.getLogger(CongressTrainMapper.class.getName());
		private AccumuloForeman aForeman = new AccumuloForeman();
		private TrainForeman tForeman = new TrainForeman();
		@Override
		public void map(Key ik, Value iv, Context context)
		{
			try
			{
				aForeman.connect();
				tForeman.connect();
				
				log.log(Level.INFO, "Grabbing a random neuron...");
				String artifactId = ik.getRow().toString();
				Neuron toTrain = aForeman.getATrainableNeuron(artifactId);
				log.log(Level.INFO,"FOUND THIS ONE"+toTrain.getHash());
				aForeman.setNeuronAvailable(artifactId, toTrain);
				//InputSet<Integer> is = null;
				//OutputSet<Integer> os = null;
				//double aerror = .5;
				//toTrain.train(is, os, aerror);
				//put it all back in
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
