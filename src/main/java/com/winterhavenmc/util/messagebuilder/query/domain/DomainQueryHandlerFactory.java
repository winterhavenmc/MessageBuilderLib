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

package com.winterhavenmc.util.messagebuilder.query.domain;

import com.winterhavenmc.util.messagebuilder.namespace.Namespace;
import com.winterhavenmc.util.messagebuilder.query.QueryHandler;
import com.winterhavenmc.util.messagebuilder.query.QueryHandlerFactory;
import com.winterhavenmc.util.messagebuilder.query.domain.constant.ConstantQueryHandler;
import com.winterhavenmc.util.messagebuilder.query.domain.item.ItemQueryHandler;
import com.winterhavenmc.util.messagebuilder.query.domain.message.MessageQueryHandler;
import com.winterhavenmc.util.messagebuilder.query.domain.time.TimeQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.Error;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class DomainQueryHandlerFactory implements QueryHandlerFactory {

	//	private final Map<Namespace.Domain, Supplier<QueryHandler>> handlerCreators = new EnumMap<>(Namespace.Domain.class);
	private final Map<Namespace.Domain, QueryHandler<?>> handlerCache = new EnumMap<>(Namespace.Domain.class);
		private final Configuration configuration;

	public DomainQueryHandlerFactory(Configuration configuration) {
		if (configuration == null) { throw new IllegalArgumentException(Error.Parameter.NULL_CONFIGURATION.getMessage()); }
		this.configuration = configuration;

		// exclude domains that do not have a corresponding section in the language file (or dedicated query handler)
//		EnumSet<Namespace.Domain> configSections = EnumSet.allOf(Namespace.Domain.class);
//		configSections.remove(Namespace.Domain.MACRO);
//
//		for (Namespace.Domain domain : configSections) {
//			ConfigurationSection section = configuration.getConfigurationSection(domain.name());
//			if (section == null) {
//				System.out.println("No configuration section found for domain '" + domain.name() + "'.");
//				// this will have to be checked for mandatory sections, and allow null for optional sections (all but messages, I think)
////				throw new IllegalArgumentException("Missing configuration section for domain: " + domain);
//			}
//			else {
//				handlerCache.put(domain, createDomainQueryHandler(domain, section));
//			}
//		}

//		Set<DomainQueryHandler<?>> handlers = new HashSet<>();
//		for (Namespace.Domain domain : Namespace.Domain.values()) {
//			ConfigurationSection section = configuration.getConfigurationSection(domain.name());
//			if (section != null) {
//				switch (domain) {
//					case CONSTANTS -> handlers.add(new ConstantQueryHandler(section));
//					case ITEMS -> handlers.add(new ItemQueryHandler(section));
//					case MESSAGES -> handlers.add(new MessageQueryHandler(section));
//					case TIME -> handlers.add(new TimeQueryHandler(section));
//				}
//			}
//		}
//		handlerCache.putAll(handlers);
	}


	public Set<DomainQueryHandler<?>> createDomainHandlers(Configuration configuration) {
		Set<DomainQueryHandler<?>> handlers = new HashSet<>();
		for (Namespace.Domain domain : Namespace.Domain.values()) {
			ConfigurationSection section = configuration.getConfigurationSection(domain.name());
			if (section != null) {
				switch (domain) {
					case CONSTANTS -> handlers.add(new ConstantQueryHandler(section));
					case ITEMS -> handlers.add(new ItemQueryHandler(section));
					case MESSAGES -> handlers.add(new MessageQueryHandler(section));
					case TIME -> handlers.add(new TimeQueryHandler(section));
				}
			}
		}
		return handlers;
	}


	public QueryHandler<?> createDomainQueryHandler(Namespace.Domain domain, ConfigurationSection section) {
		return switch (domain) {
			case CONSTANTS -> new ConstantQueryHandler(section);
			case ITEMS -> new ItemQueryHandler(section);
			case MACRO -> null;
			case MESSAGES -> new MessageQueryHandler(section);
			case TIME -> new TimeQueryHandler(section);
		};
	}


	@Override
	public <T> QueryHandler<T> createQueryHandler(Namespace.Domain domain) {
		QueryHandler<?> handler = handlerCache.get(domain);
		if (handler == null) {
			System.out.println("No handler registered for domain: " + domain);
//			throw new IllegalArgumentException("No handler registered for domain: " + domain);
			return null;
		}
		//noinspection unchecked
		return (DomainQueryHandler<T>) handler; // Cast is safe due to prior checks
	}

}
