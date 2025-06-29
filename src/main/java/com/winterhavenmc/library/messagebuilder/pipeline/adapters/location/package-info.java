/**
 * Provides support for extracting and formatting location-based macro values from objects.
 *
 * <p>This package includes the {@link com.winterhavenmc.library.messagebuilder.pipeline.adapters.location.Locatable}
 * interface and its corresponding {@link com.winterhavenmc.library.messagebuilder.pipeline.adapters.location.LocationAdapter}
 * implementation. It enables plugin-defined or Bukkit-provided objects to contribute detailed
 * location information for use in macro-based message generation.
 *
 * <h2>Macro Support</h2>
 * Objects implementing {@code Locatable} or adapting to it via {@code LocationAdapter}
 * support the following placeholder macros:
 * <ul>
 *   <li>{@code {OBJECT.LOCATION}} – Formatted location string (e.g., <i>world [123, 64, -512]</i>)</li>
 *   <li>{@code {OBJECT.LOCATION.WORLD}} – Resolved world name, optionally via Multiverse alias</li>
 *   <li>{@code {OBJECT.LOCATION.X}}, {@code {OBJECT.LOCATION.Y}}, {@code {OBJECT.LOCATION.Z}} – Localized coordinate values</li>
 * </ul>
 * <p>Many built-in Bukkit objects contain a location field that will be extracted by this adapter, including:
 * {@link org.bukkit.entity.Entity Entity},
 * {@link org.bukkit.block.Block Block},
 * {@link org.bukkit.inventory.Inventory Inventory},
 * {@link org.bukkit.loot.LootContext LootContext},
 * {@link org.bukkit.Raid Raid}, among others, including
 * {@link org.bukkit.Location Location} itself.
 *
 *
 * <p>Integration with {@code Multiverse-Core}, if installed, allows location macros to display world aliases.
 * If not present, standard Bukkit world names are used.
 *
 * <p>This package is part of the MessageBuilder macro adapter framework and contributes to the resolution
 * and substitution of macro placeholders during the message pipeline.
 *
 * @see com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter Adapter
 * @see com.winterhavenmc.library.messagebuilder.pipeline.adapters.location.Locatable Locatable
 * @see com.winterhavenmc.library.messagebuilder.pipeline.adapters.location.LocationAdapter LocationAdapter
 * @see com.winterhavenmc.library.messagebuilder.pipeline.resolvers.worldname.WorldNameResolver WorldNameResolver
 */
package com.winterhavenmc.library.messagebuilder.pipeline.adapters.location;
