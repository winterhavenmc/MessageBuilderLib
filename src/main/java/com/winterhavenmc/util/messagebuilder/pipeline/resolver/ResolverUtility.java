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

import com.winterhavenmc.util.messagebuilder.adapters.Adapter;
import com.winterhavenmc.util.messagebuilder.keys.MacroKey;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.extractor.FieldExtractor;
import com.winterhavenmc.util.messagebuilder.pipeline.result.ResultMap;

import java.util.function.BiConsumer;


public class ResolverUtility
{
	private ResolverUtility() { /* private constructor prevents instantiation */ }

	@FunctionalInterface
	public interface TriConsumer<T, U, V> {
		void accept(T t, U u, V v);
	}


	public static BiConsumer<MacroKey, Object> resolveAndMerge(final ResultMap resultMap,
															   final ContextMap contextMap,
															   final Resolver resolver)
	{
		return (subKey, ignored) -> resultMap.putAll(resolver.resolve(subKey, contextMap));
	}


	public static ResultMap mergeResults(ResultMap r1, ResultMap r2)
	{
		r1.putAll(r2);
		return r1;
	}


	public static ResultMap resolveExtractedKeys(
			FieldExtractor extractor,
			Object adapted,
			MacroKey originalKey,
			ContextMap contextMap,
			Resolver resolver,
			Adapter adapter)
	{
		return extractor.extract(adapter, adapted, originalKey).keySet().stream()
				.map(subKey -> resolver.resolve(subKey, contextMap))
				.reduce(new ResultMap(), ResolverUtility::mergeResults);
	}


	public static ResultMap resolveAdapter(
			Adapter adapter,
			Object value,
			MacroKey originalKey,
			ContextMap contextMap,
			FieldExtractor extractor,
			Resolver resolver)
	{
		return adapter.adapt(value)
				.map(adapted -> resolveExtractedKeys(extractor, adapted, originalKey, contextMap, resolver, adapter))
				.orElseGet(ResultMap::new);
	}



}

