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

package com.winterhavenmc.library.messagebuilder.models.language;

import com.winterhavenmc.library.messagebuilder.models.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidItemKey;


/**
 * An {@link ItemRecord} representing a missing or invalid item entry from the
 * {@code ITEMS} section of a language YAML file.
 * <p>
 * This record is typically returned by {@code ItemRecord.from(LegacyRecordKey, ConfigurationSection)}
 * when the associated configuration section is {@code null} or otherwise unusable.
 *
 * <p>The {@code reason} field provides a human-readable explanation of the failure,
 * useful for diagnostics or logging, but is not required for program logic.
 *
 * <p>This record is safe to pass through all systems in the library, and guarantees
 * that item-related logic does not throw {@code NullPointerException} due to
 * missing configurations.
 *
 * @param key the {@link ValidItemKey} that could not be resolved
 * @param reason an explanation of why the record is invalid (e.g., missing section)
 *
 * @see ItemRecord
 * @see ValidItemRecord
 */
public record InvalidItemRecord(RecordKey key, InvalidRecordReason reason) implements ItemRecord { }
