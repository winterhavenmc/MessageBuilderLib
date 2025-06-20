/**
 * Provides support for macro replacement of quantity-related values.
 *
 * <p>This package includes the {@link com.winterhavenmc.library.messagebuilder.pipeline.adapters.quantity.Quantifiable}
 * interface, which is used to represent objects that can supply a numeric quantity for macro substitution. These
 * quantities are commonly used in messages to represent values such as:
 * <ul>
 *     <li>Item stack sizes ({@code ItemStack})</li>
 *     <li>Inventory or chest capacities ({@code Inventory}, {@code Chest})</li>
 *     <li>Collection sizes</li>
 *     <li>Custom plugin-defined types that implement {@code Quantifiable}</li>
 * </ul>
 *
 * <p>The {@link com.winterhavenmc.library.messagebuilder.pipeline.adapters.quantity.QuantityAdapter}
 * provides automatic adaptation for known Bukkit types as well as user-defined classes.
 *
 * <p>When an object is adapted as a {@code Quantifiable}, it contributes the {@code {OBJECT.QUANTITY}}
 * macro to the pipeline, populated with a localized, formatted number string based on the current locale.
 *
 * @see com.winterhavenmc.library.messagebuilder.pipeline.adapters.quantity.Quantifiable
 * @see com.winterhavenmc.library.messagebuilder.pipeline.adapters.quantity.QuantityAdapter
 */
package com.winterhavenmc.library.messagebuilder.pipeline.adapters.quantity;
