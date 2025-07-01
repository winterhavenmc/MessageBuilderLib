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

package com.winterhavenmc.library.messagebuilder.macro.processor;

import com.winterhavenmc.library.messagebuilder.LanguageHandler;
import com.winterhavenmc.library.messagebuilder.YamlLanguageHandler;

import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ProcessorRegistryTest {

	@Mock Plugin pluginMock;


	@Test
	void notNull() {
		ProcessorRegistry macroProcessorRegistry = new ProcessorRegistry();
		assertNotNull(macroProcessorRegistry);
		LanguageHandler languageHandler = new YamlLanguageHandler(pluginMock);
		assertNotNull(languageHandler);
	}

	@Test
	void put() {
		ProcessorRegistry macroProcessorRegistry = new ProcessorRegistry();
		LanguageHandler languageHandler = new YamlLanguageHandler(pluginMock);
		macroProcessorRegistry.put(ProcessorType.STRING, ProcessorType.STRING.create(languageHandler));
		assertNotNull(macroProcessorRegistry.get(ProcessorType.STRING));
	}

	@Test
	void get() {
		ProcessorRegistry macroProcessorRegistry = new ProcessorRegistry();
		LanguageHandler languageHandler = new YamlLanguageHandler(pluginMock);
		macroProcessorRegistry.put(ProcessorType.STRING, ProcessorType.STRING.create(languageHandler));
		assertNotNull(macroProcessorRegistry.get(ProcessorType.STRING));
	}

}
