package com.winterhavenmc.library.messagebuilder.adapters.resources.configuration;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.spawnlocation.SpawnLocationResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameResolver;
import com.winterhavenmc.library.messagebuilder.models.configuration.worlds.EnabledWorldsSetting;
import com.winterhavenmc.library.messagebuilder.models.configuration.worlds.WorldRepository;

import org.bukkit.Location;
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
import java.util.Optional;
import java.util.UUID;

import static com.winterhavenmc.library.messagebuilder.adapters.resources.configuration.BukkitWorldRepository.DISABLED_WORLDS_KEY;
import static com.winterhavenmc.library.messagebuilder.adapters.resources.configuration.BukkitWorldRepository.ENABLED_WORLDS_KEY;
import static org.junit.jupiter.api.Assertions.*;
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


	@Test
	void create_with_enabled_worlds_config()
	{
		// Arrange
		FileConfiguration configuration = new YamlConfiguration();
		configuration.set(ENABLED_WORLDS_KEY, List.of("world", "world_nether", "world_the_end"));
		configuration.set(DISABLED_WORLDS_KEY, List.of());

		when(pluginMock.getServer()).thenReturn(serverMock);
		when(pluginMock.getConfig()).thenReturn(configuration);

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
		configuration.set(ENABLED_WORLDS_KEY, List.of("world", "world_nether", "world_the_end"));
		configuration.set(DISABLED_WORLDS_KEY, List.of());

		when(pluginMock.getServer()).thenReturn(serverMock);
		when(pluginMock.getConfig()).thenReturn(configuration);

		configuration.set(ENABLED_WORLDS_KEY, List.of());
		configuration.set(DISABLED_WORLDS_KEY, List.of());

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
	void get_returns_EnabledWorldsSetting()
	{
		// Arrange
		FileConfiguration configuration = new YamlConfiguration();
		configuration.set(ENABLED_WORLDS_KEY, List.of("world", "world_nether", "world_the_end"));
		configuration.set(DISABLED_WORLDS_KEY, List.of());

		when(pluginMock.getServer()).thenReturn(serverMock);
		when(pluginMock.getConfig()).thenReturn(configuration);

		WorldRepository worldRepository = BukkitWorldRepository.create(pluginMock, worldNameResolverMock, spawnLocationResolverMock);

		// Act
		var result = worldRepository.get();

		// Assert
		assertInstanceOf(EnabledWorldsSetting.class, result);
	}


	@Test
	void serverWorldUids()
	{
		// Arrange
		when(pluginMock.getServer()).thenReturn(serverMock);
		when(serverMock.getWorlds()).thenReturn(List.of(world1Mock, world2Mock, world3Mock));
		when(world1Mock.getUID()).thenReturn(world1Uid);
		when(world2Mock.getUID()).thenReturn(world2Uid);
		when(world3Mock.getUID()).thenReturn(world3Uid);

		// Act
		List<UUID> result = BukkitWorldRepository.getServerWorldUids(pluginMock);

		// Assert
		assertEquals(List.of(world1Uid, world2Uid, world3Uid), result);

		// Verify
		verify(pluginMock, atLeastOnce()).getServer();
		verify(serverMock, atLeastOnce()).getWorlds();
		verify(world1Mock, atLeastOnce()).getUID();
		verify(world2Mock, atLeastOnce()).getUID();
		verify(world3Mock, atLeastOnce()).getUID();
	}


	@Test
	void enabledUids()
	{
		// Arrange
		FileConfiguration configuration = new YamlConfiguration();
		configuration.set(ENABLED_WORLDS_KEY, List.of("world", "world_nether", "world_the_end"));
		configuration.set(DISABLED_WORLDS_KEY, List.of());

		when(pluginMock.getServer()).thenReturn(serverMock);
		when(pluginMock.getConfig()).thenReturn(configuration);

		when(serverMock.getWorld("world")).thenReturn(world1Mock);
		when(serverMock.getWorld("world_nether")).thenReturn(world2Mock);
		when(serverMock.getWorld("world_the_end")).thenReturn(world3Mock);

		when(world1Mock.getUID()).thenReturn(world1Uid);
		when(world2Mock.getUID()).thenReturn(world2Uid);
		when(world3Mock.getUID()).thenReturn(world3Uid);

		WorldRepository worldRepository = BukkitWorldRepository.create(pluginMock, worldNameResolverMock, spawnLocationResolverMock);

		// Act
		List<UUID> uuids = worldRepository.enabledUids();

		// Assert
		assertEquals(List.of(world1Uid, world2Uid, world3Uid), uuids);

		// Verify
		verify(serverMock, atLeast(3)).getWorld(anyString());
		verify(world1Mock, atLeastOnce()).getUID();
		verify(world2Mock, atLeastOnce()).getUID();
		verify(world3Mock, atLeastOnce()).getUID();
	}


	@Test
	void enabledNames()
	{
		// Arrange
		FileConfiguration configuration = new YamlConfiguration();
		configuration.set(ENABLED_WORLDS_KEY, List.of("world", "world_nether", "world_the_end"));
		configuration.set(DISABLED_WORLDS_KEY, List.of());

		when(pluginMock.getServer()).thenReturn(serverMock);
		when(pluginMock.getConfig()).thenReturn(configuration);

		when(serverMock.getWorld("world")).thenReturn(world1Mock);
		when(serverMock.getWorld("world_nether")).thenReturn(world2Mock);
		when(serverMock.getWorld("world_the_end")).thenReturn(world3Mock);

		when(serverMock.getWorld(world1Uid)).thenReturn(world1Mock);
		when(serverMock.getWorld(world2Uid)).thenReturn(world2Mock);
		when(serverMock.getWorld(world3Uid)).thenReturn(world3Mock);

		when(world1Mock.getUID()).thenReturn(world1Uid);
		when(world2Mock.getUID()).thenReturn(world2Uid);
		when(world3Mock.getUID()).thenReturn(world3Uid);

		when(world1Mock.getName()).thenReturn("world");
		when(world2Mock.getName()).thenReturn("world_nether");
		when(world3Mock.getName()).thenReturn("world_the_end");

		WorldRepository worlds = BukkitWorldRepository.create(pluginMock, worldNameResolverMock, spawnLocationResolverMock);

		// Act
		List<String> result = worlds.enabledNames();

		// Assert
		assertEquals(List.of("world", "world_nether", "world_the_end"), result);

		// Verify
		verify(pluginMock, atLeastOnce()).getServer();
		verify(pluginMock, atLeastOnce()).getConfig();
		verify(serverMock, atLeastOnce()).getWorld("world");
		verify(serverMock, atLeastOnce()).getWorld("world_nether");
		verify(serverMock, atLeastOnce()).getWorld("world_the_end");
		verify(world1Mock, atLeastOnce()).getUID();
		verify(world2Mock, atLeastOnce()).getUID();
		verify(world3Mock, atLeastOnce()).getUID();
	}


	@Test
	void isEnabled_returns_true()
	{
		// Arrange
		FileConfiguration configuration = new YamlConfiguration();
		configuration.set(ENABLED_WORLDS_KEY, List.of("world", "world_nether", "world_the_end"));
		configuration.set(DISABLED_WORLDS_KEY, List.of());

		when(pluginMock.getConfig()).thenReturn(configuration);
		when(pluginMock.getServer()).thenReturn(serverMock);
		when(serverMock.getWorld("world")).thenReturn(world1Mock);
		when(world1Mock.getUID()).thenReturn(world1Uid);

		WorldRepository worlds = BukkitWorldRepository.create(pluginMock, worldNameResolverMock, spawnLocationResolverMock);

		// Act
		boolean result = worlds.isEnabled(world1Uid);

		// Assert
		assertTrue(result);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
		verify(pluginMock, atLeastOnce()).getServer();
		verify(serverMock, atLeastOnce()).getWorld(anyString());
		verify(world1Mock, atLeastOnce()).getUID();
	}


	@Test
	void isEnabled_returns_false()
	{
		// Arrange
		FileConfiguration configuration = new YamlConfiguration();
		configuration.set(ENABLED_WORLDS_KEY, List.of("world_nether", "world_the_end"));
		configuration.set(DISABLED_WORLDS_KEY, List.of());

		when(pluginMock.getConfig()).thenReturn(configuration);
		when(pluginMock.getServer()).thenReturn(serverMock);

		WorldRepository worlds = BukkitWorldRepository.create(pluginMock, worldNameResolverMock, spawnLocationResolverMock);

		// Act
		boolean result = worlds.isEnabled(world1Uid);

		// Assert
		assertFalse(result);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
		verify(pluginMock, atLeastOnce()).getServer();
		verify(serverMock, atLeastOnce()).getWorld(anyString());
	}


	@Test
	void aliasOrName_returns_optional_string()
	{
		// Arrange
		when(pluginMock.getServer()).thenReturn(serverMock);
		when(serverMock.getWorld(world1Uid)).thenReturn(world1Mock);
		when(worldNameResolverMock.resolve(world1Mock)).thenReturn("world");
		WorldRepository worlds = BukkitWorldRepository.create(pluginMock, worldNameResolverMock, spawnLocationResolverMock);

		// Act
		Optional<String> result = worlds.aliasOrName(world1Uid);

		// Assert
		assertTrue(result.isPresent());

		// Verify
		verify(pluginMock, atLeastOnce()).getServer();
		verify(serverMock, atLeastOnce()).getWorld(world1Uid);
		verify(worldNameResolverMock, atLeastOnce()).resolve(world1Mock);
	}


	@Test
	void aliasOrName_returns_empty_optional_given_null_world()
	{
		// Arrange
		when(pluginMock.getServer()).thenReturn(serverMock);
		when(serverMock.getWorld(world1Uid)).thenReturn(null);
		WorldRepository worlds = BukkitWorldRepository.create(pluginMock, worldNameResolverMock, spawnLocationResolverMock);

		// Act
		Optional<String> result = worlds.aliasOrName(world1Uid);

		// Assert
		assertTrue(result.isEmpty());

		// Verify
		verify(pluginMock, atLeastOnce()).getServer();
		verify(serverMock, atLeastOnce()).getWorld(world1Uid);
	}


	@Test
	void spawnLocation_returns_optional_location()
	{
		// Arrange
		when(pluginMock.getServer()).thenReturn(serverMock);
		when(serverMock.getWorld(world1Uid)).thenReturn(world1Mock);
		when(spawnLocationResolverMock.resolve(world1Mock)).thenReturn(Optional.of(new Location(world1Mock, 1, 2, 3)));
		WorldRepository worlds = BukkitWorldRepository.create(pluginMock, worldNameResolverMock, spawnLocationResolverMock);

		// Act
		Optional<Location> result = worlds.spawnLocation(world1Uid);

		// Assert
		assertTrue(result.isPresent());

		// Verify
		verify(pluginMock, atLeastOnce()).getServer();
		verify(serverMock, atLeastOnce()).getWorld(world1Uid);
		verify(spawnLocationResolverMock, atLeastOnce()).resolve(world1Mock);
	}


	@Test
	void spawnLocation_returns_empty_optional_given_null_world()
	{
		// Arrange
		when(pluginMock.getServer()).thenReturn(serverMock);
		when(serverMock.getWorld(world1Uid)).thenReturn(null);
		WorldRepository worlds = BukkitWorldRepository.create(pluginMock, worldNameResolverMock, spawnLocationResolverMock);

		// Act
		Optional<Location> result = worlds.spawnLocation(world1Uid);

		// Assert
		assertTrue(result.isEmpty());

		// Verify
		verify(pluginMock, atLeastOnce()).getServer();
		verify(serverMock, atLeastOnce()).getWorld(world1Uid);
	}


	@Test
	void isEnabled_returns_true_given_enabled_world_name()
	{
		// Arrange
		FileConfiguration configuration = new YamlConfiguration();
		configuration.set(ENABLED_WORLDS_KEY, List.of("world", "world_nether", "world_the_end"));
		configuration.set(DISABLED_WORLDS_KEY, List.of());

		when(pluginMock.getServer()).thenReturn(serverMock);
		when(pluginMock.getConfig()).thenReturn(configuration);
		when(serverMock.getWorld("world")).thenReturn(world1Mock);
		when(world1Mock.getUID()).thenReturn(world1Uid);

		WorldRepository worlds = BukkitWorldRepository.create(pluginMock, worldNameResolverMock, spawnLocationResolverMock);

		// Act
		boolean result = worlds.isEnabled("world");

		// Assert
		assertTrue(result);

		// Verify
		verify(pluginMock, atLeastOnce()).getServer();
		verify(pluginMock, atLeastOnce()).getConfig();
		verify(serverMock, atLeastOnce()).getWorld("world");
		verify(world1Mock, atLeastOnce()).getUID();
	}


	@Test
	void isEnabled_returns_false_given_disabled_world_name()
	{
		// Arrange
		FileConfiguration configuration = new YamlConfiguration();
		configuration.set(ENABLED_WORLDS_KEY, List.of("world", "world_nether", "world_the_end"));
		configuration.set(DISABLED_WORLDS_KEY, List.of("world_nether"));

		when(pluginMock.getServer()).thenReturn(serverMock);
		when(pluginMock.getConfig()).thenReturn(configuration);
		when(serverMock.getWorld("world_nether")).thenReturn(world2Mock);
		when(world2Mock.getUID()).thenReturn(world2Uid);

		WorldRepository worlds = BukkitWorldRepository.create(pluginMock, worldNameResolverMock, spawnLocationResolverMock);

		// Act
		boolean result = worlds.isEnabled("world_nether");

		// Assert
		assertFalse(result);

		// Verify
		verify(pluginMock, atLeastOnce()).getServer();
		verify(pluginMock, atLeastOnce()).getConfig();
		verify(serverMock, atLeastOnce()).getWorld("world");
	}


	@Test
	void isEnabled_returns_false_given_null_world_name()
	{
		// Arrange
		FileConfiguration configuration = new YamlConfiguration();
		configuration.set(ENABLED_WORLDS_KEY, List.of("world", "world_nether", "world_the_end"));
		configuration.set(DISABLED_WORLDS_KEY, List.of("world_nether"));

		WorldRepository worlds = BukkitWorldRepository.create(pluginMock, worldNameResolverMock, spawnLocationResolverMock);

		// Act
		boolean result = worlds.isEnabled((String) null);

		// Assert
		assertFalse(result);
	}

}