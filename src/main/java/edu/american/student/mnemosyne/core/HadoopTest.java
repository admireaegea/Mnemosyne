package edu.american.student.mnemosyne.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.client.mapreduce.AccumuloInputFormat;
import org.apache.accumulo.core.client.mapreduce.AccumuloOutputFormat;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.security.Authorizations;
import org.apache.accumulo.core.util.Pair;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.encog.engine.network.activation.ActivationSigmoid;

import edu.american.student.mnemosyne.conf.ClassificationNetworkConf;
import edu.american.student.mnemosyne.core.framework.NNProcessor;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.MnemosyneConstants;
import edu.american.student.mnemosyne.core.util.NNProcessorBeanFactory;

public class HadoopTest
{

	public static void main(String[] args) throws IOException, TableNotFoundException, ClassNotFoundException, InterruptedException
	{
		// construct a base NN
		//
		Job job = new Job();
		job.setJarByClass(HadoopTest.class);
		job.setJobName("Hadoop Test");

		AccumuloForeman aForeman = new AccumuloForeman();
		aForeman.connect();
		aForeman.deleteTables();
		int inputNeuronCount = 2;
		int num = 4;

		ClassificationNetworkConf config = new ClassificationNetworkConf();
		config.setInputActivation(null);
		config.setInputBias(true);
		config.setInputNeuronCount(inputNeuronCount);

		config.setHiddenActiviation(new ActivationSigmoid());
		config.setHiddenBias(true);
		config.setHiddenNeuronCount(2 ^ num);

		config.setOutputActivation(new ActivationSigmoid());
		config.setOutputNeuronCount(2);

		config.setNumberOfCategories(num);

		NNProcessor processor = NNProcessorBeanFactory.getProcessorBean(config);
		processor.constructNetworks();
		// ClassificationNetworkConf:0
		Collection<Pair<Text, Text>> cfcqPairs = new ArrayList<Pair<Text, Text>>();
		cfcqPairs.add(new Pair<Text, Text>(new Text("ClassificationNetworkConf"), new Text("0")));

		job.setMapperClass(MyMapper.class);
		job.setInputFormatClass(AccumuloInputFormat.class);
		job.setOutputFormatClass(AccumuloOutputFormat.class);
		job.setOutputKeyClass(NullWritable.class);

		job.setOutputValueClass(NullWritable.class);
		job.setNumReduceTasks(0);
		job.setJarByClass(HadoopTest.class);

		Configuration conf = job.getConfiguration();
	
		aForeman.makeTable("table");
		AccumuloInputFormat.setInputInfo(conf, MnemosyneConstants.getAccumuloUser(), MnemosyneConstants.getAccumuloPassword().getBytes(), "table", new Authorizations("public"));
		//AccumuloInputFormat.setZooKeeperInstance(conf, MnemosyneConstants.getZookeeperInstance(), "localhost");
		AccumuloOutputFormat.setOutputInfo(conf, MnemosyneConstants.getAccumuloUser(), MnemosyneConstants.getAccumuloPassword().getBytes(), true, "table");
		AccumuloOutputFormat.setZooKeeperInstance(conf,MnemosyneConstants.getZookeeperInstance(), "localhost");
		AccumuloInputFormat.fetchColumns(conf, cfcqPairs);
		job.waitForCompletion(true);
	}

	class MyMapper extends Mapper<Key, Value, NullWritable, NullWritable>
	{
		@Override
		public void map(Key ik, Value iv, Context context)
		{
			System.out.println(ik.toString() + " " + iv.toString());
		}
	}
}
