package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.itemname;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ItemDisplayNameRetrieverTest
{
	@Mock ItemStack itemStackMock;
	@Mock ItemMeta itemMetaMock;


	@Test
	void retrieve_returns_valid_display_name()
	{
		// Arrange
		when(itemStackMock.getItemMeta()).thenReturn(itemMetaMock);
		when(itemMetaMock.hasDisplayName()).thenReturn(true);
		when(itemMetaMock.getDisplayName()).thenReturn("item display name");
		ItemDisplayNameRetriever retriever = new ItemDisplayNameRetriever();

		// Act
		var result = retriever.retrieve(itemStackMock);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("item display name", result.get());

		// Verify
		verify(itemStackMock, atLeastOnce()).getItemMeta();
		verify(itemMetaMock, atLeastOnce()).hasDisplayName();
		verify(itemMetaMock, atLeastOnce()).getDisplayName();
	}
}
