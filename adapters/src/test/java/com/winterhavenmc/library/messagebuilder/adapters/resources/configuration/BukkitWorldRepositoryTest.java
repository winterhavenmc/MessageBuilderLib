package com.winterhavenmc.library.messagebuilder.adapters.resources.configuration;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.spawnlocation.SpawnLocationResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameResolver;
import com.winterhavenmc.library.messagebuilder.models.configuration.WorldRepository;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static com.winterhavenmc.library.messagebuilder.adapters.resources.configuration.BukkitWorldRepository.DISABLED_WORLDS_KEY;
import static com.winterhavenmc.library.messagebuilder.adapters.resources.configuration.BukkitWorldRepository.ENABLED_WORLDS_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BukkitWorldRepositoryTest
{
	@Mock Plugin pluginMock;
	@Mock Server serverMock;
	@Mock World world1Mock;
	@Mock World world2Mock;
	@Mock World world3Mock;
	@Mock World world4Mock;
	@Mock WorldNameResolver worldNameResolverMock;
	@Mock SpawnLocationResolver spawnLocationResolverMock;

	UUID world1Uid = new UUID(1, 1);
	UUID world2Uid = new UUID(2, 2);
	UUID world3Uid = new UUID(3, 3);
	UUID world4Uid = new UUID(4, 4);

	List<String> serverWorldsNames = List.of("world", "world_nether", "world_the_end", "test_world");


	@Test
	void create_with_enabled_worlds_config()
	{
		// Arrange
		FileConfiguration configuration = new YamlConfiguration();
		configuration.set(ENABLED_WORLDS_KEY, List.of("world", "world_nether", "world_the_end"));
		configuration.set(DISABLED_WORLDS_KEY, List.of());

		when(pluginMock.getConfig()).thenReturn(configuration);
		when(pluginMock.getServer()).thenReturn(serverMock);

		when(serverMock.getWorld("world")).thenReturn(world1Mock);
		when(serverMock.getWorld("world_nether")).thenReturn(world2Mock);
		when(serverMock.getWorld("world_the_end")).thenReturn(world3Mock);

		when(world1Mock.getUID()).thenReturn(world1Uid);
		when(world2Mock.getUID()).thenReturn(world2Uid);
		when(world3Mock.getUID()).thenReturn(world3Uid);

		// Act
		WorldRepository worldRepository = BukkitWorldRepository.create(pluginMock, worldNameResolverMock, spawnLocationResolverMock);
		List<UUID> uuids = worldRepository.enabledUids();

		// Assert
		assertEquals(List.of(world1Uid, world2Uid, world3Uid), uuids);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
		verify(pluginMock, atLeastOnce()).getServer();
		verify(serverMock, atLeast(3)).getWorld(anyString());
		verify(world1Mock, atLeastOnce()).getUID();
		verify(world2Mock, atLeastOnce()).getUID();
		verify(world3Mock, atLeastOnce()).getUID();
	}


	@Test
	void create_with_empty_enabled_worlds_config()
	{
		// Arrange
		FileConfiguration configuration = new YamlConfiguration();
		configuration.set(ENABLED_WORLDS_KEY, List.of());
		configuration.set(DISABLED_WORLDS_KEY, List.of());

		when(pluginMock.getConfig()).thenReturn(configuration);
		when(pluginMock.getServer()).thenReturn(serverMock);
		when(serverMock.getWorlds()).thenReturn(List.of(world1Mock, world2Mock, world3Mock, world4Mock));

		when(world1Mock.getUID()).thenReturn(world1Uid);
		when(world2Mock.getUID()).thenReturn(world2Uid);
		when(world3Mock.getUID()).thenReturn(world3Uid);
		when(world4Mock.getUID()).thenReturn(world4Uid);

		// Act
		WorldRepository worldRepository = BukkitWorldRepository.create(pluginMock, worldNameResolverMock, spawnLocationResolverMock);
		List<UUID> uuids = worldRepository.enabledUids();

		// Assert
		assertEquals(List.of(world1Uid, world2Uid, world3Uid, world4Uid), uuids);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
		verify(pluginMock, atLeastOnce()).getServer();

		verify(world1Mock, atLeastOnce()).getUID();
		verify(world2Mock, atLeastOnce()).getUID();
		verify(world3Mock, atLeastOnce()).getUID();
		verify(world4Mock, atLeastOnce()).getUID();
	}


	@Test
	void get()
	{

	}

	@Test
	void serverWorldUids()
	{
//		var result = BukkitWorldRepository.getServerWorldUids(pluginMock);
	}


	@Test
	void enabledUids()
	{
		List<String> serverWorlds = List.of("world", "world_nether", "world_the_end", "test_world");
		FileConfiguration configuration = new YamlConfiguration();
		configuration.set(ENABLED_WORLDS_KEY, List.of("world", "world_nether", "world_the_end"));
		configuration.set(DISABLED_WORLDS_KEY, List.of());
		when(pluginMock.getConfig()).thenReturn(configuration);
//		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));
		when(pluginMock.getServer()).thenReturn(serverMock);

		when(serverMock.getWorld("world")).thenReturn(world1Mock);
		when(serverMock.getWorld("world_nether")).thenReturn(world2Mock);
		when(serverMock.getWorld("world_the_end")).thenReturn(world3Mock);
//		when(serverMock.getWorld("test_world")).thenReturn(world4Mock);

		when(world1Mock.getUID()).thenReturn(world1Uid);
		when(world2Mock.getUID()).thenReturn(world2Uid);
		when(world3Mock.getUID()).thenReturn(world3Uid);
//		when(world4Mock.getUID()).thenReturn(world4Uid);

		WorldRepository worldRepository = BukkitWorldRepository.create(pluginMock, worldNameResolverMock, spawnLocationResolverMock);

		List<UUID> uuids = worldRepository.enabledUids();

		assertEquals(List.of(world1Uid, world2Uid, world3Uid), uuids);
	}

	@Test
	void enabledNames()
	{
//		List<String> serverWorldsNames = List.of("world", "world_nether", "world_the_end", "test_world");
//		List<World> serverWorlds = List.of(world1Mock, world2Mock, world3Mock, world4Mock);
//
//		FileConfiguration configuration = new YamlConfiguration();
//		configuration.set(ENABLED_WORLDS_KEY, List.of("world", "world_nether", "world_the_end"));
//		configuration.set(DISABLED_WORLDS_KEY, List.of());
//
//		when(pluginMock.getConfig()).thenReturn(configuration);
//		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));
//		when(pluginMock.getServer()).thenReturn(serverMock);
//
//		when(serverMock.getWorlds()).thenReturn(serverWorlds);
//
//		when(serverMock.getWorld("world")).thenReturn(world1Mock);
//		when(serverMock.getWorld("world_nether")).thenReturn(world2Mock);
//		when(serverMock.getWorld("world_the_end")).thenReturn(world3Mock);
////		when(serverMock.getWorld("test_world")).thenReturn(world4Mock);
//
//		when(world1Mock.getUID()).thenReturn(world1Uid);
//		when(world2Mock.getUID()).thenReturn(world2Uid);
//		when(world3Mock.getUID()).thenReturn(world3Uid);
////		when(world4Mock.getUID()).thenReturn(world4Uid);
//		when(world1Mock.getName()).thenReturn("world");
//		when(world2Mock.getName()).thenReturn("world_nether");
//		when(world3Mock.getName()).thenReturn("world_the_end");
//
//		WorldRepository enabledWorldsProvider = BukkitWorldRepository.create(pluginMock);
//
//		Collection<String> names = enabledWorldsProvider.getEnabledWorldNames();
//
//		assertEquals(List.of("world", "world_nether", "world_the_end"), names);
	}

	@Test
	void isEnabled()
	{
//		// Arrange
//		FileConfiguration configuration = new YamlConfiguration();
//		configuration.set(ENABLED_WORLDS_KEY, List.of("world", "world_nether", "world_the_end"));
//		configuration.set(DISABLED_WORLDS_KEY, List.of());
//
//		when(pluginMock.getConfig()).thenReturn(configuration);
////		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));
//		when(pluginMock.getServer()).thenReturn(serverMock);
//
//		when(serverMock.getWorld("world")).thenReturn(world1Mock);
//		when(serverMock.getWorld("world_nether")).thenReturn(world2Mock);
//		when(serverMock.getWorld("world_the_end")).thenReturn(world3Mock);
////		when(serverMock.getWorld("test_world")).thenReturn(world4Mock);
//
//		when(world1Mock.getUID()).thenReturn(world1Uid);
//		when(world2Mock.getUID()).thenReturn(world2Uid);
//		when(world3Mock.getUID()).thenReturn(world3Uid);
////		when(world4Mock.getUID()).thenReturn(world4Uid);
//
//		WorldRepository enabledWorldsProvider = BukkitWorldRepository.create(pluginMock);
//
//		List<UUID> uuids = enabledWorldsProvider.getEnabledWorldUids();
//
//		// Assert
//		assertEquals(List.of(world1Uid, world2Uid, world3Uid), uuids);
//
//		// Verify
//		verify(pluginMock, atLeastOnce()).getConfig();
//		verify(pluginMock, atLeastOnce()).getServer();
//		verify(serverMock, atLeastOnce()).getWorld(anyString());
//		verify(world1Mock, atLeastOnce()).getUID();
//		verify(world2Mock, atLeastOnce()).getUID();
//		verify(world3Mock, atLeastOnce()).getUID();
	}

	@Test
	void testIsEnabled()
	{
	}

	@Test
	void testIsEnabled1()
	{
	}

	@Test
	void size()
	{
	}

	@Test
	void contains()
	{
	}
}