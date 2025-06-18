/**
 * Provides configuration accessors for server-defined language, locale, and timezone settings.
 * <p>
 * This package defines interfaces and implementations used to retrieve specific configuration
 * values from a plugin's {@code config.yml} file. These values are exposed via lightweight
 * {@link com.winterhavenmc.library.messagebuilder.resources.configuration.ConfigProvider}
 * wrappers, which encapsulate {@link java.util.function.Supplier Suppler} instances to ensure
 * up-to-date resolution of values at the time of use.
 *
 * <h2>Key Components</h2>
 * <ul>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.resources.configuration.LanguageProvider} –
 *       Retrieves the server's configured language tag (e.g., {@code en-US}).</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.resources.configuration.LocaleProvider} –
 *       Retrieves both the locale and timezone, used for date/time and number formatting.</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.resources.configuration.LanguageSetting} and
 *       {@link com.winterhavenmc.library.messagebuilder.resources.configuration.LocaleSetting} –
 *       Simple record types that wrap parsed configuration values.</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.resources.configuration.LanguageTag} –
 *       A utility class for parsing and validating IETF BCP 47 language tags.</li>
 * </ul>
 *
 * <h2>Resolution Behavior</h2>
 * <p>
 * Providers attempt to resolve values from the plugin configuration in order of preference,
 * falling back to system defaults if not present or invalid. This allows server operators
 * to easily configure language and formatting behavior while retaining safe defaults.
 * </p>
 */
package com.winterhavenmc.library.messagebuilder.resources.configuration;
