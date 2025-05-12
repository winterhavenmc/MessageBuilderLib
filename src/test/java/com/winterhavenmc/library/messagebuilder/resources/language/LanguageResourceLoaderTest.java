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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LanguageResourceLoaderTest
{
	@Mock Plugin plugin;
	@Mock FileConfiguration fileConfiguration;
	@Mock Logger logger;

	private LanguageResourceLoader loader;

	@BeforeEach
	void setUp()
	{
		loader = new LanguageResourceLoader(plugin);
	}

	@Test
	void getConfiguredLanguageTag_ReturnsLanguageTag_WhenConfigIsPresent()
	{
		when(plugin.getConfig()).thenReturn(fileConfiguration);
		when(fileConfiguration.getString("language")).thenReturn("en-US");

		Optional<LanguageTag> result = loader.getConfiguredLanguageTag();

		assertTrue(result.isPresent());
		assertEquals("en-US", result.get().toString());
	}

	@Test
	void getConfiguredLanguageTag_ReturnsEmpty_WhenConfigIsNull()
	{
		when(plugin.getConfig()).thenReturn(fileConfiguration);
		when(fileConfiguration.getString("language")).thenReturn(null);

		Optional<LanguageTag> result = loader.getConfiguredLanguageTag();

		assertTrue(result.isPresent());
		assertEquals("en-US", result.get().toString());
	}

	@Test
	void getConfiguredLanguageTag_ReturnsEmpty_WhenConfigIsBlank()
	{
		when(plugin.getConfig()).thenReturn(fileConfiguration);
		when(fileConfiguration.getString("language")).thenReturn("   ");

		Optional<LanguageTag> result = loader.getConfiguredLanguageTag();

		assertTrue(result.isPresent());
		assertEquals("en-US", result.get().toString());
	}

	@Test
	void getConfiguredLocale_ReturnsConfiguredLocale_WhenLanguageTagExists()
	{
		when(plugin.getConfig()).thenReturn(fileConfiguration);
		when(fileConfiguration.getString("language")).thenReturn("en-US");

		Locale locale = loader.getConfiguredLocale();

		assertEquals(Locale.forLanguageTag("en-US"), locale);
	}

	@Test
	void getConfiguredLocale_ReturnsDefaultLocale_WhenLanguageTagNotSet()
	{
		when(plugin.getConfig()).thenReturn(fileConfiguration);
		when(fileConfiguration.getString("language")).thenReturn(null);

		Locale locale = loader.getConfiguredLocale();

		assertEquals(Locale.getDefault(), locale);
	}

	@Test
	void load_ReturnsNonNullConfiguration_WhenLanguageTagConfigured()
	{
		when(plugin.getConfig()).thenReturn(fileConfiguration);
		when(plugin.getLogger()).thenReturn(logger);
		when(fileConfiguration.getString("language")).thenReturn("en-US");

		Configuration config = loader.load();

		assertNotNull(config);
	}

	@Test
	void load_ReturnsNull_WhenLanguageTagIsEmpty()
	{
		when(plugin.getLogger()).thenReturn(logger);
		when(plugin.getConfig()).thenReturn(fileConfiguration);
		when(fileConfiguration.getString("language")).thenReturn(null);

		Configuration config = loader.load();

		assertNotNull(config);
	}

	@Test
	void load_WithLanguageTag_LogsFileNotFound(@TempDir File tempDir)
	{
		when(plugin.getLogger()).thenReturn(logger);
		when(plugin.getDataFolder()).thenReturn(tempDir);

		LanguageTag tag = LanguageTag.of("en-US").orElseThrow();

		Configuration config = loader.load(tag);

		assertNotNull(config);
		verify(logger).severe(contains("does not exist"));
	}

	@Test
	void load_WithLanguageTag_LoadsSuccessfully(@TempDir File tempDir) throws IOException
	{
		when(plugin.getLogger()).thenReturn(logger);
		when(plugin.getDataFolder()).thenReturn(tempDir);

		writeLanguageFile(tempDir, "en-US", "greeting: Hello from test");

		LanguageTag tag = LanguageTag.of("en-US").orElseThrow();
		Configuration config = loader.load(tag);

		assertNotNull(config);
		assertEquals("Hello from test", config.getString("greeting"));

		verify(logger).info(contains("successfully loaded"));
	}

	@Test
	void load_WithLanguageTag_ThrowsIOException(@TempDir File tempDir) throws Exception
	{
		when(plugin.getLogger()).thenReturn(logger);
		when(plugin.getDataFolder()).thenReturn(tempDir);

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
		verify(logger).severe(contains("could not be read"));
	}

	@Test
	void load_WithLanguageTag_ThrowsInvalidConfigurationException(@TempDir File tempDir) throws IOException
	{
		when(plugin.getLogger()).thenReturn(logger);
		when(plugin.getDataFolder()).thenReturn(tempDir);

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
		verify(logger).severe(contains("is not valid yaml"));
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
		return new LanguageResourceLoader(plugin, yamlSupplier);
	}
}
