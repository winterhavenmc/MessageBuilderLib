package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.worldname;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameRetriever;

import org.bukkit.World;

import static com.winterhavenmc.library.messagebuilder.models.DefaultSymbol.UNKNOWN_WORLD;


/**
 * Default implementation of {@link WorldNameResolver} that returns the raw name
 * of a {@link org.bukkit.World} using {@code World#getName()}.
 * <p>
 * This resolver is used when no aliasing system (such as Multiverse) is available,
 * or when default Bukkit world names are preferred.
 * <p>
 * Example output values include {@code "world"}, {@code "world_nether"}, or
 * {@code "custom_world"}.
 *
 * @see WorldNameResolver
 * @see org.bukkit.World#getName()
 */
public class BukkitWorldNameResolver implements WorldNameResolver
{
	private final WorldNameRetriever retriever;


	/**
	 * Constructor
	 */
	private BukkitWorldNameResolver(final WorldNameRetriever retriever)
	{
		this.retriever = retriever;
	}


	/**
	 * Static factory method
	 */
	public static WorldNameResolver create(final WorldNameRetriever retriever)
	{
		return new BukkitWorldNameResolver(retriever);
	}


	/**
	 * Resolves the user-facing name of the given {@link World}, using
	 * either the native Bukkit name or a plugin-provided alias.
	 *
	 * @param world the {@link World} whose name should be resolved
	 * @return the display or alias name for the world
	 */
	@Override
	public String resolve(World world)
	{
		return retriever.getWorldName(world).orElse(UNKNOWN_WORLD.symbol());
	}

}
