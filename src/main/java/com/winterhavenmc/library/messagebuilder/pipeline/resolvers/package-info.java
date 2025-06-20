/**
 * Provides a hierarchy of {@link com.winterhavenmc.library.messagebuilder.pipeline.resolvers.Resolver}
 * implementations responsible for converting macro-related input values into string representations
 * for use in templated messages.
 * <p>
 * The {@code resolvers} package contains the core components of the macro resolution
 * pipeline in the {@code MessageBuilderLib} library.
 * Each {@link com.winterhavenmc.library.messagebuilder.pipeline.resolvers.Resolver Resolver}
 * translates values stored in a
 * {@link com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroObjectMap MacroObjectMap}
 * into one or more entries in a
 * {@link com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap MacroStringMap},
 * keyed by {@code MacroKey} placeholders.
 *
 * <h2>Design Overview</h2>
 * <ul>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.pipeline.resolvers.AtomicResolver}
 *   	handles atomic or primitive values (e.g. {@code Boolean}, {@code String}, {@code Number},
 *      {@code Duration}) and maps them directly to the base macro key.</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.pipeline.resolvers.CompositeResolver}
 *   	handles structured or compound values using a registry of
 *      Adapter instances, extracting multiple sub-values mapped to dot-notated subkeys.</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.pipeline.resolvers.FieldResolver}
 *   	serves as a delegating resolver that applies multiple resolvers in order,
 *      preserving the first-resolved value for each key.</li>
 * </ul>
 *
 * <h2>Use Case</h2>
 * These resolvers work together to enable dynamic, type-aware placeholder substitution in localized
 * messages, tooltips, and other player-visible text in Bukkit-based Minecraft plugins.
 * Implementers can add custom resolvers or extend the registry of adapters to support new types.
 *
 * @see com.winterhavenmc.library.messagebuilder.pipeline.resolvers.Resolver Resolver
 * @see com.winterhavenmc.library.messagebuilder.pipeline.resolvers.AtomicResolver AtomicResolver
 * @see com.winterhavenmc.library.messagebuilder.pipeline.resolvers.CompositeResolver CompositeResolver
 * @see com.winterhavenmc.library.messagebuilder.pipeline.resolvers.FieldResolver FieldResolver
 * @see com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter Adapter
 * @see com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterRegistry AdapterRegistry
 * @see com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroObjectMap MacroObjectMap
 * @see com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap MacroStringMap
 * @see com.winterhavenmc.library.messagebuilder.keys.MacroKey MacroKey
 */

package com.winterhavenmc.library.messagebuilder.pipeline.resolvers;
