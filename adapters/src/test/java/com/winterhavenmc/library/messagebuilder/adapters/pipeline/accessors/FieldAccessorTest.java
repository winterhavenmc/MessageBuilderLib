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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.pluralname.PluralNameAdapter;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.FieldAccessor;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.pluralname.PluralNameable;
import com.winterhavenmc.library.messagebuilder.models.configuration.ConfigRepository;

import com.winterhavenmc.library.messagebuilder.core.context.AccessorCtx;
import com.winterhavenmc.library.messagebuilder.core.context.FormatterCtx;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.displayname.DisplayNameAdapter;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.displayname.DisplayNameable;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.duration.DurationAdapter;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.duration.Durationable;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.expiration.Expirable;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.expiration.ExpirationAdapter;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.instant.Instantable;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.instant.InstantAdapter;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.killer.Killable;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.killer.KillerAdapter;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.location.Locatable;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.location.LocationAdapter;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.looter.Lootable;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.looter.LooterAdapter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.name.NameAdapter;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.name.Nameable;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.owner.Ownable;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.owner.OwnerAdapter;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.quantity.Quantifiable;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.quantity.BukkitQuantityAccessor;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.protection.Protectable;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.protection.ProtectionAdapter;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.identity.Identifiable;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.identity.UniqueIdAdapter;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.formatters.duration.DurationFormatter;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.formatters.number.NumberFormatter;

import com.winterhavenmc.library.messagebuilder.models.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.UUID;

