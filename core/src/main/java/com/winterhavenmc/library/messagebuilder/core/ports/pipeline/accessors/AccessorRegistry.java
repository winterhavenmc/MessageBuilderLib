package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors;

import java.util.stream.Stream;

public interface AccessorRegistry
{
	/**
	 * Registers a new {@link Accessor} into the registry.
	 *
	 * <p>Adapters are added to the end of the internal list and evaluated in the order
	 * they were registered. It is the caller's responsibility to ensure adapters are registered
	 * in a preferred resolution priority.
	 *
	 * @param accessor the accessor to register
	 * @throws NullPointerException if the accessor is {@code null}
	 */
	void register(Accessor accessor);

	/**
	 * Returns a stream of all registered {@link Accessor} instances that support the given object.
	 *
	 * <p>Each adapter is evaluated in order, and only those for which
	 * {@link Accessor#supports(Object)} returns {@code true} are included in the result.
	 *
	 * @param object the object to test for adapter support
	 * @return a stream of supporting adapters, or an empty stream if the object is {@code null}
	 */
	Stream<Accessor> getMatchingAdapters(Object object);
}
