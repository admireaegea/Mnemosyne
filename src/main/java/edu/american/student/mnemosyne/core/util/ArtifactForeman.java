package edu.american.student.mnemosyne.core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import edu.american.student.mnemosyne.core.model.Artifact;

public class ArtifactForeman
{

	private AccumuloForeman aForeman = new AccumuloForeman();
	Map<String,Map<Integer,String>> artifactMap = new HashMap<String,Map<Integer,String>>();
	
	public void register(String artifactId,int position, String value)
	{
		if(artifactMap.get(artifactId)==null)
		{
			Map<Integer,String> map = new HashMap<Integer,String>();
			map.put(position,value);
			artifactMap.put(artifactId, map);
		}
		else
		{
			Map<Integer,String> map =artifactMap.get(artifactId);
			map.put(position, value);
		}
	}

	public void connect()
	{
		aForeman.connect();
	}

	public List<Artifact> returnArtifacts() throws ParserConfigurationException, SAXException, IOException
	{
		List<Artifact> toReturn = new ArrayList<Artifact>();
		Iterator<Entry<String, Map<Integer, String>>> it =artifactMap.entrySet().iterator();
		Artifact toAdd = new Artifact();
		while(it.hasNext())
		{
			Entry<String, Map<Integer, String>> entry = it.next();
			toAdd.setArtifactId(entry.getKey());
			Iterator<Entry<Integer,String>> internalIt = entry.getValue().entrySet().iterator();
			while(internalIt.hasNext())
			{
				Entry<Integer,String> internalEntry =internalIt.next();
				toAdd.addLine(internalEntry.getKey(),internalEntry.getValue());
			}
			toReturn.add(toAdd);
		}
		for(Artifact artifact: toReturn)
		{
			artifact.finalizeStructure();
		}
		return toReturn;
	}
}
