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


public class StringProcessor extends MacroProcessorTemplate {

	@Override
	public ResultMap resolveContext(final String key, final ContextMap contextMap) {

		// get value from context map
		Object value = contextMap.get(key);

		ResultMap resultMap = new ResultMap();

		if (value instanceof String resultString) {
			resultMap.put(key, resultString);
		}
		return resultMap;
	}

}
