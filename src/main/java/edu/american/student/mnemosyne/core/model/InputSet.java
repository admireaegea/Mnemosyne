package edu.american.student.mnemosyne.core.model;

import java.util.List;
import java.util.Vector;

public class InputSet<K extends Number>
{
	private List<List<K>> inputSet = new Vector<List<K>>();
	
	public InputSet()
	{
		
	}
	
	public void addSet(List<K> set)
	{
		inputSet.add(set);
	}
	
	public List<List<K>> getInputSets()
	{
		return inputSet;
	}
}
