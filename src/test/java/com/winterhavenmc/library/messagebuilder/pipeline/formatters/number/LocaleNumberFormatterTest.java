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

package com.winterhavenmc.library.messagebuilder.pipeline.formatters.number;

import com.winterhavenmc.library.messagebuilder.resources.configuration.LocaleProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LocaleNumberFormatterTest
{
	@Mock
	LocaleProvider localeProviderMock;


	@Test
	void getFormatted_US()
	{
		when(localeProviderMock.getLocale()).thenReturn(Locale.US);
		LocaleNumberFormatter formatter = new LocaleNumberFormatter(localeProviderMock);

		String result = formatter.getFormatted(42000);

		assertEquals("42,000", result);
	}


	@Test
	void getFormatted_DE()
	{
		when(localeProviderMock.getLocale()).thenReturn(Locale.GERMAN);
		LocaleNumberFormatter formatter = new LocaleNumberFormatter(localeProviderMock);

		String result = formatter.getFormatted(42000);

		assertEquals("42.000", result);
	}

}
