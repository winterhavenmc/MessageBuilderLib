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

package com.winterhavenmc.util.messagebuilder.validation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;
class LoggingTest
{
	@ParameterizedTest
	@EnumSource
	void handleInvalid_valid(LogLevel logLevel)
	{
		Logging<String> logging = new Logging<>(logLevel, ErrorMessageKey.PARAMETER_NULL, Parameter.RECIPIENT);
		String string = logging.handleInvalid("a string");

		assertNotNull(string);
		assertEquals("a string", string);
	}


	@ParameterizedTest
	@EnumSource
	void handleInvalid_null(LogLevel logLevel)
	{
		Logging<String> logging = new Logging<>(logLevel, ErrorMessageKey.PARAMETER_NULL, Parameter.RECIPIENT);
		String string = logging.handleInvalid(null);

		assertNull(string);
	}

}
