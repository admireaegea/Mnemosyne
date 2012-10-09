package edu.american.student.mnemosyne.core;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.accumulo.core.client.mapreduce.AccumuloInputFormat;
import org.apache.accumulo.core.client.mapreduce.AccumuloOutputFormat;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.american.student.mnemosyne.conf.HadoopJobConfiguration;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.HadoopForeman;

public class HadoopForemanTest
{

	private static long time = System.currentTimeMillis();
	private static AccumuloForeman aForeman = new AccumuloForeman();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{

		aForeman.connect();
		aForeman.makeTable(time + "");
		aForeman.add(time + "", "ROW", "FAM", "QUAL", "VALUE");

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		aForeman.deleteTable(time + "");
	}

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void hadoopForemanTest() throws IOException, InterruptedException, ClassNotFoundException
	{
		HadoopJobConfiguration hConfig = new HadoopJobConfiguration();
		hConfig.setJobName("Hadoop Test");

		hConfig.setMapperClass(MyMapper.class);
		hConfig.setInputFormatClass(AccumuloInputFormat.class);
		hConfig.setOutputFormatClass(AccumuloOutputFormat.class);

		hConfig.overrideDefaultTable(time + "");

		HadoopForeman hForeman = new HadoopForeman();
		assertTrue(hForeman.runJob(hConfig));
	}

	public static class MyMapper extends Mapper<Key, Value, Writable, Writable>
	{
		@Override
		public void map(Key ik, Value iv, Context context)
		{
			System.out.println(ik.toString() + " " + iv.toString());

		}
	}

}
