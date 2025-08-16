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

package com.winterhavenmc.library.messagebuilder.model.language;

import com.winterhavenmc.library.messagebuilder.keys.ConstantKey;
import com.winterhavenmc.library.messagebuilder.keys.ValidConstantKey;


/**
 * A {@link ConstantRecord} representing a missing or invalid constant from the
 * {@code CONSTANTS} section of a language YAML file.
 * <p>
 * This record is typically returned by {@link ConstantRecord#from(ValidConstantKey, Object)}
 * when a constant entry is {@code null}, missing, or otherwise unusable.
 * <p>
 * The {@code reason} field provides a human-readable description of the failure,
 * which may be useful for debugging, logging, or diagnostics.
 *
 * <p>This record is safe to return and pass through the message pipeline,
 * and will not result in runtime exceptions when encountered.
 *
 * @param key the {@link ConstantKey} identifying the invalid constant
 * @param reason the explanation for why this record is invalid
 *
 * @see ConstantRecord
 * @see ValidConstantRecord
 * @see com.winterhavenmc.library.messagebuilder.query.QueryHandler QueryHandler
 */
public record InvalidConstantRecord(com.winterhavenmc.library.messagebuilder.keys.RecordKey key, String reason) implements ConstantRecord { }
