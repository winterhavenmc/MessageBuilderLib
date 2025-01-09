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

import com.winterhavenmc.util.messagebuilder.namespace.Namespace;
import com.winterhavenmc.util.messagebuilder.query.domain.DefaultDomainQueryHandler;
import com.winterhavenmc.util.messagebuilder.query.domain.DomainQueryHandler;
import com.winterhavenmc.util.messagebuilder.query.domain.DomainQueryHandlerFactory;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class QueryHandlerRegistry {

	private final Map<Namespace.Domain, DomainQueryHandler<?>> domainHandlers = new EnumMap<>(Namespace.Domain.class);
	private final DefaultDomainQueryHandler defaultHandler;

	public QueryHandlerRegistry(Set<DomainQueryHandler<?>> handlers) {
		for (DomainQueryHandler<?> handler : handlers) {
			domainHandlers.put(handler.getDomain(), handler);
		}
		defaultHandler = new DefaultDomainQueryHandler(null); // Default domain is `null` for generic use.
	}

	public QueryHandlerRegistry(DomainQueryHandlerFactory factory) {
		for (Namespace.Domain domain : Namespace.Domain.values()) {
			DomainQueryHandler<?> handler = (DomainQueryHandler<?>) factory.createQueryHandler(domain);
			if (handler != null) {
				domainHandlers.put(domain, handler);
			}
		}
		defaultHandler = new DefaultDomainQueryHandler(null);
	}

	@SuppressWarnings("unchecked")
	public <T> DomainQueryHandler<T> getDomainHandler(Namespace.Domain domain) {
		return (DomainQueryHandler<T>) domainHandlers.getOrDefault(domain, new DefaultDomainQueryHandler(domain));
	}

}
