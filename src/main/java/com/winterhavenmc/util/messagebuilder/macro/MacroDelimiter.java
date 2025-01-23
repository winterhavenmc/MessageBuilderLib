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

package com.winterhavenmc.util.messagebuilder.macro;

import java.util.EnumMap;

/**
 * Enum that contains settable LEFT and RIGHT macro delimiter characters
 */
public enum MacroDelimiter {
	OPEN('{'),
	CLOSE('}');

	private final Character defaultChar;
	private final static EnumMap<MacroDelimiter, Character> backingMap = new EnumMap<>(MacroDelimiter.class);


	/**
	 * Constructor for enum
	 *
	 * @param defaultChar the default character for a delimiter
	 */
	MacroDelimiter(final char defaultChar) {
		this.defaultChar = defaultChar;
	}


	/**
	 * Get default delimiter character for this enum constant
	 *
	 * @return Character the default character for this enum constant
	 */
	public Character getDefaultChar() {
		return this.defaultChar;
	}


	/**
	 * Set a new character as the delimiter for this enum constant
	 *
	 * @param character the character to set as the delimiter for this enum constant
	 */
	public void set(final char character) {
		backingMap.put(this, character);
	}


	/**
	 * Return the delimiter character for this enum constant as a {@code String}.
	 * If no overriding character has been set for the enum constant, the default
	 * character will be returned as a String.
	 *
	 * @return a String containing the delimiter character for this enum constant
	 */
	@Override
	public String toString() {
		return backingMap.computeIfAbsent(this, MacroDelimiter::toChar).toString();
	}


	/**
	 * Return the delimiter character for this enum constant as a {@code Character}.
	 * If no overriding character has been set for the enum constant, the default
	 * character will be returned.
	 *
	 * @return a {@code Character} containing the delimiter for this enum constant
	 */
	public Character toChar() {
		return backingMap.computeIfAbsent(this, MacroDelimiter::getDefaultChar);
	}

}
