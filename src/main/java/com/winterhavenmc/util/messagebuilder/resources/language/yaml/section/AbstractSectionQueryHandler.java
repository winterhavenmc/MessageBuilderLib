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
import com.winterhavenmc.util.messagebuilder.util.Error;

import java.util.List;

import static com.winterhavenmc.util.messagebuilder.MessageBuilder.bundle;


public abstract class AbstractSectionQueryHandler implements SectionQueryHandler {

	protected final Section section;
	protected Class<?> primaryType;
	protected List<Class<?>> handledTypes;
	protected final YamlConfigurationSupplier configurationSupplier;


	protected AbstractSectionQueryHandler(final YamlConfigurationSupplier configurationSupplier,
	                                      final Section section,
	                                      final Class<?> primaryType,
	                                      final List<Class<?>> handledTypes) {
		if (configurationSupplier == null) { throw new IllegalArgumentException(bundle.getString(Error.Parameter.NULL_CONFIGURATION_SUPPLIER.name())); }

		this.configurationSupplier = configurationSupplier;
		this.section = section;
		this.primaryType = primaryType;
		this.handledTypes = handledTypes;
	}


	public Object query(final String keyPath) {
		return configurationSupplier.getSection(section).getMapList(keyPath);
	}


	/**
	 * Return the Section constant for this query handler type
	 *
	 * @return the CONSTANTS Section constant, establishing this query handler type
	 */
	@Override
	public Section getSectionType() {
		return section;
	}


	/**
	 * The primary type returned by this query handler. A query handler may provide methods that return
	 * values of other types.
	 *
	 * @return String.class as the primary type returned by this query handler
	 */
	@Override
	public Class<?> getPrimaryType() {
		return primaryType;
	}

	/**
	 * A list of the types returned by this query handler. A query handler should not provide methods that return
	 * values of other types.
	 *
	 * @return {@code List} of class types that are handled by this query handler
	 */
	@Override
	public List<Class<?>> listHandledTypes()  {
		return handledTypes;
	}


}
