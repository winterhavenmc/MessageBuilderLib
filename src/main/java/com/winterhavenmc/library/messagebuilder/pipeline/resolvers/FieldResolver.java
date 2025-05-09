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

package com.winterhavenmc.library.messagebuilder.pipeline.resolvers;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;

import java.util.List;


public class FieldResolver implements Resolver
{
	private final List<Resolver> resolvers;


	public FieldResolver(List<Resolver> resolvers)
	{
		this.resolvers = resolvers;
	}


	@Override
	public MacroStringMap resolve(final MacroKey macroKey, final MacroObjectMap macroObjectMap)
	{
		return resolvers.stream()
				.map(resolver -> resolver.resolve(macroKey, macroObjectMap))
				.collect(MacroStringMap::new, MacroStringMap::putAll, MacroStringMap::putAll);
	}

}
