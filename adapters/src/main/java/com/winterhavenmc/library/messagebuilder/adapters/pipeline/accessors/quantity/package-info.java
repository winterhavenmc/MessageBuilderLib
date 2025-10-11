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

/**
 * Provides support for macro replacement of quantity-related values.
 *
 * <p>This package includes an implementation of the
 * {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.quantity.Quantifiable}
 * interface, which is used to represent objects that can supply a numeric quantity for macro substitution. These
 * quantities are commonly used in messages to represent values such as:
 * <ul>
 *     <li>Item stack sizes ({@code ItemStack})</li>
 *     <li>Inventory or chest capacities ({@code Inventory}, {@code Chest})</li>
 *     <li>Collection sizes</li>
 *     <li>Custom plugin-defined types that implement {@code Quantifiable}</li>
 * </ul>
 *
 * <p>The {@link com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.quantity.QuantityAdapter}
 * provides automatic adaptation for known Bukkit types as well as user-defined classes.
 *
 * <p>When an object is adapted as a {@code Quantifiable}, it contributes the {@code {OBJECT.QUANTITY}}
 * macro to the pipeline, populated with a localized, formatted number string based on the current locale.
 *
 * @see com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.quantity.Quantifiable
 * @see com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.quantity.QuantityAdapter
 */
package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.quantity;
