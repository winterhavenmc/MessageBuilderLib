/**
 * Provides an adapter for extracting and formatting display names from objects.
 *
 * <p>Any object that implements the {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.displayname.DisplayNameable DisplayNameable}
 * interface can participate in macro replacement via the {@code {OBJECT.DISPLAY_NAME}} placeholder in language file strings.
 *
 * <p>This package includes:
 * <ul>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.displayname.DisplayNameable}
 *   — a functional interface describing
 *   objects that expose a {@code getDisplayName()} method.</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.displayname.DisplayNameAdapter} — an
 *   {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor Accessor}
 *   implementation that adapts various types to the {@code DisplayNameable} contract.</li>
 * </ul>
 *
 * <p>The {@code DisplayNameAdapter} supports:
 * <ul>
 *   <li>Objects already implementing {@code DisplayNameable}</li>
 *   <li>{@link org.bukkit.entity.Player Player} — uses {@link org.bukkit.entity.Player#getDisplayName()}</li>
 *   <li>{@link org.bukkit.Nameable Nameable} — uses {@link org.bukkit.Nameable#getCustomName()}
 *   <em>Note: this references the Bukkit Nameable interface, and not the Nameable interface of the corresponding
 *   adapter found in this library.</em></li>
 *   <li>{@link org.bukkit.World World} — resolves the display name via a
 *       {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameResolver WorldNameResolver}</li>
 * </ul>
 *
 * <p>The macro value is only populated if the display name is non-null and non-blank.
 *
 * @see com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.displayname.DisplayNameable
 * @see com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.displayname.DisplayNameAdapter
 */
package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.displayname;
