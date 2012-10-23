package edu.american.student.mnemosyne.core.exception;

public class ArtifactException extends DataspaceException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1571050733039586994L;

	public ArtifactException(String message)
	{
		super(message);
	}

	public ArtifactException(String message, Throwable throwable)
	{
		super(message, throwable);
	}

}
