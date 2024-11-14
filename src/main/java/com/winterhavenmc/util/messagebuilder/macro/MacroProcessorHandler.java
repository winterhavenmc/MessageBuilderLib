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

import com.winterhavenmc.util.messagebuilder.LanguageHandler;

import com.winterhavenmc.util.messagebuilder.macro.processor.ProcessorRegistry;
import com.winterhavenmc.util.messagebuilder.macro.processor.ProcessorType;
import com.winterhavenmc.util.messagebuilder.macro.processor.ResultMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class MacroProcessorHandler {

	private final JavaPlugin plugin;
	private final LanguageHandler languageHandler;
	private final ProcessorRegistry macroProcessorRegistry;

	public enum MacroDelimiter {
		LEFT('%'),
		RIGHT('%');

		private char character;

		MacroDelimiter(final char defaultChar) {
			this.character = defaultChar;
		}

		@Override
		public String toString() {
			return String.valueOf(this.character);
		}

		public void set(final char character) {
			this.character = character;
		}
	}


	/**
	 * Class constructor
	 * @param plugin reference to plugin main class
	 * @param languageHandler reference to language handler
	 */
	public MacroProcessorHandler(final JavaPlugin plugin, final LanguageHandler languageHandler) {
		this.plugin = plugin;
		this.languageHandler = languageHandler;
		this.macroProcessorRegistry = new ProcessorRegistry();
	}


	@SuppressWarnings("unused")
	public String replaceMacros(final CommandSender recipient, final MacroObjectMap macroObjectMap, final String messageString) {

		for (ProcessorType type : ProcessorType.values()) {
			type.register(plugin, languageHandler, macroProcessorRegistry, type);
		}

		String modifiedMessageString = messageString;

		// only process macro tokens if message string contains a token marker character
		if (modifiedMessageString.contains(MacroDelimiter.LEFT.toString())) {

			ResultMap macroStringMap = new ResultMap();

			// put message recipient in object map
			macroObjectMap.put("RECIPIENT", recipient);
			if (recipient instanceof Entity entity) {
				macroObjectMap.put("RECIPIENT_LOCATION", entity.getLocation());
			}

			// put string placeholder for item in object map if not already in map
			if (!macroObjectMap.containsKey("ITEM_NAME")) {
				macroObjectMap.put("ITEM_NAME", "item_name");
			}

			// iterate over macro object map, getting macro value strings based on class type in object map
			for (Map.Entry<String, Object> entry : macroObjectMap.entrySet()) {
				ProcessorType type = ProcessorType.matchType(entry.getValue());
				macroStringMap.putAll(macroProcessorRegistry.get(type).doReplacements(macroObjectMap, entry.getKey(), entry.getValue()));
			}

			// replace macro tokens in message string with macro strings
			for (Map.Entry<String, String> entry : macroStringMap.entrySet()) {
				String macroToken = MacroDelimiter.LEFT.character + entry.getKey() + MacroDelimiter.RIGHT.character;
				modifiedMessageString = modifiedMessageString.replace(macroToken, entry.getValue());
			}
		}

		return modifiedMessageString;
	}

}
