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

import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class ConstantRecordTest
{
    @Test
    void constructorTest()
    {
        RecordKey key = RecordKey.of("testKey").orElseThrow();
        Object value = 42;
        ConstantRecord record = new ConstantRecord(key, value);

        assertNotNull(record);
        assertEquals(key, record.key());
        assertEquals(value, record.obj());
    }

    @Test
    void testConstantRecordWithNullValues() {
        ConstantRecord record = new ConstantRecord(null, null);

        assertNull(record.key());
        assertNull(record.obj());
    }

    @Test
    void testConstantRecordEquality() {
        RecordKey key = RecordKey.of("key").orElseThrow();

        ConstantRecord record1 = new ConstantRecord(key, 123);
        ConstantRecord record2 = new ConstantRecord(key, 123);

        assertEquals(record1, record2);
        assertEquals(record1.hashCode(), record2.hashCode());
    }

    @Test
    void testConstantRecordInequality() {
        RecordKey key1 = RecordKey.of("key").orElseThrow();
        RecordKey key2 = RecordKey.of("key").orElseThrow();

        ConstantRecord record1 = new ConstantRecord(key1, 123);
        ConstantRecord record2 = new ConstantRecord(key2, 456);

        assertNotEquals(record1, record2);
    }
}
