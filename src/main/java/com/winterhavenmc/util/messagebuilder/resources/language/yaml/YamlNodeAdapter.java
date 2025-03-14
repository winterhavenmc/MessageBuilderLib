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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;


/**
 * A wrapper class around a {@link Supplier} that provides a {@link Configuration} instance.
 * This allows lazy conversion of Bukkit YAML configurations into {@link YamlNode} structures.
 */
public class YamlNodeAdapter
{
	private final YamlConfigurationSupplier configurationSupplier;


	/**
	 * Class constructor. Takes YamlConfigurationSupplier as parameter
	 *
	 * @param configurationSupplier the Supplier containing a bukkit Configuration object
	 */
	public YamlNodeAdapter(final YamlConfigurationSupplier configurationSupplier)
	{
		this.configurationSupplier = configurationSupplier;
	}


	/**
	 * Get root node of YamlConfiguration
	 *
	 * @return Optional of YamlNode
	 */
	Optional<YamlNode> getRoot()
	{
		return fromBukkitObject(configurationSupplier.get());
	}


	/**
	 * Get root node of YamlConfiguration for Section type
	 *
	 * @param section enum constant representing the configuration section to use as root node
	 * @return Optional YamlNode root for section
	 */
	Optional<YamlNode> getRoot(final Section section)
	{
		return fromBukkitObject(configurationSupplier.get().get(section.name()));
	}


	/**
	 * Get root node of YamlConfiguration or throw IllegalStateException
	 *
	 * @return YamlNode the root node of the Configuration
	 */
	YamlNode getRootOrThrow()
	{
		return getRoot().orElseThrow(() ->
				new IllegalStateException("Root YAML node could not be created."));
	}


	/**
	 * Get root node of YamlConfiguration or throw IllegalStateException
	 *
	 * @return YamlNode the root node of the Configuration
	 */
	YamlNode getRootOrThrow(final Section section)
	{
		return fromBukkitObject(configurationSupplier.get().get(section.name()))
				.orElseThrow(() -> new IllegalStateException("Root YAML node could not be created."));
	}


	/**
	 * Static method to convert bukkit yaml object to YamlNode,
	 * using recursion when object type is {@link ConfigurationSection}
	 *
	 * @param obj the bukkit yaml object to adapt
	 * @return YamlNode object
	 */
	static Optional<YamlNode> fromBukkitObject(final Object obj)
	{
		return switch (obj)
		{
			case null -> Optional.of(new YamlNull());
			case String s -> Optional.of(new YamlString(s));
			case Number n -> Optional.of(new YamlNumber(n));
			case Boolean b -> Optional.of(new YamlBoolean(b));
			case List<?> list -> Optional.of(new YamlSequence(list.stream()
					.map(YamlNodeAdapter::fromBukkitObject)
					.flatMap(Optional::stream) // Flatten optionals
					.toList()));
			case ConfigurationSection section -> Optional.of(new YamlMapping(section.getKeys(false).stream()
					.collect(Collectors.toMap(key -> key, key ->
							fromBukkitObject(section.get(key)).orElse(new YamlNull()))))
			);
			default -> Optional.empty();
		};
	}

}
