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
import com.winterhavenmc.util.messagebuilder.adapters.AdapterRegistry;
import com.winterhavenmc.util.messagebuilder.adapters.displayname.DisplayNameAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.displayname.DisplayNameable;
import com.winterhavenmc.util.messagebuilder.adapters.location.Locatable;
import com.winterhavenmc.util.messagebuilder.adapters.location.LocationAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.name.NameAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.name.Nameable;
import com.winterhavenmc.util.messagebuilder.adapters.quantity.Quantifiable;
import com.winterhavenmc.util.messagebuilder.adapters.quantity.QuantityAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.uuid.Identifiable;
import com.winterhavenmc.util.messagebuilder.adapters.uuid.UniqueIdAdapter;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.DependencyContext;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.ProcessorRegistry;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.ProcessorType;
import com.winterhavenmc.util.messagebuilder.pipeline.result.ResultMap;
import com.winterhavenmc.util.messagebuilder.recordkey.RecordKey;

import java.util.*;


public class CompositeResolver implements Resolver
{
	private final ProcessorRegistry processorRegistry;
	private final AdapterRegistry adapterRegistry;


	/**
	 * Class constructor
	 */
	public CompositeResolver()
	{
		processorRegistry = new ProcessorRegistry(new DependencyContext());
		this.adapterRegistry = new AdapterRegistry();
	}


	/**
	 * Convert the value objects contained in the context map into their string representations in a
	 * new result map.
	 *
	 * @param contextMap a map containing key/value pairs of placeholder strings and their corresponding value object
	 * @return {@code ResultMap} a map containing the placeholder strings and the string representations of the values
	 */
	public ResultMap resolve(final ContextMap contextMap)
	{
		return contextMap.entrySet().stream()
				.map(entry -> processorRegistry.get(ProcessorType.matchType(entry.getValue()))
						.resolveContext(entry.getKey(), contextMap))
				.collect(ResultMap::new, ResultMap::putAll, ResultMap::putAll);
	}


	@Override
	public ResultMap resolve(final RecordKey key, final ContextMap contextMap)
	{
		ResultMap resultMap = new ResultMap();

		// get the value from the context map for the given key
		contextMap.get(key).ifPresent(value ->
				adapterRegistry.getMatchingAdapters(value)
						.forEach(adapter -> adapter.adapt(value)
								.ifPresent(adapted -> {
									// extract subfields from this adapter (e.g. NAME, UUID, LOCATION)
									Map<RecordKey, Object> extracted = extractFieldsFromAdapter(adapter, adapted, key);

									// recursively resolve each subfield
									extracted.forEach((subKey, subValue) -> {
										ResultMap subResult = resolve(subKey, contextMap);
										resultMap.putAll(subResult);
									});
								})));



		// Attempt to resolve atomic values like String, Number, etc.
//		resultMap.putAll(atomicResolver.resolve(key, contextMap));

		return resultMap;
	}


	private <T> Map<RecordKey, Object> extractFieldsFromAdapter(Adapter<T> adapter, T adapted, RecordKey baseKey)
	{
		Map<RecordKey, Object> fields = new HashMap<>();

		switch (adapter)
		{
			case NameAdapter ignored when adapted instanceof Nameable nameable ->
			{
				baseKey.append("NAME")
						.ifPresent(subKey -> fields.put(subKey, nameable.getName()));
				fields.putIfAbsent(baseKey, nameable.getName());
			}

			case DisplayNameAdapter ignored when adapted instanceof DisplayNameable displayNameable ->
			{
				baseKey.append("DISPLAY_NAME")
						.ifPresent(subKey -> fields.put(subKey, displayNameable.getDisplayName()));
				fields.putIfAbsent(baseKey, displayNameable.getDisplayName());
			}

			case UniqueIdAdapter ignored when adapted instanceof Identifiable identifiable ->
			{
				baseKey.append("UUID")
						.ifPresent(subKey -> fields.put(subKey, identifiable.getUniqueId()));
				fields.putIfAbsent(baseKey, identifiable.getUniqueId());
			}

			case LocationAdapter ignored when adapted instanceof Locatable locatable ->
			{
				baseKey.append("LOCATION")
						.ifPresent(subKey -> fields.put(subKey, locatable.gatLocation()));
				fields.putIfAbsent(baseKey, locatable.gatLocation());
			}

			case QuantityAdapter ignored when adapted instanceof Quantifiable quantifiable ->
			{
				baseKey.append("QUANTITY")
						.ifPresent(subKey -> fields.put(subKey, quantifiable.getQuantity()));
				fields.putIfAbsent(baseKey, quantifiable.getQuantity());
			}

			default -> {} // no-op
		}

		return fields;
	}

}
