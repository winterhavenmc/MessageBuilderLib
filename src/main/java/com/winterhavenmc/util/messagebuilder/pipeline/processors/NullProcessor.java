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

import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.result.ResultMap;
import com.winterhavenmc.util.messagebuilder.recordkey.RecordKey;


/**
 * A macro processor that resolves a string value of "NULL" for values stored in the context map that are
 * referenced by the given key and contain a null value.
 */
public class NullProcessor extends MacroProcessorTemplate
{
	@Override
	public ResultMap resolveContext(final RecordKey key, final ContextMap contextMap)
	{
		return new ResultMap() {{ put(key, "NULL"); }};
	}

}
