/**
 * Provides support for extracting ownership information from objects and applying it to macro placeholders.
 *
 * <p>The {@link com.winterhavenmc.library.messagebuilder.pipeline.adapters.owner.Ownable Ownable} interface represents
 * objects that expose an ownership relationship, such as pets, protected containers, or permission-locked
 * regions. Ownership is resolved via {@link org.bukkit.entity.AnimalTamer AnimalTamer}, allowing compatibility with
 * {@link org.bukkit.OfflinePlayer OfflinePlayer} and other tamable or controllable entities.
 *
 * <p>The {@link com.winterhavenmc.library.messagebuilder.pipeline.adapters.owner.OwnerAdapter} binds to objects
 * that either implement {@code Ownable} or are {@link org.bukkit.entity.Tameable}. This allows server operators
 * to use macros such as {@code {OBJECT.OWNER}} in their messages, which are populated with the name of the owner.
 *
 * <p>Common use cases include:
 * <ul>
 *   <li>Displaying the owner of a {@code DeathChest} or claimable region</li>
 *   <li>Indicating the tamer of a wolf, cat, or other pet</li>
 *   <li>Describing access-controlled resources in player messages</li>
 * </ul>
 */
package com.winterhavenmc.library.messagebuilder.pipeline.adapters.owner;
