/**
 * Provides utilities for identifying macro placeholders within message strings.
 *
 * <p>This package forms the parsing layer of the message pipeline, responsible for extracting
 * macro keys (e.g., {@code %PLAYER_NAME%}, {@code %ITEM.NAME_SINGULAR%}) from raw
 * message strings retrieved from the language YAML configuration.
 *
 * <h2>Key Components</h2>
 * <ul>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.pipeline.matcher.Matcher}
 *       – A functional interface for extracting
 *       {@link com.winterhavenmc.library.messagebuilder.keys.ValidMacroKey LegacyMacroKey}
 *       instances using a regular expression.</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.pipeline.matcher.PlaceholderMatcher}
 *       – A default implementation using Java’s built-in regex API to transform matches into macro keys.</li>
 * </ul>
 *
 * <p>The extracted keys serve as input to the resolver pipeline, determining
 * which context objects should be resolved and which placeholders should be replaced.
 *
 * @see com.winterhavenmc.library.messagebuilder.pipeline.matcher.Matcher Matcher
 * @see com.winterhavenmc.library.messagebuilder.pipeline.matcher.PlaceholderMatcher PlaceholderMatcher
 * @see com.winterhavenmc.library.messagebuilder.pipeline.resolvers.Resolver Resolver
 * @see com.winterhavenmc.library.messagebuilder.pipeline.replacer.Replacer Replacer
 */
package com.winterhavenmc.library.messagebuilder.pipeline.matcher;
