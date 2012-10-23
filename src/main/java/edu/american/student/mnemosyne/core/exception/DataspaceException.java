package edu.american.student.mnemosyne.core.exception;

public abstract class DataspaceException extends ProcessException
{

	public DataspaceException(String message)
	{
		super(message);
	}

	public DataspaceException(String message, Throwable throwable)
	{
		super(message,throwable);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2336933304983333809L;

}
