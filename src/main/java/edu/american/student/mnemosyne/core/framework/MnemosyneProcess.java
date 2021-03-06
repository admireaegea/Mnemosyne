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

import edu.american.student.mnemosyne.core.exception.ProcessException;
import edu.american.student.mnemosyne.core.util.foreman.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.foreman.ArtifactForeman;

public interface MnemosyneProcess
{
	AccumuloForeman aForeman = new AccumuloForeman();
	ArtifactForeman artifactForeman = new ArtifactForeman();
	
	public void process() throws  ProcessException;
	
	public void setup() throws ProcessException;

}
