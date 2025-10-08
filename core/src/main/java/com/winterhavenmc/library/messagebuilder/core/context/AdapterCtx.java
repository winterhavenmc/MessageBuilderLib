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

package com.winterhavenmc.library.messagebuilder.core.context;

import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.location.Locatable;
import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.name.Nameable;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname.ItemDisplayNameResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname.ItemNameResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname.ItemPluralNameResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameResolver;
import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.expiration.Expirable;


/**
 * A simple context container that provides shared services to adapter implementations during macro extraction.
 *
 * <p>This container encapsulates external utilities and formatters that adapters may require, such as
 * the ability to resolve user-friendly world names (via Multiverse, for example), or locale-aware
 * formatting for durations and instants.
 *
 * <p>It is passed to certain adapters (e.g., {@code Locatable}, {@code Expirable}, {@code Protectable})
 * to support context-sensitive formatting logic.
 *
 * @param worldNameResolver the resolver responsible for resolving world name aliases
 * @param formatterCtx the container holding time/locale-based formatters
 *
 * @see FormatterCtx FormatterCtx
 * @see Nameable Nameable
 * @see Locatable Locatable
 * @see Expirable Expirable
 */
public record AdapterCtx(WorldNameResolver worldNameResolver,
						 ItemNameResolver itemNameResolver,
						 ItemDisplayNameResolver itemDisplayNameResolver,
						 ItemPluralNameResolver itemPluralNameResolver,
						 FormatterCtx formatterCtx) { }
