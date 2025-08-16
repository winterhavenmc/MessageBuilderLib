/**
 * Provides internal map types used in macro extraction and message formatting.
 * <p>
 * This package contains two core classes:
 * <ul>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroObjectMap}
 *       — a type-safe map that holds raw object values keyed by
 *       {@link com.winterhavenmc.library.messagebuilder.keys.MacroKey LegacyMacroKey}.
 *       Used during early phases of the message pipeline, especially in adapter chains.</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap MacroStringMap}
 *   — a post-processing map that contains validated, formatted string values ready for placeholder substitution.</li>
 * </ul>
 *
 * <p>Both maps serve distinct roles in the transformation of contextual input into localized, player-visible messages.
 * The {@code MacroObjectMap} is typically used to carry domain-specific objects forward through the pipeline,
 * whereas the {@code MacroStringMap} is constructed just before message composition and delivery.</p>
 *
 * <h2>Validation</h2>
 * {@code MacroStringMap} performs lightweight validation when values are inserted. Blank or null strings are flagged
 * (typically for logging), but are not rejected. This ensures message macros are populated consistently, with the option
 * to later replace missing values with default fallbacks such as {@code "Unknown"}.
 *
 * <h2>Immutability and Thread Safety</h2>
 * Both map types are mutable and are designed for use in single-threaded message construction contexts.
 * They are not thread-safe by design.
 *
 * @see com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroObjectMap
 * @see com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap
 */
package com.winterhavenmc.library.messagebuilder.pipeline.maps;
