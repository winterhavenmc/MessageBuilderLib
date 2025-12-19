package com.winterhavenmc.library.messagebuilder.models.keys;

import java.util.Locale;

public interface Reason
{
	String name();
	String getDefaultMessage();
	String getLocalizedMessage(Locale locale);
	String getLocalizedMessage(Locale locale, Object... objects);
}
