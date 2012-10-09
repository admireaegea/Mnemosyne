package edu.american.student.mnemosyne.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;

import edu.american.student.mnemosyne.conf.HadoopJobConfiguration;
import edu.american.student.mnemosyne.core.framework.MnemosyneProcess;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.HadoopForeman;
import edu.american.student.mnemosyne.core.util.MnemosyneConstants;

public class IngestProcess implements MnemosyneProcess
{
	static String uuid;
	static List<Integer> linesProcessed = new ArrayList<Integer>();
	static Path[] pathsToProcess;
	public void setup() throws Exception
	{
		uuid = UUID.randomUUID().toString();
		aForeman.connect();
		pathsToProcess = MnemosyneConstants.getAllIngestableFiles();

	}

	public void process() throws IOException, InterruptedException, ClassNotFoundException
	{
		for(Path path: pathsToProcess)
		{
			uuid = UUID.randomUUID().toString();	
			HadoopForeman hForeman = new HadoopForeman();
			HadoopJobConfiguration conf = new HadoopJobConfiguration();
			conf.setJobName(HadoopJobConfiguration.buildJobName(IngestProcess.class));
			conf.setMapperClass(IngestMapper.class);
			conf.setInputFormatClass(TextInputFormat.class);
			conf.overridePathToProcess(path);
			conf.setOutputFormatClass(NullOutputFormat.class);
			conf.setOutputKeyClass(Text.class);
			conf.setOutputValueCLass(IntWritable.class);
			hForeman.runJob(conf);
		
		}

	}
	
	

	public static class IngestMapper extends Mapper<LongWritable, Text, Text, IntWritable>
	{
		@Override
		public void map(LongWritable ik, Text iv, Context context)
		{
			aForeman.add(AccumuloForeman.getArtifactRepositoryName(), "ARTIFACT_" + uuid, "RAW_BYTES", ik.toString(), iv.toString());

		}
	}

}
