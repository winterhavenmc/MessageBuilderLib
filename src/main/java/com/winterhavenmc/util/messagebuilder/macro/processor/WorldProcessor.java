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

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;


public class WorldProcessor extends AbstractProcessor implements Processor {

	public WorldProcessor(JavaPlugin plugin, LanguageHandler languageHandler) {
		super(plugin, languageHandler);
	}

	@Override
	public ResultMap doReplacements(MacroObjectMap macroObjectMap, String key, Object object) {

		ResultMap resultMap = new ResultMap();

		if (object instanceof World world) {
			resultMap.put(key, languageHandler.getWorldName(world).orElse(UNKNOWN_VALUE));
		}

		return resultMap;
	}

}
