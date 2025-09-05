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

package com.winterhavenmc.library.messagebuilder.model.destination;

import java.util.Objects;
import java.util.UUID;

public final class StoredDestination implements ValidDestination
{
	private final String key;
	private final String displayName;
	private final UUID worldUid;
	private final double x;
	private final double y;
	private final double z;

	public StoredDestination(String key, String displayName, UUID worldUid, double x, double y, double z)
	{
		this.key = key;
		this.displayName = displayName;
		this.worldUid = worldUid;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String key()
	{
		return key;
	}

	public String displayName()
	{
		return displayName;
	}

	public UUID worldUid()
	{
		return worldUid;
	}

	public double x()
	{
		return x;
	}

	public double y()
	{
		return y;
	}

	public double z()
	{
		return z;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (StoredDestination) obj;
		return Objects.equals(this.key, that.key) &&
				Objects.equals(this.displayName, that.displayName) &&
				Objects.equals(this.worldUid, that.worldUid) &&
				Double.doubleToLongBits(this.x) == Double.doubleToLongBits(that.x) &&
				Double.doubleToLongBits(this.y) == Double.doubleToLongBits(that.y) &&
				Double.doubleToLongBits(this.z) == Double.doubleToLongBits(that.z);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(key, displayName, worldUid, x, y, z);
	}

	@Override
	public String toString()
	{
		return "StoredDestination[" +
				"key=" + key + ", " +
				"displayName=" + displayName + ", " +
				"worldUid=" + worldUid + ", " +
				"x=" + x + ", " +
				"y=" + y + ", " +
				"z=" + z + ']';
	}
}
