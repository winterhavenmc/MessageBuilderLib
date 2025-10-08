package com.winterhavenmc.library.messagebuilder.models;

public enum DefaultSymbol
{
	NULL("ø"),
	BLANK("⬚"),
	UNKNOWN("???"),
	;

	private final String symbolString;


	DefaultSymbol(final String symbolString)
	{
		this.symbolString = symbolString;
	}


	public String symbol()
	{
		return this.symbolString;
	}
}
