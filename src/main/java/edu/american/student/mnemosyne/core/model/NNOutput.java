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
package edu.american.student.mnemosyne.core.model;

import java.util.List;

import edu.american.student.mnemosyne.conf.ClassificationNetworkConf;
import edu.american.student.mnemosyne.conf.CongressNetworkConf;
import edu.american.student.mnemosyne.core.util.BinaryUtils;

public class NNOutput
{

	public static double[] inflate(ClassificationNetworkConf conf,String value)
	{
		String doubles = value.split("\\)")[1].replace("(", "");
		String[] outputs = doubles.split(",");
		double[] toReturn = new double[outputs.length];
		for (int i = 0; i < outputs.length; i++)
		{
			toReturn[i] = Double.parseDouble(outputs[i]);
		}
		return BinaryUtils.toBinary(conf,toReturn,false);
	}

//	public static String combine(double[] results, NNMetadata data)
//	{
//		
//		List<String> outputFields = data.getOutputNameFields();
//		List<String> outputValues = data.getOutputValueFields();
//		
//		int result = Integer.parseInt(binaryString, 2);
//		for (int i = 0; i < outputValues.size(); i++)
//		{
//			String value = outputValues.get(i);
//			if (value.equals(result + ""))
//			{
//				return outputFields.get(i);
//			}
//		}
//		return null;
//	}

	public static double[] inflate( String value)
	{
		String doubles = value.split("\\)")[1].replace("(", "");
		String[] outputs = doubles.split(",");
		double[] toReturn = new double[outputs.length];
		for (int i = 0; i < outputs.length; i++)
		{
			toReturn[i] = Double.parseDouble(outputs[i]);
		}
		return toReturn;
	}

}
