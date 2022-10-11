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

import com.winterhavenmc.util.messagebuilder.LanguageHandler;
import com.winterhavenmc.util.messagebuilder.macro.MacroObjectMap;
import org.bukkit.plugin.java.JavaPlugin;


public class StringProcessor extends AbstractProcessor implements Processor {

	public StringProcessor(JavaPlugin plugin, LanguageHandler languageHandler) {
		super(plugin, languageHandler);
	}

	@Override
	public ResultMap doReplacements(MacroObjectMap macroObjectMap, String key, Object object) {

		ResultMap resultMap = new ResultMap();

		if (object instanceof String string) {
			if (key.equals("ITEM") || key.equals("ITEM_NAME")) {
				// get item name from messages file
				resultMap.putAll(mapConfigItemName(macroObjectMap, "ITEM", "ITEM_NAME"));
			}
			else {
				resultMap.put(key, string);
			}
		}
		return resultMap;
	}

}
