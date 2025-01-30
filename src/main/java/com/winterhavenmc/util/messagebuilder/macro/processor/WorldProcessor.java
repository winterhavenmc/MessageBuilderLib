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
import org.bukkit.World;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.Parameter.CONTEXT_MAP;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.Parameter.KEY;


public class WorldProcessor extends MacroProcessorTemplate {

	@Override
	public <MessageId extends Enum<MessageId>> ResultMap resolveContext(final String key, final ContextMap<MessageId> contextMap) {
		if (key == null) { throw new LocalizedException(LocalizedException.MessageKey.PARAMETER_NULL, KEY); }
		if (key.isBlank()) { throw new LocalizedException(LocalizedException.MessageKey.PARAMETER_EMPTY, KEY); }
		if (contextMap == null) { throw new LocalizedException(LocalizedException.MessageKey.PARAMETER_NULL, CONTEXT_MAP); }

		// get value from context map
		Object value = contextMap.get(key);

		ResultMap resultMap = new ResultMap();

		if (value instanceof World world) {
			//TODO: reimplement world name lookups after Multiverse alias lookups are reimplemented
			resultMap.put(key, world.getName());
		}

		return resultMap;
	}

}
