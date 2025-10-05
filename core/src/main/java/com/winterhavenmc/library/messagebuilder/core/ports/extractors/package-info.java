/**
 * Provides mechanisms for extracting macro-compatible string values from objects
 * that have been adapted by the
 * {@link com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.Adapter Adapter} system.
 * <p>
 * This package is responsible for converting functional adapter interfaces
 * (such as {@code Nameable}, {@code Locatable}, {@code Quantifiable}, etc.)
 * into a structured {@link com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap MacroStringMap}.
 * The keys and values produced are used to populate and resolve placeholder macros
 * in messages built by the {@code MessageBuilderLib} pipeline.
 * <p>
 * The {@link com.winterhavenmc.library.messagebuilder.core.ports.extractors.FieldExtractor} interface defines
 * the contract for this behavior, and the {@link com.winterhavenmc.library.messagebuilder.core.ports.extractors.FieldExtractor}
 * class provides the default implementation, dispatching to the appropriate
 * extractor methods based on the runtime type of both the adapter and the adapted object.
 *
 * <h2>Usage Example</h2>
 * Once an object has been adapted to a supported interface (e.g., {@code Nameable}),
 * the extractor will automatically invoke the correct method to produce all
 * relevant placeholder mappings (e.g., {@code {ENTITY.NAME}}).
 * <p>
 * This layer ensures consistent population and resolution of macros across the library.
 */
package com.winterhavenmc.library.messagebuilder.core.ports.extractors;
