/*
 * Copyright (c) 2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder;

import com.winterhavenmc.util.messagebuilder.macro.MacroHandler;
import com.winterhavenmc.util.messagebuilder.query.LanguageFileQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.Error;

import java.util.List;
import java.util.Optional;

public class MessageBuilderToolkit<MessageId extends Enum<MessageId>, Macro extends Enum<Macro>> implements Toolkit {

	public static final String SPAWN_DISPLAY_NAME = "SPAWN.DISPLAY_NAME";
	public static final String HOME_DISPLAY_NAME = "HOME.DISPLAY_NAME";
	private final LanguageFileQueryHandler queryHandler;


	public
	MessageBuilderToolkit(final MessageBuilder<MessageId, Macro> messageBuilder) {
		if (messageBuilder == null) { throw new IllegalArgumentException(Error.Parameter.NULL_MESSAGE_BUILDER.getMessage()); }

		this.queryHandler = messageBuilder.getQueryHandler();
	}


	/**
	 * Get query handler for fetching values from the language file
	 *
	 * @return a {@link LanguageFileQueryHandler} instance
	 */
	public LanguageFileQueryHandler getQueryHandler() {
		return this.queryHandler;
	}


	/**
	 * Set both delimiters to the same specific character
	 *
	 * @param character the character to use for both delimiters
	 */
	@Override
	public void setDelimiters(final Character character) {
		MacroHandler.MacroDelimiter.LEFT.set(character);
		MacroHandler.MacroDelimiter.RIGHT.set(character);
	}


	/**
	 * Set delimiters to unique characters by passing two parameters
	 *
	 * @param leftCharacter  the character to use for the left delimiter
	 * @param rightCharacter the character to use for the right delimiter
	 */
	@Override
	public void setDelimiters(final Character leftCharacter, final Character rightCharacter) {
		MacroHandler.MacroDelimiter.LEFT.set(leftCharacter);
		MacroHandler.MacroDelimiter.RIGHT.set(rightCharacter);
	}


	/**
	 * Retrieve the display name for spawn from the language file CONSTANTS section with the
	 * key path 'SPAWN.DISPLAY_NAME'
	 *
	 * @return an Optional of String containing the spawn name, or an empty Optional if no value was found
	 */
	@Override
	public Optional<String> getSpawnDisplayName() {
		return getQueryHandler().getString(SPAWN_DISPLAY_NAME);
	}


	/**
	 * Retrieve the display name for home from the language file CONSTANTS section with the
	 * key path 'HOME.DISPLAY_NAME'
	 *
	 * @return an Optional of String containing the home name, or an empty Optional if no value was found
	 */
	@Override
	public Optional<String> getHomeDisplayName() {
		return getQueryHandler().getString(HOME_DISPLAY_NAME);
	}


	/**
	 * Retrieve any value as a string at the supplied key path within the CONSTANTS section of the language file
	 *
	 * @param key a dot-delimited key path for the value to be retrieved
	 * @return an Optional of String containing the value, or an empty Optional if no value was found
	 */
	@Override
	public Optional<String> getString(String key) {
		return getQueryHandler().getString(key);
	}


	/**
	 * Retrieve a List of String at the supplied key path within the CONSTANTS section of the language file
	 *
	 * @param key a dot-delimited key path for the List to be retrieved
	 * @return a List of String containing the values, or an empty list if no values were found
	 */
	@Override
	public List<String> getStringList(String key) {
		return getQueryHandler().getStringList(key);
	}

}
