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

package com.winterhavenmc.util.messagebuilder.pipeline.resolver;

import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.result.ResultMap;
import com.winterhavenmc.util.messagebuilder.recordkey.RecordKey;


public class ContextResolver implements Resolver
{
	private final AtomicResolver atomicResolver;
	private final CompositeResolver compositeResolver;


	public ContextResolver()
	{
		atomicResolver = new AtomicResolver();
		compositeResolver = new CompositeResolver();
	}


	@Override
	public ResultMap resolve(final RecordKey key, final ContextMap contextMap)
	{
		ResultMap resultMap = new ResultMap();
		resultMap.putAll(compositeResolver.resolve(key, contextMap));
		resultMap.putAll(atomicResolver.resolve(key, contextMap));
		return resultMap;
	}

}
