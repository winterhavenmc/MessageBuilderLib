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

import com.winterhavenmc.util.messagebuilder.adapters.displayname.DisplayNameAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.location.LocationAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.name.NameAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.uuid.UniqueIdAdapter;
import com.winterhavenmc.util.messagebuilder.pipeline.ContextMap;

import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.command.CommandSender;

import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.validation.ExceptionMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.CONTEXT_MAP;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


/**
 * A macro processor that resolves fields for a {@link CommandSender} stored in the context map
 * and referenced by the given key.
 */
public class CommandSenderProcessor extends MacroProcessorTemplate
{
	@Override
	public ResultMap resolveContext(final RecordKey key, final ContextMap contextMap)
	{
		validate(contextMap, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, CONTEXT_MAP));

		return Optional.of(new ResultMap())
				.flatMap(resultMap ->
						contextMap.getOpt(key)
						.filter(CommandSender.class::isInstance)
						.map(CommandSender.class::cast)
						.map(commandSender ->
						{
							// populate name field
							new NameAdapter().adapt(commandSender).ifPresent(nameable ->
							{
								resultMap.put(key.toString(), nameable.getName());
								resultMap.put(key + ".NAME", nameable.getName());
							});

							// populate display name field if present
							new DisplayNameAdapter().adapt(commandSender).ifPresent(displayNameable ->
							{
								if (displayNameable.getDisplayName() != null)
								{
									resultMap.put(key + ".DISPLAY_NAME", displayNameable.getDisplayName());
								}
							});

							// populate uuid field if present
							new UniqueIdAdapter().adapt(commandSender).ifPresent(identifiable ->
							{
								if (identifiable.getUniqueId() != null)
								{
									resultMap.put(key + ".UUID", identifiable.getUniqueId().toString());
								}
							});

							// populate location field if present
							new LocationAdapter().adapt(commandSender).ifPresent(locatable ->
							{
								if (locatable.gatLocation() != null)
								{
									resultMap.putAll(insertLocationFields(key, locatable.gatLocation()));
								}
							});

							return resultMap;
						}))
				.orElseGet(ResultMap::new);
	}

}
