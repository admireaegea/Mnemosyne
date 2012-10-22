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

import edu.american.student.mnemosyne.core.MnemosyneAPI;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.CLIConstants;
import edu.american.student.mnemosyne.core.util.NNMetadata;

public class NNAnalyst  extends MnemosyneAPI implements CLI
{
	public NNAnalyst(String[] args) throws Exception
	{
		setup();
		args = new String[]{"-c"};
		CommandLineParser posix = new PosixParser();
		// create Options object
		Options options = new Options();

		// add t option
		options.addOption("c", false, "calculate");
		Option help = new Option( "help", "print this message" );
		options.addOption(help);
		CommandLine cmd = posix.parse(options, args	);
		if(cmd.hasOption(CLIConstants.CALCULATE.getTitle()))
		{
			System.out.println("CALCULATE");
			String artifactId = getOptionArtifact();
			BasicNetwork n = aForeman.inflateNetwork(AccumuloForeman.getBaseNetworkRepositoryName(), "BASE_NETWORK", artifactId);
			List<Entry<Key,Value>> metadata = aForeman.fetchByColumnFamily(AccumuloForeman.getArtifactRepositoryName(), artifactId);
			for(Entry<Key,Value> entry : metadata)
			{
				NNMetadata data = NNMetadata.inflate(entry.getValue().toString(),entry.getKey().getRow().toString());
				MLData result = n.compute(getMLDataInput(data.getInputNameFields()));
				System.out.println("### RESULT ###");
				double[] results = result.getData();
				for(double res: results)
				{
					System.out.println(res);
				}
				break;
			}
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
	public static void main(String[] args) throws Exception
	{
		NNAnalyst analyst = new NNAnalyst(args);
		
	}

	
	public void setup()
	{
		aForeman.connect();
		artifactForeman.connect();
		
	}
	
}
