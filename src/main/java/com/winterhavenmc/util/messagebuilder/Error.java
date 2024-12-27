/*
 * Copyright (c) 2024 Tim Savage.
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

/**
 * Enum of errors
 */
public enum Error {
	PARAMETER_NULL_ITEM_KEY("the itemKey parameter was null."),
	PARAMETER_NULL_MESSAGE_ID("the messageId parameter was null."),
	PARAMETER_NULL_CONFIGURATION("the configuration parameter was null."),
	;

	private final String messageString;

	Error(final String message) {
		this.messageString = message;
	}

	public String getMessage() {
		return this.messageString;
	}

}
