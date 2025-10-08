/**
 * Provides locale-aware number formatting for use in the message-building pipeline.
 *
 * <p>This package defines the
 * {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.formatters.number.NumberFormatter}
 * interface and its implementation
 * {@link com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.number.LocaleNumberFormatter}
 * which formats {@link java.lang.Number} instances into human-readable {@link java.lang.String}s
 * according to a {@link java.util.Locale}.
 *
 * <p>The formatting behavior is defined by the injected
 * {@link com.winterhavenmc.library.messagebuilder.configuration.LocaleProvider LocaleProvider},
 * enabling integration with user or server-defined localization preferences.
 *
 * <p>Typical use cases include formatting coordinates, quantities, and other numerical values
 * embedded in macro-replaced message templates.
 */
package com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.number;
