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

package com.winterhavenmc.library.messagebuilder.models.validation;


/**
 * Enumeration of structured keys used to identify localized error messages
 * for validation and configuration-related exceptions.
 * <p>
 * These keys correspond to entries in a {@link java.util.ResourceBundle},
 * allowing the library to provide translatable, context-aware messages
 * to plugin users and administrators.
 * <p>
 * Typically used in conjunction with {@link ValidationException} and
 * {@link Validator#formatMessage(ErrorMessageKey, Parameter)}.
 *
 * <p>
 * Example string in the language file: {@code PARAMETER_NULL=Parameter {0} must not be null.}
 *
 * @see ValidationException
 * @see Validator
 * @see Parameter
 */
public enum ErrorMessageKey
{
	/**
	 * Indicates that a required configuration section is invalid or missing.
	 */
	INVALID_SECTION,

	/**
	 * Indicates that a language file could not be found for the configured locale.
	 */
	MISSING_LANGUAGE_FILE,

	/**
	 * Indicates that a required language resource file is missing from the plugin jar.
	 */
	MISSING_LANGUAGE_RESOURCE,

	/**
	 * Indicates a general failure to locate a required resource.
	 */
	MISSING_RESOURCE,

	/**
	 * Indicates that a parameter failed validation, but no specific cause is given.
	 */
	PARAMETER_INVALID,

	/**
	 * Indicates that a required parameter was {@code null}.
	 */
	PARAMETER_NULL,

	/**
	 * Indicates that a parameter was of the wrong type.
	 */
	PARAMETER_TYPE_MISMATCH,

	/**
	 * Indicates that a configuration reload operation failed.
	 */
	RELOAD_FAILED,

	/**
	 * Indicates that a provided string was blank (empty or only whitespace).
	 */
	STRING_BLANK,
}
