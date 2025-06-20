/**
 * Provides support for extracting killer information from entities or plugin-defined objects.
 *
 * <p>This package defines the {@link com.winterhavenmc.library.messagebuilder.pipeline.adapters.killer.Killable}
 * interface, which exposes a method to retrieve the killer of an object—typically a
 * {@link org.bukkit.entity.Player Player}.
 * This is useful in generating messages related to entity death, such as death messages or kill logs.
 *
 * <p>The {@link com.winterhavenmc.library.messagebuilder.pipeline.adapters.killer.KillerAdapter} binds
 * to objects that implement {@code Killable}, or to
 * {@link org.bukkit.entity.LivingEntity LivingEntity}
 * instances, which provide built-in killer tracking via {@code getKiller()}.
 *
 * <p>Macros such as {@code {OBJECT.KILLER}} are replaced with the name of the killer, if resolvable.
 * The adapter supports both online and offline players through the
 * {@link org.bukkit.entity.AnimalTamer AnimalTamer}
 * abstraction, enabling broader compatibility.
 */
package com.winterhavenmc.library.messagebuilder.pipeline.adapters.killer;
