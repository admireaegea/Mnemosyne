package edu.american.student.mnemosyne.core.util;

import edu.american.student.mnemosyne.conf.ClassificationNetworkConf;
import edu.american.student.mnemosyne.conf.NetworkConf;
import edu.american.student.mnemosyne.core.ClassificationNNProcessor;
import edu.american.student.mnemosyne.core.exception.RepositoryException;
import edu.american.student.mnemosyne.core.framework.NNProcessor;

public class NNProcessorFactory
{
	
	public static NNProcessor getProcessorBean(NetworkConf conf) throws RepositoryException
	{
		if(conf instanceof ClassificationNetworkConf)
		{
			return new ClassificationNNProcessor((ClassificationNetworkConf)conf);
		}
		
		return null;
	}

}
