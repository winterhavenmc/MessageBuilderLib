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

package com.winterhavenmc.library.messagebuilder.model.language.constant;

import com.winterhavenmc.library.messagebuilder.keys.RecordKey;

import java.util.Objects;


public final class ValidConstantRecord implements ConstantRecord
{
	private final RecordKey key;
	private final Object value;


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


	private ValidConstantRecord(final RecordKey key, final Object value)
	{
		this.key = key;
		this.value = value;
	}


	public static ValidConstantRecord create(final RecordKey key, final Object value)
	{
		return new ValidConstantRecord(key, value);
	}


	@Override
	public RecordKey key()
	{
		return this.key;
	}


	public Object value()
	{
		return this.value;
	}

}
