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

import com.winterhavenmc.library.messagebuilder.LanguageHandler;
import com.winterhavenmc.library.messagebuilder.macro.MacroObjectMap;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class ItemStackProcessor extends AbstractProcessor implements Processor {

	public ItemStackProcessor(final LanguageHandler languageHandler) {
		super(languageHandler);
	}

	@Override
	public ResultMap execute(final MacroObjectMap macroObjectMap, final String key, final Object object) {

		ResultMap resultMap = new ResultMap();

		if (object instanceof ItemStack itemStack) {

			if (key.equals("ITEM") || key.equals("ITEM_NAME")) {
				// get item name from message file
				resultMap.putAll(mapConfigItemName(macroObjectMap, "ITEM", "ITEM_NAME"));
			}

			// else get item stack name from metadata or material
			else {
				String resultString = "";
				if (itemStack.hasItemMeta()) {
					ItemMeta itemMeta = itemStack.getItemMeta();
					//noinspection ConstantConditions
					if (itemMeta.hasDisplayName()) {
						resultString = itemMeta.getDisplayName();
					}
				}
				else {
					resultString = itemStack.getType().toString();
				}
				resultMap.put(key, resultString);
			}
		}

		return resultMap;
	}

}
