package edu.american.student.mnemosyne.core.framework;

import java.io.IOException;

import org.apache.accumulo.core.client.TableNotFoundException;

public abstract class NNProcessor
{
	//TODO remove old paradigm stuff
	
	public abstract void constructNetworks(String artifactId) throws IOException, TableNotFoundException, ClassNotFoundException;
	
}
