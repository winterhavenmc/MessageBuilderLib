/*
 * Copyright (c) 2022 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.macro.processor;

import com.winterhavenmc.util.messagebuilder.*;
import com.winterhavenmc.util.messagebuilder.languages.LanguageHandler;
import com.winterhavenmc.util.messagebuilder.languages.YamlLanguageFileInstaller;
import com.winterhavenmc.util.messagebuilder.languages.YamlLanguageFileLoader;
import com.winterhavenmc.util.messagebuilder.query.ConfigurationQueryHandler;
import com.winterhavenmc.util.messagebuilder.query.QueryHandler;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProcessorRegistryTest {

	Plugin plugin;

	@BeforeAll
	public void setUp() {
		plugin = mock(Plugin.class);
	}

	@AfterAll
	public void tearDown() {
		plugin = null;
	}


	@Test
	void notNull() {
		ProcessorRegistry macroProcessorRegistry = new ProcessorRegistry();
		assertNotNull(macroProcessorRegistry);
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin, new YamlLanguageFileInstaller(plugin), new YamlLanguageFileLoader(plugin));
		assertNotNull(languageHandler);
	}

	@Test
	void put() {
		ProcessorRegistry macroProcessorRegistry = new ProcessorRegistry();
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin, new YamlLanguageFileInstaller(plugin), new YamlLanguageFileLoader(plugin));
		QueryHandler queryHandler = new ConfigurationQueryHandler(plugin, languageHandler.getConfiguration());
		macroProcessorRegistry.put(ProcessorType.STRING, ProcessorType.STRING.create(queryHandler));
		assertNotNull(macroProcessorRegistry.get(ProcessorType.STRING));
	}

	@Test
	void get() {
		ProcessorRegistry macroProcessorRegistry = new ProcessorRegistry();
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin, new YamlLanguageFileInstaller(plugin), new YamlLanguageFileLoader(plugin));
		QueryHandler queryHandler = new ConfigurationQueryHandler(plugin, languageHandler.getConfiguration());
		macroProcessorRegistry.put(ProcessorType.STRING, ProcessorType.STRING.create(queryHandler));
		assertNotNull(macroProcessorRegistry.get(ProcessorType.STRING));
	}

}
