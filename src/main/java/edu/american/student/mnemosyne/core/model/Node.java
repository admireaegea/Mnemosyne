package edu.american.student.mnemosyne.core.model;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.encog.neural.networks.BasicNetwork;

import edu.american.student.mnemosyne.conf.ClassificationNetworkConf;
import edu.american.student.mnemosyne.conf.NetworkConf;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.ClassificationNetwork;

public class Node
{
	//TODO remove this class.
	private String name = UUID.randomUUID().toString().replace("-", "");
	private String confType;
	@SuppressWarnings("unused")
	private NetworkConf conf;
	private AccumuloForeman aForeman = new AccumuloForeman();
	private List<BasicNetwork> networks;
	public Node()
	{
		aForeman.connect();
	}
	
	public String getName()
	{
		return name;
	}

	public String getConfigType()
	{	
		return confType;
	}
	
	public void setConfigType(String confType)
	{
		this.confType = confType;
	}

	public void setConfig(ClassificationNetworkConf conf)
	{
		this.conf = conf;
	}

	public void constructNeuralNetworks(NetworkConf conf) throws IOException
	{
//		if(conf instanceof ClassificationNetworkConf)
//		{
//			networks = ClassificationNetwork.constructNetworks((ClassificationNetworkConf) conf);
//			for(int i=0;i<networks.size();i++)
//			{
//				BasicNetwork network = networks.get(i);
//				if(!aForeman.tableExists(this.getName()))
//				{
//					aForeman.makeTable(this.getName());
//				}
//				aForeman.saveNetwork(this, network,i);
//			}
//		}
		
	}
}
