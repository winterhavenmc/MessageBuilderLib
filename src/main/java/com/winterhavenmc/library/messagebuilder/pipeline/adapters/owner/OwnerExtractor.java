/*
 * Copyright (c) 2025 Tim Savage.
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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.owner;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;

import java.util.Optional;
import java.util.function.Predicate;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.OWNER;


@Deprecated
public class OwnerExtractor
{
	private final static String UNKNOWN_VALUE = "-";

	Predicate<Ownable> VALID_NAME = ownable -> ownable != null
			&& ownable.getOwner() != null
			&& ownable.getOwner().getName() != null
			&& !ownable.getOwner().getName().isBlank();


	MacroStringMap extract(final MacroKey baseKey, final Ownable ownable)
	{
		MacroStringMap resultMap = new MacroStringMap();
		baseKey.append(OWNER).ifPresent(macroKey -> resultMap.put(macroKey, getOwnerNameString(ownable).orElse(UNKNOWN_VALUE)));
		return resultMap;
	}


	Optional<String> getOwnerNameString(final Ownable ownable)
	{
		return ownable.format();
	}

}
