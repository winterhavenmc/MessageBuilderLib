package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.itemname;

import com.winterhavenmc.library.messagebuilder.core.ports.resolvers.itemname.ItemPluralNameResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.resourcemanagers.language.ItemRepository;
import com.winterhavenmc.library.messagebuilder.models.keys.ItemKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidItemKey;
import com.winterhavenmc.library.messagebuilder.models.language.ItemRecord;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BukkitItemPluralNameResolverTest
{
	@Mock ItemRepository itemRepositoryMock;
	@Mock ItemStack itemStackMock;
	@Mock ItemMeta itemMetaMock;
	@Mock PersistentDataContainer persistentDataContainerMock;
	@Mock Plugin pluginMock;

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
	void resolve_with_null_parameter_returns_empty_string()
	{
		// Arrange
		ItemPluralNameResolver resolver = new BukkitItemPluralNameResolver(itemRepositoryMock);

		// Act
		String result = resolver.resolve(null);

		// Assert
		assertEquals("", result);
	}


	//TODO: this test is not finished
	@Test
	@Disabled
	void resolve_with_quantity_1_item_stack_returns_singular_name() throws InvalidConfigurationException
	{
		// Arrange
		ValidItemKey itemKey = ItemKey.of("TEST_ITEM").isValid().orElseThrow();

		when(pluginMock.getName()).thenReturn("test-plugin");

		NamespacedKey namespacedKey = new NamespacedKey(pluginMock, "ITEM_KEY");

		FileConfiguration languageConfig = new YamlConfiguration();
		languageConfig.loadFromString(configString);

		ConfigurationSection itemEntry = languageConfig.getConfigurationSection("ITEMS.TEST_ITEM");

		ItemRecord itemRecord = ItemRecord.of(itemKey, itemEntry);

		ItemPluralNameResolver resolver = new BukkitItemPluralNameResolver(itemRepositoryMock);

		when(itemStackMock.hasItemMeta()).thenReturn(true);
		when(itemStackMock.getItemMeta()).thenReturn(itemMetaMock);
		when(itemMetaMock.getPersistentDataContainer()).thenReturn(persistentDataContainerMock);
		// when(itemStackMock.getAmount()).thenReturn(1);
		// when(persistentDataContainerMock.has(namespacedKey)).thenReturn(true);
		// when(itemRepositoryMock.getItemRecord(itemKey)).thenReturn(itemRecord);


		// Act
		String result = resolver.resolve(itemStackMock);

		// Assert
		assertEquals("one item", result);
	}

}
