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

package com.winterhavenmc.library.messagebuilder.model.language;

import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.keys.ValidConstantKey;


/**
 * A sealed interface representing a string–value pair loaded from the {@code CONSTANTS}
 * section of a language YAML file.
 * <p>
 * Constants are globally accessible static values — such as strings, numbers, or
 * booleans — that can be referenced in messages via macros. For example, a
 * constant named {@code SERVER_NAME} might contain the string {@code "Winterhaven"}.
 *
 * <h2>Implementations</h2>
 * <ul>
 *   <li>{@link ValidConstantRecord} – A successfully parsed constant entry</li>
 *   <li>{@link InvalidConstantRecord} – A fallback constant representing a missing or invalid value</li>
 * </ul>
 *
 * <p>Instances are created via {@link #of(ValidConstantKey, Object)} to ensure proper
 * validation. This interface extends {@link SectionRecord}, and is safe to pass
 * through the library once constructed.
 *
 * @see com.winterhavenmc.library.messagebuilder.query.QueryHandler QueryHandler
 * @see ValidConstantKey
 */
public sealed interface ConstantRecord extends SectionRecord permits ValidConstantRecord, InvalidConstantRecord
{
	/**
	 * Factory method that attempts to create a {@code ConstantRecord} from the given value.
	 * <p>
	 * If the value is {@code null}, returns an {@link InvalidConstantRecord}.
	 * Otherwise, attempts to construct a {@link ValidConstantRecord} via validation.
	 *
	 * @param constantKey the unique string identifying the constant
	 * @param constantEntry the raw object value from the configuration
	 * @return a valid or invalid {@code ConstantRecord}, depending on the input
	 */
	static ConstantRecord of(ValidConstantKey constantKey, Object constantEntry)
	{
		return (constantEntry == null)
				? ConstantRecord.empty(constantKey, InvalidRecordReason.CONSTANT_ENTRY_MISSING)
				: ValidConstantRecord.create(constantKey, constantEntry);
	}


	/**
	 * Returns an {@link InvalidConstantRecord} representing a missing or null constant entry.
	 *
	 * @param constantKey the string associated with the unresolved constant
	 * @return a fallback {@code ConstantRecord} with a standard failure reason
	 */
	static InvalidConstantRecord empty(final RecordKey constantKey, final InvalidRecordReason reason)
	{
		return new InvalidConstantRecord(constantKey, reason);
	}

}
