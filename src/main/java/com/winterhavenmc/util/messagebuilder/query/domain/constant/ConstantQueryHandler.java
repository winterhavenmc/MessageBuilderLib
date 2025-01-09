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

package com.winterhavenmc.util.messagebuilder.query.domain.constant;

import com.winterhavenmc.util.messagebuilder.namespace.Namespace;
import com.winterhavenmc.util.messagebuilder.query.domain.DomainQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.Error;
import com.winterhavenmc.util.messagebuilder.util.ReadOnlyConfigurationSection;
import com.winterhavenmc.util.messagebuilder.util.ReadOnlyConfigurationSectionAdapter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Optional;

public class ConstantQueryHandler implements DomainQueryHandler<String> {

	private final static Namespace.Domain domain = Namespace.Domain.CONSTANTS;

	private final ReadOnlyConfigurationSection section;


	/**
	 * Class constructor
	 *
	 * @param section the 'CONSTANTS' section of the language file
	 */
	public ConstantQueryHandler(final ConfigurationSection section) {
		if (section == null) { throw new IllegalArgumentException(Error.Parameter.NULL_SECTION_CONSTANTS.getMessage()); }

		// ensure only the 'CONSTANTS' section is passed in
		// TODO: Don't think this is working
		if (!section.getName().equals(domain.name())) {
			System.out.println("Section name: " + section.getName() + " does not equal domain.name()");
			throw new IllegalArgumentException(Error.Parameter.INVALID_SECTION_ITEMS.getMessage());
		}

		this.section = ReadOnlyConfigurationSectionAdapter.of(section);
	}

	@Override
	public Namespace.Domain getDomain() {
		return domain;
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
		return Optional.ofNullable(section.getString(keyPath));
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
		return section.getStringList(keyPath);
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
		return section.getInt(keyPath);
	}

}
