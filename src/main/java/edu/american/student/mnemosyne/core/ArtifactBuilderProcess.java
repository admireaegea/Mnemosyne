package edu.american.student.mnemosyne.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.accumulo.core.client.mapreduce.AccumuloInputFormat;
import org.apache.accumulo.core.client.mapreduce.AccumuloOutputFormat;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.util.Pair;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;

import edu.american.student.mnemosyne.conf.HadoopJobConfiguration;
import edu.american.student.mnemosyne.core.framework.MnemosyneProcess;
import edu.american.student.mnemosyne.core.model.Artifact;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.ArtifactForeman;
import edu.american.student.mnemosyne.core.util.HadoopForeman;

public class ArtifactBuilderProcess implements MnemosyneProcess
{
	static ArtifactForeman artifactForeman = new ArtifactForeman();
	
	public void process() throws IOException, InterruptedException, ClassNotFoundException
	{
		HadoopForeman hForeman = new HadoopForeman();
		HadoopJobConfiguration conf = new HadoopJobConfiguration();
		conf.setJobName(HadoopJobConfiguration.buildJobName(TrainProcess.class));
		conf.setMapperClass(ArtifactBuilderMapper.class);
		conf.overrideDefaultTable(AccumuloForeman.getArtifactRepositoryName());
		Collection<Pair<Text,Text>> cfPairs = new ArrayList<Pair<Text,Text>>();
		cfPairs.add(new Pair<Text,Text>(new Text("RAW_BYTES"),null));
		conf.fetchColumns(cfPairs);
		conf.setInputFormatClass(AccumuloInputFormat.class);
		conf.setOutputFormatClass(AccumuloOutputFormat.class);
		hForeman.runJob(conf);
		
		List<Artifact> artifacts =artifactForeman.returnArtifacts();
		for(Artifact artifact: artifacts)
		{
			List<String> fields = artifact.getFields();
			for(String field: fields)
			{
				aForeman.add(AccumuloForeman.getArtifactRepositoryName(), artifact.getArtifactId(), field, "", artifact.getValue(field));
			}
		}
		//lay each artifact into accumulo
		/*
		 * ROW= artifactId
		 * CF = field name
		 * CQ = ?
		 * Value = value
		 */
		
	}

	public void setup() throws Exception
	{
		artifactForeman.connect();
		aForeman.connect();
	}
	
	public static class ArtifactBuilderMapper extends Mapper<Key, Value, Writable, Writable>
	{
		@Override
		public void map(Key ik, Value iv, Context context)
		{
			System.out.println(ik.toString()+" "+iv.toString());
			artifactForeman.register(ik.getRow().toString(),Integer.parseInt(ik.getColumnQualifier().toString()),iv.toString());
		}
	}

}
