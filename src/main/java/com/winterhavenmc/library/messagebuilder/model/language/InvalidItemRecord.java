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

package com.winterhavenmc.library.messagebuilder.model.language;

import com.winterhavenmc.library.messagebuilder.keys.RecordKey;


/**
 * An {@link ItemRecord} representing a missing or invalid entry from the
 * {@code ITEMS} section of a language file.
 * <p>
 * This class is returned by the
 * {@code ItemRecord.from(RecordKey, ConfigurationSection)}
 * factory method when the provided section is {@code null}, or when a parsing error occurs.
 * <p>
 * The {@code reason} field provides a human-readable explanation for why the record is considered invalid.
 * This allows for safe logging, debugging, and non-fatal fallback behavior in the library's message pipeline.
 *
 * <p>Invalid item records are safe to pass around and will typically result in no item
 * being created when encountered.
 *
 * @param key the {@link com.winterhavenmc.library.messagebuilder.keys.RecordKey} that identifies the missing or failed item
 * @param reason a description of why this item record is invalid
 *
 * @see ItemRecord
 * @see ValidItemRecord
 * @see com.winterhavenmc.library.messagebuilder.keys.RecordKey
 */
public record InvalidItemRecord(RecordKey key, String reason) implements ItemRecord { }
