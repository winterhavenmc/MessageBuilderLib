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


public abstract class AbstractProcessor {

	protected final LanguageHandler languageHandler;
	protected final static String UNKNOWN_VALUE = "???";


	AbstractProcessor(final LanguageHandler languageHandler) {
		this.languageHandler = languageHandler;
	}

	public abstract ResultMap execute(final MacroObjectMap macroObjectMap, final String key, final Object object);


	protected ResultMap mapName(final String key, final String name) {

		ResultMap resultMap = new ResultMap();

		//TODO: This creates a key with the original key string, and additionally a second key suffixed with '_NAME' if that is not already the case.
		// This was done for backward compatibility purposes, and will be removed or modified at some point in the future

		String keyExtra = "_NAME";
		if (key.endsWith("_NAME")) {
			keyExtra = "";
		}

		resultMap.put(key, name);
		resultMap.put(key + keyExtra, name);

		return resultMap;
	}


	ResultMap mapConfigItemName(final MacroObjectMap macroObjectMap, final String... keys) {

		ResultMap resultMap = new ResultMap();

		// put item name in result map for each passed key
		for (String key : keys) {
			resultMap.put(key, getConfigItemName(macroObjectMap));
		}
		return resultMap;
	}


	private String getConfigItemName(final MacroObjectMap macroObjectMap) {

		int itemQuantity = 1;

		// if macro ITEM_QUANTITY exists and is integer, set quantity
		if (macroObjectMap.containsKey("ITEM_QUANTITY") && macroObjectMap.get("ITEM_QUANTITY") instanceof Integer integer) {
			itemQuantity = integer;
		}

		// get singular or plural item name from message file
		//noinspection SwitchStatementWithTooFewBranches
		return switch (itemQuantity) {
			case 1 -> languageHandler.getItemName().orElse(UNKNOWN_VALUE);
			default -> languageHandler.getItemNamePlural().orElse(UNKNOWN_VALUE);
		};
	}

}
