/**
 * Provides classes that expose a uniform interface for accessing structured data
 * from diverse object types used within MessageBuilderLib.
 * <p>
 * Accessors serve as lightweight wrappers around non-conforming objects — such as
 * Bukkit API types — allowing their properties to be read through a consistent
 * contract without requiring the underlying classes to implement library-specific
 * interfaces. Each {@code Accessor} adapts a specific type to a common data access
 * interface, typically by delegating to existing getter methods or equivalent
 * reflective accessors.
 * </p>
 *
 * <p>
 * These classes were formerly known as “adapters,” as they implement the classic
 * GoF Accessor pattern, but have been renamed to avoid confusion with “adapters”
 * in the context of hexagonal (ports and adapters) architecture. The term
 * <strong>accessor</strong> more accurately conveys their purpose as structured,
 * read-only views over arbitrary object types.
 * </p>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * AccessorEngine engine = new AccessorEngine();
 * Accessor<Location> accessor = new LocationAccessor(player.getLocation());
 * Map<String, String> values = engine.extractValues(accessor);
 * }</pre>
 *
 * <p>
 * The {@link com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.MacroFieldAccessor}
 * coordinates the Accessors and collects resolved values into the message context.
 * </p>
 *
 * <p>
 * Related packages:
 * <ul>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers} – For resolving macros or
 *       runtime objects to their corresponding values.</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.adapters.pipeline.processors} – For applying
 *       macro-processing logic to message strings.</li>
 * </ul>
 * </p>
 *
 * @see com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor
 * @see com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.FieldAccessor
 */
package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors;