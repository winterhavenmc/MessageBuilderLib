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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters;

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.displayname.DisplayNameAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.displayname.DisplayNameable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.duration.DurationAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.duration.Durationable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.expiration.Expirable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.expiration.ExpirationAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.instant.InstantAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.instant.Instantable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.killer.Killable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.killer.KillerAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.location.Locatable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.location.LocationAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.looter.Lootable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.looter.LooterAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.NameAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.Nameable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.owner.Ownable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.owner.OwnerAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.protection.Protectable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.protection.ProtectionAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.quantity.Quantifiable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.quantity.QuantityAdapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.uuid.Identifiable;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.uuid.UniqueIdAdapter;

import org.bukkit.Location;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class AdapterRegistryTest
{
	@Mock Expirable expirableMock;
	@Mock AdapterContextContainer adapterContextContainerMock;
	@Mock Player playerMock;
	@Mock Location locationMock;

	AdapterRegistry registry;
	ExpirationAdapter expirationAdapter = new ExpirationAdapter();
	LocationAdapter locationAdapter = new LocationAdapter();


	@BeforeEach
	void setUp()
	{
		registry = new AdapterRegistry(adapterContextContainerMock);
	}


	@Test
	void GetMatchingAdapters_returns_DisplayNameAdapter()
	{
		// Arrange
		class SampleEntity implements DisplayNameable
		{
			@Override
			public String getDisplayName()
			{
				return "Display Name";
			}
		}

		SampleEntity entity = new SampleEntity();

		// Act
		long result = registry.getMatchingAdapters(entity).filter(DisplayNameAdapter.class::isInstance).count();

		// Assert
		assertEquals(1, result);
	}


	@Test
	void GetMatchingAdapters_returns_DurationAdapter()
	{
		// Arrange
		class SampleEntity implements Durationable
		{
			@Override
			public Duration getDuration()
			{
				return Duration.ZERO;
			}
		}

		SampleEntity entity = new SampleEntity();

		// Act
		long result = registry.getMatchingAdapters(entity).filter(DurationAdapter.class::isInstance).count();

		// Assert
		assertEquals(1, result);
	}


	@Test
	void GetMatchingAdapters_returns_ExpirationAdapter()
	{
		// Arrange
		class SampleEntity implements Expirable
		{
			@Override
			public Instant getExpiration()
			{
				return Instant.now();
			}
		}

		SampleEntity entity = new SampleEntity();

		// Act
		long result = registry.getMatchingAdapters(entity).filter(ExpirationAdapter.class::isInstance).count();

		// Assert
		assertEquals(1, result);
	}


	@Test
	void GetMatchingAdapters_returns_InstantAdapter()
	{
		// Arrange
		class SampleEntity implements Instantable
		{
			@Override
			public Instant getInstant()
			{
				return Instant.now();
			}
		}

		SampleEntity entity = new SampleEntity();

		// Act
		long result = registry.getMatchingAdapters(entity).filter(InstantAdapter.class::isInstance).count();

		// Assert
		assertEquals(1, result);
	}


	@Test
	void GetMatchingAdapters_returns_KillerAdapter()
	{
		// Arrange
		class SampleEntity implements Killable
		{
			@Override
			public AnimalTamer getKiller()
			{
				return playerMock;
			}
		}

		SampleEntity entity = new SampleEntity();

		// Act
		long result = registry.getMatchingAdapters(entity).filter(KillerAdapter.class::isInstance).count();

		// Assert
		assertEquals(1, result);
	}


	@Test
	void GetMatchingAdapters_returns_LocationAdapter()
	{
		// Arrange
		class SampleEntity implements Locatable
		{
			@Override
			public Location getLocation()
			{
				return locationMock;
			}
		}

		SampleEntity entity = new SampleEntity();

		// Act
		long result = registry.getMatchingAdapters(entity).filter(LocationAdapter.class::isInstance).count();

		// Assert
		assertEquals(1, result);
	}


	@Test
	void GetMatchingAdapters_returns_LooterAdapter()
	{
		// Arrange
		class SampleEntity implements Lootable
		{
			@Override
			public AnimalTamer getLooter()
			{
				return playerMock;
			}
		}

		SampleEntity entity = new SampleEntity();

		// Act
		long result = registry.getMatchingAdapters(entity).filter(LooterAdapter.class::isInstance).count();

		// Assert
		assertEquals(1, result);
	}


	@Test
	void GetMatchingAdapters_returns_NameAdapter()
	{
		// Arrange
		class SampleEntity implements Nameable
		{
			@Override
			public String getName()
			{
				return "Name";
			}
		}

		SampleEntity entity = new SampleEntity();

		// Act
		long result = registry.getMatchingAdapters(entity).filter(NameAdapter.class::isInstance).count();

		// Assert
		assertEquals(1, result);
	}


	@Test
	void GetMatchingAdapters_returns_OwnerAdapter()
	{
		// Arrange
		class SampleEntity implements Ownable
		{
			@Override
			public AnimalTamer getOwner()
			{
				return playerMock;
			}
		}

		SampleEntity entity = new SampleEntity();

		// Act
		long result = registry.getMatchingAdapters(entity).filter(OwnerAdapter.class::isInstance).count();

		// Assert
		assertEquals(1, result);
	}


	@Test
	void GetMatchingAdapters_returns_ProtectionAdapter()
	{
		// Arrange
		class SampleEntity implements Protectable
		{
			@Override
			public Instant getProtection()
			{
				return Instant.now();
			}
		}

		SampleEntity entity = new SampleEntity();

		// Act
		long result = registry.getMatchingAdapters(entity).filter(ProtectionAdapter.class::isInstance).count();

		// Assert
		assertEquals(1, result);
	}


	@Test
	void GetMatchingAdapters_returns_QuantityAdapter()
	{
		// Arrange
		class SampleEntity implements Quantifiable
		{
			@Override
			public int getQuantity()
			{
				return 1;
			}
		}

		SampleEntity entity = new SampleEntity();

		// Act
		long result = registry.getMatchingAdapters(entity).filter(QuantityAdapter.class::isInstance).count();

		// Assert
		assertEquals(1, result);
	}


	@Test
	void GetMatchingAdapters_returns_UniqueIdAdapter()
	{
		// Arrange
		class SampleEntity implements Identifiable
		{
			@Override
			public UUID getUniqueId()
			{
				return UUID.randomUUID();
			}
		}

		SampleEntity entity = new SampleEntity();

		// Act
		long result = registry.getMatchingAdapters(entity).filter(UniqueIdAdapter.class::isInstance).count();

		// Assert
		assertEquals(1, result);
	}


	@Test
	void GetMatchingAdapters_returns_correct_adapters()
	{
		class SampleEntity implements Nameable, DisplayNameable
		{
			@Override
			public String getName()
			{
				return "Name";
			}

			@Override
			public String getDisplayName()
			{
				return "Display Name";
			}
		}

		SampleEntity entity = new SampleEntity();
		assertEquals(2, registry.getMatchingAdapters(entity).count(), "Should return 2 matching adapters");
	}


	@Test
	void testGetMatchingAdaptersReturnsEmptyStreamForNull()
	{
		assertTrue(registry.getMatchingAdapters(null).findAny().isEmpty());
	}

}
