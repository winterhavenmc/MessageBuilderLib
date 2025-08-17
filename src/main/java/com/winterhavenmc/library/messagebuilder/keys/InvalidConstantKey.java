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


public final class InvalidConstantKey implements ConstantKey
{
	private final String string;
	private final InvalidKeyReason reason;


	public InvalidConstantKey(String string, InvalidKeyReason reason)
	{
		this.string = string;
		this.reason = reason;
	}


	public InvalidKeyReason reason()
	{
		return reason;
	}


	@Override
	public String toString()
	{
		return string;
	}


	@Override
	public boolean equals(Object obj)
	{
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (InvalidConstantKey) obj;
		return Objects.equals(this.string, that.string) &&
				Objects.equals(this.reason, that.reason);
	}


	@Override
	public int hashCode()
	{
		return Objects.hash(string, reason);
	}

}
