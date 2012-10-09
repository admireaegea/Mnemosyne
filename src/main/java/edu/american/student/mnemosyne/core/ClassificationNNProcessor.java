package edu.american.student.mnemosyne.core;

import java.io.IOException;

import org.apache.accumulo.core.client.TableNotFoundException;
import org.encog.neural.networks.BasicNetwork;

import edu.american.student.mnemosyne.conf.ClassificationNetworkConf;
import edu.american.student.mnemosyne.core.framework.NNProcessor;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.ClassificationNetwork;

public class ClassificationNNProcessor extends NNProcessor
{

	private ClassificationNetworkConf conf;
	private AccumuloForeman aForeman = new AccumuloForeman();
	public ClassificationNNProcessor(ClassificationNetworkConf conf2)
	{
		this.conf = conf2;
		aForeman.connect();
	}

	@Override
	public void constructNetworks(String artifactId) throws IOException, TableNotFoundException, ClassNotFoundException
	{
		BasicNetwork network = ClassificationNetwork.constructNetworks(conf);
		aForeman.assertBaseNetwork(network,artifactId,conf);
		
	}

}
