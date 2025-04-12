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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section;

import com.winterhavenmc.util.messagebuilder.recordkey.RecordKey;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class ValidConstantRecordTest
{
    @Test
    void constructorTest()
    {
        RecordKey key = RecordKey.of("testKey").orElseThrow();
        Object value = 42;
        ValidConstantRecord record = ValidConstantRecord.create(key, value);

        assertNotNull(record);
        assertEquals(key, record.key());
        assertEquals(value, record.value());
    }


    @Test
    void testConstantRecordWithNullValues()
    {
        ValidConstantRecord record = ValidConstantRecord.create(null, null);

        assertNull(record.key());
        assertNull(record.value());
    }


    @Test
    void testConstantRecordEquality()
    {
        RecordKey key = RecordKey.of("key").orElseThrow();

        ValidConstantRecord record1 = ValidConstantRecord.create(key, 123);
        ValidConstantRecord record2 = ValidConstantRecord.create(key, 123);

        assertEquals(record1, record2);
        assertEquals(record1.hashCode(), record2.hashCode());
    }


    @Test
    void testConstantRecordInequality()
    {
        RecordKey key1 = RecordKey.of("key").orElseThrow();
        RecordKey key2 = RecordKey.of("key").orElseThrow();

        ValidConstantRecord record1 = ValidConstantRecord.create(key1, 123);
        ValidConstantRecord record2 = ValidConstantRecord.create(key2, 456);

        assertNotEquals(record1, record2);
    }

}
