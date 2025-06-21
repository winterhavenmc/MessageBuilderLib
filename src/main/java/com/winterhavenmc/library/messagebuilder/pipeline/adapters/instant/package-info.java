/**
 * Provides adapter support for objects that expose a timestamp via the {@link java.time.Instant} API.
 *
 * <p>The {@link com.winterhavenmc.library.messagebuilder.pipeline.adapters.instant.Instantable}
 * interface allows plugin-defined objects to participate in macro substitution using
 * instant-based placeholders such as:
 *
 * <pre>{@code {OBJECT.INSTANT}}</pre>
 *
 * <p>At runtime, the
 * {@link com.winterhavenmc.library.messagebuilder.pipeline.adapters.instant.InstantAdapter InstantAdapter}
 * performs type checking to detect support for this interface and extract the instant value.
 * The value is then formatted using the current locale and time zone, via Java's built-in
 * {@link java.time.format.DateTimeFormatter DateTimeFormatter} system, with a configurable
 * {@link java.time.format.FormatStyle FormatStyle}.
 *
 * <p>This adapter enables the generation of localized, human-readable date and time strings
 * from any object that implements {@code Instantable}.
 *
 * @see com.winterhavenmc.library.messagebuilder.pipeline.adapters.instant.Instantable Instantable
 * @see java.time.Instant Instant
 * @see java.time.format.DateTimeFormatter DateTimeFormatter
 */
package com.winterhavenmc.library.messagebuilder.pipeline.adapters.instant;
