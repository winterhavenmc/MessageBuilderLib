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
import com.winterhavenmc.util.messagebuilder.macro.ContextContainer;
import com.winterhavenmc.util.messagebuilder.macro.ContextMap;

import com.winterhavenmc.util.messagebuilder.macro.Namespace;
import com.winterhavenmc.util.messagebuilder.macro.NamespaceKey;
import com.winterhavenmc.util.messagebuilder.query.ConfigurationQueryHandler;
import com.winterhavenmc.util.messagebuilder.query.QueryHandler;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.*;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StringProcessorTest {

	Plugin mockPlugin;
	LanguageHandler mockLanguageHandler;
	QueryHandler queryHandler;

	@BeforeEach
	public void setUp() {

		mockPlugin = mock(Plugin.class, "MockPlugin");
		when(mockPlugin.getLogger()).thenReturn(Logger.getLogger("StringProcessorTest"));
		mockLanguageHandler = mock(YamlLanguageHandler.class,"MockLanguageHandler");

		Configuration mockConfiguration = mock(Configuration.class, "MockConfiguration");
		queryHandler = new ConfigurationQueryHandler(mockPlugin, mockConfiguration);
	}

	@AfterEach
	public void tearDown() {
		mockPlugin = null;
		mockLanguageHandler = null;
		queryHandler = null;
	}

	@Test
	void resolveContext() {

		String keyPath = "SOME_NAME";
		String stringObject = "some name";

		ContextMap contextMap = new ContextMap();
		String namespacedKey = NamespaceKey.create(keyPath, Namespace.Category.MACRO);

		contextMap.put(namespacedKey, ContextContainer.of(stringObject, ProcessorType.STRING));

		MacroProcessor macroProcessor = new StringProcessor(queryHandler);

		ResultMap resultMap = macroProcessor.resolveContext(namespacedKey, contextMap, stringObject);

		assertTrue(resultMap.containsKey(namespacedKey));
		assertEquals(stringObject, resultMap.get(namespacedKey));
	}


	@Test
	void resolveContextWithItem() {

//		LanguageHandler languageHandler = new YamlLanguageHandler(plugin, new YamlLanguageFileLoader(plugin));
//		QueryHandler queryHandler = new ConfigurationQueryHandler(plugin, languageHandler.getConfiguration());
//		MacroProcessor macroProcessor = new StringProcessor(queryHandler);
//
//		String stringKey = "ITEM";
//		String stringObject = "some item string";
//
//		ContextMap contextMap = new ContextMap();
//		ContextKey compositeKey = new CompositeKey(ProcessorType.STRING, stringKey);
//		contextMap.put(compositeKey, stringObject);
//
//		ResultMap stringMap = macroProcessor.execute("ITEM", "some item string", contextMap);
//
//		assertTrue(stringMap.containsKey(stringKey));
//		assertEquals("§aTest Item", stringMap.get(stringKey));
//		assertTrue(stringMap.containsKey("ITEM_NAME"));
//		assertEquals("§aTest Item", stringMap.get("ITEM_NAME"));

	}

}
