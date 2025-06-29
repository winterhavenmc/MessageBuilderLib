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

package com.winterhavenmc.library.messagebuilder.model.language;

import com.winterhavenmc.library.messagebuilder.keys.RecordKey;

import java.util.Objects;


/**
 * A validated, immutable {@link ConstantRecord} representing a constant value
 * loaded from the {@code CONSTANTS} section of a language YAML file.
 * <p>
 * A constant may represent a string, number, boolean, or other supported type.
 * These values are typically referenced using macros in messages (e.g. {@code %SERVER_NAME%}).
 * <p>
 * This record is considered safe and complete once constructed, and is created
 * via the {@link #create(RecordKey, Object)} static factory method.
 *
 * @see ConstantRecord
 * @see com.winterhavenmc.library.messagebuilder.keys.RecordKey RecordKey
 */
public final class ValidConstantRecord implements ConstantRecord
{
	private final RecordKey key;
	private final Object value;


	/**
	 * Constructs a {@code ValidConstantRecord} with the given key and value.
	 * <p>
	 * This constructor is private; use {@link #create(RecordKey, Object)} to instantiate.
	 *
	 * @param key the unique constant key
	 * @param value the constant value as loaded from the configuration
	 */
	private ValidConstantRecord(final RecordKey key, final Object value)
	{
		this.key = key;
		this.value = value;
	}


	/**
	 * Creates a {@code ValidConstantRecord} from the provided key and value.
	 * <p>
	 * This method should be called only after validation, typically from
	 * {@link ConstantRecord#from(RecordKey, Object)}.
	 *
	 * @param key the unique constant key
	 * @param value the raw value from the YAML configuration
	 * @return a validated constant record instance
	 */
	public static ValidConstantRecord create(final RecordKey key, final Object value)
	{
		return new ValidConstantRecord(key, value);
	}


	@Override
	public RecordKey key()
	{
		return this.key;
	}


	/**
	 * Returns the raw constant value associated with this record.
	 * <p>
	 * The type may vary depending on what was stored in the language configuration.
	 * It is typically expected to be a {@link String}, {@link Number}, or {@link Boolean}.
	 *
	 * @return the constant value
	 */
	public Object value()
	{
		return this.value;
	}


	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof ValidConstantRecord that)) return false;

		return key.equals(that.key) && Objects.equals(value, that.value);
	}


	@Override
	public int hashCode()
	{
		int result = key.hashCode();
		result = 31 * result + Objects.hashCode(value);
		return result;
	}

}
