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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.constants;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.SectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.Error;

import java.util.List;
import java.util.Optional;


/**
 * Query handler for the 'CONSTANTS' section of the language file.
 */
public class ConstantSectionQueryHandler implements SectionQueryHandler<String> {

	private final static Section section = Section.CONSTANTS;
	private final YamlConfigurationSupplier configurationSupplier;


	/**
	 * Class constructor
	 *
	 * @param yamlConfigurationSupplier the provider of the current language configuration object
	 * @throws IllegalArgumentException if the {@code yamlConfigurationSupplier} parameter is null or invalid
	 */
	public ConstantSectionQueryHandler(final YamlConfigurationSupplier yamlConfigurationSupplier) {
		if (yamlConfigurationSupplier == null) { throw new IllegalArgumentException(Error.Parameter.NULL_SECTION_CONSTANTS.getMessage()); }

		// ensure the 'CONSTANTS' section exists in the configuration provided by the supplier
		if (yamlConfigurationSupplier.getSection(Section.CONSTANTS) == null) {
			throw new IllegalArgumentException(Error.Parameter.INVALID_SECTION_CONSTANTS.getMessage());
		}

		// get configuration supplier
		this.configurationSupplier = yamlConfigurationSupplier;
	}


	/**
	 * Return the Section constant for this query handler type
	 *
	 * @return the CONSTANTS Section constant, establishing this query handler type
	 */
	@Override
	public Section getSectionType() {
		return section;
	}


	/**
	 * The primary type returned by this query handler. A query handler may provide methods that return
	 * values of other types.
	 *
	 * @return String.class as the primary type returned by this query handler
	 */
	@Override
	public Class<String> getHandledType() {
		return String.class;
	}


	/**
	 * A list of the types returned by this query handler. A query handler should not provide methods that return
	 * values of other types.
	 *
	 * @return {@code List} of class types that are handled by this query handler
	 */
	@Override
	public List<Class<?>> listHandledTypes() {
		return List.of(String.class, List.class, Integer.class);
	}


	/**
	 * Query the constants section of the language file for a String with keyPath
	 *
	 * @param keyPath the keyPath of the String to be retrieved
	 * @return an {@code Optional} String containing the String retrieved with keyPath, or an empty Optional if no
	 * value was found for the keyPath
	 */
	public Optional<String> getString(final String keyPath) {
		if (keyPath == null) { throw new IllegalArgumentException(Error.Parameter.NULL_KEY_PATH.getMessage()); }
		return Optional.ofNullable(configurationSupplier.getSection(section).getString(keyPath));
	}


	/**
	 * Query the constants section of the language file for a List of String with the keyPath
	 *
	 * @param keyPath the keyPath of the List to be retrieved
	 * @return a {@code List} of String containing the values retrieved using keyPath, or an empty List if no
	 * value was found for the keyPath
	 */
	public List<String> getStringList(final String keyPath) {
		if (keyPath == null) { throw new IllegalArgumentException(Error.Parameter.NULL_KEY_PATH.getMessage()); }
		return configurationSupplier.getSection(section).getStringList(keyPath);
	}


	/**
	 * Query the constants section of the language file for an {@code int} with the keyPath
	 *
	 * @param keyPath the keyPath of the {@code int} to be retrieved
	 * @return {@code int} containing the values retrieved using keyPath, or zero (0) if no
	 * value was found for the keyPath
	 */
	public int getInt(final String keyPath) {
		if (keyPath == null) { throw new IllegalArgumentException(Error.Parameter.NULL_KEY_PATH.getMessage()); }
		return configurationSupplier.getSection(section).getInt(keyPath);
	}

}
