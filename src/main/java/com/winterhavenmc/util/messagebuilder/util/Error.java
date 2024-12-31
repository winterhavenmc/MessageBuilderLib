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
	PARAMETER_CONFIGURATION_NULL("The configuration parameter was null."),
	PARAMETER_CONTEXT_MAP_NULL("The contextMap parameter was null."),
	PARAMETER_DIRECTORY_PATH_NULL("Target directory path cannot be null."),
	PARAMETER_DOMAIN_NULL("The domain parameter cannot be null."),
	PARAMETER_ITEM_KEY_NULL("The itemKey parameter was null."),
	PARAMETER_KEY_PATH_EMPTY("The keyPath parameter cannot be empty."),
	PARAMETER_KEY_PATH_NULL("The keyPath parameter cannot be null."),
	PARAMETER_MACRO_NULL("macro cannot be null."),
	PARAMETER_MESSAGE_ID_NULL("The messageId parameter was null."),
	PARAMETER_NAMESPACED_KEY_EMPTY("The key parameter cannot be empty."),
	PARAMETER_NAMESPACED_KEY_NULL("The key parameter cannot be null."),
	PARAMETER_PLUGIN_NULL("The plugin parameter was null."),
	PARAMETER_QUERY_HANDLER_NULL("The queryHandler parameter was null."),
	PARAMETER_RESOURCE_NAME_EMPTY("Resource name cannot be empty."),
	PARAMETER_SUBDOMAIN_ELEMENT_EMPTY("Subdomains cannot be empty."),
	PARAMETER_SUBDOMAIN_ELEMENT_NULL("Subdomains cannot be null."),
	PARAMETER_SUBDOMAINS_NULL("Subdomains array cannot be null."),
	PARAMETER_RESOURCE_NAME_NULL("Resource name cannot be null."),
	PARAMETER_VALUE_NULL("the value parameter was null."),
	;

	private final String messageString;


	Error(final String message) {
		this.messageString = message;
	}

	public String getMessage() {
		return this.messageString;
	}

}
