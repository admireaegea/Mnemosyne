package edu.american.student.mnemosyne.core;

import java.io.IOException;

import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.client.mapreduce.AccumuloInputFormat;
import org.apache.accumulo.core.client.mapreduce.AccumuloOutputFormat;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;

import edu.american.student.mnemosyne.conf.HadoopJobConfiguration;
import edu.american.student.mnemosyne.core.util.HadoopForeman;

public class HadoopTest
{

	public static void main(String[] args) throws IOException, TableNotFoundException, ClassNotFoundException, InterruptedException
	{
		HadoopJobConfiguration hConfig = new HadoopJobConfiguration();
		hConfig.setJobName("Hadoop Test");

		hConfig.setMapperClass(MyMapper.class);
		hConfig.setInputFormatClass(AccumuloInputFormat.class);
		hConfig.setOutputFormatClass(AccumuloOutputFormat.class);
		hConfig.setOutputKeyClass(WritableComparable.class);

		hConfig.setOutputValueCLass(Writable.class);
		hConfig.setNumReduceTasks(0);
		
		HadoopForeman hForeman = new HadoopForeman();
		hForeman.runJob(hConfig);
		
	}

	public static class MyMapper extends Mapper<Key,Value,WritableComparable,Writable>
	{
		@Override
		public void map(Key ik, Value iv, Context context)
		{
			System.out.println(ik.toString() + " " + iv.toString());
		}
	}
}
