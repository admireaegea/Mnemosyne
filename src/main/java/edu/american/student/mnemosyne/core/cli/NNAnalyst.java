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

import edu.american.student.mnemosyne.core.IngestProcess;
import edu.american.student.mnemosyne.core.MnemosyneAPI;
import edu.american.student.mnemosyne.core.exception.DataspaceException;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.CLIConstants;
import edu.american.student.mnemosyne.core.util.MnemosyneConstants;
import edu.american.student.mnemosyne.core.util.NNMetadata;
import edu.american.student.mnemosyne.core.util.NNOutput;

public class NNAnalyst  extends MnemosyneAPI implements CLI
{
	public NNAnalyst(String[] args) throws Exception
	{
		setup();
		args = new String[]{};
		CommandLineParser posix = new PosixParser();
		// create Options object
		Options options = new Options();

		// add t option
		options.addOption("s", false, "Start the Analyst");
		options.addOption("i",false,"Ingest from "+MnemosyneConstants.getIngestDirectory());
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
				MLData result = n.compute(getMLDataInput(data.getInputNameFields()));
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
		else
		{
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "nnanalyst", options );
		}
		//serialize the neural net to a file
		//call ingest on a different set of data using the same neural network
		// calculate on one piece of data
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
