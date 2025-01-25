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

import com.winterhavenmc.util.messagebuilder.context.ContextContainer;
import com.winterhavenmc.util.messagebuilder.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import org.bukkit.command.CommandSender;

import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;


public class CommandSenderProcessor extends MacroProcessorTemplate {

	@Override
	public ResultMap resolveContext(final String key, final ContextMap contextMap) {
		if (key == null) { throw new LocalizedException(PARAMETER_NULL, "key"); }
		if (key.isBlank()) { throw new LocalizedException(PARAMETER_EMPTY, "key"); }
		if (contextMap == null) { throw new LocalizedException(PARAMETER_NULL, "contextMap"); }

		// get context container from map
		Optional<ContextContainer<?>> container = contextMap.getContainer(key);

		// get value from container
		Object value = container.orElseThrow().value();

		ResultMap resultMap = ResultMap.empty();

		if (value instanceof CommandSender commandSender) {
			resultMap.put(key, commandSender.getName());
		}

		return resultMap;
	}

}
