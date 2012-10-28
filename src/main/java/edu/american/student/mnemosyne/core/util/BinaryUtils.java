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

import java.util.ArrayList;

import edu.american.student.mnemosyne.conf.ClassificationNetworkConf;

public class BinaryUtils
{

	public static double[] toBinary(ClassificationNetworkConf conf, double[] toReturn,boolean isInput)
	{
		ArrayList<Double> binaryDoubles = new ArrayList<Double>();
		int padding =0;
		if(isInput)
		{
			String binString = Long.toBinaryString((long)conf.getInputMax()).replace(' ', '0');;
			padding = binString.length();
		}
		else
		{
			String binString = Long.toBinaryString((long)conf.getOutputMax()).replace(' ', '0');;
			padding = binString.length();
		}
		
		for(double value: toReturn)
		{
			String binaryString = String.format("%"+padding+"s", Long.toBinaryString((long)value)).replace(' ', '0');
			char[] binaryArr =binaryString.toCharArray();
			for(char chara: binaryArr)
			{
				binaryDoubles.add(Double.parseDouble(chara+""));
			}
		}
		Double[] dubs = new Double[binaryDoubles.size()];
		binaryDoubles.toArray(dubs);
		double[] ret = new double[binaryDoubles.size()];
		for(int i=0;i<binaryDoubles.size();i++)
		{
			ret[i]=dubs[i];
		}
		return ret;
	}

	public static double[] toBinary(int max, double[] toReturn, boolean isInput)
	{
		ClassificationNetworkConf conf = new ClassificationNetworkConf();
		if(isInput)
		{
			conf.setInputMax(max);
		}
		else
		{
			conf.setOutputMax(max);
		}
		return BinaryUtils.toBinary(conf, toReturn, isInput);
	}

}
