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

package com.winterhavenmc.library.messagebuilder.keys;

import java.util.Optional;


/**
 * A type that represents a validated string for a record. This type guarantees a valid string that has been
 * validated upon creation. The static factory methods return an Optional of the LegacyRecordKey,
 * or an empty Optional if the parameter was invalid, as determined by regex pattern and Predicate.
 */
public sealed interface ConstantKey extends RecordKey permits ValidConstantKey, InvalidConstantKey
{
	static ConstantKey of(String key)
	{
		if (key == null) return new InvalidConstantKey("∅", InvalidKeyReason.KEY_NULL);
		else if (key.isBlank()) return new InvalidConstantKey("⬚", InvalidKeyReason.KEY_BLANK);
		else if (IS_INVALID_KEY.test(key)) return new InvalidConstantKey(key, InvalidKeyReason.KEY_INVALID);
		else return new ValidConstantKey(key);
	}


	default Optional<ValidConstantKey> isValid()
	{
		return (this instanceof ValidConstantKey valid)
				? Optional.of(valid)
				: Optional.empty();
	}

}
