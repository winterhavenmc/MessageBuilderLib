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

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.Parameter.CONTEXT_MAP;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.Parameter.KEY;


public class NullProcessor extends MacroProcessorTemplate {

	@Override
	public <MessageId extends Enum<MessageId>>
	ResultMap resolveContext(final String key, final ContextMap contextMap) {
		if (key == null) { throw new LocalizedException(PARAMETER_NULL, KEY); }
		if (key.isBlank()) { throw new LocalizedException(PARAMETER_EMPTY, KEY); }
		if (contextMap == null) { throw new LocalizedException(PARAMETER_NULL, CONTEXT_MAP); }

		ResultMap resultMap = new ResultMap();
		resultMap.put(key, "NULL");

		return resultMap;
	}

}
