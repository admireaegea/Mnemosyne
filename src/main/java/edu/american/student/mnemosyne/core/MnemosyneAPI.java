package edu.american.student.mnemosyne.core;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.accumulo.core.client.TableNotFoundException;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.xml.sax.SAXException;

import edu.american.student.mnemosyne.core.model.Artifact;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.ArtifactForeman;

public  abstract class MnemosyneAPI
{
	protected AccumuloForeman aForeman = new AccumuloForeman();
	protected ArtifactForeman artifactForeman = new ArtifactForeman();
	
	public abstract void setup();
	public String getOptionArtifact() throws ParserConfigurationException, SAXException, IOException, TableNotFoundException
	{
		//this.artifactForeman.persistArtifacts();
		List<Artifact> artifacts = this.artifactForeman.returnArtifacts();
		Artifact artifact = promptForArtifacts(artifacts);
		return artifact.getArtifactId();
	}
	private Artifact promptForArtifacts(List<Artifact> artifacts)
	{
		System.out.println("Choose an artifact:");
		for(int i=0;i<artifacts.size();i++)
		{
			Artifact artifact = artifacts.get(i);
			System.out.println("["+i+"] "+artifact.getArtifactId());
		}
		Scanner in = new Scanner(System.in);
		int choice = in.nextInt();
		
		return artifacts.get(choice);
	}
	
	public MLData getMLDataInput(List<String> inputFields)
	{
		
		double[] values = new double[inputFields.size()];
		for(int i=0;i<values.length;i++)
		{
			System.out.print(inputFields.get(i)+"=");
			Scanner in = new Scanner(System.in);
			boolean gotInput = false;
			while(in.hasNextDouble() && !gotInput)
			{
				if(!gotInput)
				{
					values[i]=Double.parseDouble(in.next());
				}
				gotInput=true;
			}
			
			System.out.print("\n");
		}
		MLData toReturn= new BasicMLData(values);
		return toReturn;
	}
}
