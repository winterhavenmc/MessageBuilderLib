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
 * Provides adapter and interface support for expiration-based macro replacements.
 *
 * <p>This package enables automatic extraction of expiration-related data from objects that implement
 * the {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.expiration.Expirable} interface.
 * These data points are used to populate macro placeholders in messages, such as:
 *
 * <ul>
 *   <li>{@code [OBJECT.EXPIRATION.DURATION}} – a human-readable duration until expiration</li>
 *   <li>{@code [OBJECT.EXPIRATION.INSTANT}} – a localized date/time of expiration</li>
 * </ul>
 *
 * <p>The {@link com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.expiration.ExpirationAdapter}
 * allows plugin-defined objects to participate in this macro system simply by implementing the {@code Expirable} interface.
 *
 * <p>Formatting of duration and instant values is delegated to the {@code DurationFormatter} and Java's
 * {@code DateTimeFormatter}, both of which use the configured {@code LocaleProvider} to produce localized output.
 *
 * <p>This system is commonly used to represent cooldowns, timeouts, expiration dates, and other time-limited states
 * in a clear and readable format within player-facing messages.
 */
package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.expiration;
