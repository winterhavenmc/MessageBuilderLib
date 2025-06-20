/*
 * Copyright (c) 2025 Tim Savage.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.winterhavenmc.library.messagebuilder.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.STRING_BLANK;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.ADAPTER;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.MACRO;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.*;
import static org.junit.jupiter.api.Assertions.*;


class ValidatorTest
{
	@Test @DisplayName("formatMessage() retrieves message from resource bundle.")
	void formatMessage_retrieves_message_from_resource_bundle()
	{
		// Act
		String result = Validator.formatMessage(PARAMETER_NULL, Parameter.RECIPIENT);

		// Assert
		assertEquals("The parameter 'recipient' cannot be null.", result);
	}


	@Test
	void bundleName_matches_resource_bundle()
	{
		assertEquals("exception.messages", Validator.BUNDLE_NAME);
	}


	@Test
	void throwing_throws_validation_exception()
	{
		assertThrows(ValidationException.class, () ->
				validate(null, Objects::isNull, throwing(PARAMETER_NULL, ADAPTER)));
	}


	@Test
	void throwing_does_not_throws_with_valid_parameter()
	{
		assertDoesNotThrow(() ->
				validate("not null", Objects::isNull, throwing(PARAMETER_NULL, MACRO)));
	}


	@Test
	void logging_logs_validation_error()
	{
		assertDoesNotThrow(() -> validate("Valid String", String::isBlank, logging(LogLevel.WARN, STRING_BLANK, MACRO)));
	}
}
