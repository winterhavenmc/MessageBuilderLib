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

package com.winterhavenmc.util.messagebuilder.macro;

import com.winterhavenmc.util.messagebuilder.context.ContextContainer;
import com.winterhavenmc.util.messagebuilder.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.context.Source;
import com.winterhavenmc.util.messagebuilder.context.SourceKey;
import com.winterhavenmc.util.messagebuilder.macro.processor.MacroProcessor;
import com.winterhavenmc.util.messagebuilder.macro.processor.ProcessorRegistry;
import com.winterhavenmc.util.messagebuilder.macro.processor.ProcessorType;
import com.winterhavenmc.util.messagebuilder.macro.processor.ResultMap;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import java.util.Map;


/**
 * This class provides handling of the Macro Processors and their Registry
 */
public class MacroHandler {

	// the processor registry
	private final ProcessorRegistry processorRegistry;


	/**
	 * Class constructor
	 */
	public MacroHandler(final LanguageQueryHandler queryHandler) {
		// instantiate macro processor registry
		this.processorRegistry = new ProcessorRegistry(queryHandler);
		// populate macro processor registry
//		for (ProcessorType type : ProcessorType.values()) {
//			type.register(queryHandler, processorRegistry, type);
//		}
	}


	/**
	 * Replace macros in a message to be sent
	 *
	 * @param recipient     the message recipient
	 * @param contextMap    the context map containing other objects whose values may be retrieved
	 * @param messageString the message with placeholders to be replaced by macro values
	 * @return the string with all macro replacements performed
	 */
	public String replaceMacros(final CommandSender recipient,
	                            final ContextMap contextMap,
	                            final String messageString) {

		String modifiedMessageString = messageString;

		// only process macro tokens if message string contains a token marker character
		if (modifiedMessageString.contains(MacroDelimiter.OPEN.toString())) {

			// final result map of String NameSpacedKeys and processed String values
			ResultMap replacementStringMap = new ResultMap();

			// put recipient name in context map
			String contextKey = SourceKey.create(Source.MACRO, "RECIPIENT");
			contextMap.put(contextKey, ContextContainer.of(recipient.getName(), ProcessorType.COMMAND_SENDER));

			// if recipient is an entity, put recipient location in macro object map
			if (recipient instanceof Entity entity) {
				String locationKey = contextKey.concat(".LOCATION");
				contextMap.put(locationKey, ContextContainer.of(entity.getLocation(), ProcessorType.LOCATION));
			}

			// iterate over context map, getting macro value strings based on class type in map
			for (Map.Entry<String, ContextContainer<?>> entry : contextMap.entrySet()) {

				// get name-spaced String key from entry
				String key = entry.getKey();

				// get macroProcessor type from context container
				ProcessorType processorType = entry.getValue().processorType();

				// get macroProcessor from registry by ProcessorType
				MacroProcessor macroProcessor = processorRegistry.get(processorType);

				// get resultMap from macroProcessor execution
				ResultMap resultMap = macroProcessor.resolveContext(key, contextMap, entry.getValue());

				// add all entries of resultMap to macroStringMap
				replacementStringMap.putAll(resultMap);
			}

			// replace macro tokens in message string with macro strings
			for (Map.Entry<String, String> entry : replacementStringMap.entrySet()) {
				String macroToken = MacroDelimiter.OPEN + entry.getKey() + MacroDelimiter.CLOSE;
				modifiedMessageString = modifiedMessageString.replace(macroToken, entry.getValue());
			}
		}

		return modifiedMessageString;
	}

}
