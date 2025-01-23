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

import com.winterhavenmc.util.messagebuilder.context.*;

import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageQueryHandler;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.util.MockUtility.loadConfigurationFromResource;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class StringProcessorTest {

	@Mock Player playerMock;

	LanguageQueryHandler queryHandler;

	@BeforeEach
	public void setUp() {
		Configuration configuration = loadConfigurationFromResource("language/en-US.yml");
		YamlConfigurationSupplier configurationSupplier = new YamlConfigurationSupplier(configuration);
		queryHandler = new YamlLanguageQueryHandler(configurationSupplier);
	}

	@AfterEach
	public void tearDown() {
		queryHandler = null;
	}

	@Test
	void resolveContext() {

		String keyPath = "SOME_NAME";
		String stringObject = "some name";

		ContextMap contextMap = new ContextMap(playerMock);
		String contextKey = SourceKey.create(Source.MACRO, keyPath);

		contextMap.put(contextKey, ContextContainer.of(stringObject, ProcessorType.STRING));

		MacroProcessor macroProcessor = new StringProcessor(queryHandler);

		ResultMap resultMap = macroProcessor.resolveContext(contextKey, contextMap, stringObject);

		assertTrue(resultMap.containsKey(contextKey));
		assertEquals(stringObject, resultMap.get(contextKey));
	}

}
