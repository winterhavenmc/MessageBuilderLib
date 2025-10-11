package com.winterhavenmc.library.messagebuilder.core.util;

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
class ItemForgeTest
{
	@Mock Plugin pluginMock;
	@Mock ItemRepository itemRepositoryMock;


	@Test
	@Disabled
	void createItem()
	{
		when(pluginMock.getName()).thenReturn("test-plugin");
		ItemForge itemForge = new ItemForge(pluginMock, itemRepositoryMock);

		ValidItemKey validItemKey = ItemKey.of("KEY").isValid().orElseThrow();
		Optional<ItemStack> result = itemForge.createItem(validItemKey);

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
	void getItemKeyString()
	{
	}

	@Test
	void getItemKey()
	{
	}

	@Test
	void getItemName()
	{
	}

	@Test
	void getItemDisplayName()
	{
	}

	@Test
	void getItemRecord()
	{
	}

	@Test
	void testGetItemRecord()
	{
	}
}