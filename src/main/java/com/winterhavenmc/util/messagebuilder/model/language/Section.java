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

package com.winterhavenmc.util.messagebuilder.model.language;

import com.winterhavenmc.util.messagebuilder.query.ConstantQueryHandler;
import com.winterhavenmc.util.messagebuilder.query.ItemQueryHandler;
import com.winterhavenmc.util.messagebuilder.query.MessageQueryHandler;
import com.winterhavenmc.util.messagebuilder.query.QueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.SectionProvider;

import java.util.EnumMap;
import java.util.function.Function;


/**
 * An enumeration of Sections that correspond directly to each top level {@code ConfigurationSection} of the language file.
 */
public enum Section
{
	CONSTANTS(ConstantQueryHandler::new),
	ITEMS(ItemQueryHandler::new),
	MESSAGES(MessageQueryHandler::new),
	;

	private static final EnumMap<Section, QueryHandler<?>> HANDLER_MAP = new EnumMap<>(Section.class);
	private final Function<SectionProvider, ? extends QueryHandler<? extends SectionRecord>> handlerFactory;


	/**
	 * Constructor for enum constant instances
	 *
	 * @param handlerFactory the Class of {@link QueryHandler} that is immutably bound to this enum constant
	 */
	<R extends SectionRecord> Section(Function<SectionProvider, QueryHandler<R>> handlerFactory) {
		this.handlerFactory = handlerFactory;
	}


	/**
	 * Retrieve an instance of the section query handler that is bound to this enum constant from the enum map.
	 * If the map has not been populated with an instance of its query handler, a new instance is created using
	 * reflection to call the constructor and pass the {@code ConfigurationSupplier} parameter to the constructor,
	 * which is then placed in the map for future retrievals, and returned to the caller for this use.
	 *
	 * @return an instance of the section query handler that is bound to the enum constant
	 * @param <R> the specific type of the section query handler being returned
	 */
	@SuppressWarnings("unchecked")
	public <R extends SectionRecord> QueryHandler<R> createHandler(SectionProvider provider) {
		return (QueryHandler<R>) HANDLER_MAP.computeIfAbsent(this, __ -> handlerFactory.apply(provider));
	}

}
