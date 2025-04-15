/*
 * Copyright (c) 2022-2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.pipeline.processors;

import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.result.ResultMap;
import com.winterhavenmc.util.messagebuilder.recordkey.RecordKey;
import com.winterhavenmc.util.messagebuilder.util.MultiverseHelper;

import org.bukkit.World;


/**
 * A macro processor that resolves fields for a {@link World} stored in the context map
 * and referenced by the given key.
 */
public class WorldProcessor extends MacroProcessorTemplate
{
	@Override
	public ResultMap resolveContext(final RecordKey key, final ContextMap contextMap)
	{
		ResultMap resultMap = new ResultMap();

		contextMap.get(key)
				.filter(World.class::isInstance)
				.map(World.class::cast)
				.map(world -> MultiverseHelper.getAlias(world).orElse(world.getName()))
				.ifPresent(worldName -> {
						resultMap.put(key, worldName);
						key.append("NAME").ifPresent(k ->
								resultMap.put(k, worldName));
				});

		return resultMap;
	}

}
