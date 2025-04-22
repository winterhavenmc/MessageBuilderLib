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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.item;

import com.winterhavenmc.util.messagebuilder.keys.RecordKey;


/**
 * A data object record for item information contained in the language file. This class also contains
 * an enum of fields with their corresponding path key, and a static method for retrieving a record.
 *
 * @param key the keyPath in the language file for this record
 */
public record InvalidItemRecord(RecordKey key, String reason) implements ItemRecord { }
