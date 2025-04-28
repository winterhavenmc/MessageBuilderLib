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

package com.winterhavenmc.library.messagebuilder.pipeline.extractor;

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter;
import com.winterhavenmc.library.messagebuilder.keys.MacroKey;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;


/**
 * Delegates subfield extraction from adapted objects using a shared field extractor.
 */
public class FieldExtractorRegistry
{

	private final FieldExtractor extractor = new FieldExtractor();

	/**
	 * Delegates field extraction for the given adapter and value.
	 *
	 * @param adapter the adapter used to access fields
	 * @param value the adapted object
	 * @param baseKey the base macro key (e.g. PLAYER, ITEM)
	 * @return extracted field values
	 */
	public <T> Map<MacroKey, Object> extractFields(Adapter adapter, T value, MacroKey baseKey)
	{
		Objects.requireNonNull(value, "Value to extract must not be null");
		Objects.requireNonNull(baseKey, "Base key must not be null");

		if (adapter == null) return Collections.emptyMap();

		return extractor.extract(adapter, value, baseKey);
	}

}
