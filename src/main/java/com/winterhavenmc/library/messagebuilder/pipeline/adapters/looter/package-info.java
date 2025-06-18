/**
 * Contains support for adapting objects that represent looting permissions or claims.
 *
 * <p>This package defines the {@link com.winterhavenmc.library.messagebuilder.pipeline.adapters.looter.Lootable}
 * interface, which is used to associate an object with a looter — a player or entity that has
 * permission to retrieve contents or rewards from a container, chest, or drop.
 *
 * <p>Importantly, this looter concept is not tied to an actual looting event. Instead, it expresses
 * ownership or entitlement prior to looting. This allows for message macros like {@code %OBJECT.LOOTER%}
 * to inform players of access restrictions or intended claimants.
 *
 * <p>The {@link com.winterhavenmc.library.messagebuilder.pipeline.adapters.looter.LooterAdapter}
 * supports both plugin-defined {@code Lootable} objects and Bukkit {@code LootContext} instances.
 *
 * <p><strong>Note:</strong> This is unrelated to {@code Lootable} and should not be confused
 * with the standard Bukkit interface of the same name.
 */
package com.winterhavenmc.library.messagebuilder.pipeline.adapters.looter;
