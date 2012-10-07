package edu.american.student.mnemosyne.core;

import java.io.IOException;

import org.apache.accumulo.core.client.mapreduce.AccumuloInputFormat;
import org.apache.accumulo.core.client.mapreduce.AccumuloOutputFormat;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.encog.neural.networks.BasicNetwork;

import edu.american.student.mnemosyne.conf.HadoopJobConfiguration;
import edu.american.student.mnemosyne.core.framework.MnemosyneProcess;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.HadoopForeman;

public class TrainProcess implements MnemosyneProcess
{

	public void process() throws IOException, InterruptedException, ClassNotFoundException
	{
		HadoopForeman hForeman = new HadoopForeman();
		HadoopJobConfiguration conf = new HadoopJobConfiguration();
		conf.setJobName("TRAIN PROCESS");
		conf.setMapperClass(MyMapper.class);
		conf.overrideDefaultTable("ARTIFACT_TABLE");
		conf.setInputFormatClass(AccumuloInputFormat.class);
		conf.setOutputFormatClass(AccumuloOutputFormat.class);
		hForeman.runJob(conf);
	}
	
	public static class MyMapper extends Mapper<Key, Value, Writable, Writable>
	{
		private AccumuloForeman aForeman = new AccumuloForeman();
		@Override
		public void map(Key ik, Value iv, Context context)
		{
			aForeman.connect();
			System.out.println(ik.toString() + " " + iv.toString());
			System.out.println("Grabbing the base network...");
			BasicNetwork base = null;
			try
			{
				base = aForeman.getBaseNetwork();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(base != null)
			{
				//train shit
				System.out.println("Training ...");
			}
			
		}
	}

	
}
