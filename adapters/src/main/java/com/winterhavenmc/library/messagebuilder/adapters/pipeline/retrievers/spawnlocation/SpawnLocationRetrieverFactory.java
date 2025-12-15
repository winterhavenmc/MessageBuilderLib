package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.spawnlocation;

import org.bukkit.plugin.Plugin;
import org.mvplugins.multiverse.core.MultiverseCore;

public class SpawnLocationRetrieverFactory
{
	public static SpawnLocationRetriever getSpawnLocationRetriever(final Plugin plugin)
	{
		return (plugin instanceof MultiverseCore mvPlugin && mvPlugin.isEnabled())
				? new MultiverseSpawnLocationRetriever()
				: new DefaultSpawnLocationRetriever();
	}
}
