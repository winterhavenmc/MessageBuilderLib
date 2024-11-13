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

import com.winterhavenmc.util.TimeUnit;
import com.winterhavenmc.util.messagebuilder.LanguageHandler;
import com.winterhavenmc.util.messagebuilder.macro.MacroObjectMap;
import org.bukkit.plugin.java.JavaPlugin;


public class NumberProcessor extends AbstractProcessor implements Processor {

	public NumberProcessor(JavaPlugin plugin, LanguageHandler languageHandler) {
		super(plugin, languageHandler);
	}

	@Override
	public ResultMap doReplacements(final MacroObjectMap macroObjectMap, final String key, final Object object) {

		ResultMap resultMap = new ResultMap();

		if (object == null) {
			return resultMap;
		}

		if (object instanceof Long longVar) {

			if (key.endsWith("DURATION") || key.endsWith("DURATION_SECONDS")) {
				resultMap.put(key, languageHandler.getTimeString(longVar, TimeUnit.SECONDS));
			}
			else if (key.endsWith("DURATION_TICKS")) {
				resultMap.put(key, languageHandler.getTimeString(longVar, TimeUnit.TICKS));
			}
			else if (key.endsWith("DURATION_MINUTES")) {
				resultMap.put(key, languageHandler.getTimeString(longVar, TimeUnit.MINUTES));
			}
			else if (key.endsWith("DURATION_HOURS")) {
				resultMap.put(key, languageHandler.getTimeString(longVar, TimeUnit.HOURS));
			}
			else if (key.endsWith("DURATION_DAYS")) {
				resultMap.put(key, languageHandler.getTimeString(longVar, TimeUnit.DAYS));
			}
			else {
				resultMap.put(key, String.valueOf(longVar));
			}
		}
		else {
			resultMap.put(key, String.valueOf(object));
		}

		return resultMap;
	}

}
