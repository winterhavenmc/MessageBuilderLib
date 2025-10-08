package com.winterhavenmc.library.messagebuilder.core.pipeline.adapters;

import java.util.stream.Stream;

public interface AdapterRegistry
{
	/**
	 * Registers a new {@link Adapter} into the registry.
	 *
	 * <p>Adapters are added to the end of the internal list and evaluated in the order
	 * they were registered. It is the caller's responsibility to ensure adapters are registered
	 * in a preferred resolution priority.
	 *
	 * @param adapter the adapter to register
	 * @throws NullPointerException if the adapter is {@code null}
	 */
	void register(Adapter adapter);

	/**
	 * Returns a stream of all registered {@link Adapter} instances that support the given object.
	 *
	 * <p>Each adapter is evaluated in order, and only those for which
	 * {@link Adapter#supports(Object)} returns {@code true} are included in the result.
	 *
	 * @param object the object to test for adapter support
	 * @return a stream of supporting adapters, or an empty stream if the object is {@code null}
	 */
	Stream<Adapter> getMatchingAdapters(Object object);
}
