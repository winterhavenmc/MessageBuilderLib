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

package com.winterhavenmc.util.messagebuilder.query;

import com.winterhavenmc.util.messagebuilder.language.yaml.section.SectionQueryHandlerRegistry;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.SectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.SectionQueryHandlerFactory;

import java.util.Map;


public class CompositeQueryHandlerFactory {
	private final Map<Section, SectionQueryHandlerFactory> sectionFactories;
	private final SectionQueryHandlerRegistry registry;


	/**
	 * Class constructor
	 * @param sectionFactories Map of section query handler factories, keyed by section
	 * @param registry the SectionQueryHandlerRegistry
	 */
	public CompositeQueryHandlerFactory(Map<Section, SectionQueryHandlerFactory> sectionFactories,
	                                    SectionQueryHandlerRegistry registry) {
		this.sectionFactories = sectionFactories;
		this.registry = registry;
	}


	/**
	 * Creates a query handler for the given section and type, registers it in the registry, and returns it.
	 *
	 * @param section the section for which the handler is to be created
	 * @param type   the expected type of the handler
	 * @param <T>    the type of the handler
	 * @return the created and registered query handler
	 * @throws IllegalArgumentException if no factory is registered for the section
	 * @throws ClassCastException       if the handler type does not match the provided type
	 */
	public <T> SectionQueryHandler<T> createHandler(Section section, Class<T> type) {
		SectionQueryHandlerFactory factory = sectionFactories.get(section);
		if (factory == null) {
			throw new IllegalArgumentException("No factory registered for section: " + section);
		}

		// Create the handler using the section factory
		SectionQueryHandler<?> handler = factory.createSectionHandler(section);

		// Ensure the handler matches the requested type
		if (!type.isAssignableFrom(handler.getHandledType())) {
			throw new ClassCastException(
					"Handler type mismatch for section: " + section +
							". Expected: " + type.getName() + ", Found: " + handler.getHandledType().getName()
			);
		}

		@SuppressWarnings("unchecked")
		SectionQueryHandler<T> typedHandler = (SectionQueryHandler<T>) handler;

		// Register the handler in the registry
		registry.registerQueryHandler(section, typedHandler);

		return typedHandler;
	}


	/**
	 * Utility method to register all section query handlers automatically.
	 */
	public void registerAllHandlers() {
		for (Map.Entry<Section, SectionQueryHandlerFactory> entry : sectionFactories.entrySet()) {
			Section section = entry.getKey();
			SectionQueryHandlerFactory factory = entry.getValue();

			SectionQueryHandler<?> handler = factory.createSectionHandler(section);
			registry.registerQueryHandler(section, handler);
		}
	}
}
