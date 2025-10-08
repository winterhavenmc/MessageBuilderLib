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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.extractors.duration;

import java.time.Duration;
import java.util.Optional;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.extractors.duration.Durationable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class DurationAdapterTest
{
	static class ValidTestObject implements Durationable
	{
		@Override
		public Duration getDuration()
		{
			return Duration.ofSeconds(10);
		}
	}


	static class InvalidTestObject
	{
		public Duration getDuration()
		{
			return Duration.ofSeconds(10);
		}
	}


	@Test
	public void adapt_with_valid_parameter_returns_adapted_Durationable_object()
	{
		// Arrange
		ValidTestObject validTestObject = new ValidTestObject();
		Duration result = Duration.ZERO;

		// Act
		Optional<Durationable> adapter = new DurationAdapter().adapt(validTestObject);
		if (adapter.isPresent())
		{
			result = adapter.get().getDuration();
		}

		// Assert
		assertEquals(Duration.ofSeconds(10), result, "The adapter should return the adapted Duration.");
	}


	@Test
	public void adapt_with_invalid_parameter_returns_empty_optional()
	{
		InvalidTestObject invalidTestObject = new InvalidTestObject();
		// Arrange & Act
		Optional<Durationable> adapter = new DurationAdapter().adapt(invalidTestObject);

		// Assert
		assertEquals(Optional.empty(), adapter, "The adapter should return an empty Optional for invalid object.");
	}


	@Test
	public void adapt_with_null_parameter_returns_empty_optional()
	{
		// Arrange & Act
		Optional<Durationable> adapter = new DurationAdapter().adapt(null);

		// Assert
		assertEquals(Optional.empty(), adapter, "The adapter should return an empty Optional for null object.");
	}

}
