package com.winterhavenmc.library.messagebuilder.adapters.resources.language;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ItemRepository;
import com.winterhavenmc.library.messagebuilder.models.keys.ItemKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidItemKey;
import com.winterhavenmc.library.messagebuilder.models.language.Section;
import com.winterhavenmc.library.messagebuilder.models.language.item.InvalidItemRecord;
import com.winterhavenmc.library.messagebuilder.models.language.item.ItemRecord;

import com.winterhavenmc.library.messagebuilder.models.language.item.ValidItemRecord;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class YamlItemRepositoryTest
{
	@Mock Plugin pluginMock;
	@Mock YamlLanguageResourceManager languageResourceManagerMock;

	ValidItemKey validItemKey = ItemKey.of("TEST_ITEM").isValid().orElseThrow();

	String configString = """
						ITEMS:
						  TEST_ITEM:
						    MATERIAL: NETHER_STAR
						    NAME: "Test Item"
						    PLURAL_NAME: "<choice:'0#zero items|1#one item|1<{QUANTITY} items'>"
						    DISPLAY_NAME: "Item Display Name"
						    LORE:
						      - "test item lore line 1"
						      - "test item lore line 2"
						""";

	FileConfiguration configuration = new YamlConfiguration();
	YamlLanguageSectionProvider itemSectionProvider = new YamlLanguageSectionProvider(() -> configuration, Section.ITEMS);


	@Test
	void getRecord_returns_ValidItemRecord_given_valid_item_section() throws InvalidConfigurationException
	{
		// Arrange
		configuration.loadFromString(configString);
		when(pluginMock.getName()).thenReturn("PluginName");
		when(languageResourceManagerMock.getSectionProvider(Section.ITEMS)).thenReturn(itemSectionProvider);

		// Act
		ItemRepository items = new YamlItemRepository(pluginMock, languageResourceManagerMock);
		ItemRecord result = items.getRecord(validItemKey);

		// Assert
		assertInstanceOf(ValidItemRecord.class, result);

		// Verify
		verify(pluginMock, atLeastOnce()).getName();
		verify(languageResourceManagerMock, atLeastOnce()).getSectionProvider(Section.ITEMS);
	}


	@Test
	void getRecord_returns_InvalidItemRecord_given_empty_item_section()
	{
		// Arrange
		when(pluginMock.getName()).thenReturn("PluginName");
		when(languageResourceManagerMock.getSectionProvider(Section.ITEMS)).thenReturn(itemSectionProvider);

		// Act
		ItemRepository items = new YamlItemRepository(pluginMock, languageResourceManagerMock);
		ItemRecord result = items.getRecord(validItemKey);

		// Assert
		assertInstanceOf(InvalidItemRecord.class, result);

		// Verify
		verify(pluginMock, atLeastOnce()).getName();
		verify(languageResourceManagerMock, atLeastOnce()).getSectionProvider(Section.ITEMS);
	}


	@Test
	@Disabled("needs static mock server")
	void createItem_returns_valid_item() throws InvalidConfigurationException
	{
		// Arrange
		configuration.loadFromString(configString);
		when(pluginMock.getName()).thenReturn("PluginName");
		when(languageResourceManagerMock.getSectionProvider(Section.ITEMS)).thenReturn(itemSectionProvider);

		// Act
		ItemRepository items = new YamlItemRepository(pluginMock, languageResourceManagerMock);
		Optional<ItemStack> item = items.createItem(validItemKey);

		// Assert
		assertTrue(item.isPresent());

		// Verify
		verify(pluginMock, atLeastOnce()).getName();
	}


	@Test
	@Disabled("needs static mock server")
	void createItem2()
	{
	}


	@Test
	@Disabled("needs static mock server")
	void createItem3()
	{
	}


	@Test
	void name() throws InvalidConfigurationException
	{
		// Arrange
		configuration.loadFromString(configString);
		when(pluginMock.getName()).thenReturn("PluginName");
		when(languageResourceManagerMock.getSectionProvider(Section.ITEMS)).thenReturn(itemSectionProvider);

		// Act
		ItemRepository items = new YamlItemRepository(pluginMock, languageResourceManagerMock);
		ItemRecord result = items.getRecord(validItemKey);

		// Assert
		assertInstanceOf(ValidItemRecord.class, result);
		assertEquals("Test Item", result.isValid().orElseThrow().name());

		// Verify
		verify(pluginMock, atLeastOnce()).getName();
		verify(languageResourceManagerMock, atLeastOnce()).getSectionProvider(Section.ITEMS);
	}


	@Test
	void displayName() throws InvalidConfigurationException
	{
		// Arrange
		configuration.loadFromString(configString);
		when(pluginMock.getName()).thenReturn("PluginName");
		when(languageResourceManagerMock.getSectionProvider(Section.ITEMS)).thenReturn(itemSectionProvider);

		// Act
		ItemRepository items = new YamlItemRepository(pluginMock, languageResourceManagerMock);
		ItemRecord result = items.getRecord(validItemKey);

		// Assert
		assertInstanceOf(ValidItemRecord.class, result);
		assertEquals("Item Display Name", result.isValid().orElseThrow().displayName());

		// Verify
		verify(pluginMock, atLeastOnce()).getName();
		verify(languageResourceManagerMock, atLeastOnce()).getSectionProvider(Section.ITEMS);
	}


	@Test
	void getRecordOpt() throws InvalidConfigurationException
	{
		// Arrange
		configuration.loadFromString(configString);
		when(pluginMock.getName()).thenReturn("PluginName");
		when(languageResourceManagerMock.getSectionProvider(Section.ITEMS)).thenReturn(itemSectionProvider);

		// Act
		ItemRepository items = new YamlItemRepository(pluginMock, languageResourceManagerMock);
		Optional<ItemRecord> result = items.getRecordOpt(validItemKey);

		// Assert
		assertTrue(result.isPresent());
		assertInstanceOf(ValidItemRecord.class, result.get());

		// Verify
		verify(pluginMock, atLeastOnce()).getName();
		verify(languageResourceManagerMock, atLeastOnce()).getSectionProvider(Section.ITEMS);
	}


	@Test
	@Disabled("needs static mock server")
	void isItem_returns_false_given_non_item() throws InvalidConfigurationException
	{
		// Arrange
		ItemStack itemStack = new ItemStack(Material.STONE);
		configuration.loadFromString(configString);
		when(pluginMock.getName()).thenReturn("PluginName");
		when(languageResourceManagerMock.getSectionProvider(Section.ITEMS)).thenReturn(itemSectionProvider);

		// Act
		ItemRepository items = new YamlItemRepository(pluginMock, languageResourceManagerMock);
		boolean result = items.isItem(itemStack);

		// Assert
		assertFalse(result);

		// Verify
		verify(pluginMock, atLeastOnce()).getName();
		verify(languageResourceManagerMock, atLeastOnce()).getSectionProvider(Section.ITEMS);
	}


	@Test
	@Disabled("needs static mock server; will use createItem() method")
	void key() throws InvalidConfigurationException
	{
		// Arrange
		configuration.loadFromString(configString);
		when(pluginMock.getName()).thenReturn("PluginName");
		when(languageResourceManagerMock.getSectionProvider(Section.ITEMS)).thenReturn(itemSectionProvider);

		// Act
		ItemRepository items = new YamlItemRepository(pluginMock, languageResourceManagerMock);
		Optional<ItemStack> itemStack = items.createItem(validItemKey);
		ItemKey result;

		result = itemStack.map(items::key).orElse(null);

		// Assert
		assertNull(result);
	}


	@Test
	@Disabled("needs static mock server")
	void setItemFlags()
	{
		ItemStack itemStack = new ItemStack(Material.STONE);
		ItemMeta itemMeta = itemStack.getItemMeta();

		YamlItemRepository.setItemFlags(itemMeta);

		Set<ItemFlag> itemFlags = itemMeta.getItemFlags();

		assertTrue(itemFlags.contains(ItemFlag.HIDE_ATTRIBUTES));
		assertTrue(itemFlags.contains(ItemFlag.HIDE_ENCHANTS));
		assertTrue(itemFlags.contains(ItemFlag.HIDE_UNBREAKABLE));
	}

}
