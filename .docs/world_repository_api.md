# World Repository

The world repository provides utility methods for querying world 
parameters and configuration settings. Its primary purpose is to
provide an internal list of enabled worlds for the plugin to reference,
which is the set difference between the "enabled-worlds" string list 
setting and the "disabled-worlds" string list setting in the 
plugin's config.yml file.

Other convenience methods are provided for querying world parameters that
may return a different result than the standard Bukkit methods. For instance,
if the Multiverse plugin is available, it will be used to query the world
alias if set, or the spawn location which may differ from the world's
spawn.

Note that Multiverse spawn locations include yaw and pitch, and
therefore allow more precise positioning of a player on respawn.

The world repository and its methods are always accessed via an instance
of MessageBuilder. 

*Example:*
```(java)
boolean enabled = messageBuilder.worlds().isEnabled(worldUid);
```

```(java)
Optional<String> worldName = messageBuilder.worlds().aliasOrName(worldUid);
```

```(java)
Optional<Location> spawnLocation = messageBuilder.worlds().spawnLocation(worldUid);
```
