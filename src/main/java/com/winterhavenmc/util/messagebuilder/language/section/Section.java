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

package com.winterhavenmc.util.messagebuilder.language.section;

import com.winterhavenmc.util.messagebuilder.language.section.constants.ConstantQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.items.ItemQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.messages.MessageQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.time.TimeQueryHandler;


/**
 * An enumeration of Sections that correspond directly to each top level {@code ConfigurationSection} of the language file.
 */
public enum Section {
	CONSTANTS(ConstantQueryHandler.class, "Constant", "Constants", "CONST"),
	ITEMS(ItemQueryHandler.class, "Item", "Items", "ITEM"),
	MESSAGES(MessageQueryHandler.class, "Message", "Messages", "MSG"),
	TIME(TimeQueryHandler.class, "Time", "Time", "TIME"),
	;

	private final Class<? extends SectionQueryHandler<?>> handlerClass;
	private final String singularName;
	private final String pluralName;
	private final String placeholderPrefix;


	Section(final Class<? extends SectionQueryHandler<?>> handlerClass,
	        final String singularName,
	        final String pluralName,
	        final String placeholderPrefix) {
		this.handlerClass = handlerClass;
		this.singularName = singularName;
		this.pluralName = pluralName;
		this.placeholderPrefix = placeholderPrefix;

		// Validate the First Law of the Library
		if (!SectionQueryHandler.class.isAssignableFrom(handlerClass)) {
			throw new IllegalArgumentException(
					handlerClass.getSimpleName() + " must implement SectionQueryHandler"
			);
		}
	}


	public Class<? extends SectionQueryHandler<?>> getHandlerClass() {
		return handlerClass;
	}

	public String getSingularName() {
		return singularName;
	}

	public String getPluralName() {
		return pluralName;
	}

	public String getPlaceholderPrefix() {
		return placeholderPrefix;
	}

}
