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

import com.winterhavenmc.library.messagebuilder.models.configuration.ConfigRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ValidationContextTest
{
	@Mock ConfigRepository configRepositoryMockUS;
	@Mock ConfigRepository configRepositoryMockFR;


	@AfterEach
	void tearDown() {
		ValidationContext.reset();
	}


	@Test @DisplayName("initialize() does not throw exception with valid parameter.")
	void initialize_does_not_throw_with_valid_parameter()
	{
		assertDoesNotThrow(() -> ValidationContext.initialize(configRepositoryMockUS));
	}

	@Test @DisplayName("initialize() throws ValidationException with null parameter.")
	void initialize_throws_exception_with_null_parameter()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class, () ->
			ValidationContext.initialize(null));

		// Assert
		assertEquals("The parameter 'localeProvider' cannot be null.", exception.getMessage());
	}

	@Test @DisplayName("initialize() does not overwrite existing value.")
	void initialize_twice_does_not_mutate_localeProvider_field()
	{
		// Arrange
		lenient().when(configRepositoryMockFR.locale()).thenReturn(Locale.FRANCE);
		lenient().when(configRepositoryMockUS.locale()).thenReturn(Locale.US);

		// Act
		ValidationContext.initialize(configRepositoryMockFR);
		ValidationContext.initialize(configRepositoryMockUS);

		// Assert
		assertEquals(Locale.FRANCE, ValidationContext.getLocale());
	}


	@Test @DisplayName("getLocale() returns valid locale object.")
	@Disabled
	void getLocale_returns_valid_locale_object()
	{
		// Arrange
		when(configRepositoryMockUS.locale()).thenReturn(Locale.FRANCE);

		// Act
		Locale result = ValidationContext.getLocale();

		// Assert
//		assertEquals(Locale.FRANCE, result);

		// Verify
		verify(configRepositoryMockUS, atLeastOnce()).locale();
	}

}
