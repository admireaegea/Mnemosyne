package org.mnemosyne.examples;

import org.mnemosyne.examples.util.Deploy;

import edu.american.student.mnemosyne.core.cli.NNAnalyst;

/**
 * 
 * @author cam
 *
 */
public class BreastCancerNN
{

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		Deploy.deployPsuedoDistrubtedSystem();
		String[] setup = {"-setup"};
		NNAnalyst analyst = new NNAnalyst(setup);
		String[] ingest = {"-ingest"};
		analyst = new NNAnalyst(ingest);
		String[] build = {"-build"};
		analyst = new NNAnalyst(build);
		String[] construct = {"-construct"};
		
		String[] train = {"-train"};
		
	
	}

}
