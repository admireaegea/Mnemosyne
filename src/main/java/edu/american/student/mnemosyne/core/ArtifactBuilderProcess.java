package edu.american.student.mnemosyne.core;

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
import edu.american.student.mnemosyne.core.exception.ArtifactException;
import edu.american.student.mnemosyne.core.exception.ProcessException;
import edu.american.student.mnemosyne.core.framework.MnemosyneProcess;
import edu.american.student.mnemosyne.core.model.Artifact;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.HadoopForeman;

public class ArtifactBuilderProcess implements MnemosyneProcess
{
	

	public void process() throws ProcessException
	{
		HadoopForeman hForeman = new HadoopForeman();
		HadoopJobConfiguration conf = new HadoopJobConfiguration();
		conf.setJobName(HadoopJobConfiguration.buildJobName(this.getClass()));
		conf.setMapperClass(ArtifactBuilderMapper.class);
		conf.overrideDefaultTable(AccumuloForeman.getArtifactRepositoryName());
		Collection<Pair<Text, Text>> cfPairs = new ArrayList<Pair<Text, Text>>();
		cfPairs.add(new Pair<Text, Text>(new Text("RAW_BYTES"), null));
		conf.fetchColumns(cfPairs);
		conf.setInputFormatClass(AccumuloInputFormat.class);
		conf.setOutputFormatClass(AccumuloOutputFormat.class);
		hForeman.runJob(conf);

		List<Artifact> artifacts = artifactForeman.returnArtifacts();
		artifactForeman.persistArtifacts();
		for (Artifact artifact : artifacts)
		{
			String definitions = artifact.grabDefinitions();
			aForeman.add(AccumuloForeman.getArtifactRepositoryName(), artifact.getArtifactId(), artifact.getArtifactId(), "DEFINITIONS", definitions);
			List<String> sets = artifact.grabSets();
			for (int j = 0; j < sets.size(); j++)
			{
				aForeman.add(AccumuloForeman.getArtifactRepositoryName(), artifact.getArtifactId(), artifact.getArtifactId()+":"+"FIELD", "SET" + j, sets.get(j));
			}

		}

	}

	public void setup() throws ArtifactException
	{
		artifactForeman.connect();
	}

	public static class ArtifactBuilderMapper extends Mapper<Key, Value, Writable, Writable>
	{
		@Override
		public void map(Key ik, Value iv, Context context)
		{
			artifactForeman.register(ik.getRow().toString(), Integer.parseInt(ik.getColumnQualifier().toString()), iv.toString());
		}
	}

}
