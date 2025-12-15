package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.itemname;

import net.kyori.adventure.text.minimessage.MiniMessage;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PersistentPluralNameRetrieverTest
{
	@Mock Plugin pluginMock;
	@Mock ItemStack itemStackMock;
	@Mock ItemMeta itemMetaMock;
	@Mock PersistentDataContainer persistentDataContainerMock;

	MiniMessage miniMessage = MiniMessage.miniMessage();


	@Test
	void retrieve_returns_empty_optional_given_ItemStack_with_no_metadata()
	{
		// Arrange
		PersistentPluralNameRetriever retriever = new PersistentPluralNameRetriever(pluginMock, miniMessage);

		// Act
		Optional<String> result = retriever.retrieve(itemStackMock);

		// Assert
		assertTrue(result.isEmpty());
	}


	@Test
	void retrieve_returns_empty_optional_given_ItemStack_with_no_persisted_plural_name()
	{
		// Arrange
		when(pluginMock.getName()).thenReturn("test-plugin");
		when(itemStackMock.getItemMeta()).thenReturn(itemMetaMock);
		when(itemMetaMock.getPersistentDataContainer()).thenReturn(persistentDataContainerMock);

		PersistentPluralNameRetriever retriever = new PersistentPluralNameRetriever(pluginMock, miniMessage);

		// Act
		Optional<String> result = retriever.retrieve(itemStackMock);

		// Assert
		assertTrue(result.isEmpty());

		// Verify
		verify(pluginMock, atLeastOnce()).getName();
		verify(itemStackMock, atLeastOnce()).getItemMeta();
		verify(itemMetaMock, atLeastOnce()).getPersistentDataContainer();
	}


	@Test
	void retrieve_returns_optional_string_given_ItemStack_with_persisted_plural_name()
	{
		// Arrange
		when(pluginMock.getName()).thenReturn("test-plugin");
		NamespacedKey namespacedKey = new NamespacedKey(pluginMock, "PLURAL_NAME");

		when(itemStackMock.getItemMeta()).thenReturn(itemMetaMock);
		when(itemMetaMock.getPersistentDataContainer()).thenReturn(persistentDataContainerMock);
		when(persistentDataContainerMock.has(namespacedKey, PersistentDataType.STRING)).thenReturn(true);
		when(persistentDataContainerMock.get(namespacedKey, PersistentDataType.STRING)).thenReturn("<choice:'0#zero items|1#one item|1<{QUANTITY} items'>");

		PersistentPluralNameRetriever retriever = new PersistentPluralNameRetriever(pluginMock, miniMessage);

		// Act
		Optional<String> result = retriever.retrieve(itemStackMock);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("<choice:'0#zero items|1#one item|1<{QUANTITY} items'>", result.get());

		// Verify
		verify(pluginMock, atLeastOnce()).getName();
		verify(itemStackMock, atLeastOnce()).getItemMeta();
		verify(itemMetaMock, atLeastOnce()).getPersistentDataContainer();
		verify(persistentDataContainerMock, atLeastOnce()).has(namespacedKey, PersistentDataType.STRING);
		verify(persistentDataContainerMock, atLeastOnce()).get(namespacedKey, PersistentDataType.STRING);
	}


	@Test
	void deserializePluralName_returns_plural_name_string()
	{
		// Arrange
		PersistentPluralNameRetriever retriever = new PersistentPluralNameRetriever(pluginMock, miniMessage);

		// Act
		String result = retriever.deserializePluralName("<choice:'0#zero items|1#one item|1<{QUANTITY} items'>", 0);

		// Assert
		assertEquals("zero items", result);
	}

}
