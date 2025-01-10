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
import com.winterhavenmc.util.messagebuilder.language.section.item.ItemQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.message.MessageQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.time.TimeQueryHandler;


/**
 * An enumeration of Sections that correspond directly to each top level {@code ConfigurationSection} of the language file.
 */
public enum Section {
	CONSTANTS(ConstantQueryHandler.class),
	ITEMS(ItemQueryHandler.class),
	MESSAGES(MessageQueryHandler.class),
	TIME(TimeQueryHandler.class),
	;

	private final Class<? extends SectionQueryHandler<?>> handlerClass;


	Section(final Class<? extends SectionQueryHandler<?>> handlerClass) {
		this.handlerClass = handlerClass;
	}

	public Class<? extends SectionQueryHandler<?>> getHandlerClass() {
		return handlerClass;
	}

}
