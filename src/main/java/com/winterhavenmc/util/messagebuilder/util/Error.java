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

public class Error {

	/**
	 * Enum of parameter errors
	 */
	public enum Parameter implements ErrorEnum {
		EMPTY_KEY_PATH("The keyPath parameter cannot be empty."),
		EMPTY_NAMESPACED_KEY("The key parameter cannot be empty."),
		EMPTY_RESOURCE_NAME("Resource name cannot be empty."),
		EMPTY_SUBDOMAIN_ELEMENT("Subdomains cannot be empty."),
		NULL_CONFIGURATION("The configuration parameter was null."),
		NULL_CONTEXT_MAP("The contextMap parameter was null."),
		NULL_DIRECTORY_PATH("Target directory path cannot be null."),
		NULL_DOMAIN("The domain parameter cannot be null."),
		NULL_ITEM_KEY("The itemKey parameter was null."),
		NULL_KEY_PATH("The keyPath parameter cannot be null."),
		NULL_MACRO("macro cannot be null."),
		NULL_MESSAGE_ID("The messageId parameter was null."),
		NULL_NAMESPACED_KEY("The key parameter cannot be null."),
		NULL_PLUGIN("The plugin parameter cannot be null."),
		NULL_PLUGIN_MANAGER("The pluginManager parameter was null."),
		NULL_RECIPIENT("The recipient parameter was null."),
		NULL_QUERY_HANDLER("The queryHandler parameter was null."),
		NULL_SUBDOMAIN_ELEMENT("Subdomains cannot be null."),
		NULL_SUBDOMAINS("Subdomains array cannot be null."),
		NULL_RESOURCE_NAME("Resource name cannot be null."),
		NULL_VALUE("the value parameter was null."),
		NULL_WORLD("The world parameter cannot be null."),
		NULL_MESSAGE_BUILDER("The messageBuilder parameter cannot be null.");

		private final String messageString;

		Parameter(final String message) {
			this.messageString = message;
		}

		public String getMessage() {
			return this.messageString;
		}
	}


	public enum LanguageConfiguration implements ErrorEnum {
		RELOAD_FAILED("The language configuration could not be reloaded. Keeping existing configuration."),
		;

		private final String messageString;

		LanguageConfiguration(final String message) {
			this.messageString = message;
		}

		public String getMessage() {
			return this.messageString;
		}
	}

}
