
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

package com.winterhavenmc.library.messagebuilder.models.language.message;

import com.winterhavenmc.library.messagebuilder.models.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMessageKey;
import com.winterhavenmc.library.messagebuilder.models.language.InvalidRecordReason;


/**
 * A {@link MessageRecord} representing a missing or invalid entry from the
 * {@code MESSAGES} section of a language file.
 * <p>
 * This class is returned by the
 * {@code MessageRecord.from(LegacyRecordKey, ConfigurationSection)}
 * factory method when the provided section is {@code null}, or when a parsing error occurs.
 * <p>
 * The {@code reason} field provides a human-readable explanation for why the record is considered invalid.
 * This allows for safe logging, debugging, and non-fatal fallback behavior in the library's message pipeline.
 *
 * <p>Invalid message records are safe to pass around and will typically result in no message
 * being rendered or sent when encountered.
 *
 * @param key the {@link ValidMessageKey} that identifies the missing or failed message
 * @param reason a description of why this message record is invalid
 *
 * @see MessageRecord
 * @see ValidMessageRecord
 * @see ValidMessageKey
 */
public record InvalidMessageRecord(RecordKey key, InvalidRecordReason reason) implements MessageRecord { }
