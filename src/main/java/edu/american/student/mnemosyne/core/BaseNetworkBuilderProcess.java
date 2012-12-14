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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.accumulo.core.client.mapreduce.AccumuloInputFormat;
import org.apache.accumulo.core.client.mapreduce.AccumuloOutputFormat;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.util.Pair;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.encog.engine.network.activation.ActivationSigmoid;

import edu.american.student.mnemosyne.conf.ClassificationNetworkConf;
import edu.american.student.mnemosyne.conf.HadoopJobConfiguration;
import edu.american.student.mnemosyne.core.exception.ArtifactException;
import edu.american.student.mnemosyne.core.exception.ProcessException;
import edu.american.student.mnemosyne.core.exception.RepositoryException;
import edu.american.student.mnemosyne.core.exception.StopMapperException;
import edu.american.student.mnemosyne.core.framework.MnemosyneProcess;
import edu.american.student.mnemosyne.core.framework.NNProcessor;
import edu.american.student.mnemosyne.core.model.Artifact;
import edu.american.student.mnemosyne.core.model.NNMetadata;
import edu.american.student.mnemosyne.core.util.BinaryUtils;
import edu.american.student.mnemosyne.core.util.factory.NNProcessorFactory;
import edu.american.student.mnemosyne.core.util.foreman.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.foreman.HadoopForeman;

/**
 * This process constructs a base neural network for every artifact
 * @author cam
 *
 */
public class BaseNetworkBuilderProcess implements MnemosyneProcess
{
	private final static Logger log = Logger.getLogger(BaseNetworkBuilderProcess.class.getName());
	/**
	 * For every artifact, Build a base network in Accumulo
	 */
	public void process() throws ProcessException
	{
		List<Artifact> artifacts = artifactForeman.returnArtifacts();
		for (Artifact artifact : artifacts)
		{
			HadoopForeman hForeman = new HadoopForeman();
			HadoopJobConfiguration conf = new HadoopJobConfiguration();
			conf.setJobName(HadoopJobConfiguration.buildJobName(BaseNetworkBuilderProcess.class));
			conf.setMapperClass(BaseNetworkBuilderMapper.class);
			conf.overrideDefaultTable(AccumuloForeman.getArtifactRepositoryName());
			conf.setJarClass(this.getClass());
			Collection<Pair<Text, Text>> cfPairs = new ArrayList<Pair<Text, Text>>();
			cfPairs.add(new Pair<Text, Text>(new Text(artifact.getArtifactId()), null));
			conf.setFetchColumns(cfPairs);
			conf.setInputFormatClass(AccumuloInputFormat.class);
			conf.setOutputFormatClass(AccumuloOutputFormat.class);
			hForeman.runJob(conf);
		}

	}

	/**
	 * Connect to the artifact foreman
	 */
	public void setup() throws ArtifactException
	{
		artifactForeman.connect();

	}

	/**
	 * Mapper to inflate the inputs, outputs and metadata from ingest, then create a network and asser it
	 * @author cam
	 *
	 */
	public static class BaseNetworkBuilderMapper extends Mapper<Key, Value, Writable, Writable>
	{
		@Override
		public void map(Key ik, Value iv, Context context)
		{
			NNMetadata metadata = NNMetadata.inflate(iv.toString(), ik.getRow().toString());
			int inputNeuronCount = BinaryUtils.toBinary(metadata.getInputMax(),new double[]{ metadata.getInputMax()},true).length;
			int num = BinaryUtils.toBinary(metadata.getOutputMax(),new double[]{ metadata.getOutputMax() },false).length;
			int categories = metadata.getOutputNameFields().size();

			ClassificationNetworkConf conf = new ClassificationNetworkConf();
			conf.setInputMax(metadata.getInputMax());
			conf.setOutputMax(metadata.getOutputMax());
			conf.setInputActivation(null);
			conf.setInputBias(true);
			conf.setInputNeuronCount(inputNeuronCount);

			conf.setHiddenActiviation(new ActivationSigmoid());
			conf.setHiddenBias(true);
			conf.setHiddenNeuronCount(2 ^ categories);
			conf.setOutputActivation(new ActivationSigmoid());
			conf.setOutputNeuronCount(num);

			conf.setNumberOfCategories(categories);//FIXME:This is bogus now
			conf.setBasicMLInput(this.getRandomArray(inputNeuronCount));//FIXME:This is bogus now
			conf.setBasicIdealMLOutput(this.getRandomArray(num));//FIXME:This is bogus now

			try
			{
				NNProcessor processor = NNProcessorFactory.getProcessorBean(conf);
				processor.constructNetworks(metadata.getArtifactId());
			}
			catch (RepositoryException e)
			{
				String gripe = "Access to the Repository Services died";
				log.log(Level.SEVERE,gripe,e);
				throw new StopMapperException(gripe,e);
			}

		}

		private double[][] getRandomArray(int inputNeuronCount)
		{
			double[][] toReturn = new double[1][inputNeuronCount];
			for (int i = 0; i < inputNeuronCount; i++)
			{
				toReturn[0][i] = new Random().nextDouble();
			}
			return toReturn;
		}

	}

}
