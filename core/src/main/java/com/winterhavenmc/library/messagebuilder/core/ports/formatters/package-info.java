/**
 * Contains formatters responsible for converting data types into localized, human-readable strings.
 *
 * <p>This package serves as a container for specialized subpackages that provide formatting
 * support for numeric values and durations. These formatters are used throughout the
 * {@code MessageBuilderLib} pipeline to transform objects into displayable strings based on the
 * active {@link java.util.Locale}, ensuring messages are appropriately localized for the end user.
 *
 * <h2>Subpackages</h2>
 * <ul>
 *   <li>{@code number} – Provides formatting for numeric values such as coordinates or quantities
 *       using locale-aware grouping and decimal symbols.</li>
 *   <li>{@code duration} – Provides duration formatters that can classify and format time intervals
 *       using natural language expressions and localization, with support for concepts such as
 *       "unlimited" and "less than".</li>
 * </ul>
 *
 * <p>These formatters are internally used by adapters such as {@code Locatable}, {@code Durationable},
 * and {@code Expirable}, allowing macro replacements to reflect user-friendly and linguistically
 * appropriate representations.
 *
 * @see com.winterhavenmc.library.messagebuilder.core.ports.formatters.number
 * @see com.winterhavenmc.library.messagebuilder.core.ports.formatters.duration
 */
package com.winterhavenmc.library.messagebuilder.core.ports.formatters;
