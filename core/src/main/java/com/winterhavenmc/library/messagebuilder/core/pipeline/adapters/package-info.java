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
 * Defines the adapter mechanism for macro value extraction within the message-building pipeline.
 *
 * <p>The {@link com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.Adapter Adapter} interface provides a common
 * contract for extracting structured or formatted string values from arbitrary domain objects. These values
 * are placed into the macro resolution context and used to replace placeholders in message templates.
 *
 * <h2>Built-in Adapter Support</h2>
 * <p>The library provides built-in adapters for many common object types, such as:
 * <ul>
 *   <li>{@code Nameable} — provides {@code {OBJECT.NAME}}</li>
 *   <li>{@code Locatable} — provides {@code {OBJECT.LOCATION}}, {@code {OBJECT.LOCATION.X}}, etc.</li>
 *   <li>{@code Expirable} and {@code Protectable} — provide {@code DURATION} and {@code INSTANT} subkeys</li>
 *   <li>{@code Owner}, {@code Killer}, {@code Looter}, {@code UUID}, and others</li>
 * </ul>
 *
 * <h2>Plugin Extension</h2>
 * <p>Plugin developers can integrate with the macro system simply by implementing one or more of the supported adapter
 * interfaces on their custom types (e.g., {@code Nameable}, {@code Locatable}, etc.). This allows macros like
 * {@code {MYOBJECT.NAME}} or {@code {MYOBJECT.LOCATION.WORLD}} to be automatically resolved when passed
 * into the message builder.
 *
 * <h2>Adapter Registration and Precedence</h2>
 * <p>All adapters are managed by {@link com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.AdapterRegistry AdapterRegistry},
 * which maintains registration order to enforce resolution precedence. The first adapter to produce a value for
 * a macro string wins; subsequent adapters cannot overwrite that string.
 *
 * <p>This enables library consumers to override or extend built-in behavior with plugin-defined adapters.
 *
 * @see com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.Adapter Adapter
 * @see com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.AdapterRegistry AdapterRegistry
 * @see com.winterhavenmc.library.messagebuilder.core.context.AdapterCtx AdapterCtx
 * @see com.winterhavenmc.library.messagebuilder.core.ports.resolvers.macro.CompositeResolver CompositeResolver
 */
package com.winterhavenmc.library.messagebuilder.core.pipeline.adapters;
