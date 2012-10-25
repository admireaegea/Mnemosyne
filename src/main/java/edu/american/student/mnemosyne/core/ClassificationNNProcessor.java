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

import org.encog.neural.networks.BasicNetwork;

import edu.american.student.mnemosyne.conf.ClassificationNetworkConf;
import edu.american.student.mnemosyne.core.exception.RepositoryException;
import edu.american.student.mnemosyne.core.framework.NNProcessor;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.ClassificationNetwork;

/**
 * 
 * @author cam
 *
 */
public class ClassificationNNProcessor extends NNProcessor
{
	/**
	 * The definitions of the Classification Network (passed)
	 */
	private ClassificationNetworkConf conf;
	/**
	 * An instance of the Accumulo Interface
	 */
	private AccumuloForeman aForeman = new AccumuloForeman();
	
	/**
	 * Constructs a Classification Network given a configuration
	 * @param conf2
	 * @throws RepositoryException
	 */
	public ClassificationNNProcessor(ClassificationNetworkConf conf2) throws RepositoryException
	{
		this.conf = conf2;
		aForeman.connect();
	}

	/**
	 * Constructs a Classification Network given the ArtifactId
	 */
	@Override
	public void constructNetworks(String artifactId) throws  RepositoryException
	{
		BasicNetwork network = ClassificationNetwork.constructNetworks(conf);
		aForeman.assertBaseNetwork(network,artifactId,conf);
		
	}

}
