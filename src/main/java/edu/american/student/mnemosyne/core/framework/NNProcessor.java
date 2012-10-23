package edu.american.student.mnemosyne.core.framework;

import edu.american.student.mnemosyne.core.exception.RepositoryException;

public abstract class NNProcessor
{	
	public abstract void constructNetworks(String artifactId) throws RepositoryException;
	
}
