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
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import edu.american.student.mnemosyne.conf.ClassificationNetworkConf;
import edu.american.student.mnemosyne.conf.HadoopJobConfiguration;
import edu.american.student.mnemosyne.core.exception.ProcessException;
import edu.american.student.mnemosyne.core.exception.RepositoryException;
import edu.american.student.mnemosyne.core.framework.MnemosyneProcess;
import edu.american.student.mnemosyne.core.model.Artifact;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.ClassificationNetwork;
import edu.american.student.mnemosyne.core.util.HadoopForeman;
import edu.american.student.mnemosyne.core.util.InputOutputHolder;
import edu.american.student.mnemosyne.core.util.NNInput;
import edu.american.student.mnemosyne.core.util.NNOutput;
/**
 * Process that trains the base network and grows it in parallel
 * @author cam
 *
 */
public class TrainProcess implements MnemosyneProcess
{

	/**
	 * Represents the number of artifact inputs processed
	 */
	private static int round = 0;
	private static final Logger log = Logger.getLogger(TrainProcess.class.getName());
	/**
	 * For every artifact (therefore for every network)
	 * Call the train mapper
	 */
	public void process() throws ProcessException
	{
		artifactForeman.connect();
		List<Artifact> artifacts = artifactForeman.returnArtifacts();
		for (Artifact artifact : artifacts)
		{
			HadoopForeman hForeman = new HadoopForeman();
			HadoopJobConfiguration conf = new HadoopJobConfiguration();
			conf.setJobName(HadoopJobConfiguration.buildJobName(this.getClass()));
			conf.setMapperClass(NNTrainMapper.class);
			conf.overrideDefaultTable(AccumuloForeman.getArtifactRepositoryName());
			Collection<Pair<Text, Text>> cfPairs = new ArrayList<Pair<Text, Text>>();
			cfPairs.add(new Pair<Text, Text>(new Text(artifact.getArtifactId() + ":FIELD"), null));
			conf.setFetchColumns(cfPairs);
			conf.setInputFormatClass(AccumuloInputFormat.class);
			conf.setOutputFormatClass(AccumuloOutputFormat.class);
			hForeman.runJob(conf);
		}
	}

	/**
	 * Inflates the base network, trains over the new input and all past input until an acceptable error is hit, or it times out.
	 * Saves over the base network. 
	 * @author cam
	 *
	 */
	public static class NNTrainMapper extends Mapper<Key, Value, Writable, Writable>
	{
		private AccumuloForeman aForeman = new AccumuloForeman();

		@Override
		public void map(Key ik, Value iv, Context context)
		{
			try
			{
				aForeman.connect();
			}
			catch (RepositoryException e3)
			{
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			log.log(Level.INFO,"Grabbing the base network...");
			BasicNetwork base = null;
			ClassificationNetworkConf baseConf = null;
			double error = 1;
			try
			{
				base = aForeman.getBaseNetwork(ik.getRow().toString());
				baseConf = aForeman.getBaseNetworkConf(ik.getRow().toString());
				error = aForeman.getBaseNetworkError(ik.getRow().toString());
			}
			catch (Exception e)
			{
			}
			if (base != null)
			{
				log.log(Level.INFO,"Training ...");
				long start = System.currentTimeMillis();
				long timeout = baseConf.getTimeout();
				int epochTimeout = baseConf.getEpochTimeout();
				double[] input = NNInput.inflate(baseConf,iv.toString());
				double[] output = NNOutput.inflate(baseConf,iv.toString());
				MLDataSet trainingSet = null;

				try
				{
					trainingSet = constructTrainingSet(ik.getRow().toString(), input, output);
					BasicNetwork modified = ClassificationNetwork.addLayerToNetwork(base, baseConf, new BasicLayer(new ActivationSigmoid(), true, baseConf.getNumberOfCategories()));
					final ResilientPropagation train = new ResilientPropagation(modified, trainingSet);
					int epoch = 1;

					error = aForeman.getBaseNetworkError(ik.getRow().toString());
				
					long elapsed = System.currentTimeMillis() - start;
					do
					{
						train.iteration();
						elapsed = System.currentTimeMillis() - start;
						log.log(Level.INFO,"Round:" + round + " Epoch #" + epoch + " Error:" + train.getError() + " acceptable error:" + error + " Elapsed:" + elapsed + " Timeout:" + (elapsed > timeout));
						epoch++;
					}
					while (train.getError() > error && ((elapsed )< timeout) && epoch < epochTimeout);
					round++;
					start = System.currentTimeMillis();

					aForeman.assertBaseNetwork(modified, ik.getRow().toString(), baseConf);
					aForeman.assertBaseNetworkError(train.getError(), ik.getRow().toString());
				}
				catch (RepositoryException e2)
				{
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

			}

		}

		private MLDataSet constructTrainingSet(String artifactId, double[] input, double[] output) throws RepositoryException
		{
			InputOutputHolder newHolder = new InputOutputHolder(new double[][]{input},new double[][]{output});
			aForeman.assertInputOutputHolder(artifactId,newHolder);
			List<InputOutputHolder> holders = aForeman.getInputOutputHolders(artifactId);
			double[][] newIn = new double[holders.size()][holders.get(0).getInput()[0].length];
			double[][] newOut = new double[holders.size()][holders.get(0).getOutput()[0].length];
			//fill in all the new inputs
			//for every holder, put it into a double[][] for input output
			for(int i=0;i<holders.size();i++)
			{
				InputOutputHolder indexed = holders.get(i);
				double[][] toAdd= indexed.getInput();
				for(int k=0;k<toAdd.length;k++)
				{
					for(int j=0;j<toAdd[k].length;j++)
					{
						newIn[i][j]=toAdd[k][j];
					}
				}
				
				double[][] out = indexed.getOutput();
				for(int k=0;k<out.length;k++)
				{
					for(int j=0;j<out[k].length;j++)
					{
						newOut[i][j]=out[k][j];
					}
				}
			}
			MLDataSet trainingSet = new BasicMLDataSet(newIn, newOut);
			return trainingSet;
		}
	}

	static double aerror;

	public void setup() throws ProcessException
	{
		aerror = .000005;

	}

}
