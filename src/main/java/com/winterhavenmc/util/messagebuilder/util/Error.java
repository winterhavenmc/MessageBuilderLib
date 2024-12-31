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

package com.winterhavenmc.util.messagebuilder.util;

/**
 * Enum of errors
 */
public enum Error {
	PARAMETER_NULL_ITEM_KEY("The itemKey parameter was null."),
	PARAMETER_NULL_MESSAGE_ID("The messageId parameter was null."),
	PARAMETER_NULL_CONFIGURATION("The configuration parameter was null."),
	PARAMETER_NULL_PLUGIN("The plugin parameter was null."),
	PARAMETER_NULL_QUERY_HANDLER("The queryHandler parameter was null."),
	PARAMETER_NULL_KEY_PATH("The keyPath parameter cannot be null."),
	PARAMETER_EMPTY_KEY_PATH("The keyPath parameter cannot be empty."),
	PARAMETER_NULL_DOMAIN("Namespace.Domain cannot be null or empty."),
	PARAMETER_NULL_OR_EMPTY_SUBDOMAIN("Subdomains cannot be null or empty."),
	PARAMETER_NULL_COMPOSITE_KEY("compositeKey cannot be null."),
	PARAMETER_NULL_MACRO("macro cannot be null."),
	PARAMETER_NULL_NAMESPACED_KEY("The key parameter cannot be null."),
	PARAMETER_EMPTY_NAMESPACED_KEY("The key parameter cannot be empty."),
	PARAMETER_NULL_CONTEXT_MAP("The contextMap parameter was null."),
	PARAMETER_NULL_VALUE("the value parameter was null.");

	private final String messageString;


	Error(final String message) {
		this.messageString = message;
	}

	public String getMessage() {
		return this.messageString;
	}

}
