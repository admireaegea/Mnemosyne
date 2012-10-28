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

import java.io.Serializable;

public class InputOutputHolder implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7455128628876031515L;
	private double[][] input;
	private double[][] output;
	public InputOutputHolder(double[][] input, double[][] output)
	{
		this.input = input;
		this.output = output;
	}
	
	public double[][] getInput()
	{
		return this.input;
	}
	
	public double[][] getOutput()
	{
		return this.output;
	}
}
