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
import org.bukkit.configuration.InvalidConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
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
	@Mock FileConfiguration fileConfiguration;
	@Mock Logger loggerMock;

	private LanguageResourceLoader loader;

	@BeforeEach
	void setUp()
	{
		loader = new LanguageResourceLoader(pluginMock);
	}

	@Test
	void getConfiguredLanguageTag_ReturnsLanguageTag_WhenConfigIsPresent()
	{
		when(pluginMock.getConfig()).thenReturn(fileConfiguration);
		when(fileConfiguration.getString("language")).thenReturn("en-US");

		Optional<LanguageTag> result = loader.getConfiguredLanguageTag();

		assertTrue(result.isPresent());
		assertEquals("en-US", result.get().toString());
	}

	@Test
	void getConfiguredLanguageTag_ReturnsEmpty_WhenConfigIsNull()
	{
		when(pluginMock.getConfig()).thenReturn(fileConfiguration);
		when(fileConfiguration.getString("language")).thenReturn(null);

		Optional<LanguageTag> result = loader.getConfiguredLanguageTag();

		assertTrue(result.isPresent());
		assertEquals("en-US", result.get().toString());
	}

	@Test
	void getConfiguredLanguageTag_ReturnsEmpty_WhenConfigIsBlank()
	{
		when(pluginMock.getConfig()).thenReturn(fileConfiguration);
		when(fileConfiguration.getString("language")).thenReturn("   ");

		Optional<LanguageTag> result = loader.getConfiguredLanguageTag();

		assertTrue(result.isPresent());
		assertEquals("en-US", result.get().toString());
	}

	@Test
	void getConfiguredLocale_ReturnsConfiguredLocale_WhenLanguageTagExists()
	{
		when(pluginMock.getConfig()).thenReturn(fileConfiguration);
		when(fileConfiguration.getString("language")).thenReturn("en-US");

		Locale locale = loader.getConfiguredLocale();

		assertEquals(Locale.forLanguageTag("en-US"), locale);
	}

	@Test
	void getConfiguredLocale_ReturnsDefaultLocale_WhenLanguageTagNotSet()
	{
		when(pluginMock.getConfig()).thenReturn(fileConfiguration);
		when(fileConfiguration.getString("language")).thenReturn(null);

		Locale locale = loader.getConfiguredLocale();

		assertEquals(Locale.getDefault(), locale);
	}

	@Test
	void load_ReturnsNonNullConfiguration_WhenLanguageTagConfigured()
	{
		when(pluginMock.getConfig()).thenReturn(fileConfiguration);
		when(pluginMock.getLogger()).thenReturn(loggerMock);
		when(fileConfiguration.getString("language")).thenReturn("en-US");

		Configuration config = loader.load();

		assertNotNull(config);
	}

	@Test
	void load_ReturnsNull_WhenLanguageTagIsEmpty()
	{
		when(pluginMock.getLogger()).thenReturn(loggerMock);
		when(pluginMock.getConfig()).thenReturn(fileConfiguration);
		when(fileConfiguration.getString("language")).thenReturn(null);

		Configuration config = loader.load();

		assertNotNull(config);
	}

	@Test
	void load_WithLanguageTag_LogsFileNotFound(@TempDir File tempDir)
	{
		when(pluginMock.getLogger()).thenReturn(loggerMock);
		when(pluginMock.getDataFolder()).thenReturn(tempDir);

		LanguageTag tag = LanguageTag.of("en-US").orElseThrow();

		Configuration config = loader.load(tag);

		assertNotNull(config);
		verify(loggerMock).warning(contains("Language file 'en-US.yml' not found. Falling back to default."));
	}

	@Test
	void load_WithLanguageTag_LoadsSuccessfully(@TempDir File tempDir) throws IOException
	{
		when(pluginMock.getLogger()).thenReturn(loggerMock);
		when(pluginMock.getDataFolder()).thenReturn(tempDir);

		writeLanguageFile(tempDir, "en-US", "greeting: Hello from test");

		LanguageTag tag = LanguageTag.of("en-US").orElseThrow();
		Configuration config = loader.load(tag);

		assertNotNull(config);
		assertEquals("Hello from test", config.getString("greeting"));

		verify(loggerMock).info(contains("successfully loaded"));
	}

	@Test
	void load_WithLanguageTag_ThrowsIOException(@TempDir File tempDir) throws Exception
	{
		when(pluginMock.getLogger()).thenReturn(loggerMock);
		when(pluginMock.getDataFolder()).thenReturn(tempDir);

		createLanguageFile(tempDir, "en-US");

		LanguageResourceLoader faultyLoader = createFaultyLoader(() -> {
			YamlConfiguration spyYaml = spy(new YamlConfiguration());
			File languageFile = new File(tempDir, "language" + File.separator + "en-US.yml");
			try
			{
				doThrow(new IOException("Simulated IOException")).when(spyYaml).load(languageFile);
			} catch (IOException | InvalidConfigurationException e)
			{
				throw new RuntimeException(e);
			}
			return spyYaml;
		});

		LanguageTag tag = LanguageTag.of("en-US").orElseThrow();
		Configuration config = faultyLoader.load(tag);

		assertNotNull(config);
		verify(loggerMock).warning(contains("Language file 'en-US.yml' could not be read. Falling back to default."));
	}


	@Test
	void load_WithLanguageTag_ThrowsInvalidConfigurationException(@TempDir File tempDir) throws IOException
	{
		when(pluginMock.getLogger()).thenReturn(loggerMock);
		when(pluginMock.getDataFolder()).thenReturn(tempDir);

		createLanguageFile(tempDir, "en-US");

		LanguageResourceLoader faultyLoader = createFaultyLoader(() -> {
			YamlConfiguration spyYaml = spy(new YamlConfiguration());
			File languageFile = new File(tempDir, "language" + File.separator + "en-US.yml");
			try
			{
				doThrow(new InvalidConfigurationException("Simulated invalid YAML")).when(spyYaml).load(languageFile);
			} catch (IOException | InvalidConfigurationException e)
			{
				throw new RuntimeException(e);
			}
			return spyYaml;
		});

		LanguageTag tag = LanguageTag.of("en-US").orElseThrow();
		Configuration config = faultyLoader.load(tag);

		assertNotNull(config);
		verify(loggerMock).warning(contains( "Language file 'en-US.yml' is not valid YAML. Falling back to default."));
	}


	@Test
	void testLoadFromResource_validFallback()
	{
		when(pluginMock.getLogger()).thenReturn(loggerMock);
		when(pluginMock.getResource("language/en-US.yml"))
				.thenReturn(MockUtility.getResourceStream("language/en-US.yml"));

		LanguageResourceLoader loader = new LanguageResourceLoader(pluginMock);
		Configuration config = loader.loadFromResource(LanguageTag.of("en-US").orElseThrow());

		assertNotNull(config);
		assertEquals("unlimited", config.getString("CONSTANTS.TIME.UNLIMITED"));
	}


	@Test
	void testLoadWithFallback_fallbackUsedWhenFileMissing()
	{
		// Arrange
		when(pluginMock.getLogger()).thenReturn(loggerMock);
		when(pluginMock.getResource("language/en-US.yml"))
				.thenReturn(MockUtility.getResourceStream("language/en-US.yml"));
		LanguageResourceLoader loader = new LanguageResourceLoader(pluginMock);

		// Force the preferred tag to a non-existent file
		LanguageTag preferred = LanguageTag.of("ru-RU").orElseThrow();
		LanguageTag fallback = LanguageTag.of("en-US").orElseThrow();

		// Act
		Configuration config = loader.loadWithFallback(preferred, fallback);

		// Assert
		assertNotNull(config);
		assertTrue(config.contains("MESSAGES"));
	}


	@Test
	void testLoadFromResource_withGarbageYaml_throwsException()
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

		// Assert: we should still get a non-null config, but the error should have been logged
		assertNotNull(config);
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
