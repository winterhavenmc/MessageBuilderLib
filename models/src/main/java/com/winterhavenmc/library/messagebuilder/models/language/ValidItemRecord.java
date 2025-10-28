/*
 * Copyright (c) 2024-2025 Tim Savage.
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

package com.winterhavenmc.library.messagebuilder.models.language;

import com.winterhavenmc.library.messagebuilder.models.keys.ItemKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidItemKey;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;


/**
 * A validated, immutable {@link ItemRecord} representing a localized or macro-enabled item
 * definition loaded from the {@code ITEMS} section of a language YAML file.
 * <p>
 * A {@code ValidItemRecord} includes singular and plural display names, optional inventory-specific
 * name overrides, and a list of lore lines. It also implements {@code Pluralizable} to provide
 * dynamic name selection based on quantity.
 *
 * <p>This class is instantiated using the static {@link #create(ValidItemKey, ConfigurationSection)} method,
 * which applies YAML-based parsing and normalization of optional fields.
 *
 * @see ItemRecord
 * @see InvalidItemRecord
 * @see ValidItemKey
 */
public final class ValidItemRecord implements ItemRecord
{
	private final ItemKey key;
	private final String material;
	private final String name;
	private final String pluralName;
	private final String displayName;
	private final List<String> itemLore;


	/**
	 * Constructs a {@code ValidItemRecord} with parsed item metadata.
	 * This constructor is private; use {@link #create(ValidItemKey, ConfigurationSection)} instead.
	 *
	 * @param key               the string that uniquely identifies this item record
	 * @param name        the singular item name
	 * @param pluralName  the plural item name
	 * @param displayName       the inventory-specific singular display name
	 * @param itemLore          the list of lore lines (may be empty but not {@code null})
	 */
	private ValidItemRecord(final ValidItemKey key,
							final String material,
							final String name,
							final String pluralName,
							final String displayName,
							final List<String> itemLore)
	{
		this.key = key;
		this.material = material;
		this.name = name;
		this.pluralName = pluralName;
		this.displayName = displayName;
		this.itemLore = itemLore;
	}


	/**
	 * Creates a {@code ValidItemRecord} from a configuration section.
	 * <p>
	 * This method parses each field from the YAML structure and wraps the
	 * result into a validated record. All fields are assumed to be non-null
	 * if present, and fallback behavior is the caller's responsibility.
	 *
	 * @param key the unique identifier for this item
	 * @param section the configuration section representing the item
	 * @return a new validated item record
	 */
	public static ValidItemRecord create(final ValidItemKey key, final ConfigurationSection section)
	{
		return new ValidItemRecord(key,
				section.getString(Field.MATERIAL.toString()),
				section.getString(Field.NAME.toString()),
				section.getString(Field.PLURAL_NAME.toString()),
				section.getString(Field.DISPLAY_NAME.toString()),
				section.getStringList(Field.LORE.toString()));
	}


	@Override
	public ItemKey key()
	{
		return key;
	}

	public String name()
	{
		return (name != null)
				? name
				: "";
	}

	public String pluralName()
	{
		return (pluralName != null)
				? pluralName
				: "";
	}

	public String displayName()
	{
		return (displayName != null)
				? displayName
				: "";
	}

	public List<String> lore()
	{
		return itemLore;
	}

	public String material()
	{
		return (material != null)
				? material
				: "";
	}

}
