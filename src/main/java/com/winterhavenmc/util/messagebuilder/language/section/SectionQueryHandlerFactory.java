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

import com.winterhavenmc.util.messagebuilder.namespace.Namespace;
import com.winterhavenmc.util.messagebuilder.query.QueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.query.constant.ConstantQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.query.item.ItemQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.query.message.MessageQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.query.time.TimeQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.Error;

import org.bukkit.configuration.Configuration;

import java.util.EnumMap;
import java.util.Map;


public class SectionQueryHandlerFactory {
	private final Map<Namespace.Domain, QueryHandler<?>> domainHandlerCache = new EnumMap<>(Namespace.Domain.class);
	private final Configuration configuration;

	public SectionQueryHandlerFactory(Configuration configuration) {
		if (configuration == null) { throw new IllegalArgumentException(Error.Parameter.NULL_CONFIGURATION.getMessage()); }
		this.configuration = configuration;
	}

	/**
	 * Creates a query handler based on the provided domain.
	 *
	 * @param domain the domain for which the query handler is to be created
	 * @return the corresponding SectionQueryHandler
	 * @throws IllegalArgumentException if no handler can be created for the given domain
	 */
	public SectionQueryHandler<?> createHandler(Namespace.Domain domain) {
		return switch (domain) {
			case CONSTANTS -> new ConstantQueryHandler(configuration.getConfigurationSection(domain.name()));
			case ITEMS -> new ItemQueryHandler(configuration.getConfigurationSection(domain.name()));
			case MESSAGES -> new MessageQueryHandler(configuration.getConfigurationSection(domain.name()));
			case TIME -> new TimeQueryHandler(configuration.getConfigurationSection(domain.name()));
			default -> throw new IllegalArgumentException("No handler available for domain: " + domain);
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
