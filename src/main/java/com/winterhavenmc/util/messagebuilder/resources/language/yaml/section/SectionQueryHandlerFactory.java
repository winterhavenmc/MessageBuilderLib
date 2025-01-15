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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.constants.ConstantSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.items.ItemSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.time.TimeSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.Error;

import java.util.EnumMap;
import java.util.Map;


/**
 * A factory that produces section query handlers for each top level section of the language file,
 * as enumerated by the constants of the {@link Section} enum
 */
public class SectionQueryHandlerFactory {

	private final Map<Section, SectionQueryHandler> sectionHandlerCache = new EnumMap<>(Section.class);
	private final YamlConfigurationSupplier configurationSupplier;


	/**
	 * Class constructor for the section query handler factory. The constructor accepts a Bukkit Configuration
	 * as a parameter, and passes the appropriate top level ConfigurationSection to the constructor of the
	 * section query handler being produced.
	 *
	 * @param configurationSupplier the provider of the language configuration
	 */
	public SectionQueryHandlerFactory(YamlConfigurationSupplier configurationSupplier) {
		if (configurationSupplier == null) { throw new IllegalArgumentException(Error.Parameter.NULL_CONFIGURATION.getMessage()); }
		this.configurationSupplier = configurationSupplier;
	}


	/**
	 * Get a section query handler. If the requested query handler is not in the cache, a new instance is created
	 * to be returned and placed in the cache.
	 *
	 * @param section A constant of the Section enum specifying which SectionQueryHandler to return
	 * @return The requested SectionQueryHandler
	 */
	public SectionQueryHandler getQueryHandler(Section section) {
		return sectionHandlerCache.computeIfAbsent(section, this::createSectionHandler);
	}


	/**
	 * Creates a query handler based on the provided section.
	 *
	 * @param section the section for which the query handler is to be created
	 * @return the corresponding SectionQueryHandler
	 * @throws IllegalArgumentException if no handler can be created for the given section
	 */
	public SectionQueryHandler createSectionHandler(Section section) {
		return switch (section) {
			case CONSTANTS -> new ConstantSectionQueryHandler(configurationSupplier);
			case ITEMS -> new ItemSectionQueryHandler(configurationSupplier);
			case MESSAGES -> new MessageSectionQueryHandler(configurationSupplier);
			case TIME -> new TimeSectionQueryHandler(configurationSupplier);
			// leaving line below commented so any new section declared in the Section enum needs an explicit handler here
			//default -> new DefaultSectionQueryHandler();
		};
	}


	/**
	 * Creates a section query handler for the section indicated by the {@link Section} enum constant parameter.
	 *
	 * @param section a constant of the {@code Section} enum
	 * @return {@code SectionQueryHandler} of the appropriate type for the section
	 */
	public SectionQueryHandler createSectionHandlerDynamically(Section section) {
		try {
			return section.getHandlerClass().getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("Failed to create handler for section: " + section, e);
		}
	}
}
