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

import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import java.util.Map;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;


/**
 * This class provides handling of the Macro Processors and their Registry
 */
public class MacroReplacer {

	private final static String DELIMITER_OPEN = "{";
	private final static String DELIMITER_CLOSE = "}";

	private final ProcessorRegistry processorRegistry;


	/**
	 * Class constructor
	 */
	public MacroReplacer() {
		this.processorRegistry = new ProcessorRegistry(new DependencyContext());
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

		// only process macro tokens if message string contains a pair of macro delimiters
		if (modifiedMessageString.contains(DELIMITER_OPEN)) {

			// add recipient fields to context map
			addRecipientContext(recipient, contextMap);

			// final result map of String NameSpacedKeys and processed String values
			ResultMap replacementStringMap = new ResultMap();
			replacementStringMap.putAll(convertValuesToStrings(contextMap));

			// do macro replacements on message string
			modifiedMessageString = performReplacements(replacementStringMap, modifiedMessageString);
		}

		return modifiedMessageString;
	}


	void addRecipientContext(CommandSender recipient, ContextMap contextMap) {
		if (recipient == null) { throw new LocalizedException(PARAMETER_NULL, "recipient"); }
		if (contextMap == null) { throw new LocalizedException(PARAMETER_NULL, "contextMap"); }

		// put recipient name in context map
		String key = "RECIPIENT";
		contextMap.put(key, recipient.getName());

		// if recipient is an entity, put recipient location in macro object map
		if (recipient instanceof Entity entity) {
			String locationKey = key.concat(".LOCATION");
			contextMap.put(locationKey, entity.getLocation());
		}
	}


	ResultMap convertValuesToStrings(ContextMap contextMap) {
		if (contextMap == null) { throw new LocalizedException(PARAMETER_NULL, "contextMap"); }

		ResultMap resultMap = new ResultMap();
		for (Map.Entry<String, Object> entry : contextMap.entrySet()) {
			ProcessorType processorType = ProcessorType.matchType(entry.getValue());
			MacroProcessor macroProcessor = processorRegistry.get(processorType);
			resultMap.putAll(macroProcessor.resolveContext(entry.getKey(), contextMap));
		}
		return resultMap;
	}


	String performReplacements(final ResultMap replacementMap, final String messageString) {
		if (replacementMap == null) { throw new LocalizedException(PARAMETER_NULL, "replacementMap"); }
		if (messageString == null) { throw new LocalizedException(PARAMETER_NULL, "messageString"); }

		String modifiedMessageString = messageString;

		for (Map.Entry<String, String> entry : replacementMap.entrySet()) {
			String macroToken = DELIMITER_OPEN + entry.getKey() + DELIMITER_CLOSE;
			modifiedMessageString = modifiedMessageString.replace(macroToken, entry.getValue());
		}
		return modifiedMessageString;
	}

}
