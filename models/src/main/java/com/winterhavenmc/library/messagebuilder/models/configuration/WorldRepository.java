package com.winterhavenmc.library.messagebuilder.models.configuration;

import org.bukkit.Location;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface WorldRepository extends ConfigProvider<EnabledWorldsSetting>
{
	/**
	 * Returns the current {@link EnabledWorldsSetting}.
	 *
	 * @return the current enabled worlds setting
	 */
	@Override
	EnabledWorldsSetting get();


	/**
	 * Returns a list of world uuids that are enabled by the plugin config. Only world uuids that match
	 * a current server world are included in the list.
	 *
	 * @return List of enabled world uuids
	 */
	List<UUID> enabledUids();


	/**
	 * get collection of enabled world names from registry
	 *
	 * @return a Collection of String containing enabled world names
	 */
	List<String> enabledNames();


	Optional<String> aliasOrName(UUID worldUid);


	/**
	 * Check if a world is enabled by bukkit world UID
	 *
	 * @param worldUID Unique Identifier for world
	 * @return {@code true} if world is enabled, {@code false} if disabled
	 */
	boolean isEnabled(UUID worldUID);


	/**
	 * Check if a world is enabled by name
	 *
	 * @param worldName name of world as string to check
	 * @return {@code true} if world is enabled, {@code false} if disabled
	 */
	boolean isEnabled(String worldName);


	/**
	 * check if uuid is present in the registry
	 *
	 * @param uuid the uuid of a world
	 * @return {@code boolean} true if the world uuid is present in the registry, or false if not
	 */
	@Contract(pure = true)
	boolean contains(UUID uuid);


	Optional<Location> spawnLocation(UUID worldUid);

}
