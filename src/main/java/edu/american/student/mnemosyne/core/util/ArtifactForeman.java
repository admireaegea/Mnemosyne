package edu.american.student.mnemosyne.core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
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

	public List<Artifact> returnArtifacts() throws ParserConfigurationException, SAXException, IOException, TableNotFoundException
	{
		List<Artifact> toReturn = new ArrayList<Artifact>();
		Iterator<Entry<String, Map<Integer, String>>> it =artifactMap.entrySet().iterator();
		if(it.hasNext())
		{
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
		}
		else
		{
			List<Entry<Key, Value>> entries = aForeman.fetchByColumnFamily(AccumuloForeman.getArtifactRepositoryName(), "ARTIFACT_ENTRY");
			
			for(Entry<Key,Value> entry: entries)
			{
				toReturn.add(Artifact.inflate(entry.getKey().getRow().toString(),entry.getValue().toString()));
			}
		}
		
		return toReturn;
	}

	public void persistArtifacts()
	{
		Iterator<Entry<String, Map<Integer, String>>> it = artifactMap.entrySet().iterator();
		
		while(it.hasNext())
		{
			Entry<String,Map<Integer,String>> entry = it.next();
			String artifactId = entry.getKey();
			String serialized = "";
			Iterator<Entry<Integer,String>> internalIt =entry.getValue().entrySet().iterator();
			while(internalIt.hasNext())
			{
				Entry<Integer,String> internalEntry = internalIt.next();
				serialized+="("+internalEntry.getKey()+","+internalEntry.getValue()+")";
			}
			aForeman.connect();
			aForeman.add(AccumuloForeman.getArtifactRepositoryName(), artifactId, "ARTIFACT_ENTRY",artifactId, serialized);
		}
		
	}
}
