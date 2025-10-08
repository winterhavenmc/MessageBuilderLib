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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.extractors.protection;

import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.Adapter;
import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.protection.Protectable;

import java.util.Optional;


/**
 * An {@link Adapter} implementation for adapting objects that implement the {@link Protectable} interface.
 *
 * <p>This adapter is responsible for identifying objects that expose a protection period via
 * a {@link java.time.Instant} (i.e., a protection expiration timestamp). If the provided object implements
 * {@code Protectable}, it is wrapped and returned as an {@code Optional<Protectable>}; otherwise,
 * an empty Optional is returned.
 *
 * <p>Once adapted, the object can be used to populate macro placeholders such as:
 * <ul>
 *   <li>{@code [OBJECT.PROTECTION.DURATION}} – a localized string showing the remaining protection time</li>
 *   <li>{@code [OBJECT.PROTECTION.INSTANT}} – the formatted expiration date/time</li>
 * </ul>
 *
 * <p>These macros are generated using the {@link Protectable#extractProtection} method.
 */
public class ProtectionAdapter implements Adapter
{
	/**
	 * Attempts to adapt the given object to the {@link Protectable} interface.
	 *
	 * @param obj the object to test for {@code Protectable} compatibility
	 * @return an {@code Optional<Protectable>} if the object implements {@code Protectable}, otherwise {@code Optional.empty()}
	 */
	@Override
	public Optional<Protectable> adapt(final Object obj)
	{
		return (obj instanceof Protectable protectable)
				? Optional.of(protectable)
				: Optional.empty();
	}

}
