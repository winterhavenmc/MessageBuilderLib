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

import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.LanguageResourceManager;
import com.winterhavenmc.library.messagebuilder.models.keys.MessageKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMessageKey;
import com.winterhavenmc.library.messagebuilder.models.language.*;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.LanguageSectionProvider;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.SectionProvider;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class YamlMessageRepositoryTest
{
	@Mock LanguageResourceManager languageResourceManagerMock;

	FileConfiguration languageConfig;
	SectionProvider languageSectionProvider;
	String configString = """
		MESSAGES:
		  TEST_MESSAGE:
		    MESSAGE_TEXT: "This is a test message."
		""";


	@BeforeEach
	void setUp() throws InvalidConfigurationException
	{
		languageConfig = new YamlConfiguration();
		languageConfig.loadFromString(configString);

		Supplier<Configuration> configurationSupplier = () -> languageConfig;
		languageSectionProvider = new LanguageSectionProvider(configurationSupplier, Section.MESSAGES);
	}


	@Test
	void getMessageRecord_returns_invalid_record_when_no_entry_for_key()
	{
		// Arrange
		when(languageResourceManagerMock.getSectionProvider(Section.MESSAGES)).thenReturn(languageSectionProvider);
		ValidMessageKey validMessageKey = MessageKey.of("KEY").isValid().orElseThrow();

		// Act
		YamlMessageRepository messageRepository = new YamlMessageRepository(languageResourceManagerMock);
		MessageRecord result = messageRepository.getMessageRecord(validMessageKey);

		// Assert
		assertInstanceOf(InvalidMessageRecord.class, result);
		assertEquals(InvalidRecordReason.MESSAGE_ENTRY_MISSING, ((InvalidMessageRecord) result).reason());
	}


	@Test
	void getMessageRecord_with_valid_parameter_returns_ValidMessageRecord()
	{
		// Arrange
		when(languageResourceManagerMock.getSectionProvider(Section.MESSAGES)).thenReturn(languageSectionProvider);
		ValidMessageKey validMessageKey = MessageKey.of("TEST_MESSAGE").isValid().orElseThrow();

		// Act
		YamlMessageRepository messageRepository = new YamlMessageRepository(languageResourceManagerMock);
		MessageRecord result = messageRepository.getMessageRecord(validMessageKey);

		// Assert
		assertInstanceOf(ValidMessageRecord.class, result);
		assertEquals("This is a test message.", ((ValidMessageRecord) result).message());
	}

}
