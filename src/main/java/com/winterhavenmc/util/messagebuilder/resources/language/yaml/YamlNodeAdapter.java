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
	private final Supplier<Configuration> supplier;


	public YamlNodeAdapter(Supplier<Configuration> supplier)
	{
		this.supplier = supplier;
	}


	Optional<YamlNode> getRoot()
	{
		return fromBukkitObject(supplier.get());
	}


	Optional<YamlNode> getRoot(final Section section)
	{
		return fromBukkitObject(supplier.get().get(section.name()));
	}


	YamlNode getRootOrThrow() {
		return getRoot().orElseThrow(() ->
				new IllegalStateException("Root YAML node could not be created."));
	}


	/**
	 * Static method to convert bukkit yaml object to YamlNode,
	 * using recursion when object type is {@link ConfigurationSection}
	 *
	 * @param obj the bukkit yaml object to adapt
	 * @return YamlNode object
	 */
	static Optional<YamlNode> fromBukkitObject(Object obj)
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
