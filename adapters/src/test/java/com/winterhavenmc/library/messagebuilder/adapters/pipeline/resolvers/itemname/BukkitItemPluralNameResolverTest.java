package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.itemname;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname.ItemPluralNameResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ItemRepository;
import com.winterhavenmc.library.messagebuilder.models.keys.ItemKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidItemKey;
import com.winterhavenmc.library.messagebuilder.models.language.item.ItemRecord;

import com.winterhavenmc.library.messagebuilder.models.language.item.ValidItemRecord;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BukkitItemPluralNameResolverTest
{
	@Mock ItemRepository itemRepositoryMock;
	@Mock ItemStack itemStackMock;
	@Mock ItemMeta itemMetaMock;
	@Mock PersistentDataContainer persistentDataContainerMock;
	@Mock Plugin pluginMock;

	ValidItemKey validItemKey;
	FileConfiguration configuration;
	ValidItemRecord validItemRecord;

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
	}


//	@Test
//	void resolve_with_null_parameter_returns_empty_string()
//	{
//		// Arrange
//		ItemPluralNameResolver resolver = new BukkitItemPluralNameResolver(itemRepositoryMock, MiniMessage.miniMessage());
//
//		// Act
//		String result = resolver.resolve(null);
//
//		// Assert
//		assertEquals("", result);
//	}


	//TODO: this test is not finished
	@Test
	@Disabled
	void resolve_with_quantity_1_item_stack_returns_singular_name() throws InvalidConfigurationException
	{
		// Arrange
		when(pluginMock.getName()).thenReturn("test-plugin");

		NamespacedKey namespacedKey = new NamespacedKey(pluginMock, "ITEM_KEY");

		FileConfiguration languageConfig = new YamlConfiguration();
		languageConfig.loadFromString(configString);

		ConfigurationSection itemEntry = languageConfig.getConfigurationSection("ITEMS.TEST_ITEM");

		ItemRecord itemRecord = ItemRecord.of(validItemKey, itemEntry);

		ItemPluralNameResolver resolver = new BukkitItemPluralNameResolver(pluginMock, MiniMessage.miniMessage());

		when(itemStackMock.hasItemMeta()).thenReturn(true);
		when(itemStackMock.getItemMeta()).thenReturn(itemMetaMock);
		when(itemMetaMock.getPersistentDataContainer()).thenReturn(persistentDataContainerMock);
		// when(itemStackMock.getAmount()).thenReturn(1);
		// when(persistentDataContainerMock.has(namespacedKey)).thenReturn(true);
		// when(itemRecordRepositoryMock.getItemRecord(itemKey)).thenReturn(itemRecord);


		// Act
		String result = resolver.resolve(itemStackMock);

		// Assert
		assertEquals("one item", result);
	}


//	@Test
//	void resolveName_returns_optional_item_name()
//	{
//		// Arrange
//		when(itemStackMock.getItemMeta()).thenReturn(itemMetaMock);
//		when(itemMetaMock.hasItemName()).thenReturn(true);
//		when(itemMetaMock.getItemName()).thenReturn("item name");
//		BukkitItemPluralNameResolver resolver = new BukkitItemPluralNameResolver(itemRepositoryMock, MiniMessage.miniMessage());
//
//		// Act
//		String result = resolver.resolve(itemStackMock);
//
//		// Assert
//		assertNotNull(result);
//		assertEquals("item name", result);
//
//		// Verify
//		verify(itemStackMock, atLeastOnce()).getItemMeta();
//		verify(itemMetaMock, atLeastOnce()).hasItemName();
//		verify(itemMetaMock, atLeastOnce()).getItemName();
//	}


//	@Test
//	void resolveName_returns_empty_optional_given_no_meta_item()
//	{
//		// Arrange
//		when(itemStackMock.getItemMeta()).thenReturn(itemMetaMock);
//		BukkitItemPluralNameResolver resolver = new BukkitItemPluralNameResolver(itemRepositoryMock, MiniMessage.miniMessage());
//
//		// Act
//		String result = resolver.resolve(itemStackMock);
//
//		// Assert
//		assertNotNull(result);
//		assertEquals("", result);
//	}


//	@Test
//	void resolveDisplayName_returns_optional_item_name()
//	{
//		// Arrange
//		when(itemStackMock.getItemMeta()).thenReturn(itemMetaMock);
//		when(itemMetaMock.hasDisplayName()).thenReturn(true);
//		when(itemMetaMock.getDisplayName()).thenReturn("item display name");
//		BukkitItemPluralNameResolver resolver = new BukkitItemPluralNameResolver(itemRepositoryMock, MiniMessage.miniMessage());
//
//		// Act
//		String result = resolver.resolve(itemStackMock);
//
//		// Assert
//		assertNotNull(result);
//		assertEquals("item display name", result);
//
//		// Verify
//		verify(itemStackMock, atLeastOnce()).getItemMeta();
//		verify(itemMetaMock, atLeastOnce()).hasDisplayName();
//		verify(itemMetaMock, atLeastOnce()).getDisplayName();
//	}


//	@Test
//	void resolveDisplayName_returns_empty_optional_given_no_meta_item()
//	{
//		// Arrange
//		when(itemStackMock.getItemMeta()).thenReturn(itemMetaMock);
//		BukkitItemPluralNameResolver resolver = new BukkitItemPluralNameResolver(itemRepositoryMock, MiniMessage.miniMessage());
//
//		// Act
//		String result = resolver.resolve(itemStackMock);
//
//		// Assert
//		assertTrue(result.isEmpty());
//	}


//	@Test
//	void resolvePluralName_returns_empty_optional_given_non_custom_item()
//	{
//		// Arrange & Act
//		BukkitItemPluralNameResolver resolver = new BukkitItemPluralNameResolver(itemRepositoryMock);
//		Optional<String> result = resolver.resolvePluralName(itemStackMock, MiniMessage.miniMessage());
//
//		// Assert
//		assertTrue(result.isEmpty());
//	}


//	@Test
//	void resolvePluralName_returns_valid_plural_name_given_custom_item()
//	{
//		// Arrange
//		when(itemRepositoryMock.isItem(itemStackMock)).thenReturn(true);
//		when(itemRepositoryMock.key(itemStackMock)).thenReturn(validItemKey);
//		when(itemRepositoryMock.getRecord(validItemKey)).thenReturn(validItemRecord);
//
//		// Act
//		BukkitItemPluralNameResolver resolver = new BukkitItemPluralNameResolver(itemRepositoryMock);
//		Optional<String> result = resolver.resolvePluralName(itemStackMock, MiniMessage.miniMessage());
//
//		// Assert
//		assertTrue(result.isPresent());
//
//		// Verify
//		verify(itemRepositoryMock, atLeastOnce()).isItem(itemStackMock);
//		verify(itemRepositoryMock, atLeastOnce()).key(itemStackMock);
//		verify(itemRepositoryMock, atLeastOnce()).getRecord(validItemKey);
//	}

}
