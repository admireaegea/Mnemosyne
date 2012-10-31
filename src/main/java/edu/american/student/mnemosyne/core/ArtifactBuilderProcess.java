/* Copyright 2012 Cameron Cook
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
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
import edu.american.student.mnemosyne.core.exception.DataspaceException;
import edu.american.student.mnemosyne.core.exception.ProcessException;
import edu.american.student.mnemosyne.core.framework.MnemosyneProcess;
import edu.american.student.mnemosyne.core.model.Artifact;
import edu.american.student.mnemosyne.core.util.foreman.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.foreman.HadoopForeman;

/**
 * This process builds and persists artifacts gathered from the IngestProcess in the ARTIFACT_TABLE
 * @author cam
 *
 */
public class ArtifactBuilderProcess implements MnemosyneProcess
{
	
	/**
	 * Calls the MR Process to look over RAW BYTES asserted from IngestProcess in the ARTIFACT_TABLE
	 * 
	 * The MR Process created ARTIFACT_ENTRYs which the ArtifactForman then persists
	 */
	public void process() throws ProcessException
	{
		HadoopForeman hForeman = new HadoopForeman();
		HadoopJobConfiguration conf = new HadoopJobConfiguration();
		
		conf.setJobName(HadoopJobConfiguration.buildJobName(this.getClass()));
		conf.setMapperClass(ArtifactBuilderMapper.class);
		conf.overrideDefaultTable(AccumuloForeman.getArtifactRepositoryName());
		
		Collection<Pair<Text, Text>> cfPairs = new ArrayList<Pair<Text, Text>>();
		cfPairs.add(new Pair<Text, Text>(new Text(AccumuloForeman.getArtifactRepository().rawBytes()), null));
		conf.setFetchColumns(cfPairs);
		
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

	/**
	 * Connect to the ArtifactForman
	 */
	public void setup() throws DataspaceException
	{
		aForeman.connect();
		artifactForeman.connect();
	}

	/**
	 * For every RAW_BYTES entry in ARTIFACT_TABLE, register that artifact with the artifact foreman
	 * @author cam
	 *
	 */
	public static class ArtifactBuilderMapper extends Mapper<Key, Value, Writable, Writable>
	{
		@Override
		public void map(Key ik, Value iv, Context context)
		{
			String row = ik.getRow().toString();//This is the Artifact ID
			int lineNumber = Integer.parseInt(ik.getColumnQualifier().toString());
			String raw = iv.toString();
			artifactForeman.register(row, lineNumber, raw);
		}
	}

}
