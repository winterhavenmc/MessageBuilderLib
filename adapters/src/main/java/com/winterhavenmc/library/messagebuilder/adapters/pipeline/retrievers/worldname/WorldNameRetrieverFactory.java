package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.worldname;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameRetriever;
import org.bukkit.plugin.Plugin;
import org.mvplugins.multiverse.core.MultiverseCore;


public class WorldNameRetrieverFactory
{
	public static WorldNameRetriever getWorldNameRetriever(final Plugin plugin)
	{
		return (plugin instanceof MultiverseCore mvPlugin && mvPlugin.isEnabled())
				? new MultiverseWorldNameRetriever(mvPlugin)
				: new DefaultWorldNameRetriever();
	}
}