import static com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor.BuiltIn.DURATION;
import static com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor.BuiltIn.EXPIRATION;
import static com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor.BuiltIn.INSTANT;
import static com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor.BuiltIn.KILLER;
import static com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor.BuiltIn.LOOTER;
import static com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor.BuiltIn.NAME;
import static com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor.BuiltIn.OWNER;
import static com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor.BuiltIn.PROTECTION;
import static com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor.BuiltIn.QUANTITY;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FieldAccessorTest
{
	@Mock Player playerMock;
	@Mock World worldMock;
	@Mock DisplayNameAdapter displayNameAdapterMock;
	@Mock PluralNameAdapter pluralNameAdapterMock;

	@Mock DisplayNameable displayNameableMock;
	@Mock PluralNameable pluralNameableMock;
	@Mock Identifiable identifiableMock;
	@Mock Locatable locatableMock;

	@Mock AccessorCtx adapterContextContainerMock;
	@Mock FormatterCtx formatterContainerMock;
	@Mock DurationFormatter durationFormatterMock;
	@Mock NumberFormatter numberFormatterMock;
	@Mock ConfigRepository configRepositoryMock;

	FieldAccessor extractor;
	ValidMacroKey baseKey;


	@BeforeEach
	void setup()
	{
		extractor = new MacroFieldAccessor(adapterContextContainerMock);
		baseKey = MacroKey.of("TEST").isValid().orElseThrow();
	}


	@Test
	@DisplayName("DisplayNameAdapter adapts DisplayNameable objects.")
	void DisplayNameAdapter_adapts_DisplayNameable_objects()
	{
		// Arrange
		MacroStringMap expected = new MacroStringMap();
		expected.put(baseKey, "FancyName");
		expected.put(baseKey.append(Accessor.BuiltIn.DISPLAY_NAME).isValid().orElseThrow(), "FancyName_subfield");
		when(displayNameableMock.getDisplayName()).thenReturn("FancyName");
		when(displayNameableMock.extractDisplayName(baseKey, adapterContextContainerMock)).thenReturn(expected);

		// Act
		MacroStringMap result = extractor.extract(baseKey, displayNameAdapterMock, displayNameableMock);

		// Assert
		assertEquals(2, result.size());
		assertNotNull(result.get(baseKey));
		assertEquals("FancyName", result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append(Accessor.BuiltIn.DISPLAY_NAME).isValid().orElseThrow()));

		// Verify
		verify(displayNameableMock, atLeastOnce()).extractDisplayName(any(), any());
	}


	@Test
	@DisplayName("PluralNameAdapter adapts PluralNameable objects.")
	void PluralNameAdapter_adapts_PluralNameable_objects()
	{
		// Arrange
		MacroStringMap expected = new MacroStringMap();
		expected.put(baseKey, "PluralName");
		expected.put(baseKey.append(Accessor.BuiltIn.PLURAL_NAME).isValid().orElseThrow(), "PluralName_subfield");
		when(pluralNameableMock.getPluralName()).thenReturn("PluralName");
		when(pluralNameableMock.extractPluralName(baseKey, adapterContextContainerMock)).thenReturn(expected);

		// Act
		MacroStringMap result = extractor.extract(baseKey, pluralNameAdapterMock, pluralNameableMock);

		// Assert
		assertEquals(2, result.size());
		assertNotNull(result.get(baseKey));
		assertEquals("PluralName", result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append(Accessor.BuiltIn.PLURAL_NAME).isValid().orElseThrow()));

		// Verify
		verify(pluralNameableMock, atLeastOnce()).extractPluralName(any(), any());
	}


	@Test @DisplayName("DurationAdapter adapts Durationable objects.")
	void DurationAdapter_adapts_Durationable_objects()
	{
		// Arrange
		class TestObject implements Durationable
		{
			@Override
			public Duration getDuration()
			{
				return Duration.ofMinutes(10);
			}
		}

		DurationAdapter durationAdapter = new DurationAdapter();
		TestObject testObject = new TestObject();
		ValidMacroKey subKey = baseKey.append(DURATION).isValid().orElseThrow();

		when(adapterContextContainerMock.formatterCtx()).thenReturn(formatterContainerMock);
		when(formatterContainerMock.durationFormatter()).thenReturn(durationFormatterMock);
		when(durationFormatterMock.format(any(), eq(ChronoUnit.MINUTES))).thenReturn("formatted duration");

		// Act
		MacroStringMap result = extractor.extract(baseKey, durationAdapter, testObject);

		// Assert
		assertEquals(1, result.size());
		assertNotNull(result.get(subKey));
		assertEquals("formatted duration", result.get(subKey));
		assertTrue(result.containsKey(baseKey.append(Accessor.BuiltIn.DURATION).isValid().orElseThrow()));

		// Verify
		verify(formatterContainerMock, atLeastOnce()).durationFormatter();
		verify(durationFormatterMock, atLeastOnce()).format(any(), eq(ChronoUnit.MINUTES));
	}


	@Test @DisplayName("DurationAdapter adapts Durationable objects.")
	void ExpirationAdapter_adapts_Expirable_objects()
	{
		// Arrange
		class TestObject implements Expirable
		{
			@Override
			public Instant getExpiration()
			{
				return Instant.EPOCH;
			}
		}

		ExpirationAdapter expirationAdapter = new ExpirationAdapter();
		TestObject testObject = new TestObject();

		ValidMacroKey subKey = baseKey.append(EXPIRATION).isValid().orElseThrow();
		ValidMacroKey durationKey = subKey.append(DURATION).isValid().orElseThrow();
		ValidMacroKey instantKey = subKey.append(INSTANT).isValid().orElseThrow();

		when(adapterContextContainerMock.formatterCtx()).thenReturn(formatterContainerMock);
		when(formatterContainerMock.durationFormatter()).thenReturn(durationFormatterMock);
		when(formatterContainerMock.configRepository()).thenReturn(configRepositoryMock);
		when(durationFormatterMock.format(any(), eq(ChronoUnit.MINUTES))).thenReturn("formatted duration");
		when(configRepositoryMock.zoneId()).thenReturn(ZoneId.of("UTC"));
		when(configRepositoryMock.dateLocale()).thenReturn(Locale.US);

		// Act
		MacroStringMap result = extractor.extract(baseKey, expirationAdapter, testObject);

		// Assert
		assertEquals("formatted duration", result.get(durationKey));
		assertEquals("Jan 1, 1970, 12:00:00 AM", result.get(instantKey));

		// Verify
		verify(adapterContextContainerMock, atLeastOnce()).formatterCtx();
		verify(formatterContainerMock, atLeastOnce()).durationFormatter();
		verify(formatterContainerMock, atLeastOnce()).configRepository();
		verify(durationFormatterMock, atLeastOnce()).format(any(), eq(ChronoUnit.MINUTES));
		verify(configRepositoryMock, atLeastOnce()).zoneId();
	}


	@Test @DisplayName("InstantAdapter adapts Instantable objects.")
	void InstantAdapter_adapts_Instantable_objects()
	{
		// Arrange
		class TestObject implements Instantable
		{
			@Override
			public Instant getInstant()
			{
				return Instant.EPOCH;
			}
		}

		ValidMacroKey subKey = baseKey.append(INSTANT).isValid().orElseThrow();
		InstantAdapter instantAdapter = new InstantAdapter();
		TestObject testObject = new TestObject();

		when(adapterContextContainerMock.formatterCtx()).thenReturn(formatterContainerMock);
		when(formatterContainerMock.configRepository()).thenReturn(configRepositoryMock);
		when(configRepositoryMock.zoneId()).thenReturn(ZoneId.of("UTC"));
		when(configRepositoryMock.dateLocale()).thenReturn(Locale.US);

		// Act
		MacroStringMap result = extractor.extract(baseKey, instantAdapter, testObject);

		// Assert
		assertEquals("Jan 1, 1970, 12:00:00 AM", result.get(subKey));

		// Verify
		verify(adapterContextContainerMock, atLeastOnce()).formatterCtx();
		verify(formatterContainerMock, atLeastOnce()).configRepository();
		verify(configRepositoryMock, atLeastOnce()).zoneId();
	}


	@Test @DisplayName("KillerAdapter adapts Killable objects.")
	void KillerAdapter_adapts_Killable_objects()
	{
		// Arrange
		class TestObject implements Killable
		{
			@Override
			public AnimalTamer getKiller()
			{
				return playerMock;
			}
		}

		ValidMacroKey subKey = baseKey.append(KILLER).isValid().orElseThrow();
		KillerAdapter killerAdapter = new KillerAdapter();
		TestObject testObject = new TestObject();
		when(playerMock.getName()).thenReturn("Killer Name");

		// Act
		MacroStringMap result = extractor.extract(baseKey, killerAdapter, testObject);

		// Assert
		assertEquals("Killer Name", result.get(subKey));

		// Verify
		verify(playerMock, atLeastOnce()).getName();
	}


	@Test @DisplayName("LocationAdapter adapts Locatable objects.")
	void LocationAdapter_adapts_Locatable_objects()
	{
		// Arrange
		LocationAdapter locationAdapter = new LocationAdapter();
		Location location = new Location(worldMock, 11, 12,13);
		ValidMacroKey locationKey = baseKey.append("LOCATION").isValid().orElseThrow();
		MacroStringMap expected = new MacroStringMap();
		expected.put(locationKey, "test-world [11, 12, 13]");
		expected.put(locationKey.append("STRING").isValid().orElseThrow(), "test-world [11, 12, 13]");
		expected.put(locationKey.append("WORLD").isValid().orElseThrow(), "test-world");
		expected.put(locationKey.append("X").isValid().orElseThrow(), "11");
		expected.put(locationKey.append("Y").isValid().orElseThrow(), "12");
		expected.put(locationKey.append("Z").isValid().orElseThrow(), "13");
		when(locatableMock.extractLocation(baseKey, adapterContextContainerMock)).thenReturn(expected);

		// Act
		MacroStringMap result = extractor.extract(baseKey, locationAdapter, locatableMock);

		// Assert
		assertEquals("test-world [11, 12, 13]", result.get(locationKey));
		assertEquals("test-world", result.get(locationKey.append("WORLD").isValid().orElseThrow()));
		assertEquals("11", result.get(locationKey.append("X").isValid().orElseThrow()));
		assertEquals("12", result.get(locationKey.append("Y").isValid().orElseThrow()));
		assertEquals("13", result.get(locationKey.append("Z").isValid().orElseThrow()));
		assertTrue(result.containsKey(locationKey));

		// Verify
		verify(locatableMock, atLeastOnce()).extractLocation(any(), any());
	}


	@Test @DisplayName("LocationAdapter adapts location string.")
	void LocationAdapter_adapts_location_key()
	{
		// Arrange
		LocationAdapter locationAdapter = new LocationAdapter();
		when(worldMock.getName()).thenReturn("test-world");
		Location location = new Location(worldMock, 11, 12,13);
		ValidMacroKey locationKey = MacroKey.of("LOCATION").isValid().orElseThrow();
		MacroStringMap expected = new MacroStringMap();
		expected.put(locationKey, "test-world [11, 12, 13]");
		expected.put(locationKey.append("WORLD").isValid().orElseThrow(), "test-world");
		expected.put(locationKey.append("X").isValid().orElseThrow(), "11");
		expected.put(locationKey.append("Y").isValid().orElseThrow(), "12");
		expected.put(locationKey.append("Z").isValid().orElseThrow(), "13");
		when(locatableMock.extractLocation(locationKey, adapterContextContainerMock)).thenReturn(expected);

		// Act
		MacroStringMap result = extractor.extract(locationKey, locationAdapter, locatableMock);

		// Assert
		assertEquals("test-world [11, 12, 13]", result.get(locationKey));
		assertEquals(worldMock.getName(), result.get(locationKey.append("WORLD").isValid().orElseThrow()));
		assertEquals(String.valueOf(location.getBlockX()), result.get(locationKey.append("X").isValid().orElseThrow()));
		assertEquals(String.valueOf(location.getBlockY()), result.get(locationKey.append("Y").isValid().orElseThrow()));
		assertEquals(String.valueOf(location.getBlockZ()), result.get(locationKey.append("Z").isValid().orElseThrow()));
		assertTrue(result.containsKey(locationKey));

		// Verify
		verify(locatableMock, atLeastOnce()).extractLocation(any(), any());
	}


	@Test @DisplayName("LocationAdapter adapts location with null World.")
	void LocationAdapter_adapts_with_null_world()
	{
		// Arrange
		LocationAdapter locationAdapter = new LocationAdapter();
		Location location = new Location(null, 11, 12,13);
		ValidMacroKey locationKey = MacroKey.of("LOCATION").isValid().orElseThrow();
		MacroStringMap expected = new MacroStringMap();
		expected.put(locationKey, "- [11, 12, 13]");
		expected.put(locationKey.append("STRING").isValid().orElseThrow(), "- [11, 12, 13]");
		expected.put(locationKey.append("WORLD").isValid().orElseThrow(), "-");
		expected.put(locationKey.append("X").isValid().orElseThrow(), "11");
		expected.put(locationKey.append("Y").isValid().orElseThrow(), "12");
		expected.put(locationKey.append("Z").isValid().orElseThrow(), "13");
		when(locatableMock.extractLocation(locationKey, adapterContextContainerMock)).thenReturn(expected);

		// Act
		MacroStringMap result = extractor.extract(locationKey, locationAdapter, locatableMock);

		// Assert
		assertEquals("- [11, 12, 13]", result.get(locationKey));
		assertEquals(String.valueOf(location.getBlockX()), result.get(locationKey.append("X").isValid().orElseThrow()));
		assertEquals(String.valueOf(location.getBlockY()), result.get(locationKey.append("Y").isValid().orElseThrow()));
		assertEquals(String.valueOf(location.getBlockZ()), result.get(locationKey.append("Z").isValid().orElseThrow()));
		assertTrue(result.containsKey(locationKey));

		// Verify
		verify(locatableMock, atLeastOnce()).extractLocation(any(), any());
	}


	@Test @DisplayName("LooterAdapter adapts Lootable objects.")
	void LooterAdapter_adapts_Lootable_objects()
	{
		// Arrange
		class TestObject implements Lootable
		{
			@Override
			public AnimalTamer getLooter() { return playerMock; }
		}

		ValidMacroKey subKey = baseKey.append(LOOTER).isValid().orElseThrow();
		LooterAdapter looterAdapter = new LooterAdapter();
		TestObject testObject = new TestObject();
		when(playerMock.getName()).thenReturn("Looter Name");

		// Act
		MacroStringMap result = extractor.extract(baseKey, looterAdapter, testObject);

		// Assert
		assertEquals("Looter Name", result.get(subKey));

		// Verify
		verify(playerMock, atLeastOnce()).getName();
	}


	@Test @DisplayName("NameAdapter adapts Nameable objects.")
	void NameAdapter_adapts_with_Nameable_objects()
	{
		// Arrange
		class TestObject implements Nameable
		{
			@Override
			public String getName() { return "Test Name"; }
		}

		ValidMacroKey subKey = baseKey.append(NAME).isValid().orElseThrow();
		NameAdapter nameAdapter = new NameAdapter(adapterContextContainerMock);
		TestObject testObject = new TestObject();

		// Act
		MacroStringMap result = extractor.extract(baseKey, nameAdapter, testObject);

		// Assert
		assertEquals("Test Name", result.get(subKey));
	}


	@Test @DisplayName("OwnerAdapter adapts Ownable objects.")
	void OwnerAdapter_adapts_Ownable_objects()
	{
		// Arrange
		class TestObject implements Ownable
		{
			@Override
			public AnimalTamer getOwner() { return playerMock; }
		}

		ValidMacroKey subKey = baseKey.append(OWNER).isValid().orElseThrow();
		OwnerAdapter ownerAdapter = new OwnerAdapter();
		TestObject testObject = new TestObject();
		when(playerMock.getName()).thenReturn("Owner Name");

		// Act
		MacroStringMap result = extractor.extract(baseKey, ownerAdapter, testObject);

		// Assert
		assertEquals("Owner Name", result.get(subKey));

		// Verify
		verify(playerMock, atLeastOnce()).getName();
	}


	@Test @DisplayName("ProtectionAdapter adapts Protectable objects.")
	void ProtectionAdapter_adapts_Protectable_objects()
	{
		// Arrange
		class TestObject implements Protectable
		{
			@Override
			public Instant getProtection() { return Instant.EPOCH; }
		}

		ValidMacroKey subKey = baseKey.append(PROTECTION).isValid().orElseThrow();
		ValidMacroKey durationKey = subKey.append(DURATION).isValid().orElseThrow();
		ValidMacroKey instantKey = subKey.append(INSTANT).isValid().orElseThrow();

		when(adapterContextContainerMock.formatterCtx()).thenReturn(formatterContainerMock);
		when(formatterContainerMock.durationFormatter()).thenReturn(durationFormatterMock);
		when(formatterContainerMock.configRepository()).thenReturn(configRepositoryMock);
		when(durationFormatterMock.format(any(), eq(ChronoUnit.MINUTES))).thenReturn("formatted duration");
		when(configRepositoryMock.zoneId()).thenReturn(ZoneId.of("UTC"));
		when(configRepositoryMock.dateLocale()).thenReturn(Locale.US);

		ProtectionAdapter protectionAdapter = new ProtectionAdapter();
		TestObject testObject = new TestObject();

		// Act
		MacroStringMap result = extractor.extract(baseKey, protectionAdapter, testObject);

		// Assert
		assertEquals("formatted duration", result.get(durationKey));
		assertEquals("Jan 1, 1970, 12:00:00 AM", result.get(instantKey));

		// Verify
		verify(durationFormatterMock, atLeastOnce()).format(any(), eq(ChronoUnit.MINUTES));
		verify(configRepositoryMock, atLeastOnce()).zoneId();
	}


	@Test @DisplayName("BukkitQuantityAccessor adapts Quantifiable objects.")
	void QuantityAdapter_adapts_Quantifiable_objects()
	{
		// Arrange
		class TestObject implements Quantifiable
		{
			@Override
			public int getQuantity() { return 10; }
		}

		ValidMacroKey subKey = baseKey.append(QUANTITY).isValid().orElseThrow();
		BukkitQuantityAccessor quantityAdapter = new BukkitQuantityAccessor();
		TestObject testObject = new TestObject();

		when(adapterContextContainerMock.formatterCtx()).thenReturn(formatterContainerMock);
		when(formatterContainerMock.localeNumberFormatter()).thenReturn(numberFormatterMock);
		when(numberFormatterMock.format(any())).thenReturn("formatted number");

		// Act
		MacroStringMap result = extractor.extract(baseKey, quantityAdapter, testObject);

		// Assert
		assertEquals("formatted number", result.get(subKey));

		// Verify
		verify(adapterContextContainerMock, atLeastOnce()).formatterCtx();
		verify(formatterContainerMock, atLeastOnce()).localeNumberFormatter();
		verify(numberFormatterMock, atLeastOnce()).format(any());
	}


	@Test @DisplayName("UniqueIdAdapter adapts Identifiable objects.")
	void UniqueIdAdapter_adapts_Identifiable_objects()
	{
		// Arrange
		UniqueIdAdapter uniqueIdAdapter = new UniqueIdAdapter();
		UUID uuid = new UUID(42, 42);
		MacroStringMap expected = new MacroStringMap();
		expected.put(baseKey, uuid.toString());
		expected.put(baseKey.append(Accessor.BuiltIn.UUID).isValid().orElseThrow(), uuid.toString());
		when(identifiableMock.extractUid(baseKey, adapterContextContainerMock)).thenReturn(expected);

		// Act
		MacroStringMap result = extractor.extract(baseKey, uniqueIdAdapter, identifiableMock);

		// Assert
		assertEquals(2, result.size());
		assertNotNull(result.get(baseKey));
		assertEquals(uuid.toString(), result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append(Accessor.BuiltIn.UUID).isValid().orElseThrow()));

		// Verify
		verify(identifiableMock, atLeastOnce()).extractUid(any(), any());
	}


	@Test @DisplayName("NoOpAdapter returns empty stream.")
	void NoOpAdapter_returns_empty_stream()
	{
		Accessor unknownAccessor = mock(Accessor.class);

		Object randomObject = new Object();
		MacroStringMap result = extractor.extract(baseKey, unknownAccessor, randomObject);

		assertTrue(result.isEmpty(), "Expected result to be empty for unmatched adapter type");
	}

}
