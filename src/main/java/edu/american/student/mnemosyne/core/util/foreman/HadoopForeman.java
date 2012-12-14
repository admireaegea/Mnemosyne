/*
 * Copyright 2012 Cameron Cook Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package edu.american.student.mnemosyne.core.util.foreman;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.accumulo.core.client.mapreduce.AccumuloInputFormat;
import org.apache.accumulo.core.client.mapreduce.AccumuloOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import edu.american.student.mnemosyne.conf.HadoopJobConfiguration;
import edu.american.student.mnemosyne.core.exception.HadoopException;
import edu.american.student.mnemosyne.core.util.MnemosyneConstants;

public class HadoopForeman extends Configured implements Tool
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
			DistributedCache.setCacheArchives(new URI[]{new URI("/cache/accumulo-core-1.4.1.jar"),
					new URI("/cache/accumulo-server-1.4.1.jar"),new URI("/cache/accumulo-start-1.4.1.jar"),
							new URI("/cache/cloudtrace-1.4.1.jar"), new URI("/cache/commons-collections-3.2.jar"),
							new URI("/cache/commons-configuration-1.5.jar"), new URI("/cache/commons-io-1.4.jar"),
							new URI("/cache/commons-jci-core-1.0.jar"), new URI("/cache/commons-jci-fam-1.0.jar"),
							new URI("/cache/commons-lang-2.4.jar"), new URI("/cache/commons-logging-1.0.4.jar"),
							new URI("/cache/commons-logging-api-1.0.4.jar"), new URI("/cache/jline-0.9.94.jar"),
							new URI("/cache/libthrift-0.6.1.jar"), new URI("/cache/log4j-1.2.16.jar")}, job.getConfiguration());
			job.setJobName(conf.getJobName());
			System.out.println("Setting jar class "+conf.getJarClass());
			((JobConf) job.getConfiguration()).setJar("/opt/mnemosyne.jar");
			job.setJarByClass(conf.getJarClass());
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
		catch (URISyntaxException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	private static HadoopJobConfiguration hConfig;

	public boolean runJob(HadoopJobConfiguration hConfig) throws HadoopException
	{
		this.hConfig = hConfig;
		try
		{
			ToolRunner.run(this, new String[] {});
		}
		catch (Exception e)
		{
			String gripe = "Could not start a Hadoop job";
			log.log(Level.SEVERE, gripe, e);
			throw new HadoopException(gripe, e);
		}
		return true;

	}

	public int run(String[] arg0) throws Exception
	{
		this.getHadoopJob(hConfig).waitForCompletion(true);
		return 0;
	}



}
