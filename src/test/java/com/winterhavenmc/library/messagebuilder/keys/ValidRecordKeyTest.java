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

import org.junit.jupiter.api.Nested;


class ValidRecordKeyTest
{
	enum TestEnum
	{
		INVALID$KEY,
		VALID_KEY
	}


	@Nested
	class TestStaticFactoryMethod
	{


//	@Test
//	void recordKeyEquality_ShouldBeTrueForSameKey()
//	{
//		Optional<LegacyRecordKey> key1 = LegacyRecordKey.of("SAME_KEY");
//		Optional<LegacyRecordKey> key2 = LegacyRecordKey.of("SAME_KEY");
//
//		assertTrue(key1.isPresent() && key2.isPresent());
//		assertEquals(key1.get(), key2.get());
//	}


//	@Test
//	void recordKeyEquality_ShouldBeFalseForDifferentKeys()
//	{
//		Optional<RecordKey> key1 = LegacyRecordKey.of("KEY_ONE");
//		Optional<LegacyRecordKey> key2 = LegacyRecordKey.of("KEY_TWO");
//
//		assertTrue(key1.isPresent() && key2.isPresent());
//		assertNotEquals(key1.get(), key2.get());
//	}


//	@Test
//	void hashCode_ShouldBeConsistentWithEquals()
//	{
//		Optional<LegacyRecordKey> key1 = LegacyRecordKey.of("HASH_TEST");
//		Optional<LegacyRecordKey> key2 = LegacyRecordKey.of("HASH_TEST");
//
//		assertTrue(key1.isPresent() && key2.isPresent());
//		assertEquals(key1.get().hashCode(), key2.get().hashCode());
//	}


//	@Test
//	void toString_ShouldReturnCorrectKey()
//	{
//		Optional<LegacyRecordKey> key = LegacyRecordKey.of("TO_STRING_TEST");
//		assertTrue(key.isPresent());
//		assertEquals("TO_STRING_TEST", key.get().toString());
//	}


	}
}
