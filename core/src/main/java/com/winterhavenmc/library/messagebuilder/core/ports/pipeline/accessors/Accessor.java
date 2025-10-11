/*
 * Copyright (c) 2025 Tim Savage.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors;

import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;

import java.util.Optional;


/**
 * Defines a uniform contract for accessing structured data from objects that do
 * not directly implement a library-specific interface.
 * <p>
 * Implementations of this interface act as type-specific wrappers that expose
 * relevant properties of an underlying object in a consistent and predictable
 * way. Accessors are typically used by the
 * {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor}
 * to extract string representations of object data for macro substitution or
 * message composition.
 * </p>
 *
 * <p>
 * The primary purpose of an {@code Accessor} is to bridge the gap between
 * heterogeneous object types—such as Bukkit entities, item stacks, or locations—
 * and the uniform data model expected by the MessageBuilder library. This enables
 * the library to operate on user-defined or third-party objects without requiring
 * those objects to implement additional interfaces.
 * </p>
 *
 * <h2>Design Notes</h2>
 * <ul>
 *   <li>Accessors are lightweight, immutable views over their wrapped objects.</li>
 *   <li>They are intended for read-only data access; mutation of the underlying
 *       object is not supported.</li>
 *   <li>This interface replaces the previous “adapter” terminology to avoid
 *       confusion with architectural adapters in hexagonal design.</li>
 * </ul>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * Accessor<Location> accessor = new LocationAccessor(player.getLocation());
 * String worldName = accessor.get("world");
 * String x = accessor.get("x");
 * }</pre>
 *
 *
 * @see com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.FieldAccessor
 *
 *<p>
 * OLD JAVADOC BELOW
 *
 *<p>
/**
 * A functional interface representing an adapter capable of extracting macro-relevant data from an object.
 *
 * <p>Adapters are responsible for converting arbitrary plugin or Bukkit types into structured representations
 * that expose specific fields used in macro substitution. This enables dynamic replacement of placeholders
 * in message templates with values derived from complex or nested objects.
 *
 * <p>Each {@code Accessor} implementation defines logic for a particular type or interface contract, such as:
 * <ul>
 *     <li>{@code Nameable} - exposes {@code getName()} field</li>
 *     <li>{@code Locatable} - exposes a {@link org.bukkit.Location}, mapped to multiple subfields</li>
 *     <li>{@code Expirable} - exposes a {@link java.time.Duration} and {@link java.time.Instant}</li>
 * </ul>
 *
 * <p>Adapters return values wrapped in {@link Optional}, allowing the pipeline to gracefully skip unsupported types.
 * Compound adapters may populate multiple subfields (e.g., {@code LOCATION.WORLD}, {@code EXPIRATION.DURATION}, etc.).
 *
 * <p>Plugin developers can participate in macro resolution by implementing supported interfaces like {@code Nameable},
 * {@code Locatable}, {@code Expirable}, and others in their own types.
 *
 * @see Accessor.BuiltIn BuiltIn
 * @see MacroStringMap MacroStringMap
 */
public interface Accessor
{
	String UNKNOWN_VALUE = "-";


	/**
	 * Attempts to adapt the given object to a type-specific representation for macro extraction.
	 *
	 * <p>This method may return a known interface (e.g., {@code Nameable}) or a domain object
	 * suitable for field extraction by a resolver. If the object is not compatible,
	 * an empty {@link Optional} is returned.
	 *
	 * @param object the object to evaluate and adapt
	 * @return an optional adapted representation, or empty if not supported
	 */
	Optional<?> adapt(Object object);


	/**
	 * Returns {@code true} if this adapter can successfully adapt the given object.
	 *
	 * <p>This is equivalent to checking {@code adapt(object).isPresent()} and is typically
	 * used to filter compatible types before performing macro resolution.
	 *
	 * @param object the object to check
	 * @return true if the object is supported by this adapter
	 */
	default boolean supports(final Object object)
	{
		return adapt(object).isPresent();
	}


	/**
	 * Enumeration of all built-in adapter types supported by the message builder pipeline.
	 *
	 * <p>This enum also serves as a human-readable guide to available macro categories and
	 * their expected behavior.
	 *
	 * <h2>Single-field Adapters</h2>
	 * <ul>
	 *   <li>{@link #NAME}</li>
	 *   <li>{@link #DISPLAY_NAME}</li>
	 *   <li>{@link #OWNER}</li>
	 *   <li>{@link #LOOTER}</li>
	 *   <li>{@link #KILLER}</li>
	 *   <li>{@link #DURATION} – localized via Time4J PrettyTime</li>
	 *   <li>{@link #INSTANT} – localized via Java's {@code DateTimeFormatter}</li>
	 *   <li>{@link #QUANTITY}</li>
	 *   <li>{@link #UUID}</li>
	 * </ul>
	 *
	 * <h2>Compound-field Adapters</h2>
	 * <ul>
	 *   <li>{@link #LOCATION} – provides {@code LOCATION.WORLD}, {@code .X}, {@code .Y}, {@code .Z}</li>
	 *   <li>{@link #EXPIRATION} – provides {@code DURATION} and {@code INSTANT} subfields</li>
	 *   <li>{@link #PROTECTION} – provides {@code DURATION} and {@code INSTANT} subfields</li>
	 * </ul>
	 *
	 * <p>These identifiers may be used for documentation, registry lookup, or user-facing plugin introspection tools.
	 */
	enum BuiltIn
	{
		// single value fields
		NAME,
		PLURAL_NAME,
		DISPLAY_NAME,
		OWNER,
		LOOTER,
		KILLER,
		DURATION,
		INSTANT,
		QUANTITY,
		UUID,
		URL,
		VERSION,

		// compound fields
		LOCATION,
		EXPIRATION,
		PROTECTION,
	}

}
