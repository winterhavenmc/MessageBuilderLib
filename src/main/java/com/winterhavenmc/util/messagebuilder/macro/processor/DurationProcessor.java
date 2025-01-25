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

import com.winterhavenmc.util.time.TimeString;
import com.winterhavenmc.util.messagebuilder.context.ContextMap;

import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Locale;


public class DurationProcessor<T> extends MacroProcessorTemplate<T> {

	@Override
	public ResultMap resolveContext(final String keyPath, final ContextMap contextMap, final T value) {

		ResultMap resultMap = new ResultMap();

		if (value instanceof Duration duration) {

			Locale locale = null;

			if (contextMap.getRecipient() instanceof Player player) {
				locale = Locale.forLanguageTag(player.getLocale());
			}

			if (locale == null) {
				locale = Locale.getDefault();
			}

			resultMap.put(keyPath, TimeString.getTimeString(locale, duration.toMillis()));
		}
		return resultMap;
	}

}
