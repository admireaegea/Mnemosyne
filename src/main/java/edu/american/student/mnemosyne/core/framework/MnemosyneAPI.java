package edu.american.student.mnemosyne.core.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;

import edu.american.student.mnemosyne.core.exception.ArtifactException;
import edu.american.student.mnemosyne.core.exception.DataspaceException;
import edu.american.student.mnemosyne.core.model.Artifact;
import edu.american.student.mnemosyne.core.util.AccumuloForeman;
import edu.american.student.mnemosyne.core.util.ArtifactForeman;
import edu.american.student.mnemosyne.core.util.NNInput;

public  abstract class MnemosyneAPI
{
	protected AccumuloForeman aForeman = new AccumuloForeman();
	protected ArtifactForeman artifactForeman = new ArtifactForeman();
	
	public abstract void setup() throws  DataspaceException;
	public String getOptionArtifact() throws ArtifactException
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
		ArrayList<double[]> values = new ArrayList<double[]>();
		int totalSize = 0;
		for(int i=0;i<inputFields.size();i++)
		{
			System.out.print(inputFields.get(i)+"=");
			Scanner in = new Scanner(System.in);
			boolean gotInput = false;
			while( !gotInput)
			{
				if(!gotInput)
				{
					double[] input = NNInput.inflate(in.next());
					totalSize +=input.length;
					values.add(input);
				}
				gotInput=true;
			}
			
			System.out.print("\n");
		}
		double[] binaryValues = new double[totalSize];
		int master = 0;
		for(double[] binary: values)
		{
			for(double bit: binary)
			{
				binaryValues[master]=bit;
				master++;
			}
		}
		
		MLData toReturn= new BasicMLData(binaryValues);
		return toReturn;
	}
}
