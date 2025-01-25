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

import com.winterhavenmc.util.messagebuilder.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;

import org.bukkit.OfflinePlayer;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;


public class OfflinePlayerProcessor<T> extends MacroProcessorTemplate<T> {

	@Override
	public ResultMap resolveContext(final String keyPath, final ContextMap contextMap, final T value) {
		if (keyPath == null) { throw new LocalizedException(PARAMETER_NULL, "keyPath"); }
		if (keyPath.isBlank()) { throw new LocalizedException(PARAMETER_EMPTY, "keyPath"); }
		if (contextMap == null) { throw new LocalizedException(PARAMETER_NULL, "contextMap"); }
		if (value == null) { throw new LocalizedException(PARAMETER_NULL, "value"); }

		ResultMap resultMap = new ResultMap();

		if (value instanceof OfflinePlayer offlinePlayer) {
			resultMap.put(keyPath, offlinePlayer.getName());
		}

		return resultMap;
	}

}
