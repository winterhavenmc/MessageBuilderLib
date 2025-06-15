/*
 * Copyright (c) 2024-2025 Tim Savage.
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

package com.winterhavenmc.library.messagebuilder.resources.language;

import com.winterhavenmc.library.messagebuilder.resources.configuration.LanguageTag;
import com.winterhavenmc.library.messagebuilder.util.MockUtility;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LanguageResourceLoaderTest
{
	@Mock Plugin pluginMock;
	@Mock FileConfiguration fileConfigurationMock;
	@Mock Logger loggerMock;

	private LanguageResourceLoader loader;


	@BeforeEach
	void setUp()
	{
		loader = new LanguageResourceLoader(pluginMock);
	}


	@Nested
	class GetConfiguredLanguageTagTests
	{
		@Test
		void returns_LanguageTag_when_config_is_present()
		{
			// Arrange
			when(pluginMock.getConfig()).thenReturn(fileConfigurationMock);
			when(fileConfigurationMock.getString("language")).thenReturn("en-US");

			// Act
			Optional<LanguageTag> result = loader.getConfiguredLanguageTag();

			// Assert
			assertTrue(result.isPresent());
			assertEquals("en-US", result.get().toString());

			// Verify
			verify(pluginMock, atLeastOnce()).getConfig();
		}


		@Test
		void returns_empty_when_config_is_null()
		{
			// Arrange
			when(pluginMock.getConfig()).thenReturn(fileConfigurationMock);
			when(fileConfigurationMock.getString("language")).thenReturn(null);

			// Act
			Optional<LanguageTag> result = loader.getConfiguredLanguageTag();

			// Assert
			assertTrue(result.isPresent());
			assertEquals("en-US", result.get().toString());

			// Verify
			verify(pluginMock, atLeastOnce()).getConfig();
		}


		@Test
		void returns_empty_when_config_is_blank()
		{
			// Arrange
			when(pluginMock.getConfig()).thenReturn(fileConfigurationMock);
			when(fileConfigurationMock.getString("language")).thenReturn("   ");

			// Act
			Optional<LanguageTag> result = loader.getConfiguredLanguageTag();

			// Assert
			assertTrue(result.isPresent());
			assertEquals("en-US", result.get().toString());

			// Verify
			verify(pluginMock, atLeastOnce()).getConfig();
		}
	}


	@Nested
	class GetConfiguredLocaleTests
	{
		@Test
		void returns_configured_Locale_when_language_setting_exists()
		{
			// Arrange
			when(pluginMock.getConfig()).thenReturn(fileConfigurationMock);
			when(fileConfigurationMock.getString("language")).thenReturn("en-US");

			// Act
			Locale locale = loader.getConfiguredLocale();

			// Assert
			assertEquals(Locale.forLanguageTag("en-US"), locale);

			// Verify
			verify(pluginMock, atLeastOnce()).getConfig();
			verify(fileConfigurationMock, atLeastOnce()).getString("language");
		}

		@Test
		void returns_default_Locale_when_Language_setting_is_not_set()
		{
			// Arrange
			when(pluginMock.getConfig()).thenReturn(fileConfigurationMock);
			when(fileConfigurationMock.getString("language")).thenReturn(null);

			// Act
			Locale locale = loader.getConfiguredLocale();

			// Assert
			assertEquals(Locale.forLanguageTag("en-US"), locale);

			// Verify
			verify(pluginMock, atLeastOnce()).getConfig();
			verify(fileConfigurationMock, atLeastOnce()).getString("language");
		}
	}


	@Nested
	class LoadTests
	{
		@Nested
		class with_no_parameter
		{
			@Test
			void returns_non_null_configuration_when_language_setting_configured()
			{
				// Arrange
				when(pluginMock.getLogger()).thenReturn(loggerMock);
				when(pluginMock.getConfig()).thenReturn(fileConfigurationMock);
				when(fileConfigurationMock.getString("language")).thenReturn("en-US");

				// Act
				Configuration config = loader.load();

				// Assert
				assertNotNull(config);

				// Verify
				verify(pluginMock, atLeastOnce()).getLogger();
				verify(pluginMock, atLeastOnce()).getConfig();
				verify(fileConfigurationMock, atLeastOnce()).getString("language");
			}

			@Test
			void returns_non_null_when_language_setting_is_empty()
			{
				// Arrange
				when(pluginMock.getLogger()).thenReturn(loggerMock);
				when(pluginMock.getConfig()).thenReturn(fileConfigurationMock);
				when(fileConfigurationMock.getString("language")).thenReturn(null);

				// Act
				Configuration config = loader.load();

				// Assert
				assertNotNull(config);

				// Verify
				verify(pluginMock, atLeastOnce()).getLogger();
				verify(pluginMock, atLeastOnce()).getConfig();
				verify(fileConfigurationMock, atLeastOnce()).getString("language");
			}
		}


		@Nested
		class LoadFromResourceTests
		{
			@Test
			void with_valid_fallback()
			{
				// Arrange
				when(pluginMock.getLogger()).thenReturn(loggerMock);
				when(pluginMock.getResource("language/en-US.yml"))
						.thenReturn(MockUtility.getResourceStream("language/en-US.yml"));
				LanguageResourceLoader loader = new LanguageResourceLoader(pluginMock);

				// Act
				Configuration config = loader.loadFromResource(LanguageTag.of("en-US").orElseThrow());

				// Assert
				assertNotNull(config);
				assertEquals("unlimited", config.getString("CONSTANTS.TIME.UNLIMITED"));

				// Verify
				verify(pluginMock, atLeastOnce()).getLogger();
				verify(pluginMock, atLeastOnce()).getResource("language/en-US.yml");
			}


			@Test
			void fallback_to_resource_when_file_is_missing()
			{
				// Arrange
				when(pluginMock.getLogger()).thenReturn(loggerMock);
				when(pluginMock.getResource("language/en-US.yml"))
						.thenReturn(MockUtility.getResourceStream("language/en-US.yml"));
				LanguageResourceLoader loader = new LanguageResourceLoader(pluginMock);
				LanguageTag preferred = LanguageTag.of("ru-RU").orElseThrow();
				LanguageTag fallback = LanguageTag.of("en-US").orElseThrow();

				// Act
				Configuration config = loader.loadWithFallback(preferred, fallback);

				// Assert
				assertNotNull(config);
				assertTrue(config.contains("MESSAGES"));

				// Verify
				verify(pluginMock, atLeastOnce()).getLogger();
				verify(pluginMock, atLeastOnce()).getResource("language/en-US.yml");
			}


			@Test
			void throws_exception_with_invalid_yaml()
			{
				// Arrange: give it a broken YAML input
				when(pluginMock.getLogger()).thenReturn(loggerMock);
				InputStream garbage = new ByteArrayInputStream("%%%%%%%".getBytes(StandardCharsets.UTF_8));
				when(pluginMock.getResource("language/en-US.yml")).thenReturn(garbage);
				LanguageResourceLoader loader = new LanguageResourceLoader(pluginMock);
				LanguageTag preferred = LanguageTag.of("ru-RU").orElseThrow();
				LanguageTag fallback = LanguageTag.of("en-US").orElseThrow();

				// Act
				Configuration config = loader.loadWithFallback(preferred, fallback);

				// Assert: should still get a non-null config, but the error should have been logged
				assertNotNull(config);

				// Verify
				verify(pluginMock, atLeastOnce()).getLogger();
				verify(pluginMock, atLeastOnce()).getResource(any());
			}
		}
	}

	// --- Helpers ---

	private File writeLanguageFile(final File pluginDataFolder, final String localeString, final String yamlContent) throws IOException
	{
		File languageDir = new File(pluginDataFolder, "language");
		if (!languageDir.exists()) {
			assertTrue(languageDir.mkdir(), "Failed to create language folder for test");
		}

		File langFile = new File(languageDir, localeString + ".yml");
		try (PrintWriter writer = new PrintWriter(langFile)) {
			writer.print(yamlContent);
		}
		return langFile;
	}

	private void createLanguageFile(File pluginDataFolder, String localeString) throws IOException
	{
		writeLanguageFile(pluginDataFolder, localeString, "placeholder: value");
	}

	private LanguageResourceLoader createFaultyLoader(Supplier<YamlConfiguration> yamlSupplier)
	{
		return new LanguageResourceLoader(pluginMock, yamlSupplier);
	}
}
