package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.itemname;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ItemNameRetrieverTest
{
	@Mock ItemStack itemStackMock;
	@Mock ItemMeta itemMetaMock;


	@Test
	void retrieve_returns_valid_name()
	{
		// Arrange
		when(itemStackMock.getItemMeta()).thenReturn(itemMetaMock);
		when(itemMetaMock.hasItemName()).thenReturn(true);
		when(itemMetaMock.getItemName()).thenReturn("item name");
		ItemNameRetriever retriever = new ItemNameRetriever();

		// Act
		var result = retriever.retrieve(itemStackMock);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("item name", result.get());

		// Verify
		verify(itemStackMock, atLeastOnce()).getItemMeta();
		verify(itemMetaMock, atLeastOnce()).hasItemName();
		verify(itemMetaMock, atLeastOnce()).getItemName();
	}
}
