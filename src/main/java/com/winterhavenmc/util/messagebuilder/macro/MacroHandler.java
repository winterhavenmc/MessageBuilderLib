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

import com.winterhavenmc.util.messagebuilder.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.macro.processor.*;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import java.util.Map;


/**
 * This class provides handling of the Macro Processors and their Registry
 */
public class MacroHandler {

	private final ProcessorRegistry processorRegistry;


	/**
	 * Class constructor
	 */
	public MacroHandler(final LanguageQueryHandler queryHandler) {
		this.processorRegistry = new ProcessorRegistry(new DependencyContext(), queryHandler);
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

		// only process macro tokens if message string contains a macro delimiter
		if (modifiedMessageString.contains(MacroDelimiter.OPEN.toString())) {

			// final result map of String NameSpacedKeys and processed String values
			ResultMap replacementStringMap = new ResultMap();

			// put recipient name in context map
			String key = "RECIPIENT";
			contextMap.put(key, recipient.getName());

			// if recipient is an entity, put recipient location in macro object map
			if (recipient instanceof Entity entity) {
				String locationKey = key.concat(".LOCATION");
				contextMap.put(locationKey, entity.getLocation());
			}

			// iterate over context map, getting macro value strings based on class type in map
			for (Map.Entry<String, Object> entry : contextMap.entrySet()) {

				// get processor type
				ProcessorType processorType = ProcessorType.matchType(entry.getValue());

				// get macroProcessor from registry by ProcessorType
				MacroProcessor macroProcessor = processorRegistry.get(processorType);

				// get resultMap from macroProcessor execution
				ResultMap resultMap = macroProcessor.resolveContext(key, contextMap);

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
