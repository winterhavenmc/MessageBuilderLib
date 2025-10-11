package com.winterhavenmc.library.messagebuilder.models.language;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class InvalidRecordReasonTest
{
	@Test
	void getDefaultMessage()
	{
		String result = InvalidRecordReason.ITEM_KEY_INVALID.getDefaultMessage();

		assertEquals("Invalid item key.", result);
	}

	@Test
	void getLocalizedMessage_US()
	{
		String result = InvalidRecordReason.ITEM_KEY_INVALID.getLocalizedMessage(Locale.US);

		assertEquals("Invalid item key.", result);
	}

	@Test
	void getLocalizedMessage_FR()
	{
		String result = InvalidRecordReason.ITEM_KEY_INVALID.getLocalizedMessage(Locale.FRANCE);

		assertEquals("Invalid item key.", result);
	}

	@Test
	void getLocalizeMessage()
	{
		// Arrange
		Locale locale = Locale.US;
		Object[] objects = {"section"};

		// Act
		String result = InvalidRecordReason.MESSAGE_SECTION_MISSING.getLocalizeMessage(locale, objects);

		// Assert
		assertEquals("Missing message section.", result);
	}
}
