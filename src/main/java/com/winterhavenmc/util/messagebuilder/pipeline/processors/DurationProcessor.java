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

import com.winterhavenmc.util.messagebuilder.pipeline.result.ResultMap;
import com.winterhavenmc.util.messagebuilder.recordkey.RecordKey;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.time.PrettyTimeFormatter;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Locale;


/**
 * A macro processor that resolves a string value for a {@link Duration} stored in the context map
 * and referenced by the given key.
 */
public class DurationProcessor extends MacroProcessorTemplate
{
	@Override
	public ResultMap resolveContext(final RecordKey key, final ContextMap contextMap)
	{
		ResultMap resultMap = new ResultMap();
		CommandSender sender = contextMap.getRecipient().sender();

		Locale locale = sender instanceof Player player
				? Locale.forLanguageTag(player.getLocale())
				: Locale.getDefault();

		contextMap.get(key)
				.filter(Duration.class::isInstance)
				.map(Duration.class::cast)
				.ifPresent(duration -> resultMap
						.put(key, new PrettyTimeFormatter().getFormatted(locale, duration))
				);

		return resultMap;
	}

}
