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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.extractors.expiration;

import java.time.Instant;
import java.util.Optional;

import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.expiration.Expirable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ExpirationAdapterTest
{
	static class ValidTestObject implements Expirable
	{
		@Override
		public Instant getExpiration()
		{
			return Instant.EPOCH;
		}
	}


	static class InvalidTestObject
	{
		public Instant getExpiration()
		{
			return Instant.EPOCH;
		}
	}


	@Test
	void adapt_with_valid_parameter_returns_adapted_Expirable_object()
	{
		// Arrange
		ValidTestObject validTestObject = new ValidTestObject();
		Instant result = Instant.MIN;

		// Act
		Optional<Expirable> adapter = new ExpirationAdapter().adapt(validTestObject);
		if (adapter.isPresent())
		{
			result = adapter.get().getExpiration();
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
		Optional<Expirable> adapter = new ExpirationAdapter().adapt(invalidTestObject);

		// Assert
		assertEquals(Optional.empty(), adapter, "The adapter should return an empty Optional for invalid object.");
	}


	@Test
	void adapt_with_null_parameter_returns_empty_optional()
	{
		// Arrange & Act
		Optional<Expirable> adapter = new ExpirationAdapter().adapt(null);

		// Assert
		assertEquals(Optional.empty(), adapter, "The adapter should return an empty Optional for null object.");
	}


	@Test
	void supports_with_valid_object_returns_true()
	{
		// Arrange
		ValidTestObject validTestObject = new ValidTestObject();

		// Act
		boolean result = new ExpirationAdapter().supports(validTestObject);

		// Assert
		assertTrue(result);
	}


	@Test
	void supports_with_invalid_object_returns_false()
	{
		// Arrange
		InvalidTestObject invalidTestObject = new InvalidTestObject();

		// Act
		boolean result = new ExpirationAdapter().supports(invalidTestObject);

		// Assert
		assertFalse(result);
	}

}
