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

import com.winterhavenmc.util.messagebuilder.query.QueryHandler;
import com.winterhavenmc.util.messagebuilder.query.QueryHandlerRegistry;
import com.winterhavenmc.util.messagebuilder.util.Error;

import java.util.EnumMap;
import java.util.Map;


/**
 * A registry to manage QueryHandler instances.
 */
public class SectionQueryHandlerRegistry implements QueryHandlerRegistry {

	private final Map<Section, QueryHandler<?>> handlers = new EnumMap<>(Section.class);


	/**
	 * Registers a QueryHandler for a specific section.
	 *
	 * @param section  the section associated with the handler
	 * @param handler the QueryHandler instance
	 * @throws IllegalArgumentException if a handler is already registered for the section
	 */
	@Override
	public void registerQueryHandler(Section section, QueryHandler<?> handler) {
		if (handlers.containsKey(section)) {
			throw new IllegalArgumentException("Handler already registered for section: " + section);
		}
		handlers.put(section, handler);
	}


	/**
	 * Retrieves the QueryHandler for the specified section.
	 *
	 * @param section the section to retrieve the handler for
	 * @param <T>    the type of the handler
	 * @return an Optional containing the QueryHandler, or empty if none is registered
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> QueryHandler<T> getQueryHandler(Section section) {
		if (section == null) { throw new IllegalArgumentException(Error.Parameter.NULL_SECTION.getMessage()); }

		return (SectionQueryHandler<T>) handlers.get(section);
	}


	/**
	 * Checks whether a QueryHandler is registered for the specified section.
	 *
	 * @param section the section to check
	 * @return true if a handler is registered, false otherwise
	 */
	@Override
	public boolean hasQueryHandler(Section section) {
		return handlers.containsKey(section);
	}


	/**
	 * Clears all registered handlers.
	 */
	@Override
	public void clearQueryHandlers() {
		handlers.clear();
	}

}
