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

import com.winterhavenmc.library.messagebuilder.validation.ValidationHandler;
import org.bukkit.entity.AnimalTamer;

import java.util.*;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.STRING_BLANK;
import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.NAME;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


public class Owner
{
	private final String name;
	private final UUID uniqueId;
	private final Set<UUID> ownedObjects = new HashSet<>();


	private Owner(final String name, final UUID uniqueId)
	{
		validate(name, Objects::isNull, ValidationHandler.throwing(PARAMETER_NULL, NAME));
		validate(name, String::isBlank, ValidationHandler.throwing(STRING_BLANK, NAME));

		this.name = name;
		this.uniqueId = (uniqueId != null) ? uniqueId : new UUID(0, 0);
	}


	public static Optional<Owner> of(AnimalTamer animalTamer)
	{
		return (animalTamer != null && animalTamer.getName() != null && !animalTamer.getName().isBlank())
				? Optional.of(new Owner(animalTamer.getName(), animalTamer.getUniqueId()))
				: Optional.empty();
	}


	public String getName()
	{
		return this.name;
	}


	public UUID getUniqueId()
	{
		return this.uniqueId;
	}


	public boolean hasOwnership(final UUID uniqueId)
	{
		return this.ownedObjects.contains(uniqueId);
	}


	synchronized public boolean addOwnership(final UUID uniqueId)
	{
		return this.ownedObjects.add(uniqueId);
	}


	synchronized public boolean removeOwnership(final UUID uniqueId)
	{
		return this.ownedObjects.remove(uniqueId);
	}


	@Override
	public String toString()
	{
		return this.name;
	}

}
