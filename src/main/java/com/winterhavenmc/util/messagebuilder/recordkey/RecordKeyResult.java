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

package com.winterhavenmc.util.messagebuilder.recordkey;

import java.util.Optional;


public sealed interface RecordKeyResult permits ValidRecordKey, InvalidRecordKey
{
	/**
	 * Static factory method for instantiating a record key from a string
	 *
	 * @param key a String to be used in the creation of a record key
	 * @return an Optional ValidRecordKey, or an empty Optional if the key parameter is null or invalid
	 */
	static RecordKeyResult of(final String key)
	{
		return (key == null || key.isBlank())
			? new InvalidRecordKey()
			: RecordKeyResult.of(key);
	}


	/**
	 * Static factory method for instantiating a record key from an enum constant
	 *
	 * @param key an enum constant whose name is used to create a record key
	 * @return an Optional ValidRecordKey, or an empty Optional if the key parameter is null or invalid
	 * @param <E> an enum constant
	 */
	static <E extends Enum<E>> Optional<ValidRecordKey> of(final E key)
	{
		return Optional.ofNullable(key)
				.map(Enum::name)
				.map(ValidRecordKey::new);
	}

}
