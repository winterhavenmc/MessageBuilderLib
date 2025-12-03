/**
 * Provides formatting for {@link java.time.Duration} values into localized, natural language strings.
 *
 * <p>This package defines the {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.formatters.duration.DurationFormatter}
 * interface and multiple implementations that support natural-language duration formatting.
 * The formatters are capable of truncating durations to a desired level of precision (e.g., minutes, hours),
 * classifying durations into semantic categories such as normal, unlimited, or less-than-threshold,
 * and formatting them accordingly.
 *
 * <h2>Implementations</h2>
 * <ul>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.duration.Time4jDurationFormatter}:
 *       Uses the Time4J {@code PrettyTime} engine to generate fluent, grammatically correct
 *       and locale-aware representations of durations, such as "2 days, 3 hours, 4 minutes".</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.duration.LocalizedDurationFormatter}:
 *       Wraps another {@code DurationFormatter} and handles special classifications like unlimited
 *       or sub-threshold ("less than") durations, using constant strings from the language file if present.</li>
 * </ul>
 *
 * <h2>Classification</h2>
 * <p>Durations are classified using {@link com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.duration.DurationType}:
 * <ul>
 *   <li>{@code NORMAL} – a standard duration is formatted directly by the delegate formatter.</li>
 *   <li>{@code UNLIMITED} – a negative duration represents "unlimited" and is rendered with a symbol or constant string.</li>
 *   <li>{@code LESS_THAN} – durations smaller than the configured {@link java.time.temporal.ChronoUnit} precision
 *       are considered less than a measurable threshold and rendered with a prefix "&lt;" if a constant string
 *       is not found. The constant string has a placeholder for {@code {DURATION}}, to accommodate languages
 *       that may have a different word order.</li>
 * </ul>
 *
 * <h2>Fallback Behavior</h2>
 * <p>If no matching constant is found in the language file for {@code TIME.UNLIMITED} or {@code TIME.LESS_THAN},
 * the formatter uses language-agnostic fallback symbols:
 * <ul>
 *   <li>{@code "∞"} – for unlimited durations</li>
 *   <li>{@code "< {DURATION}"} – for less-than durations, where {@code {DURATION}} is replaced with the formatted unit</li>
 * </ul>
 * These ensure the output remains meaningful and intelligible even in the absence of localized strings.
 *
 * @see com.winterhavenmc.library.messagebuilder.core.ports.pipeline.formatters.duration.DurationFormatter
 * @see com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.duration.DurationType
 */
package com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.duration;
