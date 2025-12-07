package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import com.winterhavenmc.library.messagebuilder.adapters.util.SoundId;
import com.winterhavenmc.library.messagebuilder.models.sound.InvalidSoundRecord;
import com.winterhavenmc.library.messagebuilder.models.sound.SoundRecord;
import com.winterhavenmc.library.messagebuilder.models.sound.ValidSoundRecord;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class YamlSoundRepositoryTest
{
	@Mock Plugin pluginMock;
	@Mock Player playerMock;
	@Mock YamlSoundResourceManager soundResourceManagerMock;

	FileConfiguration configuration = new YamlConfiguration();

	String configString = """
			COMMAND_SUCCESS:
			  ENABLED: true
			  PLAYER_ONLY: true
			  SOUND_NAME: minecraft:entity.player.levelup
			  VOLUME: 1
			  PITCH: 1.25
			COMMAND_FAIL:
			  ENABLED: true
			  PLAYER_ONLY: true
			  SOUND_NAME: minecraft:entity.villager.no
			  VOLUME: 1
			  PITCH: 1
			""";


	@Test @DisplayName("getRecord() should return a ValidSoundRecord when given a key for a valid entry.")
	void getRecord_returns_valid_sound_record_given_enum_constant_key() throws InvalidConfigurationException
	{
		// Arrange
		configuration.loadFromString(configString);
		YamlSoundConfigurationProvider configurationProvider = new YamlSoundConfigurationProvider(() -> configuration);
		when(soundResourceManagerMock.getConfigurationProvider()).thenReturn(configurationProvider);

		// Act
		YamlSoundRepository soundRepository = new YamlSoundRepository(pluginMock, soundResourceManagerMock);
		SoundRecord result = soundRepository.getRecord(SoundId.COMMAND_SUCCESS);

		// Assert
		assertInstanceOf(ValidSoundRecord.class, result);

		// Verify
		verify(soundResourceManagerMock, atLeastOnce()).getConfigurationProvider();
	}


	@Test @DisplayName("getRecord() should return an InvalidSoundRecord when given a key for a non-existent entry.")
	void getRecord_returns_invalid_sound_record_given_nonexistent_key()
	{
		// Arrange
		YamlSoundConfigurationProvider configurationProvider = new YamlSoundConfigurationProvider(() -> configuration);
		when(soundResourceManagerMock.getConfigurationProvider()).thenReturn(configurationProvider);

		// Act
		YamlSoundRepository soundRepository = new YamlSoundRepository(pluginMock, soundResourceManagerMock);
		SoundRecord result = soundRepository.getRecord(SoundId.COMMAND_SUCCESS);

		// Assert
		assertInstanceOf(InvalidSoundRecord.class, result);

		// Verify
		verify(soundResourceManagerMock, atLeastOnce()).getConfigurationProvider();
	}


	@Test @DisplayName("getRecord() should return a ValidSoundRecord when given a key for a valid entry.")
	void getRecord_returns_valid_sound_record_given_valid_string_key() throws InvalidConfigurationException
	{
		// Arrange
		configuration.loadFromString(configString);
		YamlSoundConfigurationProvider configurationProvider = new YamlSoundConfigurationProvider(() -> configuration);
		when(soundResourceManagerMock.getConfigurationProvider()).thenReturn(configurationProvider);

		// Act
		YamlSoundRepository soundRepository = new YamlSoundRepository(pluginMock, soundResourceManagerMock);
		SoundRecord result = soundRepository.getRecord("COMMAND_SUCCESS");

		// Assert
		assertInstanceOf(ValidSoundRecord.class, result);

		// Verify
		verify(soundResourceManagerMock, atLeastOnce()).getConfigurationProvider();
	}


	@Test @DisplayName("getRecord() should return an InvalidSoundRecord when given a key for a non-existent entry.")
	void getRecord_returns_invalid_sound_record_given_nonexistent_string_key()
	{
		// Arrange
		YamlSoundConfigurationProvider configurationProvider = new YamlSoundConfigurationProvider(() -> configuration);
		when(soundResourceManagerMock.getConfigurationProvider()).thenReturn(configurationProvider);

		// Act
		YamlSoundRepository soundRepository = new YamlSoundRepository(pluginMock, soundResourceManagerMock);
		SoundRecord result = soundRepository.getRecord("NONEXISTENT_KEY");

		// Assert
		assertInstanceOf(InvalidSoundRecord.class, result);

		// Verify
		verify(soundResourceManagerMock, atLeastOnce()).getConfigurationProvider();
	}


	@Test
	void getKeys() throws InvalidConfigurationException
	{
		configuration.loadFromString(configString);
		YamlSoundConfigurationProvider configurationProvider = new YamlSoundConfigurationProvider(() -> configuration);
		when(soundResourceManagerMock.getConfigurationProvider()).thenReturn(configurationProvider);

		// Act
		YamlSoundRepository soundRepository = new YamlSoundRepository(pluginMock, soundResourceManagerMock);
		Set<String> result = soundRepository.getKeys();

		// Assert
		assertEquals(Set.of("COMMAND_SUCCESS", "COMMAND_FAIL"), result);

		// Verify
		verify(soundResourceManagerMock, atLeastOnce()).getConfigurationProvider();
	}


	@Test
	@Disabled("needs static mock")
	void isValidBukkitSoundName()
	{
		// Arrange
		YamlSoundRepository soundRepository = new YamlSoundRepository(pluginMock, soundResourceManagerMock);

		// Act
		boolean result = soundRepository.isValidBukkitSoundName("BLOCK_ANVIL_BREAK");

		// Assert
		assertTrue(result);
	}


	@Test
	@Disabled("needs static mock")
	void isRegistrySound()
	{
		// Arrange
		YamlSoundRepository soundRepository = new YamlSoundRepository(pluginMock, soundResourceManagerMock);

		// Act
		boolean result = soundRepository.isRegistrySound("BLOCK_ANVIL_BREAK");

		// Assert
		assertTrue(result);
	}


	@Test
	void isValidSoundConfigKey() throws InvalidConfigurationException
	{
		// Arrange
		configuration.loadFromString(configString);
		YamlSoundConfigurationProvider configurationProvider = new YamlSoundConfigurationProvider(() -> configuration);
		when(soundResourceManagerMock.getConfigurationProvider()).thenReturn(configurationProvider);
		YamlSoundRepository soundRepository = new YamlSoundRepository(pluginMock, soundResourceManagerMock);

		// Act
		boolean result = soundRepository.isValidSoundConfigKey("COMMAND_SUCCESS");

		// Assert
		assertTrue(result);

		// Verify
		verify(soundResourceManagerMock, atLeastOnce()).getConfigurationProvider();
	}


	@Test
	void getBukkitSoundName() throws InvalidConfigurationException
	{
		// Arrange
		configuration.loadFromString(configString);
		YamlSoundConfigurationProvider configurationProvider = new YamlSoundConfigurationProvider(() -> configuration);
		when(soundResourceManagerMock.getConfigurationProvider()).thenReturn(configurationProvider);

		// Act
		YamlSoundRepository soundRepository = new YamlSoundRepository(pluginMock, soundResourceManagerMock);
		String result = soundRepository.getBukkitSoundName("COMMAND_SUCCESS");

		// Assert
		assertEquals("minecraft:entity.player.levelup", result);

		// Verify
		verify(soundResourceManagerMock, atLeastOnce()).getConfigurationProvider();
	}


	@Test
	@Disabled("needs static mock")
	void play() throws InvalidConfigurationException
	{
		// Arrange
		configuration.loadFromString(configString);
		YamlSoundConfigurationProvider configurationProvider = new YamlSoundConfigurationProvider(() -> configuration);
		when(soundResourceManagerMock.getConfigurationProvider()).thenReturn(configurationProvider);
		FileConfiguration pluginConfig = new YamlConfiguration();
		pluginConfig.set("sound-effects", true);
		when(pluginMock.getConfig()).thenReturn(pluginConfig);

		// Act
		YamlSoundRepository soundRepository = new YamlSoundRepository(pluginMock, soundResourceManagerMock);

		soundRepository.play(playerMock, SoundId.COMMAND_SUCCESS);

		// Assert
		//assertEquals("minecraft:entity.player.levelup", result);

		// Verify
		verify(soundResourceManagerMock, atLeastOnce()).getConfigurationProvider();
		verify(pluginMock, atLeastOnce()).getConfig();
	}


	@Test
	void testPlay()
	{
	}


	@Test
	void testPlay1()
	{
	}


	@Test
	void soundEffectsDisabled_returns_false_when_configured()
	{
		// Arrange
		FileConfiguration pluginConfig = new YamlConfiguration();
		pluginConfig.set("sound-effects", true);
		when(pluginMock.getConfig()).thenReturn(pluginConfig);

		// Act
		YamlSoundRepository soundRepository = new YamlSoundRepository(pluginMock, soundResourceManagerMock);
		boolean result = soundRepository.soundEffectsDisabled();

		// Assert
		assertFalse(result);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}


	@Test
	void soundEffectsDisabled_returns_true_when_configured()
	{
		// Arrange
		FileConfiguration pluginConfig = new YamlConfiguration();
		pluginConfig.set("sound-effects", false);
		when(pluginMock.getConfig()).thenReturn(pluginConfig);

		// Act
		YamlSoundRepository soundRepository = new YamlSoundRepository(pluginMock, soundResourceManagerMock);
		boolean result = soundRepository.soundEffectsDisabled();

		// Assert
		assertTrue(result);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}


	@Test
	void matchLongest() throws InvalidConfigurationException
	{
		// Arrange
		configuration.loadFromString(configString);
		YamlSoundConfigurationProvider configurationProvider = new YamlSoundConfigurationProvider(() -> configuration);
		when(soundResourceManagerMock.getConfigurationProvider()).thenReturn(configurationProvider);

		// Act
		YamlSoundRepository soundRepository = new YamlSoundRepository(pluginMock, soundResourceManagerMock);
		Optional<String> result = soundRepository.matchLongest(SoundId.COMMAND_SUCCESS);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("COMMAND_SUCCESS", result.get());

		// Verify
		verify(soundResourceManagerMock, atLeastOnce()).getConfigurationProvider();
	}

}