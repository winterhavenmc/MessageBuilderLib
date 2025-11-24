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

package com.winterhavenmc.library.messagebuilder.models.language;

import com.winterhavenmc.library.messagebuilder.models.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.models.language.message.MessageRecord;
import com.winterhavenmc.library.messagebuilder.models.language.constant.ConstantRecord;
import com.winterhavenmc.library.messagebuilder.models.language.item.ItemRecord;


/**
 * A sealed interface representing a validated record loaded from a structured
 * section of a language YAML file.
 * <p>
 * All {@code SectionRecord} implementations represent data modeled from one of the
 * three top-level sections in a language file:
 *
 * <ul>
 *   <li>{@code MESSAGES} – See {@link MessageRecord}</li>
 *   <li>{@code ITEMS} – See {@link ItemRecord}</li>
 *   <li>{@code CONSTANTS} – See {@link ConstantRecord}</li>
 * </ul>
 *
 * <p>All implementations are validated on creation following a
 * <strong>data-driven design pattern</strong>—they are considered safe and complete once
 * constructed, and can be freely passed throughout the library without additional checks.
 *
 * <h2>Usage</h2>
 * These records are typically retrieved through a repository
 * and form the foundation for macro resolution or dynamic message construction.
 *
 * @see ConstantRecord
 * @see ItemRecord
 * @see MessageRecord
 */
public interface SectionRecord
{
	RecordKey key();
}
