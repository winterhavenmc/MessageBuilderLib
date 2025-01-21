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
import com.winterhavenmc.util.messagebuilder.util.Error;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Supplier;

import static com.winterhavenmc.util.messagebuilder.MessageBuilder.bundle;


/**
 * Class that implements the Java Supplier interface to provide the Bukkit {@link Configuration} object to consumers
 */
public final class YamlConfigurationSupplier implements Supplier<Configuration> {
	private final Configuration configuration;


	/**
	 * Class constructor for yaml language configuration supplier supplier
	 *
	 * @param configuration the Configuration object loaded from the yaml language configuration file
	 */
	public YamlConfigurationSupplier(Configuration configuration) {
		this.configuration = configuration;
	}


	/**
	 * Retrieve the {@link Configuration} for the language resource delivered by this supplier
	 *
	 * @return {@code Configuration} object containing the language resource
	 */
	@Override
	public Configuration get() {
		return configuration;
	}


	/**
	 * Retrieve the {@link ConfigurationSection} as specified by the enum constant parameter.
	 *
	 * @param section enum constant indicating the language resource section to retrieve
	 * @return {@code ConfigurationSection} of the language resource pertaining to the enum constant parameter
	 * @throws IllegalArgumentException if section parameter is null
	 */
	public ConfigurationSection getSection(final Section section) {
		if (section == null) { throw new IllegalArgumentException(bundle.getString(Error.Parameter.NULL_SECTION.name())); }

		return configuration.getConfigurationSection(section.name());
	}

}
