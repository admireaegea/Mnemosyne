package edu.american.student.mnemosyne.core.framework;

import java.io.IOException;

import org.apache.accumulo.core.client.TableNotFoundException;

public abstract class NNProcessor
{

	
	public abstract void constructNetworks() throws IOException, TableNotFoundException, ClassNotFoundException;
	
	public abstract void assignNodes();
	
	public abstract void process();
}
