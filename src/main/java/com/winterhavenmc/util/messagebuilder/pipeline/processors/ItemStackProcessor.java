/*
 * Copyright (c) 2024-2025 Tim Savage.
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

import com.winterhavenmc.util.messagebuilder.adapters.quantity.QuantityAdapter;
import com.winterhavenmc.util.messagebuilder.pipeline.ContextMap;

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.CONTEXT_MAP;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.KEY;
import static com.winterhavenmc.util.messagebuilder.validation.Validate.validate;


/**
 * A macro processor that resolves fields for an {@link ItemStack} stored in the context map
 * and referenced by the given key.
 */
public class ItemStackProcessor extends MacroProcessorTemplate
{
	@Override
	public ResultMap resolveContext(final String key, final ContextMap contextMap)
	{
		validate(key, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, KEY));
		validate(key, String::isBlank, () -> new ValidationException(PARAMETER_EMPTY, KEY));
		validate(contextMap, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, CONTEXT_MAP));

		ResultMap resultMap = new ResultMap();

		contextMap.getOpt(key)
				.filter(ItemStack.class::isInstance)
				.map(ItemStack.class::cast)
				.ifPresent(itemStack -> {
					// put item stack type as replacement string for key, in case other suitable field is not present
					resultMap.put(key, itemStack.getType().toString());

					// if an itemstack quantity field does not exist in the context map, use itemStack amount for quantity
					if (!contextMap.contains(key + ".QUANTITY")) {
						QuantityAdapter.asQuantifiable(itemStack).ifPresent(quantifiable ->
								resultMap.put(key + ".QUANTITY", String.valueOf(quantifiable.getQuantity()))
						);

						// put item stack translation key in result map for possible use
						//resultMap.put(key + ".TRANSLATION_KEY", itemStack.getTranslationKey());

						//TODO: add adapter or otherwise extract metadata for fields (displayName, lore, quantity, etc)
					}
				});

		return resultMap;
	}

}
