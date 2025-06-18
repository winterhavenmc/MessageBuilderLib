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
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import org.bukkit.entity.AnimalTamer;

import java.util.Optional;
import java.util.function.Predicate;

import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn.OWNER;
import static com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.UNKNOWN_VALUE;


/**
 * Represents an object that has a concept of ownership, expressed through an {@link AnimalTamer}.
 *
 * <p>This interface is used to extract an owner identifier—typically a player name—for insertion into a
 * {@link MacroStringMap} used in macro replacement. Implementing this interface enables support for
 * the {@code {OBJECT.OWNER}} macro in localized message templates.
 *
 * <p>Ownership in this context is not limited to entity taming. It may also refer to logical or permission-based
 * ownership, such as a player who owns or controls a placed structure or data-bound object—e.g., the owner of
 * a {@code DeathChest}, or a protected area. The use of {@link AnimalTamer} allows support for a broad range of
 * owner-capable entities, including {@link org.bukkit.OfflinePlayer}, which is important for messages about players
 * who may be offline.
 */
@FunctionalInterface
public interface Ownable
{
	/**
	 * Returns the {@link AnimalTamer} representing the owner of this object.
	 * This may include tamed entities, or other objects with an ownership link.
	 *
	 * @return the owner of this object, or {@code null} if no owner is defined
	 */
	AnimalTamer getOwner();


	/**
	 * Extracts the macro value for the owner's name and adds it to a {@link MacroStringMap},
	 * using the {@code {OBJECT.OWNER}} key.
	 *
	 * @param baseKey the macro key prefix (e.g., {@code OBJECT}) used to construct the full macro key
	 * @param ctx the adapter context container, which holds formatting dependencies
	 * @return a {@code MacroStringMap} containing the extracted owner name,
	 * or an empty map if the owner is null or invalid
	 */
	default MacroStringMap extractOwner(final MacroKey baseKey, final AdapterContextContainer ctx)
	{
		return baseKey.append(OWNER)
				.map(macroKey -> new MacroStringMap()
				.with(macroKey, formatOwner(this.getOwner()).orElse(UNKNOWN_VALUE)))
				.orElseGet(MacroStringMap::empty);
	}


	/**
	 * Predicate used to validate an owner. An owner is considered valid if it is non-null and has a non-blank name.
	 */
	Predicate<AnimalTamer> VALID_OWNER = animalTamer -> animalTamer != null
			&& animalTamer.getName() != null
			&& !animalTamer.getName().isBlank();


	/**
	 * Returns a formatted string representing the owner's name, if available and valid.
	 *
	 * @param owner the {@link AnimalTamer} to format
	 * @return an {@code Optional<String>} containing the owner's name, or empty if not valid
	 */
	static Optional<String> formatOwner(final AnimalTamer owner)
	{
		return (VALID_OWNER.test(owner))
				? Optional.ofNullable(owner.getName())
				: Optional.empty();
	}

}
