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

package com.winterhavenmc.util.messagebuilder.resources;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.SectionRecord;


public class QueryHandlerFactory
{
	private final YamlConfigurationSupplier configurationSupplier;


	/**
	 * Class constructor
	 *
	 * @param configurationSupplier yaml configuration supplier
	 */
	public QueryHandlerFactory(final YamlConfigurationSupplier configurationSupplier)
	{
		this.configurationSupplier = configurationSupplier;
	}


	/**
	 * Retrieve query handler for section
	 *
	 * @param section the section for which a query handler is returned
	 * @return a query handler for the section
	 */
	public <R extends SectionRecord> QueryHandler<R> getQueryHandler(final Section section)
	{
		return section.createHandler(configurationSupplier);
	}

}
