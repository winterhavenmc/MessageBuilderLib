/*
 * Copyright (c) 2022-2025 Tim Savage.
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

package com.winterhavenmc.library.messagebuilder.adapters.resources.configuration;

import com.winterhavenmc.library.messagebuilder.models.configuration.EnabledWorldsProvider;
import com.winterhavenmc.library.messagebuilder.models.configuration.EnabledWorldsSetting;

import org.bukkit.World;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;

import java.util.*;
import java.util.function.Supplier;


public final class BukkitEnabledWorldsProvider implements EnabledWorldsProvider
{
	private final Plugin plugin;
	private final Supplier<EnabledWorldsSetting> enabledWorldsSupplier;

	static final String ENABLED_WORLDS_KEY = "enabled-worlds";
	static final String DISABLED_WORLDS_KEY = "disabled-worlds";


	/**
	 * private constructor, use {@code #create(plugin)} to instantiate
	 */
	private BukkitEnabledWorldsProvider(final Plugin plugin,
										final Supplier<EnabledWorldsSetting> enabledWorldsSupplier)
	{
		this.plugin = plugin;
		this.enabledWorldsSupplier = enabledWorldsSupplier;
	}


	/**
	 * Static factory method creates instance of EnabledWorldsProvider
	 *
	 * @param plugin an instance of the plugin
	 * @return an EnabledWorldsProvider
	 */
	public static EnabledWorldsProvider create(final Plugin plugin)
	{
		return new BukkitEnabledWorldsProvider(plugin, () -> getEnabledWorldsSetting(plugin));
	}


	@Override
	public EnabledWorldsSetting get()
	{
		return enabledWorldsSupplier.get();
	}


	@Override
	public List<UUID> getEnabledWorldUids()
	{
		return enabledWorldsSupplier.get().worldUids();
	}


	@Override
	public List<String> getEnabledWorldNames()
	{
		return enabledWorldsSupplier.get().worldUids().stream()
				.map(uid -> plugin.getServer().getWorld(uid))
				.filter(Objects::nonNull)
				.map(WorldInfo::getName).toList();
	}


	static EnabledWorldsSetting getEnabledWorldsSetting(final Plugin plugin)
	{
		final List<UUID> enabledWorldUids = (!getConfigEnabledWorldUids(plugin).isEmpty())
				? getConfigEnabledWorldUids(plugin)
				: getServerWorldUids(plugin);

		enabledWorldUids.removeAll(getConfigDisabledWorldUids(plugin));

		return new EnabledWorldsSetting(enabledWorldUids);
	}


	static List<UUID> getConfigEnabledWorldUids(final Plugin plugin)
	{
		return new ArrayList<>(plugin.getConfig().getStringList(ENABLED_WORLDS_KEY).stream()
				.map(worldName -> plugin.getServer().getWorld(worldName))
				.filter(Objects::nonNull)
				.map(WorldInfo::getUID).toList());
	}


	static List<UUID> getConfigDisabledWorldUids(final Plugin plugin)
	{
		return plugin.getConfig().getStringList(DISABLED_WORLDS_KEY).stream()
				.map(worldName -> plugin.getServer().getWorld(worldName))
				.filter(Objects::nonNull)
				.map(WorldInfo::getUID).toList();
	}


	static List<UUID> getServerWorldUids(final Plugin plugin)
	{
		return new ArrayList<>(plugin.getServer().getWorlds().stream().map(WorldInfo::getUID).toList());
	}


	/**
	 * Check if a world is enabled by bukkit world UID
	 *
	 * @param worldUID Unique Identifier for world
	 * @return {@code true} if world is enabled, {@code false} if disabled
	 */
	@Override
	public boolean isEnabled(final UUID worldUID)
	{
		return worldUID != null && enabledWorldsSupplier.get().worldUids().contains(worldUID);
	}


	/**
	 * Check if a world is enabled by bukkit world object
	 *
	 * @param world bukkit world object
	 * @return {@code true} if world is enabled, {@code false} if disabled
	 */
	@Override
	public boolean isEnabled(final World world)
	{
		return world != null && enabledWorldsSupplier.get().worldUids().contains(world.getUID());
	}


	/**
	 * Check if a world is enabled by name
	 *
	 * @param worldName name of world as string to check
	 * @return {@code true} if world is enabled, {@code false} if disabled
	 */
	@Override
	public boolean isEnabled(final String worldName)
	{
		if (worldName == null || worldName.isBlank())
		{
			return false;
		}

		World world = plugin.getServer().getWorld(worldName);

		return world != null && enabledWorldsSupplier.get().worldUids().contains(world.getUID());
	}


	/**
	 * get the current size of the registry. used for testing
	 *
	 * @return {@code int} the size of the registry
	 */
	@Contract(pure = true)
	int size()
	{
		return enabledWorldsSupplier.get().worldUids().size();
	}


	/**
	 * check if uuid is present in the registry
	 *
	 * @param uuid the uuid of a world
	 * @return {@code boolean} true if the world uuid is present in the registry, or false if not
	 */
	@Contract(pure = true)
	@Override
	public boolean contains(final UUID uuid)
	{
		return enabledWorldsSupplier.get().worldUids().contains(uuid);
	}

}
