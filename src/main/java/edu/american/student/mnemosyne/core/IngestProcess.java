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
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;

import edu.american.student.mnemosyne.conf.HadoopJobConfiguration;
import edu.american.student.mnemosyne.core.exception.ProcessException;
import edu.american.student.mnemosyne.core.exception.RepositoryException;
import edu.american.student.mnemosyne.core.exception.StopMapperException;
import edu.american.student.mnemosyne.core.framework.MnemosyneProcess;
import edu.american.student.mnemosyne.core.util.MnemosyneConstants;
import edu.american.student.mnemosyne.core.util.factory.ArtifactIdFactory;
import edu.american.student.mnemosyne.core.util.foreman.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.foreman.HadoopForeman;

/**
 * Ingests an XML file in Mnemosyne XML and asserts its inputs and outputs into the dataspace
 * @author cam
 *
 */
public class IngestProcess implements MnemosyneProcess
{
	/**
	 * The UUID used to make an artifact id 
	 */
	static String uuid;
	static List<Integer> linesProcessed = new ArrayList<Integer>();
	static String fileName="";
	static Logger log = Logger.getLogger(IngestProcess.class.getName());
	/**
	 * Multiple files to ingest
	 */
	static Path[] pathsToProcess;
	/**
 	* @see MnemosyneProcess
 	*/
	public void setup() throws ProcessException
	{
		uuid = UUID.randomUUID().toString();
		aForeman.connect();
		pathsToProcess = MnemosyneConstants.getAllIngestableFiles();
	}

	/**
	 * for every path 
	 * grab every line of the file, and throw it into accumulo
	 */
	@SuppressWarnings("static-access")
	public void process() throws ProcessException
	{
		for(Path path: pathsToProcess)
		{
			uuid = UUID.randomUUID().toString();	
			this.fileName = path.getName();
			HadoopForeman hForeman = new HadoopForeman();
			HadoopJobConfiguration conf = new HadoopJobConfiguration();
			conf.setJobName(HadoopJobConfiguration.buildJobName(this.getClass()));
			conf.setMapperClass(IngestMapper.class);
			conf.setInputFormatClass(TextInputFormat.class);
			conf.overridePathToProcess(path);
			conf.setOutputFormatClass(NullOutputFormat.class);
			conf.setOutputKeyClass(Text.class);
			conf.setOutputValueClass(IntWritable.class);
			hForeman.runJob(conf);
		
		}

	}
	
	

	/**
	 * Throws every line of a file into accumulo
	 * @author cam
	 *
	 */
	public static class IngestMapper extends Mapper<LongWritable, Text, Text, IntWritable>
	{
		@Override
		public void map(LongWritable ik, Text iv, Context context)
		{
			try
			{
				aForeman.add(AccumuloForeman.getArtifactRepositoryName(), ArtifactIdFactory.buildArtifactId(uuid,fileName), AccumuloForeman.getArtifactRepository().rawBytes(), ik.toString(), iv.toString());
			}
			catch (RepositoryException e)
			{
				String gripe = "Could not access the Repository Services";
				log.log(Level.SEVERE,gripe,e);
				throw new StopMapperException(gripe,e);
			}	

		}
	}

}
