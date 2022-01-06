/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

/**
 * Exception raised when Pojo object can't be parsed
 */
public class ParsePojoException extends Exception
{
	private static final long serialVersionUID = 1L;

	public ParsePojoException(String message)
	{
		super(message);
	}

	public ParsePojoException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ParsePojoException(Throwable cause)
	{
		super(cause);
	}
}
