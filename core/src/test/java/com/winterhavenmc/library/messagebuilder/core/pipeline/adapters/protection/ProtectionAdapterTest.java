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

package com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.protection;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ProtectionAdapterTest
{
	static class ValidTestObject implements Protectable
	{
		@Override
		public Instant getProtection()
		{
			return Instant.EPOCH;
		}
	}


	static class InvalidTestObject
	{
		public Instant getProtection()
		{
			return Instant.EPOCH;
		}
	}


	@Test
	void adapt_with_valid_parameter_returns_adapted_Protectable_object()
	{
		// Arrange
		ValidTestObject validTestObject = new ValidTestObject();
		Instant result = Instant.MIN;

		// Act
		Optional<Protectable> adapter = new ProtectionAdapter().adapt(validTestObject);
		if (adapter.isPresent())
		{
			result = adapter.get().getProtection();
		}

		// Assert
		assertEquals(Instant.EPOCH, result, "The adapter should return the adapted Instant.");
	}


	@Test
	void adapt_with_invalid_parameter_returns_empty_optional()
	{
		// Arrange
		InvalidTestObject invalidTestObject = new InvalidTestObject();

		// Act
		Optional<Protectable> adapter = new ProtectionAdapter().adapt(invalidTestObject);

		// Assert
		assertEquals(Optional.empty(), adapter, "The adapter should return an empty Optional for invalid object.");
	}


	@Test
	void adapt_with_null_parameter_returns_empty_optional()
	{
		// Arrange & Act
		Optional<Protectable> adapter = new ProtectionAdapter().adapt(null);

		// Assert
		assertEquals(Optional.empty(), adapter, "The adapter should return an empty Optional for null object.");
	}


	@Test
	void supports_with_valid_object_returns_true()
	{
		// Arrange
		ValidTestObject validTestObject = new ValidTestObject();

		// Act
		boolean result = new ProtectionAdapter().supports(validTestObject);

		// Assert
		assertTrue(result);
	}


	@Test
	void supports_with_invalid_object_returns_false()
	{
		// Arrange
		InvalidTestObject invalidTestObject = new InvalidTestObject();

		// Act
		boolean result = new ProtectionAdapter().supports(invalidTestObject);

		// Assert
		assertFalse(result);
	}

}
