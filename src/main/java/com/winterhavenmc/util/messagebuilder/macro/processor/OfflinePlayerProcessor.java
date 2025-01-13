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
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.Error;
import org.bukkit.OfflinePlayer;


public class OfflinePlayerProcessor extends MacroProcessorTemplate implements MacroProcessor {

	public OfflinePlayerProcessor(final LanguageQueryHandler queryHandler) {
		super(queryHandler);
	}

	@Override
	public <T> ResultMap resolveContext(final String key, final ContextMap contextMap, final T value) {
		if (key == null) { throw new IllegalArgumentException(Error.Parameter.NULL_KEY_PATH.getMessage()); }
		if (contextMap == null) { throw new IllegalArgumentException(Error.Parameter.NULL_CONTEXT_MAP.getMessage()); }
		if (value == null) { throw new IllegalArgumentException(Error.Parameter.NULL_VALUE.getMessage()); }

		ResultMap resultMap = new ResultMap();

		if (value instanceof OfflinePlayer offlinePlayer) {
			resultMap.put(key, offlinePlayer.getName());
		}

		return resultMap;
	}

}
