package edu.american.student.mnemosyne.core;

import java.util.List;

import edu.american.student.mnemosyne.conf.CongressNetworkConf;
import edu.american.student.mnemosyne.core.exception.RepositoryException;
import edu.american.student.mnemosyne.core.framework.CongressNetwork;
import edu.american.student.mnemosyne.core.framework.NNProcessor;
import edu.american.student.mnemosyne.core.model.Neuron;
import edu.american.student.mnemosyne.core.util.foreman.AccumuloForeman;

public class CongressNNProcessor extends NNProcessor
{

	
	/**
	 * The definitions of the Classification Network (passed)
	 */
	private CongressNetworkConf conf;
	/**
	 * An instance of the Accumulo Interface
	 */
	private AccumuloForeman aForeman = new AccumuloForeman();
	
	public CongressNNProcessor(CongressNetworkConf conf) throws RepositoryException
	{
		this.conf = conf;
		aForeman.connect();
	}

	@Override
	public void constructNetworks(String artifactId) throws RepositoryException
	{
		List<Neuron> neuronsCreated = CongressNetwork.constructNetworks(conf);
		aForeman.assertCongress(neuronsCreated,artifactId,conf);

	}

}
