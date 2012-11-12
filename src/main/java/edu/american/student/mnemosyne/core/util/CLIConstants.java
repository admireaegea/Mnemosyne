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
package edu.american.student.mnemosyne.core.util;

public enum CLIConstants
{

	START("analyze"),
	SETUP("setup"),
	BUILD("build"),
	CONSTRUCT("construct"),
	TRAIN("train"),
	ERROR("calculateError"),
	SAVE("save"),
	INFLATE("inflate"),
	INGEST("ingest"), 
	VERIFY("verify");
	
	String title;
	CLIConstants(String title)
	{
		this.title = title;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public static CLIConstants getTitle(String passed)
	{
		CLIConstants[] constants = CLIConstants.class.getEnumConstants();
		for(CLIConstants constant: constants)
		{
			if(constant.getTitle().equals(passed))
			{
				return constant;
			}
		}
		return null;
	}
}
