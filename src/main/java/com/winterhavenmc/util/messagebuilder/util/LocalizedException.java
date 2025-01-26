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
import java.util.Locale;
import java.util.ResourceBundle;


public class LocalizedException extends IllegalArgumentException {

	private static final String ERROR_BUNDLE_NAME = "language.errors";


	private final MessageKey messageKey;
	private final Object[] placeholders;

	public LocalizedException(MessageKey messageKey, Object... placeholders) {
		this.messageKey = messageKey;
		this.placeholders = placeholders;
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
		MESSAGE_ID("messageId"),
		MESSAGE_STRING("messageString"),
		KEY("key"),
		RECIPIENT("recipient"),
		REPLACEMENT_MAP("replacementMap"),
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
			return getDisplayName();
		}
	}

}
