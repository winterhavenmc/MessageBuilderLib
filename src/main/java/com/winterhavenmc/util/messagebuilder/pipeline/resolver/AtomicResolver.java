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
import com.winterhavenmc.util.messagebuilder.pipeline.processors.ResultMap;
import com.winterhavenmc.util.messagebuilder.recordkey.RecordKey;
import com.winterhavenmc.util.time.PrettyTimeFormatter;

import java.time.Duration;
import java.util.Locale;
import java.util.UUID;

public class AtomicResolver implements Resolver
{
	public ResultMap resolveContext(final RecordKey key, final ContextMap contextMap)
	{
		ResultMap result = new ResultMap();

		contextMap.get(key).ifPresent(value ->
		{
			switch (value)
			{
				case Duration duration -> result.putIfAbsent(key, formatDuration(duration));
				case Number number -> result.putIfAbsent(key, number.toString()); // localized number formatter to come
				case Enum<?> constant -> result.putIfAbsent(key, constant.toString());
				case UUID uuid -> result.putIfAbsent(key, uuid.toString());
				case Boolean bool -> result.putIfAbsent(key, bool.toString());
				case String string -> result.putIfAbsent(key, string);
				default -> result.putIfAbsent(key, value.toString());
			}
		});

		return result;
	}


	private String formatDuration(final Duration duration)
	{
		//TODO: provide PrettyTimeFormatter through injection rather than instantiating new instance every time
		return new PrettyTimeFormatter().getFormatted(Locale.getDefault(), duration);
	}

}
