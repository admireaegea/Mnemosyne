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
package edu.american.student.mnemosyne.core.cli;

import java.util.List;
import java.util.Map.Entry;

import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.encog.ml.data.MLData;
import org.encog.neural.networks.BasicNetwork;

import edu.american.student.mnemosyne.core.ArtifactBuilderProcess;
import edu.american.student.mnemosyne.core.BaseNetworkBuilderProcess;
import edu.american.student.mnemosyne.core.IngestProcess;
import edu.american.student.mnemosyne.core.TrainProcess;
import edu.american.student.mnemosyne.core.VerifyProcess;
import edu.american.student.mnemosyne.core.exception.DataspaceException;
import edu.american.student.mnemosyne.core.framework.MnemosyneAPI;
import edu.american.student.mnemosyne.core.model.NNMetadata;
import edu.american.student.mnemosyne.core.model.NNOutput;
import edu.american.student.mnemosyne.core.util.CLIConstants;
import edu.american.student.mnemosyne.core.util.InputOutputHolder;
import edu.american.student.mnemosyne.core.util.MnemosyneConstants;
import edu.american.student.mnemosyne.core.util.foreman.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.foreman.MnemosyneAccumuloAdministrator;
/**
 * TODO NEEDS CLEANUP
 * @author cam
 *
 */
public class NNAnalyst  extends MnemosyneAPI implements CLI
{
	public NNAnalyst(String[] args) throws Exception
	{
		setup();
		CommandLineParser posix = new PosixParser();
		// create Options object
		Options options = new Options();

		// add t option
		options.addOption(CLIConstants.START.getTitle(), false, "Start the Analyst");
		options.addOption(CLIConstants.INGEST.getTitle(),false,"Ingest from "+MnemosyneConstants.getIngestDirectory());
		options.addOption(CLIConstants.SETUP.getTitle(),false,"Start Mnemosyne setup");
		options.addOption(CLIConstants.BUILD.getTitle(),false,"Build artifacts");
		options.addOption(CLIConstants.CONSTRUCT.getTitle(),false,"Build base Neural Network");
		options.addOption(CLIConstants.TRAIN.getTitle(),false,"Train Neural Networks");
		options.addOption(CLIConstants.ERROR.getTitle(),false,"Compute the error of this network");
		options.addOption(CLIConstants.SAVE.getTitle(),false,"Save a NN");
		options.addOption(CLIConstants.INFLATE.getTitle(), true, "Inflate a NN");
		options.addOption(CLIConstants.VERIFY.getTitle(),false,"Use the train foreman to verify the neural network");
		Option help = new Option( "help", "print this message" );
		options.addOption(help);
		CommandLine cmd = posix.parse(options, args	);
		if(cmd.hasOption(CLIConstants.START.getTitle()))
		{
			String artifactId = getOptionArtifact();
			BasicNetwork n = aForeman.getBaseNetwork(artifactId);
			List<Entry<Key,Value>> metadata = aForeman.fetchByColumnFamily(AccumuloForeman.getArtifactRepositoryName(), artifactId);
			for(Entry<Key,Value> entry : metadata)
			{
				NNMetadata data = NNMetadata.inflate(entry.getValue().toString(),entry.getKey().getRow().toString());
				System.out.println(n==null);
				System.out.println(data == null	);;
				MLData result = n.compute(getMLDataInput(aForeman.getBaseNetworkConf(artifactId),data.getInputNameFields()));
				System.out.println("### RESULT ###");
				double[] results = result.getData();
				String stringResults = NNOutput.combine(results,data);
				System.out.println(stringResults);
				break;
			}
		}
		else if (cmd.hasOption(CLIConstants.INGEST.getTitle()))
		{
			IngestProcess ip = new IngestProcess();
			ip.setup();
			ip.process();
		}
		else if (cmd.hasOption(CLIConstants.SETUP.getTitle()))
		{
			MnemosyneAccumuloAdministrator.setup();
		}
		else if (cmd.hasOption(CLIConstants.BUILD.getTitle()))
		{
			ArtifactBuilderProcess pro = new ArtifactBuilderProcess();
			pro.setup();
			pro.process();
		}
		else if (cmd.hasOption(CLIConstants.CONSTRUCT.getTitle()))
		{
			BaseNetworkBuilderProcess pro = new BaseNetworkBuilderProcess();
			pro.setup();
			pro.process();
		}
		else if (cmd.hasOption(CLIConstants.TRAIN.getTitle()))
		{
			TrainProcess train = new TrainProcess();
			train.setup();
			train.process();
		}
		else if (cmd.hasOption(CLIConstants.ERROR.getTitle()))
		{
			//inflateInputOutputContainers
			String artifactId = getOptionArtifact();
			List<InputOutputHolder> holders =aForeman.getInputOutputHolders(artifactId);
			for(InputOutputHolder holder: holders)
			{
				System.out.println("### HOLDER ###");
				print(holder.getInput());
				print(holder.getOutput());
			}
		}
		else if (cmd.hasOption(CLIConstants.SAVE.getTitle()))
		{
			String artifactId = getOptionArtifact();
			aForeman.saveNetworkToFile("/home/cam/Desktop/card", artifactId);
		}
		else if (cmd.hasOption(CLIConstants.INFLATE.getTitle()))
		{
			String[] cmdArgs = cmd.getArgs();
			System.out.println(cmdArgs.length);
			//aForeman.saveNetwork(AccumuloForeman.getBaseNetworkRepositoryName(), AccumuloForeman.getBaseNetworkRepository().baseNetwork(), artifactId, network, conf)
		}
		else if (cmd.hasOption(CLIConstants.VERIFY.getTitle()))
		{
			VerifyProcess pro  = new VerifyProcess();
			pro.setup();
			pro.process();
		}
		else
		{
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "nnanalyst", options );
		}
		//serialize the neural net to a file
		//call ingest on a different set of data using the same neural network
		// calculate on one piece of data
	}
	private void print(double[][] input)
	{
		for(double[] doubs : input)
		{
			System.out.print("{");
			for(double dub: doubs)
			{
				System.out.print(dub+" ");
			}
			System.out.print("}\n");
		}
		
	}
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception
	{
		NNAnalyst analyst = new NNAnalyst(args);
		
	}

	
	public void setup() throws DataspaceException
	{
		aForeman.connect();
		artifactForeman.connect();
		
	}
	
}
