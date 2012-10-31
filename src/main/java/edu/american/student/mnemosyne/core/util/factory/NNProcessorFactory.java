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
package edu.american.student.mnemosyne.core.util.factory;

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
