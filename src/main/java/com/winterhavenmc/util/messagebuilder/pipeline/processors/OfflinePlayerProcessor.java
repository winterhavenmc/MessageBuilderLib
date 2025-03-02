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

import com.winterhavenmc.util.messagebuilder.adapters.location.LocationAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.name.NameAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.uuid.UniqueIdAdapter;
import com.winterhavenmc.util.messagebuilder.pipeline.ContextMap;

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.OfflinePlayer;

import java.util.Objects;

import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.CONTEXT_MAP;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.KEY;
import static com.winterhavenmc.util.messagebuilder.validation.Validate.validate;


/**
 * A macro processor that resolves fields for a {@link OfflinePlayer} stored in the context map
 * and referenced by the given key.
 */
public class OfflinePlayerProcessor extends MacroProcessorTemplate
{
	@Override
	public ResultMap resolveContext(final String key, final ContextMap contextMap)
	{
		validate(key, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, KEY));
		validate(key, String::isBlank, () -> new ValidationException(PARAMETER_EMPTY, KEY));
		validate(contextMap, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, CONTEXT_MAP));

		ResultMap resultMap = new ResultMap();

		contextMap.getOpt(key)
				.filter(OfflinePlayer.class::isInstance)
				.map(OfflinePlayer.class::cast)
				.ifPresent(offlinePlayer -> {

					NameAdapter.asNameable(offlinePlayer).ifPresent(nameable ->
					{
						if (nameable.getName() != null) {
							resultMap.put(key, nameable.getName());
							resultMap.put(key + ".NAME", nameable.getName());
						}
					});

					UniqueIdAdapter.asIdentifiable(offlinePlayer).ifPresent(identifiable ->
					{
						if (identifiable.getUniqueId() != null) {
							resultMap.put(key + ".UUID", identifiable.getUniqueId().toString());
						}
					});

					LocationAdapter.asLocatable(offlinePlayer).ifPresent(locatable ->
					{
						if (locatable.gatLocation() != null) {
							resultMap.putAll(insertLocationFields(key, locatable.gatLocation()));
						}
					});
				});

		return resultMap;
	}

}
