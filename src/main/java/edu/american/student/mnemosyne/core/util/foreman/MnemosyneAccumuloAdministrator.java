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
package edu.american.student.mnemosyne.core.util.foreman;

import edu.american.student.mnemosyne.core.exception.RepositoryException;

public class MnemosyneAccumuloAdministrator
{

	public static void main(String[] args) throws RepositoryException
	{
		MnemosyneAccumuloAdministrator.setup();
	}
	public static void setup() throws RepositoryException
	{
		System.out.println("Starting Accumulo Setup");
		AccumuloForeman aForeman = new AccumuloForeman();
		aForeman.connect();
		aForeman.deleteTables();
		aForeman.makeTable(AccumuloForeman.getBaseNetworkRepositoryName());
		aForeman.makeTable(AccumuloForeman.getArtifactRepositoryName());
		aForeman.makeTable("CONGRESS");
	}
}
