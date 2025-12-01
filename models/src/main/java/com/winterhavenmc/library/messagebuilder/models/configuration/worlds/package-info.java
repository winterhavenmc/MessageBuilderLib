/**
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
 * <i>Example:</i>
 * {@snippet :
 *  boolean enabled = messageBuilder.worlds().isEnabled(worldUid);
 *
 *  Optional<String> worldName = messageBuilder.worlds().aliasOrName(worldUid);
 *
 *  Optional<Location> spawnLocation = messageBuilder.worlds().spawnLocation(worldUid);
 * }
 */
package com.winterhavenmc.library.messagebuilder.models.configuration.worlds;
