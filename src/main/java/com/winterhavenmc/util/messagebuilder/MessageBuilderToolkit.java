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

import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.constants.ConstantSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.macro.MacroHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.SectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import com.winterhavenmc.util.messagebuilder.util.Toolkit;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;


/**
 * Provides an instance of a MessageBuilderToolkit, that allows some access to internal library functions.
 */
public class MessageBuilderToolkit<MessageId extends Enum<MessageId>, Macro extends Enum<Macro>> implements Toolkit {

	public static final String SPAWN_DISPLAY_NAME = "SPAWN.DISPLAY_NAME";
	public static final String HOME_DISPLAY_NAME = "HOME.DISPLAY_NAME";
	private final LanguageQueryHandler queryHandler;


	/**
	 * Class constructor
	 *
	 * @param messageBuilder the MessageBuilder instance
	 */
	public MessageBuilderToolkit(final MessageBuilder<MessageId, Macro> messageBuilder) {
		if (messageBuilder == null) { throw new LocalizedException(PARAMETER_NULL, "messageBuilder"); }

		this.queryHandler = messageBuilder.getLanguageQueryHandler();
	}


	/**
	 * Get ItemRecord handler for fetching values from the language file
	 *
	 * @return a {@link LanguageQueryHandler} instance
	 */
	public LanguageQueryHandler getQueryHandler() {
		return this.queryHandler;
	}


	/**
	 * Get query handler for section
	 *
	 * @param section the section of the query handler requested
	 * @return the section query handler
	 */
	public SectionQueryHandler getQueryHandler(Section section) {
		return queryHandler.getSectionQueryHandler(section);
	}


	/**
	 * Set both delimiters to the same specific character
	 *
	 * @param character the character to use for both delimiters
	 */
	//TODO: Consider making this a parameter (or a component of a settings object) passed to a MessageBuilder constructor.
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
		ConstantSectionQueryHandler constantSectionQueryHandler = (ConstantSectionQueryHandler) queryHandler.getSectionQueryHandler(Section.CONSTANTS);
		if (constantSectionQueryHandler != null) {
			return constantSectionQueryHandler.getString(SPAWN_DISPLAY_NAME);
		}
		return Optional.empty();
	}


	/**
	 * Retrieve the display name for home from the language file CONSTANTS section with the
	 * key path 'HOME.DISPLAY_NAME'
	 *
	 * @return an Optional of String containing the home name, or an empty Optional if no value was found
	 */
	@Override
	public Optional<String> getHomeDisplayName() {
		ConstantSectionQueryHandler constantSectionQueryHandler = (ConstantSectionQueryHandler) queryHandler.getSectionQueryHandler(Section.CONSTANTS);
		if (constantSectionQueryHandler != null) {
			return constantSectionQueryHandler.getString(HOME_DISPLAY_NAME);
		}
		return Optional.empty();
	}


	/**
	 * Retrieve any value as a string at the supplied key path within the CONSTANTS section of the language file
	 *
	 * @param keyPath a dot-delimited key path for the value to be retrieved
	 * @return an Optional of String containing the value, or an empty Optional if no value was found
	 */
	@Override
	public Optional<String> getString(String keyPath) {
		ConstantSectionQueryHandler constantSectionQueryHandler = (ConstantSectionQueryHandler) queryHandler.getSectionQueryHandler(Section.CONSTANTS);
		if (constantSectionQueryHandler != null) {
			return constantSectionQueryHandler.getString(keyPath);
		}
		return Optional.empty();
	}


	/**
	 * Retrieve a List of String at the supplied key path within the CONSTANTS section of the language file
	 *
	 * @param keyPath a dot-delimited key path for the List to be retrieved
	 * @return a List of String containing the values, or an empty list if no values were found
	 */
	@Override
	public List<String> getStringList(String keyPath) {
		ConstantSectionQueryHandler constantSectionQueryHandler = (ConstantSectionQueryHandler) queryHandler.getSectionQueryHandler(Section.CONSTANTS);
		if (constantSectionQueryHandler != null) {
			return constantSectionQueryHandler.getStringList("TEST_LIST");
		}
		return Collections.emptyList();
	}

}
