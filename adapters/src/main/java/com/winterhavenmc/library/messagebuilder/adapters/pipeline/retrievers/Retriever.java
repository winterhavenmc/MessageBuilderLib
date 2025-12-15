package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.spawnlocation.DefaultSpawnLocationRetriever;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.spawnlocation.MultiverseSpawnLocationRetriever;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.spawnlocation.SpawnLocationRetriever;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.worldname.DefaultWorldNameRetriever;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.worldname.MultiverseWorldNameRetriever;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameRetriever;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.mvplugins.multiverse.core.MultiverseCore;

import java.util.Optional;


public interface Retriever
{
	Optional<String> retrieve(ItemStack itemStack);


	static WorldNameRetriever getWorldNameRetriever(final Plugin plugin)
	{
		return (plugin instanceof MultiverseCore mvPlugin && mvPlugin.isEnabled())
				? new MultiverseWorldNameRetriever(mvPlugin)
				: new DefaultWorldNameRetriever();
	}


	static SpawnLocationRetriever getSpawnLocationRetriever(final Plugin plugin)
	{
		return (plugin instanceof MultiverseCore mvPlugin && mvPlugin.isEnabled())
				? new MultiverseSpawnLocationRetriever()
				: new DefaultSpawnLocationRetriever();
	}
}
