/**
 * Provides mechanisms for extracting macro-compatible string values from objects
 * that have been adapted by the
 * {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor Accessor} system.
 * <p>
 * This package is responsible for converting functional adapter interfaces
 * (such as {@code Nameable}, {@code Locatable}, {@code Quantifiable}, etc.)
 * into a structured {@link com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap MacroStringMap}.
 * The keys and values produced are used to populate and resolve placeholder macros
 * in messages built by the {@code MessageBuilderLib} pipeline.
 * <p>
 * The {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.FieldAccessor} interface defines
 * the contract for this behavior, and the {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.FieldAccessor}
 * class provides the default implementation, dispatching to the appropriate
 * extractor methods based on the runtime type of both the adapter and the adapted object.
 *
 * <h2>Usage Example</h2>
 * Once an object has been adapted to a supported interface (e.g., {@code Nameable}),
 * the extractor will automatically invoke the correct method to produce all
 * relevant placeholder mappings (e.g., {@code {ENTITY.NAME}}).
 * <p>
 * This layer ensures consistent population and resolution of macros across the library.
 *
 * <p>
 * [ Alternate JavaDoc from obsolete package ]
 *
 * <p>
 * Defines the adapter mechanism for macro value extraction within the message-building pipeline.
 *
 * <p>The {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor Accessor} interface provides a common
 * contract for extracting structured or formatted string values from arbitrary domain objects. These values
 * are placed into the macro resolution context and used to replace placeholders in message templates.
 *
 * <h2>Built-in Accessor Support</h2>
 * <p>The library provides built-in adapters for many common object types, such as:
 * <ul>
 *   <li>{@code Nameable} — provides {@code {OBJECT.NAME}}</li>
 *   <li>{@code Locatable} — provides {@code {OBJECT.LOCATION}}, {@code {OBJECT.LOCATION.X}}, etc.</li>
 *   <li>{@code Expirable} and {@code Protectable} — provide {@code DURATION} and {@code INSTANT} subkeys</li>
 *   <li>{@code Owner}, {@code Killer}, {@code Looter}, {@code UUID}, and others</li>
 * </ul>
 *
 * <h2>Plugin Extension</h2>
 * <p>Plugin developers can integrate with the macro system simply by implementing one or more of the supported adapter
 * interfaces on their custom types (e.g., {@code Nameable}, {@code Locatable}, etc.). This allows macros like
 * {@code {MYOBJECT.NAME}} or {@code {MYOBJECT.LOCATION.WORLD}} to be automatically resolved when passed
 * into the message builder.
 *
 * <h2>Accessor Registration and Precedence</h2>
 * <p>All adapters are managed by {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.AccessorRegistry AccessorRegistry},
 * which maintains registration order to enforce resolution precedence. The first adapter to produce a value for
 * a macro string wins; subsequent adapters cannot overwrite that string.
 *
 * <p>This enables library consumers to override or extend built-in behavior with plugin-defined adapters.
 *
 * @see com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor Accessor
 * @see com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.AccessorRegistry AccessorRegistry
 * @see com.winterhavenmc.library.messagebuilder.core.context.AdapterCtx AdapterCtx
 * @see com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.macro.ValueResolver ValueResolver
 */
package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors;
