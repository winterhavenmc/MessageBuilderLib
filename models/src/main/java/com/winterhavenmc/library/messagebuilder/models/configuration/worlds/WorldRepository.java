package com.winterhavenmc.library.messagebuilder.models.configuration.worlds;

import com.winterhavenmc.library.messagebuilder.models.configuration.ConfigProvider;
import org.bukkit.Location;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * <h1>World Repository</h1>
 * <p>
 * The world repository provides utility methods for querying world
 * parameters and configuration settings. Its primary purpose is to
 * provide an internal list of enabled worlds for the plugin to reference,
 * which is the set difference between the "enabled-worlds" string list
 * setting and the "disabled-worlds" string list setting in the
 * plugin's config.yml file.
 * <p>
 * Other convenience methods are provided for querying world parameters that
 * may return a different result than the standard Bukkit methods. For instance,
 * if the Multiverse plugin is available, it will be used to query the world
 * alias if set, or the spawn location which may differ from the world's
 * spawn.
 * <p>
 * Note that Multiverse spawn locations include yaw and pitch, and
 * therefore allow more precise positioning of a player on respawn.
 * <p>
 * The world repository and its methods are always accessed via an instance
 * of MessageBuilder.
 * <p>
 * <i>Examples:</i>
 {@snippet :
   boolean enabled = messageBuilder.worlds().isEnabled(worldUid);

   String worldName = messageBuilder.worlds().aliasOrName(worldUid);

   Location spawnLocation = messageBuilder.worlds().spawnLocation(worldUid);
 }
 */
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


	Optional<Location> spawnLocation(UUID worldUid);

}
