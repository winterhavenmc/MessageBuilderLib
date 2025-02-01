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

package com.winterhavenmc.util.messagebuilder.util;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;


public class LocalizedException extends IllegalArgumentException {

	private static final String ERROR_BUNDLE_NAME = "language.errors";
	private static final Locale DEFAULT_LOCALE = Locale.US;

	private final MessageKey messageKey;
	private final String parameterDisplayName;
	private final Object[] placeholders;


	public LocalizedException(MessageKey messageKey, Object... placeholders) {
		this.messageKey = messageKey;
		this.parameterDisplayName = Arrays.stream(placeholders).findFirst().orElse("unknown").toString();
		this.placeholders = placeholders;
	}


	public LocalizedException(MessageKey messageKey, Parameter parameter, Object... placeholders) {
		this.messageKey = messageKey;
		this.parameterDisplayName = parameter.getDisplayName();

		// create new array for placeholders with Parameter display name string as first element
		Object[] tempArray = new Object[placeholders.length+1];
		tempArray[0] = this.parameterDisplayName;
		System.arraycopy(placeholders, 0, tempArray, 1, placeholders.length);
		this.placeholders = tempArray;
	}


	@Override
	public String getMessage() {
		return getLocalizedMessage(Locale.getDefault());
	}


	public String getLocalizedMessage(Locale locale) {
		ResourceBundle bundle = ResourceBundle.getBundle(ERROR_BUNDLE_NAME, locale);
		String pattern = bundle.getString(messageKey.name());
		return MessageFormat.format(pattern, placeholders);
	}


	public enum MessageKey {
		INVALID_SECTION,
		MISSING_RESOURCE,
		PARAMETER_EMPTY,
		PARAMETER_INVALID,
		PARAMETER_NULL,
		PARAMETER_TYPE_MISMATCH,
		RELOAD_FAILED,
	}


	public enum Parameter {
		CONTEXT_MAP("contextMap"),
		COMMAND_SENDER("commandSender"),
		CONFIGURATION_SUPPLIER("configurationSupplier"),
		DEPENDENCY_CONTEXT("dependencyContext"),
		DURATION("duration"),
		ENTITY("entity"),
		ITEM_SECTION("itemSection"),
		KEY("key"),
		LANGUAGE_QUERY_HANDLER("languageQueryHandler"),
		LANGUAGE_TAG("languageTag"),
		LOCALE("locale"),
		MACRO("macro"),
		MESSAGE("message"),
		MESSAGE_ID("messageId"),
		MESSAGE_SECTION("messageSection"),
		MESSAGE_RECORD("messageRecord"),
		MESSAGE_STRING("messageString"),
		PLUGIN("plugin"),
		RECIPIENT("recipient"),
		REPLACEMENT_MAP("replacementMap"),
		RESOURCE_INSTALLER("resourceInstaller"),
		RESOURCE_LOADER("resourceLoader"),
		RESOURCE_NAME("resourceName"),
		SECTION("section"),
		TARGET_DIR_PATH("targetDirPath"),
		VALUE("value"),
		;

		private final String displayName;

		Parameter(final String displayName) {
			this.displayName = displayName;
		}

		public String getDisplayName() {
			return displayName;
		}

		@Override
		public String toString() {
			return displayName;
		}
	}

}
