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

package com.winterhavenmc.library.messagebuilder.query;

import com.winterhavenmc.library.messagebuilder.model.language.SectionRecord;
import com.winterhavenmc.library.messagebuilder.keys.RecordKey;


/**
 * An interface that declares a query handler that is typed constrained to
 * implementations of the SectionRecord interface
 */
@FunctionalInterface
public interface QueryHandler<T extends SectionRecord>
{
	T getRecord(RecordKey key);
}
