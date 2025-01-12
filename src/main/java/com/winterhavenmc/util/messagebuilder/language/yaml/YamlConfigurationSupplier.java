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

package com.winterhavenmc.util.messagebuilder.language.yaml;

import com.winterhavenmc.util.messagebuilder.language.yaml.section.Section;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Supplier;


/**
 * Class that implements the Java Supplier interface to provide the Bukkit {@link Configuration} object to consumers
 */
public class YamlConfigurationSupplier implements Supplier<Configuration> {
	private final Configuration configuration;


	/**
	 * Class constructor for yaml language configuration supplier supplier
	 *
	 * @param configuration the Configuration object loaded from the yaml language configuration file
	 */
	public YamlConfigurationSupplier(Configuration configuration) {
		this.configuration = configuration;
	}


	@Override
	public Configuration get() {
		return configuration;
	}


	public ConfigurationSection getSection(final Section section) {
		return configuration.getConfigurationSection(section.name());
	}

}
