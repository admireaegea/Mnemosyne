package edu.american.student.mnemosyne.core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.xml.sax.SAXException;

import edu.american.student.mnemosyne.core.exception.ArtifactException;
import edu.american.student.mnemosyne.core.exception.RepositoryException;
import edu.american.student.mnemosyne.core.model.Artifact;

public class ArtifactForeman
{

	private AccumuloForeman aForeman = new AccumuloForeman();
	private static final Logger log = Logger.getLogger(ArtifactForeman.class.getName());
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

	public void connect() throws ArtifactException
	{
		try
		{
			aForeman.connect();
		}
		catch (RepositoryException e)
		{
			String gripe = "Could not connect the Artifact Foreman.";
			log.log(Level.SEVERE,gripe,e);
			throw new ArtifactException(gripe,e);
		}
	}

	public List<Artifact> returnArtifacts() throws ArtifactException
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
				try
				{
					artifact.finalizeStructure();
				}
				catch (ParserConfigurationException e)
				{
					String gripe ="Could not create artifacts from the dataspace.";
					log.log(Level.SEVERE,gripe,e);
					throw new ArtifactException(gripe,e);
				}
				catch (SAXException e)
				{
					String gripe ="Could not create artifacts from the dataspace.";
					log.log(Level.SEVERE,gripe,e);
					throw new ArtifactException(gripe,e);
				}
				catch (IOException e)
				{
					String gripe ="Could not create artifacts from the dataspace.";
					log.log(Level.SEVERE,gripe,e);
					throw new ArtifactException(gripe,e);
				}
			}
		}
		else
		{
			List<Entry<Key, Value>> entries;
			try
			{
				entries = aForeman.fetchByColumnFamily(AccumuloForeman.getArtifactRepositoryName(), "ARTIFACT_ENTRY");
				for(Entry<Key,Value> entry: entries)
				{
					toReturn.add(Artifact.inflate(entry.getKey().getRow().toString(),entry.getValue().toString()));
				}
			}
			catch (RepositoryException e)
			{
				String gripe = "Could not return artifacts from the dataspace.";
				log.log(Level.SEVERE,gripe,e);
				throw new ArtifactException(gripe,e);
			}

		}
		return toReturn;
	}

	public void persistArtifacts() throws ArtifactException
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
			try
			{
				aForeman.connect();
				aForeman.add(AccumuloForeman.getArtifactRepositoryName(), artifactId, "ARTIFACT_ENTRY",artifactId, serialized);

			}
			catch (RepositoryException e)
			{
				String gripe = "Could not persist artifacts in the dataspace";
				log.log(Level.SEVERE,gripe,e);
				throw new ArtifactException(gripe,e);
			}
		}
		
	}
}
