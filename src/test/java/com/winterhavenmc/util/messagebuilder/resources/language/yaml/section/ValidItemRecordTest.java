/*
 * Copyright (c) 2024-2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section;

import com.winterhavenmc.util.messagebuilder.resources.RecordKey;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class ValidItemRecordTest
{
	private final static String TEST_ITEM = "TEST_ITEM_1";


	@Test
	public void constructorTest()
	{
		RecordKey recordKey = RecordKey.of(TEST_ITEM).orElseThrow();

		ValidItemRecord testRecord = new ValidItemRecord(
				recordKey,
				Optional.of("Test Item"),
				Optional.of("Test Items"),
				Optional.of("Inventory Test Item"),
				Optional.of("Inventory Test Items"),
				List.of("Lore line 1", "Lore line 2"));

		assertNotNull(testRecord, "the newly created record is null.");
	}


	@ParameterizedTest
	@EnumSource
	void testFields(ItemSectionQueryHandler.Field field)
	{
		String keyPath = field.getKeyPath();
		assertTrue(keyPath.matches("[A-Za-z][A-Za-z\\d_.]*"));
	}


	@Test
	public void testPluralized()
	{
		RecordKey recordKey = RecordKey.of(TEST_ITEM).orElseThrow();

		ValidItemRecord testRecord = new ValidItemRecord(
				recordKey,
				Optional.of("Test Item"),
				Optional.of("Test Items"),
				Optional.of("Inventory Test Item"),
				Optional.of("Inventory Test Items"),
				List.of("Lore line 1", "Lore line 2"));

		assertEquals(Optional.of("Test Items"), testRecord.nameFor(0));
		assertEquals(Optional.of("Test Item"), testRecord.nameFor(1));
		assertEquals(Optional.of("Test Items"), testRecord.nameFor(2));
	}

}
