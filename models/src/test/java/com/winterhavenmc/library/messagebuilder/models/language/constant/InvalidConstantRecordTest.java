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

package com.winterhavenmc.library.messagebuilder.models.language.constant;

import com.winterhavenmc.library.messagebuilder.models.keys.ConstantKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidConstantKey;
import com.winterhavenmc.library.messagebuilder.models.language.InvalidRecordReason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class InvalidConstantRecordTest
{
	ValidConstantKey constantKey;
	InvalidConstantRecord invalidConstantRecord;


	@BeforeEach
	void setUp()
	{
		constantKey = ConstantKey.of("NON_EXISTENT").isValid().orElseThrow();
		invalidConstantRecord = ConstantRecord.empty(constantKey, InvalidRecordReason.CONSTANT_ENTRY_MISSING);
	}

	@Test
	void testReturnType()
	{
		assertInstanceOf(InvalidConstantRecord.class, invalidConstantRecord);
	}


	@Test
	void getKey()
	{
		assertEquals(constantKey, invalidConstantRecord.key());
	}

	@Test
	void getReason()
	{
		assertEquals(InvalidRecordReason.CONSTANT_ENTRY_MISSING, invalidConstantRecord.reason());
	}

}
