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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.extractors.name;

import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.Adapter;
import com.winterhavenmc.library.messagebuilder.core.context.AdapterCtx;
import com.winterhavenmc.library.messagebuilder.core.pipeline.adapters.name.Nameable;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.profile.PlayerProfile;

import java.util.Optional;


/**
 * An {@link Adapter Adapter} for extracting
 * name information from objects that expose a {@code getName()} method.
 *
 * <p>This adapter wraps supported objects into the {@link Nameable} interface, which unifies
 * access to name-like properties through a standard {@link Nameable#getName()} accessor.
 * The resulting {@code Nameable} can then be used in the message pipeline to perform macro substitution
 * for placeholders such as {@code {OBJECT.NAME}}.
 *
 * <p>Supported types include:
 * <ul>
 *   <li>{@link org.bukkit.command.CommandSender}</li>
 *   <li>{@link org.bukkit.OfflinePlayer}</li>
 *   <li>{@link org.bukkit.profile.PlayerProfile}</li>
 *   <li>{@link org.bukkit.World}</li>
 *   <li>{@link org.bukkit.Server}</li>
 *   <li>{@link org.bukkit.plugin.Plugin}</li>
 * </ul>
 *
 * <p>If the provided object is already an instance of {@code Nameable}, it is returned directly.
 * Otherwise, the adapter attempts to map a known {@code getName()} source into a {@code Nameable}
 * lambda reference. If no match is found, an empty {@code Optional} is returned.
 *
 * @see Nameable
 * @see Adapter
 */
public class NameAdapter implements Adapter
{
	private final AdapterCtx ctx;


	/**
	 * Constructs a new {@code DisplayNameAdapter} with the given context container.
	 *
	 * @param ctx the adapter context providing services such as world name resolution
	 */
	public NameAdapter(final AdapterCtx ctx)
	{
		this.ctx = ctx;
	}


	/**
	 * Attempts to adapt the given object to a {@link Nameable}, if it exposes a name field.
	 *
	 * <p>Returns a {@link java.util.Optional} containing a {@code Nameable} wrapper
	 * if the object is a known type that provides a {@code getName()} method, either directly or indirectly.
	 * Returns an empty {@code Optional} if the object does not expose a supported name source.
	 *
	 * @param obj the object to evaluate and adapt
	 * @return an optional {@code Nameable} if the object is name-compatible, otherwise empty
	 */
	@Override
	public Optional<Nameable> adapt(final Object obj)
	{
		return switch (obj)
		{
			case Nameable nameable -> Optional.of(nameable);
			case CommandSender commandSender -> Optional.of(commandSender::getName);
			case OfflinePlayer offlinePlayer -> Optional.of(offlinePlayer::getName);
			case PlayerProfile playerProfile -> Optional.of(playerProfile::getName);
			case World world -> Optional.of(world::getName);
			case Server server -> Optional.of(server::getName);
			case Plugin plugin -> Optional.of(plugin::getName);
			case ItemStack itemStack -> Optional.of(() -> ctx.itemNameResolver().resolve(itemStack));
			case null, default -> Optional.empty();
		};
	}

}
