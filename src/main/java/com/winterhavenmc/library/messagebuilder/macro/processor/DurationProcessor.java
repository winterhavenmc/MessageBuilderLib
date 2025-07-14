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

package com.winterhavenmc.library.messagebuilder.macro.processor;

import com.winterhavenmc.library.TimeUnit;
import com.winterhavenmc.library.messagebuilder.LanguageHandler;
import com.winterhavenmc.library.messagebuilder.macro.MacroObjectMap;

import java.time.Duration;


public class DurationProcessor extends AbstractProcessor implements Processor
{

	public DurationProcessor(final LanguageHandler languageHandler)
	{
		super(languageHandler);
	}

	@Override
	public ResultMap execute(final MacroObjectMap macroObjectMap, final String key, final Object object)
	{
		ResultMap resultMap = new ResultMap();

		if (object == null)
		{
			return resultMap;
		}

		if (object instanceof Duration duration)
		{
			if (key.endsWith("DURATION_MINUTES"))
			{
				resultMap.put(key, languageHandler.getTimeString(duration.toMillis(), TimeUnit.MINUTES));
			}
			else
			{
				resultMap.put(key, languageHandler.getTimeString(duration.toMillis(), TimeUnit.SECONDS));
			}
		}
		else
		{
			resultMap.put(key, String.valueOf(object));
		}

		return resultMap;
	}

}
