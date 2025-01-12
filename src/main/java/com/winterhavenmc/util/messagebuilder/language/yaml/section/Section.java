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

package com.winterhavenmc.util.messagebuilder.language.yaml.section;

import com.winterhavenmc.util.messagebuilder.language.yaml.section.constants.ConstantSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.items.ItemSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.messages.MessageSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.time.TimeSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.query.QueryHandlerRegistryKey;


/**
 * An enumeration of Sections that correspond directly to each top level {@code ConfigurationSection} of the language file.
 */
public enum Section implements QueryHandlerRegistryKey {
	CONSTANTS(ConstantSectionQueryHandler.class, "Constant", "Constants", "CONST"),
	ITEMS(ItemSectionQueryHandler.class, "Item", "Items", "ITEM"),
	MESSAGES(MessageSectionQueryHandler.class, "Message", "Messages", "MSG"),
	TIME(TimeSectionQueryHandler.class, "Time", "Time", "TIME"),
	;

	private final Class<? extends SectionQueryHandler<?>> handlerClass;
	private final String singularName;
	private final String pluralName;
	private final String mnemonic;


	/**
	 * Constructor for enum constant instances
	 * @param handlerClass the Class of {@link SectionQueryHandler} that is immutably bound to this enum constant
	 * @param singularName the singular name of this section (ex: Item)
	 * @param pluralName the plural name for this section (ex: Items)
	 * @param mnemonic the short mnemonic for this section. To be used in key generation, or other programmatic purposes.
	 */
	Section(final Class<? extends SectionQueryHandler<?>> handlerClass,
	        final String singularName,
	        final String pluralName,
	        final String mnemonic
	) {
		this.handlerClass = handlerClass;
		this.singularName = singularName;
		this.pluralName = pluralName;
		this.mnemonic = mnemonic;

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

	public String getMnemonic() {
		return mnemonic;
	}

}
