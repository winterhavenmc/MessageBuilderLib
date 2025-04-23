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

package com.winterhavenmc.util.messagebuilder.formatters.duration;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;


class BoundedDurationTest
{
	@Test
	void duration()
	{
		// Arrange
		BoundedDuration dp = new BoundedDuration(Duration.ofSeconds(10), ChronoUnit.SECONDS);

		// Act
		Duration result = dp.duration();

		// Assert
		assertEquals(Duration.ofSeconds(10), result);
	}

	@Test
	void precision()
	{
		// Arrange
		BoundedDuration dp = new BoundedDuration(Duration.ofSeconds(10), ChronoUnit.SECONDS);

		// Act
		ChronoUnit result = dp.precision();

		// Assert
		assertEquals(ChronoUnit.SECONDS, result);
	}

}
