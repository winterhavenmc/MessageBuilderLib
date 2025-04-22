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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.constant;

import com.winterhavenmc.util.messagebuilder.keys.RecordKey;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.SectionRecord;


public sealed interface ConstantRecord extends SectionRecord permits ValidConstantRecord, InvalidConstantRecord
{
	static ConstantRecord from(RecordKey constantKey, Object constantEntry)
	{
		return (constantEntry == null)
				? ConstantRecord.empty(constantKey)
				: ValidConstantRecord.create(constantKey, constantEntry);
	}


	static InvalidConstantRecord empty(final RecordKey constantKey)
	{
		return new InvalidConstantRecord(constantKey, "Missing constant section.");
	}

}
