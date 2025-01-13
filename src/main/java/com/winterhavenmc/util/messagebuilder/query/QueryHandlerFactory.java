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

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.SectionQueryHandlerFactory;

import java.util.HashMap;
import java.util.Map;

public class QueryHandlerFactory {
	private final Map<Class<?>, Object> factories = new HashMap<>();
	private final YamlConfigurationSupplier yamlConfigurationSupplier;

	public QueryHandlerFactory(final YamlConfigurationSupplier yamlConfigurationSupplier) {
		this.yamlConfigurationSupplier = yamlConfigurationSupplier;
	};

	@SuppressWarnings("unchecked")
	public <T> T getQueryHandlerFactory(Class<T> factoryType) {
		return (T) factories.computeIfAbsent(factoryType, this::createQueryHandlerFactory);
	}

	private Object createQueryHandlerFactory(Class<?> factoryType) {
		if (factoryType == SectionQueryHandlerFactory.class) {
			return new SectionQueryHandlerFactory(yamlConfigurationSupplier);
		}
		throw new IllegalArgumentException("Unknown factory type: " + factoryType);
	}
}
