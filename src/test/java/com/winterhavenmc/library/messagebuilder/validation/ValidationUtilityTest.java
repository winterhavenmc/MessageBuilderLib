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

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static com.winterhavenmc.library.messagebuilder.validation.ValidationUtility.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ValidationUtilityTest
{
	@Test
	void testFormatMessage()
	{
		String result = formatMessage(ErrorMessageKey.PARAMETER_NULL, Parameter.RECIPIENT);

		assertEquals("The parameter 'recipient' cannot be null.", result);
	}


	@Test
	void testGetConfiguredLocale()
	{
		Locale result = getConfiguredLocale();

		assertEquals(Locale.getDefault(), result);
	}


	@Test
	void testBundleName()
	{
		assertEquals("exception.messages", BUNDLE_NAME);
	}

}
