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
package edu.american.student.mnemosyne.core.framework;

import edu.american.student.mnemosyne.core.util.MnemosyneConstants;

public class BaseNetworkRepository implements AccumuloTable
{
	private String repositoryName = MnemosyneConstants.getBaseNetworkRepositoryName();
	private String rawBytesField = MnemosyneConstants.getBaseNetworkTableRawBytes();
	private String baseConfiguration = MnemosyneConstants.getBaseNetworkTableConfiguration();
	private String baseError = MnemosyneConstants.getBaseNetworkTableError();
	private String trainData = MnemosyneConstants.getBaseNetworkTableTrainData();
	private String baseNetwork = MnemosyneConstants.getBaseNetworkTableBaseNetwork();
	
	@Override
	public String toString()
	{
		return repositoryName;
	}
	
	public String getRawBytesField()
	{
		return rawBytesField;
	}

	public String baseConfiguration()
	{
		return baseConfiguration;
		
	}
	
	public String baseError()
	{
		return baseError;
	}

	public String trainData()
	{
		return trainData;
	}

	public String baseNetwork()
	{
		return baseNetwork;
	}
}
