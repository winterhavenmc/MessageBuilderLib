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

package com.winterhavenmc.library.messagebuilder.models.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;


class LogLevelTest
{
	@ParameterizedTest(name = "{0} should map to correct java.util.logging.Level")
	@EnumSource(LogLevel.class)
	@DisplayName("Test LogLevel toJavaUtilLevel mappings")
	void testToJavaUtilLevel(LogLevel logLevel)
	{
		Level expected;
		switch (logLevel)
		{
			case TRACE -> expected = Level.FINEST;
			case DEBUG -> expected = Level.FINER;
			case INFO -> expected = Level.INFO;
			case WARN -> expected = Level.WARNING;
			case ERROR -> expected = Level.SEVERE;
			default -> throw new IllegalStateException("Unexpected value: " + logLevel);
		}

		assertEquals(expected, logLevel.toJavaUtilLevel(),
				"Expected " + logLevel + " to map to " + expected);
	}

}
