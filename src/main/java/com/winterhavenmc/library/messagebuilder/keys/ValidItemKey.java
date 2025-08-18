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

package com.winterhavenmc.library.messagebuilder.keys;

import java.util.Objects;


public final class ValidItemKey implements ItemKey
{
	private final String string;


	ValidItemKey(final String string)
	{
		this.string = string;
	}


	@Override
	public String toString()
	{
		return this.string;
	}


	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (ValidItemKey) obj;
		return Objects.equals(this.string, that.string);
	}


	@Override
	public int hashCode()
	{
		return Objects.hash(string);
	}

}
