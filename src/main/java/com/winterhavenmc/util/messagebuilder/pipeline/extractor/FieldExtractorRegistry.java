package com.winterhavenmc.util.messagebuilder.pipeline.extractor;

import com.winterhavenmc.util.messagebuilder.adapters.Adapter;
import com.winterhavenmc.util.messagebuilder.keys.MacroKey;

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
