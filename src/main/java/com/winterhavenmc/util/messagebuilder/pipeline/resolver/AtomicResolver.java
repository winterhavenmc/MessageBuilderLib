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

import com.winterhavenmc.util.messagebuilder.keys.MacroKey;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.result.ResultMap;
import com.winterhavenmc.util.time.PrettyTimeFormatter;

import java.time.Duration;
import java.util.Locale;
import java.util.UUID;


public class AtomicResolver implements Resolver
{
	private final PrettyTimeFormatter prettyTimeFormatter;


	public AtomicResolver(final PrettyTimeFormatter prettyTimeFormatter)
	{
		this.prettyTimeFormatter = prettyTimeFormatter;
	}


	public ResultMap resolve(final MacroKey macroKey, final ContextMap contextMap)
	{
		ResultMap result = new ResultMap();

		contextMap.get(macroKey).ifPresent(value ->
		{
			switch (value)
			{
				case Duration duration -> result.putIfAbsent(macroKey, formatDuration(duration));
				case Number number -> result.putIfAbsent(macroKey, number.toString()); // localized number formatter to come
				case Enum<?> constant -> result.putIfAbsent(macroKey, constant.toString());
				case UUID uuid -> result.putIfAbsent(macroKey, uuid.toString());
				case Boolean bool -> result.putIfAbsent(macroKey, bool.toString());
				case String string -> result.putIfAbsent(macroKey, string);
				default -> result.putIfAbsent(macroKey, value.toString());
			}
		});

		return result;
	}


	private String formatDuration(final Duration duration)
	{
		return prettyTimeFormatter.getFormatted(Locale.getDefault(), duration);
	}

}
