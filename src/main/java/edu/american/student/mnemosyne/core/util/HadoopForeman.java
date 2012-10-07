package edu.american.student.mnemosyne.core.util;

import java.io.IOException;

import org.apache.accumulo.core.client.mapreduce.AccumuloInputFormat;
import org.apache.accumulo.core.client.mapreduce.AccumuloOutputFormat;
import org.apache.accumulo.core.security.Authorizations;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;

import edu.american.student.mnemosyne.conf.HadoopJobConfiguration;

public class HadoopForeman
{

	public Job getHadoopJob(HadoopJobConfiguration conf) throws IOException, InterruptedException, ClassNotFoundException
	{
		Job job = new Job();
		job.setJobName(conf.getJobName());


		job.setMapperClass(conf.getMapperClass());
		job.setInputFormatClass(conf.getInputFormatClass());
		job.setOutputFormatClass(conf.getOutputFormatClass());
		job.setOutputKeyClass(conf.getOutputKeyClass());

		job.setOutputValueClass(conf.getOutputValueClass());
		job.setNumReduceTasks(0);
		Configuration conf1 = job.getConfiguration();
		if(conf.getInputFormatClass() == AccumuloInputFormat.class)
		{
			AccumuloInputFormat.setInputInfo(conf1, MnemosyneConstants.getAccumuloUser(), MnemosyneConstants.getAccumuloPassword().getBytes(),  MnemosyneConstants.getDefaultTable(), new Authorizations());
			AccumuloInputFormat.setZooKeeperInstance(conf1,MnemosyneConstants.getZookeeperInstanceName(), MnemosyneConstants.getZookeeperInstance());
		
		}
		if(conf.getOutputFormatClass() == AccumuloOutputFormat.class)
		{
			AccumuloOutputFormat.setOutputInfo(conf1, MnemosyneConstants.getAccumuloUser(), MnemosyneConstants.getAccumuloPassword().getBytes(), true, MnemosyneConstants.getDefaultTable());
			AccumuloOutputFormat.setZooKeeperInstance(conf1,MnemosyneConstants.getZookeeperInstanceName(), MnemosyneConstants.getZookeeperInstance());
			
		}
		return job;
	}

	public void runJob(HadoopJobConfiguration hConfig) throws IOException, InterruptedException, ClassNotFoundException
	{
		this.getHadoopJob(hConfig).waitForCompletion(true);
		
	}
}
