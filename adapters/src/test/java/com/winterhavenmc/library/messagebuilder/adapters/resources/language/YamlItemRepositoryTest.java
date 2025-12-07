package com.winterhavenmc.library.messagebuilder.adapters.resources.language;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.SectionProvider;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ItemRepository;
import com.winterhavenmc.library.messagebuilder.models.keys.ItemKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidItemKey;
import com.winterhavenmc.library.messagebuilder.models.language.Section;
import com.winterhavenmc.library.messagebuilder.models.language.item.ItemRecord;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class YamlItemRepositoryTest
{
	@Mock Plugin pluginMock;
	@Mock YamlLanguageResourceManager languageResourceManagerMock;
	@Mock SectionProvider sectionProviderMock;

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

	@Test
	void getRecord()
	{
		// Arrange
		ValidItemKey validItemKey = ItemKey.of("TEST").isValid().orElseThrow();

		when(pluginMock.getName()).thenReturn("PluginName");
		when(languageResourceManagerMock.getSectionProvider(Section.ITEMS.name())).thenReturn(sectionProviderMock);

		ItemRepository items = new YamlItemRepository(pluginMock, languageResourceManagerMock);

		// Act
		ItemRecord result = items.getRecord(validItemKey);

		// Assert
		assertInstanceOf(ItemRecord.class, result);

		// Verify
		verify(pluginMock, atLeastOnce()).getName();
		verify(languageResourceManagerMock, atLeastOnce()).getSectionProvider(Section.ITEMS.name());
	}


	@Test
	@Disabled
	void createItem() throws InvalidConfigurationException
	{
		// Arrange
		FileConfiguration configuration = new YamlConfiguration();
		configuration.loadFromString(configString);

		ValidItemKey validItemKey = ItemKey.of("TEST_ITEM").isValid().orElseThrow();

		when(pluginMock.getName()).thenReturn("PluginName");

		when(languageResourceManagerMock.getSectionProvider(Section.ITEMS.name())).thenReturn(sectionProviderMock);
		when(sectionProviderMock.getSection()).thenReturn(configuration.getConfigurationSection(Section.ITEMS.name()));

		ItemRepository items = new YamlItemRepository(pluginMock, languageResourceManagerMock);

		// Act
		Optional<ItemStack> item = items.createItem(validItemKey);

		// Assert
		assertTrue(item.isPresent());

		// Verify
		verify(pluginMock, atLeastOnce()).getName();
	}


	@Test
	void testCreateItem()
	{
	}


	@Test
	void testCreateItem1()
	{
	}


	@Test
	void name()
	{
	}


	@Test
	void displayName()
	{
	}


	@Test
	void getRecordOpt()
	{
	}


	@Test
	void isItem()
	{
	}


	@Test
	void getItemKeyString()
	{
	}


	@Test
	void key()
	{
	}
}