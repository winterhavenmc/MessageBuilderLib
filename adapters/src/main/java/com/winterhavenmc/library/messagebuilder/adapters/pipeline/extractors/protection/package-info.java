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
 * Provides support for adapting objects with time-based protection attributes into macro string values.
 *
 * <p>This package defines the {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.extractors.protection.Protectable}
 * interface, which is used to describe objects that have a protection expiration {@link java.time.Instant}.
 * It also includes an adapter class, {@link com.winterhavenmc.library.messagebuilder.adapters.pipeline.extractors.protection.ProtectionAdapter},
 * which detects and adapts compatible objects for macro string population.
 *
 * <p>Once adapted, placeholder macros such as:
 * <ul>
 *   <li>{@code {OBJECT.PROTECTION.DURATION}}</li>
 *   <li>{@code {OBJECT.PROTECTION.INSTANT}}</li>
 * </ul>
 * will be automatically populated using the data extracted from the {@code Protectable} object.
 *
 * <p>The {@code DURATION} macro is formatted using the library's configured
 * {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.formatters.duration.DurationFormatter DurationFormatter},
 * while the {@code INSTANT} macro uses a localized {@link java.time.format.DateTimeFormatter DateTimeFormatter}.
 *
 * <p>Like the {@code Expirable} adapter, this design allows developers to easily annotate domain objects with protection logic,
 * and provides users with natural language representations of those time fields within localized messages.
 */
package com.winterhavenmc.library.messagebuilder.adapters.pipeline.extractors.protection;
