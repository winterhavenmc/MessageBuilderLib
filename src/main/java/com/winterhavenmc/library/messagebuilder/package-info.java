/**
 * The core package of <strong>MessageBuilderLib</strong>, a library designed to simplify
 * the dynamic construction and macro-based substitution of messages in Bukkit plugins.
 * <p>
 * At its core, this library provides a fluent builder API that allows plugin developers
 * to compose rich, type-aware messages that include contextual values, such as player names,
 * world locations, item metadata, and more.
 *
 * <h2>Key Concepts</h2>
 * <ul>
 *   <li><strong>{@link com.winterhavenmc.library.messagebuilder.MessageBuilder}</strong> –
 *       The primary entry point into the library, providing the fluent API used to construct and send messages.</li>
 *   <li><strong>{@link com.winterhavenmc.library.messagebuilder.query.QueryHandler QueryHandler}</strong> –
 *       A customizable source for message templates and item definitions, often backed by YAML files.</li>
 *   <li><strong>{@code MacroObjectMap}</strong> –
 *       A runtime container of objects used to resolve macros into final string values.</li>
 *   <li><strong>{@code Macro}</strong> – Placeholders in messages (e.g., {@code {PLAYER_NAME}}, {@code {LOCATION}})
 *       that are dynamically replaced with values drawn from objects supplied at runtime.</li>
 * </ul>
 *
 * <h2>Design Highlights</h2>
 * <ul>
 *   <li>Supports localization and per-player language settings</li>
 *   <li>Allows multiple macros to be resolved from a single object via adapters</li>
 *   <li>Built-in support for standard Bukkit types like {@code Player}, {@code Location}, {@code ItemStack}, etc.</li>
 *   <li>Extensible via custom {@code MacroProcessor}, {@code Resolver}, or {@code Adapter} implementations</li>
 * </ul>
 *
 * <h2>Typical Usage</h2>
 * Messages are composed fluently using the {@link com.winterhavenmc.library.messagebuilder.MessageBuilder} API:
 *
 * <pre>{@code
 * messageBuilder.compose(sender, MessageId.WELCOME)
 *               .setMacro(Macro.PLAYER, player)
 *               .setMacro(Macro.WORLDNAME, world)
 *               .send();
 * }</pre>
 *
 * This will retrieve the message template for {@code WELCOME}, substitute relevant
 * macro values, and send the result to the command sender.
 *
 * @see com.winterhavenmc.library.messagebuilder.MessageBuilder MessageBuilder
 * @see com.winterhavenmc.library.messagebuilder.query.QueryHandler QueryHandler
 * @see com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroObjectMap MacroObjectMap
 * @see com.winterhavenmc.library.messagebuilder.pipeline.resolvers resolvers
 */
package com.winterhavenmc.library.messagebuilder;
