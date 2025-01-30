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

package com.winterhavenmc.util.messagebuilder.util;

import org.junit.jupiter.api.Test;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.*;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;

class LocalizedExceptionTest {

	@Test
	void testConstructor() {
		LocalizedException exception = new LocalizedException(PARAMETER_NULL, RECIPIENT);
		assertEquals("The parameter 'recipient' cannot be null.", exception.getMessage());
	}

	@Test
	void testConstructor2() {
		LocalizedException exception = new LocalizedException(INVALID_SECTION, "CONSTANTS");
		assertEquals("The configuration section returned by the configuration supplier was an invalid 'CONSTANTS' section.", exception.getMessage());
	}

	@Test
	void getLocalizedMessage() {
		LocalizedException exception = new LocalizedException(PARAMETER_NULL, RECIPIENT);
		assertEquals("The parameter 'recipient' cannot be null.", exception.getMessage());
	}

	@Test
	void testToString() {
		assertEquals("recipient", RECIPIENT.toString());
	}

}
