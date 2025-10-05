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

package com.winterhavenmc.library.messagebuilder.models.keys;

import java.util.Optional;


/**
 * A type that represents a validated string for a record. This type guarantees a valid string that has been
 * validated upon creation. The static factory methods return an Optional of the LegacyRecordKey,
 * or an empty Optional if the parameter was invalid, as determined by regex pattern and Predicate.
 */
public sealed interface ItemKey extends RecordKey permits ValidItemKey, InvalidItemKey
{
	static ItemKey of(String key)
	{
		if (key == null) return new InvalidItemKey("∅", InvalidKeyReason.KEY_NULL);
		else if (key.isBlank()) return new InvalidItemKey("⬚", InvalidKeyReason.KEY_BLANK);
		else if (IS_INVALID_KEY.test(key)) return new InvalidItemKey(key, InvalidKeyReason.KEY_INVALID);
		else return new ValidItemKey(key);
	}


	default Optional<ValidItemKey> isValid()
	{
		return (this instanceof ValidItemKey valid)
				? Optional.of(valid)
				: Optional.empty();
	}

}
