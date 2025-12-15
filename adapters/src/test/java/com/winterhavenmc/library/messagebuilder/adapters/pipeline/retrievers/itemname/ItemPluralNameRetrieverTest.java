package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.itemname;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ItemRepository;
import com.winterhavenmc.library.messagebuilder.models.keys.ItemKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidItemKey;
import com.winterhavenmc.library.messagebuilder.models.language.item.ItemRecord;
import com.winterhavenmc.library.messagebuilder.models.language.item.ValidItemRecord;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ItemPluralNameRetrieverTest
{
	@Mock ItemRepository itemRepositoryMock;
	@Mock ItemStack itemStackMock;

	MiniMessage miniMessage = MiniMessage.miniMessage();

	ValidItemKey validItemKey;
	FileConfiguration configuration;
	ValidItemRecord validItemRecord;
	ItemPluralNameRetriever retriever;

	String configString = """
						ITEMS:
						  TEST_ITEM:
						    MATERIAL: NETHER_STAR
						    NAME: "Test Item"
						    PLURAL_NAME: "<choice:'0#zero items|1#one item|1<{QUANTITY} items'>"
						    DISPLAY_NAME: "Lodestar | {DESTINATION}"
						    LORE:
						      - "test item lore line 1"
						      - "test item lore line 2"
						""";


	@BeforeEach
	void setUp() throws InvalidConfigurationException
	{
		configuration = new YamlConfiguration();
		configuration.loadFromString(configString);
		validItemKey = ItemKey.of("TEST_ITEM").isValid().orElseThrow();

		validItemRecord = ItemRecord.of(validItemKey, configuration
				.getConfigurationSection("ITEMS.TEST_ITEM")).isValid().orElseThrow();

		retriever = new ItemPluralNameRetriever(itemRepositoryMock, miniMessage);
	}


	@Test
	void retrieve_returns_empty_optional()
	{
		// Arrange
		when(itemRepositoryMock.isItem(itemStackMock)).thenReturn(true);

		// Act
		Optional<String> result = retriever.retrieve(itemStackMock);

		// Assert
		assertTrue(result.isEmpty());

		// Verify
		verify(itemRepositoryMock, atLeastOnce()).isItem(itemStackMock);
	}


	@Test
	void getValidKey_returns_empty_optional()
	{
		// Arrange & Act
		Optional<ValidItemKey> result = retriever.getValidKey(itemStackMock);

		// Assert
		assertTrue(result.isEmpty());
	}


	@Test
	void getValidKey_returns_valid_key()
	{
		// Arrange
		validItemKey = ItemKey.of("TEST_ITEM").isValid().orElseThrow();
		when(itemRepositoryMock.key(itemStackMock)).thenReturn(validItemKey);

		// Act
		Optional<ValidItemKey> result = retriever.getValidKey(itemStackMock);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("TEST_ITEM", result.get().toString());

		// Verify
		verify(itemRepositoryMock, atLeastOnce()).key(itemStackMock);
	}


	@Test
	void getValidRecord_returns_empty_optional()
	{
		// Arrange & Act
		Optional<ValidItemRecord> result = retriever.getValidRecord(validItemKey);

		// Assert
		assertTrue(result.isEmpty());
	}


	@Test
	void getValidRecord_returns_valid_record()
	{
		// Arrange
		validItemKey = ItemKey.of("TEST_ITEM").isValid().orElseThrow();
		when(itemRepositoryMock.getRecordOpt(validItemKey)).thenReturn(Optional.of(validItemRecord));

		// Act
		Optional<ValidItemRecord> result = retriever.getValidRecord(validItemKey);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("Test Item", result.get().name());

		// Verify
		verify(itemRepositoryMock, atLeastOnce()).getRecordOpt(validItemKey);
	}


	@Test
	void deserializePluralName_returns_plural_name_string()
	{
		// Arrange
		ConfigurationSection itemEntry = configuration.getConfigurationSection("ITEMS.TEST_ITEM");
		ValidItemRecord validItemRecord = ItemRecord.of(validItemKey, itemEntry).isValid().orElseThrow();

		// Act
		String result = retriever.deserializePluralName(validItemRecord, itemStackMock, miniMessage);

		// Assert
		assertEquals("zero items", result);
	}

}
