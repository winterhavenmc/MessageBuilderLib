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

import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import com.winterhavenmc.util.time.PrettyTimeFormatter;
import com.winterhavenmc.util.messagebuilder.pipeline.ContextMap;

import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Locale;
import java.util.Objects;

import static com.winterhavenmc.util.messagebuilder.validation.ExceptionMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.CONTEXT_MAP;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


/**
 * A macro processor that resolves a string value for a {@link Duration} stored in the context map
 * and referenced by the given key.
 */
public class DurationProcessor extends MacroProcessorTemplate
{
	@Override
	public ResultMap resolveContext(final RecordKey key, final ContextMap contextMap)
	{
		validate(contextMap, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, CONTEXT_MAP));

		ResultMap resultMap = new ResultMap();

		contextMap.get(key)
				.filter(Duration.class::isInstance)
				.map(Duration.class::cast)
				.ifPresent(duration -> {
					//TODO: deal with minecraft's mangled language tags
					Locale locale = contextMap.getRecipient()
							.filter(Player.class::isInstance)
							.map(Player.class::cast)
							.map(Player::getLocale)
							.map(Locale::forLanguageTag)
							.orElse(Locale.getDefault());

					resultMap.put(key.toString(), new PrettyTimeFormatter().getFormatted(locale, duration));
				});

		return resultMap;
	}

}
