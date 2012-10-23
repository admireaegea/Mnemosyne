package edu.american.student.mnemosyne.core.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.accumulo.core.client.mapreduce.AccumuloInputFormat;
import org.apache.accumulo.core.client.mapreduce.AccumuloOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

import edu.american.student.mnemosyne.conf.HadoopJobConfiguration;
import edu.american.student.mnemosyne.core.exception.HadoopException;

public class HadoopForeman
{
	private final static Logger log = Logger.getLogger(HadoopForeman.class.getName());

	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	public Job getHadoopJob(HadoopJobConfiguration conf) throws HadoopException
	{
		Job job;
		try
		{
			job = new Job();
			job.setJobName(conf.getJobName());

			job.setMapperClass(conf.getMapperClass());
			job.setInputFormatClass((Class<? extends InputFormat>) conf.getInputFormatClass());

			if (conf.getOutputFormatClass() != null)
			{
				job.setOutputFormatClass((Class<? extends OutputFormat>) conf.getOutputFormatClass());
			}
			if (conf.getOutputKeyClass() != null)
			{
				job.setOutputKeyClass(conf.getOutputKeyClass());
			}
			if (conf.getOutputValueClass() != null)
			{
				job.setOutputValueClass(conf.getOutputValueClass());
			}
			if (conf.getReducerClass() != null)
			{
				job.setReducerClass(conf.getReducerClass());
			}

			job.setNumReduceTasks(conf.getNumReduceTasks());
			Configuration conf1 = job.getConfiguration();
			if (conf.getInputFormatClass() == AccumuloInputFormat.class)
			{
				AccumuloInputFormat.setInputInfo(conf1, MnemosyneConstants.getAccumuloUser(), MnemosyneConstants.getAccumuloPassword().getBytes(), conf.getDefaultTable(), conf.getDefaultAuths());
				AccumuloInputFormat.setZooKeeperInstance(conf1, MnemosyneConstants.getZookeeperInstanceName(), MnemosyneConstants.getZookeeperInstance());

			}
			if (conf.getFetchColumns() != null)
			{
				AccumuloInputFormat.fetchColumns(conf1, conf.getFetchColumns());
			}
			else if (conf.getInputFormatClass() == TextInputFormat.class)
			{
				if (conf.getPathToProcess() != null)
				{
					FileInputFormat.setInputPaths(job, conf.getPathToProcess());
				}
			}
			if (conf.getOutputFormatClass() == AccumuloOutputFormat.class)
			{
				AccumuloOutputFormat.setOutputInfo(conf1, MnemosyneConstants.getAccumuloUser(), MnemosyneConstants.getAccumuloPassword().getBytes(), true, conf.getDefaultTable());
				AccumuloOutputFormat.setZooKeeperInstance(conf1, MnemosyneConstants.getZookeeperInstanceName(), MnemosyneConstants.getZookeeperInstance());

			}
			return job;

		}
		catch (IOException e)
		{
			String gripe = "Could not configure a Hadoop job";
			log.log(Level.SEVERE, gripe, e);
			throw new HadoopException(gripe, e);

		}

	}

	public boolean runJob(HadoopJobConfiguration hConfig) throws HadoopException
	{
		try
		{
			return this.getHadoopJob(hConfig).waitForCompletion(true);
		}
		catch (IOException e)
		{
			String gripe = "Could not start a Hadoop job";
			log.log(Level.SEVERE, gripe, e);
			throw new HadoopException(gripe, e);
		}
		catch (InterruptedException e)
		{
			String gripe = "Could not start a Hadoop job";
			log.log(Level.SEVERE, gripe, e);
			throw new HadoopException(gripe, e);
		}
		catch (ClassNotFoundException e)
		{
			String gripe = "Could not start a Hadoop job";
			log.log(Level.SEVERE, gripe, e);
			throw new HadoopException(gripe, e);
		}

	}
}
