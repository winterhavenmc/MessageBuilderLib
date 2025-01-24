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
import org.bukkit.entity.Entity;


class EntityProcessor extends MacroProcessorTemplate implements MacroProcessor {

	public EntityProcessor(final LanguageQueryHandler queryHandler) {
		super(queryHandler);
	}

	public <T> ResultMap resolveContext(final String key, final ContextMap contextMap, final T value) {

		ResultMap resultMap = new ResultMap();

		if (value instanceof Entity entity) {

			// put entity macro replacements in result map
			resultMap.put(key, entity.getName());

			//TODO: put additional field processing here, including entity location
		}

		return resultMap;
	}

}
