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

package com.winterhavenmc.library.messagebuilder.validation;

/**
 * Enumerates the names of parameters that may be subject to validation
 * within the MessageBuilder library.
 * <p>
 * Each enum constant corresponds to a symbolic parameter name used in
 * validation methods and exception messages. This design provides compile-time
 * safety, IDE auto-completion, and helps ensure consistency across the codebase.
 * <p>
 * The {@link #displayName} associated with each parameter is used in
 * localized messages and can be retrieved via {@link #getDisplayName()}.
 * <p>
 * Example usage:
 * {@snippet lang="java":
 * throw new ValidationException(PARAMETER_NULL, Parameter.COMMAND_SENDER);
 * }
 *
 * <p>
 * Inspired by the best practices outlined in "Effective Java" (Joshua Bloch),
 * this approach avoids stringly-typed parameter keys while enabling
 * internationalization support via structured message formatting.
 *
 * @see ValidationException
 * @see ErrorMessageKey
 */
public enum Parameter
{
	ADAPTER("adapter"),
	CONTEXT_MAP("contextMap"),
	COMMAND_SENDER("commandSender"),
	CONFIGURATION_SUPPLIER("configurationSupplier"),
	DELEGATE("delegate"),
	DEPENDENCY_CONTEXT("dependencyContext"),
	DURATION("duration"),
	ENTITY("entity"),
	ITEM_SECTION("itemSection"),
	KEY("key"),
	LANGUAGE_FILE("languageFile"),
	LANGUAGE_QUERY_HANDLER("languageQueryHandler"),
	LANGUAGE_RESOURCE_MANAGER("languageResourceManager"),
	LANGUAGE_TAG("languageTag"),
	LOCALE("locale"),
	LOCALE_PROVIDER("localeProvider"),
	LOCATION("location"),
	LOWER_BOUND("lowerBound"),
	MACRO("macro"),
	MACRO_KEY("macroKey"),
	MESSAGE("message"),
	MESSAGE_ID("messageId"),
	MESSAGE_PROCESSOR("messageProcessor"),
	MESSAGE_SECTION("messageSection"),
	MESSAGE_RECORD("messageRecord"),
	MESSAGE_STRING("messageString"),
	NAME("name"),
	PLACEHOLDER("placeholder"),
	PLUGIN("plugin"),
	PRECISION("precision"),
	QUANTITY("quantity"),
	QUERY_HANDLER("queryHandler"),
	RECIPIENT("recipient"),
	RECORD_TYPE("recordType"),
	REPLACEMENT_MAP("replacementMap"),
	FORMATTER_CONTAINER("formatterContainer"),
	RESOURCE_INSTALLER("resourceInstaller"),
	RESOURCE_LOADER("resourceLoader"),
	RESOURCE_NAME("resourceName"),
	RESOURCE_TYPE("resourceType"),
	SECTION("section"),
	SECTION_SUPPLIER("sectionSupplier"),
	TARGET_DIR_PATH("targetDirPath"),
	TYPE("type"),
	UNIQUE_ID("uniqueId"),
	UNKNOWN("«UNKNOWN»"),
	VALUE("value"),
	WORLD("world"),
	;

	private final String displayName;


	/**
	 * Constructs a {@code Parameter} enum constant with the given display name.
	 *
	 * @param displayName the canonical or user-facing name of the parameter
	 */
	Parameter(final String displayName)
	{
		this.displayName = displayName;
	}


	/**
	 * Returns the canonical display name for this parameter, as used in
	 * localized error messages.
	 *
	 * @return the parameter's display name
	 */
	public String getDisplayName()
	{
		return displayName;
	}


	/**
	 * Returns the {@linkplain #getDisplayName() display name} of the parameter.
	 * Equivalent to {@code getDisplayName()}.
	 *
	 * @return the parameter's display name
	 */
	@Override
	public String toString()
	{
		return displayName;
	}

}
