package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.itemname;

import net.kyori.adventure.text.minimessage.MiniMessage;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class PersistentPluralNameRetrieverTest
{
	@Mock Plugin pluginMock;
	@Mock ItemStack itemStackMock;

	MiniMessage miniMessage = MiniMessage.miniMessage();


	@Test
	void retrieve()
	{
	}


	@Test
	void deserializePluralName_returns_plural_name_string()
	{
		// Arrange
		PersistentPluralNameRetriever retriever = new PersistentPluralNameRetriever(pluginMock, miniMessage);

		// Act
		String result = retriever.deserializePluralName("{QUANTITY} items", 0, miniMessage);

		// Assert
		assertEquals("0 items", result);
	}

}
