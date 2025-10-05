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

package com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Instant;
import java.util.Optional;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class InstantAdapterTest
{
	static class ValidTestObject implements Instantable
	{
		@Override
		public Instant getInstant()
		{
			return Instant.EPOCH;
		}
	}


	static class InvalidTestObject
	{
		public Instant getInstant()
		{
			return Instant.EPOCH;
		}
	}


	@Test
	public void adapt_with_valid_object_returns_adapted_Instantable_object()
	{
		// Arrange
		ValidTestObject validTestObject = new ValidTestObject();
		Instant result = Instant.MIN;

		// Act
		Optional<Instantable> adapter = new InstantAdapter().adapt(validTestObject);
		if (adapter.isPresent())
		{
			result = adapter.get().getInstant();
		}

		// Assert
		assertEquals(Instant.EPOCH, result, "The adapter should return the adapted Instant.");
	}


	@Test
	public void adapt_with_invalid_object_returns_empty_optional()
	{
		// Arrange & Act
		InvalidTestObject invalidTestObject = new InvalidTestObject();
		Optional<Instantable> adapter = new InstantAdapter().adapt(invalidTestObject);

		// Assert
		assertEquals(Optional.empty(), adapter, "The adapter should return an empty Optional for invalid object.");
	}


	@Test
	public void adapt_with_null_parameter_returns_empty_optional()
	{
		// Arrange & Act
		Optional<Instantable> adapter = new InstantAdapter().adapt(null);

		// Assert
		assertEquals(Optional.empty(), adapter, "The adapter should return an empty Optional for null object.");
	}

}
