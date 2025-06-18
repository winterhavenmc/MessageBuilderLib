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

package com.winterhavenmc.library.messagebuilder.pipeline.extractor;

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.displayname.DisplayNameAdapter;
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
import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.displayname.DisplayNameable;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.LocalizedDurationFormatter;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.number.LocaleNumberFormatter;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.FormatterContainer;
import com.winterhavenmc.library.messagebuilder.resources.configuration.LocaleProvider;

import org.bukkit.Location;
import org.bukkit.World;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FieldExtractorTest
{
	@Mock DisplayNameAdapter displayNameAdapterMock;
	@Mock Player playerMock;
	@Mock World worldMock;

	@Mock DisplayNameable displayNameableMock;
	@Mock Identifiable identifiableMock;
	@Mock Locatable locatableMock;

	@Mock AdapterContextContainer adapterContextContainerMock;
	@Mock FormatterContainer formatterContainerMock;
	@Mock LocalizedDurationFormatter durationFormatterMock;
	@Mock LocaleNumberFormatter numberFormatterMock;
	@Mock LocaleProvider localeProviderMock;

	FieldExtractor extractor;
	MacroKey baseKey;


	@BeforeEach
	void setup()
	{
		extractor = new FieldExtractor(adapterContextContainerMock);
		baseKey = MacroKey.of("TEST").orElseThrow();
	}


	@Test @DisplayName("DisplayNameAdapter adapts DisplayNameable objects.")
	void DisplayNameAdapter_adapts_DisplayNameable_objects()
	{
		// Arrange
		MacroStringMap expected = new MacroStringMap();
		expected.put(baseKey, "FancyName");
		expected.put(baseKey.append(Adapter.BuiltIn.DISPLAY_NAME).orElseThrow(), "FancyName_subfield");
		when(displayNameableMock.getDisplayName()).thenReturn("FancyName");
		when(displayNameableMock.extractDisplayName(baseKey, adapterContextContainerMock)).thenReturn(expected);

		// Act
		MacroStringMap result = extractor.extract(baseKey, displayNameAdapterMock, displayNameableMock);

		// Assert
		assertEquals(2, result.size());
		assertNotNull(result.get(baseKey));
		assertEquals("FancyName", result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append(Adapter.BuiltIn.DISPLAY_NAME).orElseThrow()));

		// Verify
		verify(displayNameableMock, atLeastOnce()).extractDisplayName(any(), any());
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
		MacroKey subKey = baseKey.append(DURATION).orElseThrow();

		when(adapterContextContainerMock.formatterContainer()).thenReturn(formatterContainerMock);
		when(formatterContainerMock.durationFormatter()).thenReturn(durationFormatterMock);
		when(durationFormatterMock.format(any(), eq(ChronoUnit.MINUTES))).thenReturn("formatted duration");

		// Act
		MacroStringMap result = extractor.extract(baseKey, durationAdapter, testObject);

		// Assert
		assertEquals(1, result.size());
		assertNotNull(result.get(subKey));
		assertEquals("formatted duration", result.get(subKey));
		assertTrue(result.containsKey(baseKey.append(Adapter.BuiltIn.DURATION).orElseThrow()));

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

		MacroKey subKey = baseKey.append(EXPIRATION).orElseThrow();
		MacroKey durationKey = subKey.append(DURATION).orElseThrow();
		MacroKey instantKey = subKey.append(INSTANT).orElseThrow();

		when(adapterContextContainerMock.formatterContainer()).thenReturn(formatterContainerMock);
		when(formatterContainerMock.durationFormatter()).thenReturn(durationFormatterMock);
		when(formatterContainerMock.localeProvider()).thenReturn(localeProviderMock);
		when(durationFormatterMock.format(any(), eq(ChronoUnit.MINUTES))).thenReturn("formatted duration");
		when(localeProviderMock.getZoneId()).thenReturn(ZoneId.of("UTC"));

		// Act
		MacroStringMap result = extractor.extract(baseKey, expirationAdapter, testObject);

		// Assert
		assertEquals("formatted duration", result.get(durationKey));
		assertEquals("Jan 1, 1970, 12:00:00 AM", result.get(instantKey));

		// Verify
		verify(adapterContextContainerMock, atLeastOnce()).formatterContainer();
		verify(formatterContainerMock, atLeastOnce()).durationFormatter();
		verify(formatterContainerMock, atLeastOnce()).localeProvider();
		verify(durationFormatterMock, atLeastOnce()).format(any(), eq(ChronoUnit.MINUTES));
		verify(localeProviderMock, atLeastOnce()).getZoneId();
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

		MacroKey subKey = baseKey.append(INSTANT).orElseThrow();
		InstantAdapter instantAdapter = new InstantAdapter();
		TestObject testObject = new TestObject();

		when(adapterContextContainerMock.formatterContainer()).thenReturn(formatterContainerMock);
		when(formatterContainerMock.localeProvider()).thenReturn(localeProviderMock);
		when(localeProviderMock.getZoneId()).thenReturn(ZoneId.of("UTC"));

		// Act
		MacroStringMap result = extractor.extract(baseKey, instantAdapter, testObject);

		// Assert
		assertEquals("Jan 1, 1970, 12:00:00 AM", result.get(subKey));

		// Verify
		verify(adapterContextContainerMock, atLeastOnce()).formatterContainer();
		verify(formatterContainerMock, atLeastOnce()).localeProvider();
		verify(localeProviderMock, atLeastOnce()).getZoneId();
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

		MacroKey subKey = baseKey.append(KILLER).orElseThrow();
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
		MacroKey locationKey = baseKey.append("LOCATION").orElseThrow();
		MacroStringMap expected = new MacroStringMap();
		expected.put(locationKey, "test-world [11, 12, 13]");
		expected.put(locationKey.append("STRING").orElseThrow(), "test-world [11, 12, 13]");
		expected.put(locationKey.append("WORLD").orElseThrow(), "test-world");
		expected.put(locationKey.append("X").orElseThrow(), "11");
		expected.put(locationKey.append("Y").orElseThrow(), "12");
		expected.put(locationKey.append("Z").orElseThrow(), "13");
		when(locatableMock.extractLocation(baseKey, adapterContextContainerMock)).thenReturn(expected);

		// Act
		MacroStringMap result = extractor.extract(baseKey, locationAdapter, locatableMock);

		// Assert
		assertEquals("test-world [11, 12, 13]", result.get(locationKey));
		assertEquals("test-world", result.get(locationKey.append("WORLD").orElseThrow()));
		assertEquals("11", result.get(locationKey.append("X").orElseThrow()));
		assertEquals("12", result.get(locationKey.append("Y").orElseThrow()));
		assertEquals("13", result.get(locationKey.append("Z").orElseThrow()));
		assertTrue(result.containsKey(locationKey));

		// Verify
		verify(locatableMock, atLeastOnce()).extractLocation(any(), any());
	}


	@Test @DisplayName("LocationAdapter adapts location key.")
	void LocationAdapter_adapts_location_key()
	{
		// Arrange
		LocationAdapter locationAdapter = new LocationAdapter();
		when(worldMock.getName()).thenReturn("test-world");
		Location location = new Location(worldMock, 11, 12,13);
		MacroKey locationKey = MacroKey.of("LOCATION").orElseThrow();
		MacroStringMap expected = new MacroStringMap();
		expected.put(locationKey, "test-world [11, 12, 13]");
		expected.put(locationKey.append("WORLD").orElseThrow(), "test-world");
		expected.put(locationKey.append("X").orElseThrow(), "11");
		expected.put(locationKey.append("Y").orElseThrow(), "12");
		expected.put(locationKey.append("Z").orElseThrow(), "13");
		when(locatableMock.extractLocation(locationKey, adapterContextContainerMock)).thenReturn(expected);

		// Act
		MacroStringMap result = extractor.extract(locationKey, locationAdapter, locatableMock);

		// Assert
		assertEquals("test-world [11, 12, 13]", result.get(locationKey));
		assertEquals(worldMock.getName(), result.get(locationKey.append("WORLD").orElseThrow()));
		assertEquals(String.valueOf(location.getBlockX()), result.get(locationKey.append("X").orElseThrow()));
		assertEquals(String.valueOf(location.getBlockY()), result.get(locationKey.append("Y").orElseThrow()));
		assertEquals(String.valueOf(location.getBlockZ()), result.get(locationKey.append("Z").orElseThrow()));
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
		MacroKey locationKey = MacroKey.of("LOCATION").orElseThrow();
		MacroStringMap expected = new MacroStringMap();
		expected.put(locationKey, "- [11, 12, 13]");
		expected.put(locationKey.append("STRING").orElseThrow(), "- [11, 12, 13]");
		expected.put(locationKey.append("WORLD").orElseThrow(), "-");
		expected.put(locationKey.append("X").orElseThrow(), "11");
		expected.put(locationKey.append("Y").orElseThrow(), "12");
		expected.put(locationKey.append("Z").orElseThrow(), "13");
		when(locatableMock.extractLocation(locationKey, adapterContextContainerMock)).thenReturn(expected);

		// Act
		MacroStringMap result = extractor.extract(locationKey, locationAdapter, locatableMock);

		// Assert
		assertEquals("- [11, 12, 13]", result.get(locationKey));
		assertEquals(String.valueOf(location.getBlockX()), result.get(locationKey.append("X").orElseThrow()));
		assertEquals(String.valueOf(location.getBlockY()), result.get(locationKey.append("Y").orElseThrow()));
		assertEquals(String.valueOf(location.getBlockZ()), result.get(locationKey.append("Z").orElseThrow()));
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

		MacroKey subKey = baseKey.append(LOOTER).orElseThrow();
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

		MacroKey subKey = baseKey.append(NAME).orElseThrow();
		NameAdapter nameAdapter = new NameAdapter();
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

		MacroKey subKey = baseKey.append(OWNER).orElseThrow();
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

		MacroKey subKey = baseKey.append(PROTECTION).orElseThrow();
		MacroKey durationKey = subKey.append(DURATION).orElseThrow();
		MacroKey instantKey = subKey.append(INSTANT).orElseThrow();

		when(adapterContextContainerMock.formatterContainer()).thenReturn(formatterContainerMock);
		when(formatterContainerMock.durationFormatter()).thenReturn(durationFormatterMock);
		when(formatterContainerMock.localeProvider()).thenReturn(localeProviderMock);
		when(durationFormatterMock.format(any(), eq(ChronoUnit.MINUTES))).thenReturn("formatted duration");
		when(localeProviderMock.getZoneId()).thenReturn(ZoneId.of("UTC"));

		ProtectionAdapter protectionAdapter = new ProtectionAdapter();
		TestObject testObject = new TestObject();

		// Act
		MacroStringMap result = extractor.extract(baseKey, protectionAdapter, testObject);

		// Assert
		assertEquals("formatted duration", result.get(durationKey));
		assertEquals("Jan 1, 1970, 12:00:00 AM", result.get(instantKey));

		// Verify
		verify(durationFormatterMock, atLeastOnce()).format(any(), eq(ChronoUnit.MINUTES));
		verify(localeProviderMock, atLeastOnce()).getZoneId();
	}


	@Test @DisplayName("QuantityAdapter adapts Quantifiable objects.")
	void QuantityAdapter_adapts_Quantifiable_objects()
	{
		// Arrange
		class TestObject implements Quantifiable
		{
			@Override
			public int getQuantity() { return 10; }
		}

		MacroKey subKey = baseKey.append(QUANTITY).orElseThrow();
		QuantityAdapter quantityAdapter = new QuantityAdapter();
		TestObject testObject = new TestObject();

		when(adapterContextContainerMock.formatterContainer()).thenReturn(formatterContainerMock);
		when(formatterContainerMock.localeNumberFormatter()).thenReturn(numberFormatterMock);
		when(numberFormatterMock.getFormatted(any())).thenReturn("formatted number");

		// Act
		MacroStringMap result = extractor.extract(baseKey, quantityAdapter, testObject);

		// Assert
		assertEquals("formatted number", result.get(subKey));

		// Verify
		verify(adapterContextContainerMock, atLeastOnce()).formatterContainer();
		verify(formatterContainerMock, atLeastOnce()).localeNumberFormatter();
		verify(numberFormatterMock, atLeastOnce()).getFormatted(any());
	}


	@Test @DisplayName("UniqueIdAdapter adapts Identifiable objects.")
	void UniqueIdAdapter_adapts_Identifiable_objects()
	{
		// Arrange
		UniqueIdAdapter uniqueIdAdapter = new UniqueIdAdapter();
		UUID uuid = new UUID(42, 42);
		MacroStringMap expected = new MacroStringMap();
		expected.put(baseKey, uuid.toString());
		expected.put(baseKey.append(Adapter.BuiltIn.UUID).orElseThrow(), uuid.toString());
		when(identifiableMock.extractUid(baseKey, adapterContextContainerMock)).thenReturn(expected);

		// Act
		MacroStringMap result = extractor.extract(baseKey, uniqueIdAdapter, identifiableMock);

		// Assert
		assertEquals(2, result.size());
		assertNotNull(result.get(baseKey));
		assertEquals(uuid.toString(), result.get(baseKey));
		assertTrue(result.containsKey(baseKey.append(Adapter.BuiltIn.UUID).orElseThrow()));

		// Verify
		verify(identifiableMock, atLeastOnce()).extractUid(any(), any());
	}


	@Test @DisplayName("NoOpAdapter returns empty stream.")
	void NoOpAdapter_returns_empty_stream()
	{
		Adapter unknownAdapter = mock(Adapter.class);

		Object randomObject = new Object();
		MacroStringMap result = extractor.extract(baseKey, unknownAdapter, randomObject);

		assertTrue(result.isEmpty(), "Expected result to be empty for unmatched adapter type");
	}

}
