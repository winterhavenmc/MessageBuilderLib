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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters;

import java.util.Optional;


/**
 * A functional interface representing an adapter capable of extracting macro-relevant data from an object.
 *
 * <p>Adapters are responsible for converting arbitrary plugin or Bukkit types into structured representations
 * that expose specific fields used in macro substitution. This enables dynamic replacement of placeholders
 * in message templates with values derived from complex or nested objects.
 *
 * <p>Each {@code Adapter} implementation defines logic for a particular type or interface contract, such as:
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
 * @see com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter.BuiltIn BuiltIn
 * @see com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap MacroStringMap
 * @see com.winterhavenmc.library.messagebuilder.pipeline.resolvers.CompositeResolver CompositeResolver
 */
public interface Adapter
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

		// compound fields
		LOCATION,
		EXPIRATION,
		PROTECTION,
	}

}
