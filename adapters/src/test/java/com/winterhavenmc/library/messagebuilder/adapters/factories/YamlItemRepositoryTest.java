package com.winterhavenmc.library.messagebuilder.adapters.factories;

import com.winterhavenmc.library.messagebuilder.adapters.resources.language.YamlItemRepository;

import com.winterhavenmc.library.messagebuilder.adapters.resources.language.YamlLanguageResourceManager;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ItemRepository;
import com.winterhavenmc.library.messagebuilder.models.keys.ItemKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidItemKey;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class YamlItemRepositoryTest
{
	@Mock Plugin pluginMock;
	@Mock ItemRepository itemRepositoryMock;
	@Mock
	YamlLanguageResourceManager yamlLanguageResourceManager;


	@Test
	@Disabled
	void createItem()
	{
		when(pluginMock.getName()).thenReturn("test-plugin");
		ItemRepository itemRepository = new YamlItemRepository(pluginMock, yamlLanguageResourceManager);

		ValidItemKey validItemKey = ItemKey.of("KEY").isValid().orElseThrow();
		Optional<ItemStack> result = itemRepository.createItem(validItemKey);

		assertTrue(result.isPresent());
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
	void testCreateItem2()
	{
	}

	@Test
	void testCreateItem3()
	{
	}

	@Test
	void testCreateItem4()
	{
	}

	@Test
	void isCustomItem()
	{
	}

	@Test
	void isItem()
	{
	}

	@Test
	void keyString()
	{
	}

	@Test
	void key()
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
	void record()
	{
	}

	@Test
	void testRecord()
	{
	}
}