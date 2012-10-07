package edu.american.student.mnemosyne.core;

import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;

public class HadoopProcessor
{
	
	public static class MyMapper extends Mapper<Key, Value, Writable, Writable>
	{
		@Override
		public void map(Key ik, Value iv, Context context)
		{
			System.out.println(ik.toString() + " " + iv.toString());
		}
	}
}
