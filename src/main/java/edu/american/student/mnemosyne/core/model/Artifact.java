package edu.american.student.mnemosyne.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Artifact
{

	private List<String> fields = new ArrayList<String>();
	private String artifactId = "";
	private Map<String,String> fieldMap = new HashMap<String,String>();
	int lineSize = 0;
	private ArrayList<String> organizedFile = new ArrayList<String>();
	public List<String> getFields()
	{
		return fields;
	}

	public String getArtifactId()
	{
		return this.artifactId;
	}

	public String getValue(String field)
	{
		return fieldMap.get(field);
	}

	public void setArtifactId(String key)
	{
		this.artifactId = key;
		
	}

	public void addLine(Integer lineNumber, String value)
	{
		if(lineNumber > lineSize)
		{
			lineSize = lineNumber;
		}
		ensureCapasity(lineNumber+1);
		organizedFile.set(lineNumber, value);
		
	}

	private void ensureCapasity(Integer lineNumber)
	{
		while(organizedFile.size() <lineNumber)
		{
			organizedFile.add("");
		}
		
	}

	public void finalizeStructure()
	{
		StringBuilder sb = new StringBuilder();
		for(String line:organizedFile)
		{
			if(!line.equals(""))
			{
				sb.append(line+"\n");
			}
			
		}
		constructFieldMap(sb.toString());
	}

	private void constructFieldMap(String fullFile)
	{
		// TODO Auto-generated method stub
		
	}

}
