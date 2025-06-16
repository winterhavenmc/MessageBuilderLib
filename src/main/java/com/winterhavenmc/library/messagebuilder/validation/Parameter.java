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


	Parameter(final String displayName)
	{
		this.displayName = displayName;
	}


	public String getDisplayName()
	{
		return displayName;
	}


	@Override
	public String toString()
	{
		return displayName;
	}

}
