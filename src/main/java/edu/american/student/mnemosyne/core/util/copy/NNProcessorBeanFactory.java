package edu.american.student.mnemosyne.core.util.copy;

import edu.american.student.mnemosyne.conf.BasicNetworkConf;
import edu.american.student.mnemosyne.conf.NetworkConf;
import edu.american.student.mnemosyne.core.BasicNNProcessor;
import edu.american.student.mnemosyne.core.framework.NNProcessor;

public class NNProcessorBeanFactory
{

	
	public NNProcessor getProcessorBean(NetworkConf conf)
	{
		if(conf instanceof BasicNetworkConf)
		{
			return new BasicNNProcessor(conf);
		}
		
		return null;
	}

}
