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

package com.winterhavenmc.library.messagebuilder.models.language;


public enum InvalidRecordReason
{
	CONSTANT_SECTION_MISSING("Missing constant section."),
	CONSTANT_ENTRY_MISSING("Missing constant entry."),
	CONSTANT_KEY_INVALID("Invalid constant key."),

	ITEM_SECTION_MISSING("Missing item section."),
	ITEM_ENTRY_MISSING("Missing item entry."),
	ITEM_KEY_INVALID("Invalid item key."),

	MESSAGE_SECTION_MISSING("Missing message section."),
	MESSAGE_ENTRY_MISSING("Missing message entry."),
	MESSAGE_KEY_INVALID("Invalid message key."),
	;

	private final String defaultMessage;


	InvalidRecordReason(final String defaultMessage)
	{
		this.defaultMessage = defaultMessage;
	}


	public String getDefaultMessage()
	{
		return this.defaultMessage;
	}

}
