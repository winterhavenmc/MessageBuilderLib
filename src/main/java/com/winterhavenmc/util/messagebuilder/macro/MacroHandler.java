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

import com.winterhavenmc.util.messagebuilder.macro.processor.MacroProcessor;
import com.winterhavenmc.util.messagebuilder.macro.processor.ProcessorRegistry;
import com.winterhavenmc.util.messagebuilder.macro.processor.ProcessorType;
import com.winterhavenmc.util.messagebuilder.macro.processor.ResultMap;
import com.winterhavenmc.util.messagebuilder.language.LanguageQueryHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.Map;


/**
 * This class provides handling of the Macro Processors and their Registry
 */
public class MacroHandler {

	// the processor registry
	private final ProcessorRegistry processorRegistry;


	/**
	 * Enum that contains settable LEFT and RIGHT macro delimiter characters
	 */
	//TODO: get rid of this enum. it was a fun experiment.
	public enum MacroDelimiter {
		LEFT('%'),
		RIGHT('%');

		// the delimiter character
		private char character;

		/**
		 * Constructor for enum
		 *
		 * @param defaultChar the default character for a delimiter
		 */
		MacroDelimiter(final char defaultChar) {
			this.character = defaultChar;
		}

		@Override
		public String toString() {
			return String.valueOf(this.character);
		}

		public char toChar() {
			return this.character;
		}

		public void set(final char character) {
			this.character = character;
		}
	}


	/**
	 * Class constructor
	 */
	public MacroHandler(final Plugin plugin, final LanguageQueryHandler queryHandler) {
		// instantiate macro processor registry
		this.processorRegistry = new ProcessorRegistry();
		// populate macro processor registry
		for (ProcessorType type : ProcessorType.values()) {
			type.register(queryHandler, processorRegistry, type);
		}
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
		if (modifiedMessageString.contains(MacroDelimiter.LEFT.toString())) {

			// final result map of String NameSpacedKeys and processed String values
			ResultMap replacementStringMap = new ResultMap();

			//TODO: THESE WILL NEED TO BE ADDED ELSEWHERE, LIKE IN THE APPROPRIATE MACRO PROCESSOR
			// perhaps we need a RECIPIENT type; at any rate, there will need to be a method
			// to add items to the ContextMap manually, before macros are processed
//			// put message recipient in macro object map
//			// create key for recipient in contextMap
//			ContextKey compositeKey = new CompositeKey(processorType, "RECIPIENT");
//
//			contextMap.put(compositeKey, recipient);
//
//			// if recipient is an entity, put recipient location in macro object map
//			if (recipient instanceof Entity entity) {
//				contextMap.put("RECIPIENT_LOCATION", entity.getLocation());
//			}
//
//			// put string placeholder for item in object map if not already in map
//			if (!contextMap.containsKey("ITEM_NAME")) {
//				contextMap.put("ITEM_NAME", "item_name");
//			}

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
				String macroToken = MacroDelimiter.LEFT + entry.getKey() + MacroDelimiter.RIGHT;
				modifiedMessageString = modifiedMessageString.replace(macroToken, entry.getValue());
			}
		}

		return modifiedMessageString;
	}

}
