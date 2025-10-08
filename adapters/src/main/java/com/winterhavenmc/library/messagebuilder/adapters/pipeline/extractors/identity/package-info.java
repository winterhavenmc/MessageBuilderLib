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

/**
 * Provides support for macro replacement of UUID values from objects.
 *
 * <p>This package contains the {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.extractors.identity.Identifiable}
 * interface and its corresponding adapter {@link UniqueIdAdapter}.
 * These components enable automatic extraction and macro substitution of UUIDs for objects such as:
 * <ul>
 *     <li>Entities</li>
 *     <li>Offline players</li>
 *     <li>Player profiles</li>
 *     <li>Worlds</li>
 *     <li>Plugin-defined types implementing {@code Identifiable}</li>
 * </ul>
 *
 * <p>When a match is found, the object's UUID will be formatted as a string and placed into the macro
 * map under the {@code {OBJECT.UUID}} placeholder.</p>
 */
package com.winterhavenmc.library.messagebuilder.adapters.pipeline.extractors.identity;
