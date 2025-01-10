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

import com.winterhavenmc.util.messagebuilder.query.QueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.constants.ConstantQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.items.ItemQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.messages.MessageQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.time.TimeQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.Error;

import org.bukkit.configuration.Configuration;

import java.util.EnumMap;
import java.util.Map;


/**
 * A factory that produces section query handlers for each top level section of the language file,
 * as enumerated by the constants of the {@link Section} enum
 */
public class SectionQueryHandlerFactory {
	private final Map<Section, QueryHandler<?>> domainHandlerCache = new EnumMap<>(Section.class);
	private final Configuration configuration;


	/**
	 * Class constructor for the section query handler factory. The constructor accepts a Bukkit Configuration
	 * as a parameter, and passes the appropriate top level ConfigurationSection to the constructor of the
	 * section query handler being produced.
	 *
	 * @param configuration the language configuration
	 */
	public SectionQueryHandlerFactory(Configuration configuration) {
		if (configuration == null) { throw new IllegalArgumentException(Error.Parameter.NULL_CONFIGURATION.getMessage()); }
		this.configuration = configuration;
	}


	/**
	 * Creates a query handler based on the provided domain.
	 *
	 * @param section the domain for which the query handler is to be created
	 * @return the corresponding SectionQueryHandler
	 * @throws IllegalArgumentException if no handler can be created for the given domain
	 */
	public SectionQueryHandler<?> createHandler(Section section) {
		return switch (section) {
			case CONSTANTS -> new ConstantQueryHandler(configuration.getConfigurationSection(section.name()));
			case ITEMS -> new ItemQueryHandler(configuration.getConfigurationSection(section.name()));
			case MESSAGES -> new MessageQueryHandler(configuration.getConfigurationSection(section.name()));
			case TIME -> new TimeQueryHandler(configuration.getConfigurationSection(section.name()));
			// leaving line below commented so any new section declared in the Section enum needs an explicit handler here
			//default -> new DefaultSectionQueryHandler();
		};
	}


	/**
	 * Creates a section query handler for the section indicated by the {@link Section} enum constant parameter.
	 *
	 * @param section a constant of the {@code Section} enum
	 * @return {@code SectionQueryHandler} of the appropriate type for the section
	 * @param <T> The Type of the section query handler being produced
	 */
	@SuppressWarnings("unchecked")
	public <T> SectionQueryHandler<T> createSectionHandler(Section section) {
		try {
			return (SectionQueryHandler<T>) section.getHandlerClass().getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("Failed to create handler for section: " + section, e);
		}
	}
}
