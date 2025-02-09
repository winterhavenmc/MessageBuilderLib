/*
 * Copyright (c) 2022 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.macro.processor;

import com.winterhavenmc.util.messagebuilder.adapters.displayname.DisplayNameAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.location.LocationAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.name.NameAdapter;
import com.winterhavenmc.util.messagebuilder.adapters.uuid.UniqueIdAdapter;
import com.winterhavenmc.util.messagebuilder.context.ContextMap;

import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import org.bukkit.command.CommandSender;

import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.util.MessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.util.messagebuilder.util.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.util.Parameter.KEY;
import static com.winterhavenmc.util.messagebuilder.util.Parameter.CONTEXT_MAP;
import static com.winterhavenmc.util.messagebuilder.util.Validate.validate;


/**
 * A macro processor that resolves fields for a {@link CommandSender} stored in the context map
 * and referenced by the given key.
 */
public class CommandSenderProcessor extends MacroProcessorTemplate
{
	@Override
	public ResultMap resolveContext(final String key, final ContextMap contextMap)
	{
		validate(key, Objects::isNull, () -> new LocalizedException(PARAMETER_NULL, KEY));
		validate(key, String::isBlank, () -> new LocalizedException(PARAMETER_EMPTY, KEY));
		validate(contextMap, Objects::isNull, () -> new LocalizedException(PARAMETER_NULL, CONTEXT_MAP));

		return Optional.of(new ResultMap())
				.flatMap(resultMap ->
						contextMap.getOpt(key)
						.filter(CommandSender.class::isInstance)
						.map(CommandSender.class::cast)
						.map(commandSender ->
						{
							// populate name field
							NameAdapter.asNameable(commandSender).ifPresent(nameable ->
							{
								resultMap.put(key, nameable.getName());
								resultMap.put(key + ".NAME", nameable.getName());
							});

							// populate display name field if present
							DisplayNameAdapter.asDisplayNameable(commandSender).ifPresent(displayNameable ->
							{
								if (displayNameable.getDisplayName() != null)
								{
									resultMap.put(key + ".DISPLAY_NAME", displayNameable.getDisplayName());
								}
							});

							// populate uuid field if present
							UniqueIdAdapter.asIdentifiable(commandSender).ifPresent(identifiable ->
							{
								if (identifiable.getUniqueId() != null)
								{
									resultMap.put(key + ".UUID", identifiable.getUniqueId().toString());
								}
							});

							// populate location field if present
							LocationAdapter.asLocatable(commandSender).ifPresent(locatable ->
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
