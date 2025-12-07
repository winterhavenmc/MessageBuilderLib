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

package com.winterhavenmc.library.messagebuilder.adapters.resources.language;

import com.winterhavenmc.library.messagebuilder.models.keys.ConstantKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidConstantKey;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.SectionProvider;

import com.winterhavenmc.library.messagebuilder.models.language.Section;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class YamlConstantRepositoryTest
{
	@Mock
	YamlLanguageResourceManager languageResourceManagerMock;
	@Mock
	YamlLanguageSectionProvider languageSectionProviderMock;

	FileConfiguration languageConfig;
	SectionProvider languageSectionProvider;
	YamlConstantRepository constantRepository;

	String configString = """
		CONSTANTS:
		  TIME:
		    LESS_THAN: "less than {DURATION}"
		    UNLIMITED: "unlimited"
		
		  LOCATION:
		    SPAWN: "Spawn"
		    HOME: "Home"

		  STRING_LIST:
		    - "one"
		    - "two"
		    - "three"

		  INTEGER: 42

		  BOOLEAN: true
		""";


	@BeforeEach
	void setUp() throws InvalidConfigurationException
	{
		languageConfig = new YamlConfiguration();
		languageConfig.loadFromString(configString);

		Supplier<Configuration> configurationSupplier = () -> languageConfig;
		when(languageResourceManagerMock.getSectionProvider(Section.CONSTANTS.name())).thenReturn(languageSectionProviderMock);
		ConfigurationSection section = languageConfig.getConfigurationSection(Section.CONSTANTS.name());
		when(languageSectionProviderMock.getSection()).thenReturn(section);
		constantRepository = new YamlConstantRepository(languageResourceManagerMock);
	}


	@Test
	void getString_returns_empty_Optional_when_no_entry_for_key()
	{
		// Arrange
		ValidConstantKey validConstantKey = ConstantKey.of("KEY").isValid().orElseThrow();

		// Act
		Optional<java.lang.String> result = constantRepository.getString(validConstantKey);

		// Assert
		assertEquals(Optional.empty(), result);
	}


	@Test
	void getString_with_valid_key_returns_Optional_string()
	{
		// Arrange
		ValidConstantKey validConstantKey = ConstantKey.of("LOCATION.SPAWN").isValid().orElseThrow();

		// Act
		Optional<java.lang.String> result = constantRepository.getString(validConstantKey);

		// Assert
		assertEquals(Optional.of("Spawn"), result);
	}


	@Test
	void getStringList_with_valid_key_returns_List_of_String()
	{
		// Arrange
		ValidConstantKey validConstantKey = ConstantKey.of("STRING_LIST").isValid().orElseThrow();

		// Act
		List<java.lang.String> result = constantRepository.getStringList(validConstantKey);

		// Assert
		assertEquals(List.of("one", "two", "three"), result);
	}


	@Test
	void getStringList_with_valid_key_for_nonexistent_entry_returns_empty_List()
	{
		// Arrange
		ValidConstantKey validConstantKey = ConstantKey.of("NON_EXISTENT").isValid().orElseThrow();

		// Act
		List<java.lang.String> result = constantRepository.getStringList(validConstantKey);

		// Assert
		assertEquals(List.of(), result);
	}


	@Test
	void getInteger_with_valid_key_returns_Optional_Integer()
	{
		// Arrange
		ValidConstantKey validConstantKey = ConstantKey.of("INTEGER").isValid().orElseThrow();

		// Act
		Optional<Integer> result = constantRepository.getInteger(validConstantKey);

		// Assert
		assertEquals(Optional.of(42), result);
	}


	@Test
	void getInteger_with_valid_key_for_nonexistent_entry_returns_empty_Optional()
	{
		// Arrange
		ValidConstantKey validConstantKey = ConstantKey.of("NON_EXISTENT").isValid().orElseThrow();

		// Act
		Optional<Integer> result = constantRepository.getInteger(validConstantKey);

		// Assert
		assertEquals(Optional.empty(), result);
	}


	@Test
	void getBoolean_with_valid_key_returns_Optional_Boolean()
	{
		// Arrange
		ValidConstantKey validConstantKey = ConstantKey.of("BOOLEAN").isValid().orElseThrow();

		// Act
		Optional<Boolean> result = constantRepository.getBoolean(validConstantKey);

		// Assert
		assertEquals(Optional.of(true), result);
	}


	@Test
	void getBoolean_with_valid_key_for_nonexistent_entry_returns_empty_Optional()
	{
		// Arrange
		ValidConstantKey validConstantKey = ConstantKey.of("NON_EXISTENT").isValid().orElseThrow();

		// Act
		Optional<Boolean> result = constantRepository.getBoolean(validConstantKey);

		// Assert
		assertEquals(Optional.empty(), result);
	}

}
