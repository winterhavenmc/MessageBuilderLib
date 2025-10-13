/**
 * Provides a hierarchy of {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.macro.ValueResolver}
 * implementations responsible for converting macro-related input values into string representations
 * for use in templated messages.
 * <p>
 * The {@code resolvers} package contains the core components of the macro resolution
 * pipeline in the {@code MessageBuilderLib} library.
 * Each {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.macro.ValueResolver ValueResolver}
 * translates values stored in a
 * {@link com.winterhavenmc.library.messagebuilder.core.maps.MacroObjectMap MacroObjectMap}
 * into one or more entries in a
 * {@link com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap MacroStringMap},
 * keyed by {@code LegacyMacroKey} placeholders.
 *
 * <h2>Design Overview</h2>
 * <ul>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.macro.ValueResolver}
 *   	handles atomic or primitive values (e.g. {@code Boolean}, {@code Section}, {@code Number},
 *      {@code Duration}) and maps them directly to the base macro string.</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.macro.ValueResolver}
 *   	handles structured or compound values using a registry of
 *      Accessor instances, extracting multiple sub-values mapped to dot-notated subkeys.</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.macro.ValueResolver}
 *   	serves as a delegating resolver that applies multiple resolvers in order,
 *      preserving the first-resolved value for each string.</li>
 * </ul>
 *
 * <h2>Use Case</h2>
 * These resolvers work together to enable dynamic, type-aware placeholder substitution in localized
 * messages, tooltips, and other player-visible text in Bukkit-based Minecraft plugins.
 * Implementers can add custom resolvers or extend the registry of adapters to support new types.
 *
 * @see com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.macro.ValueResolver ValueResolver
 * @see com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor Accessor
 * @see com.winterhavenmc.library.messagebuilder.core.maps.MacroObjectMap MacroObjectMap
 * @see com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap MacroStringMap
 * @see com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey ValidMacroKey
 */

package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.macro;
