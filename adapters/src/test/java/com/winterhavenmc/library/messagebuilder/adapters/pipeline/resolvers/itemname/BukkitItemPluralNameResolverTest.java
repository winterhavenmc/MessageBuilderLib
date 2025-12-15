package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.itemname;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.itemname.ItemDisplayNameRetriever;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.itemname.ItemNameRetriever;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.itemname.PersistentPluralNameRetriever;
import com.winterhavenmc.library.messagebuilder.core.context.NameResolverCtx;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ItemRepository;

import org.bukkit.inventory.ItemStack;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BukkitItemPluralNameResolverTest
{
	@Mock ItemRepository itemRepositoryMock;
	@Mock ItemNameRetriever itemNameRetrieverMock;
	@Mock ItemDisplayNameRetriever itemDisplayNameRetrieverMock;
	@Mock PersistentPluralNameRetriever itemPluralNameRetrieverMock;
	@Mock ItemStack itemStackMock;


	@Test
	void resolve_with_null_parameter_returns_empty_string()
	{
		// Arrange
		NameResolverCtx nameResolverCtx = new NameResolverCtx(itemNameRetrieverMock, itemDisplayNameRetrieverMock, itemPluralNameRetrieverMock);
		BukkitItemPluralNameResolver resolver = new BukkitItemPluralNameResolver(nameResolverCtx);

		// Act
		String result = resolver.resolve(null);

		// Assert
		assertEquals("", result);
	}


	@Test
	void resolve_returns_valid_plural_name()
	{
		// Arrange
		NameResolverCtx nameResolverCtx = new NameResolverCtx(itemNameRetrieverMock, itemDisplayNameRetrieverMock, itemPluralNameRetrieverMock);
		BukkitItemPluralNameResolver resolver = new BukkitItemPluralNameResolver(nameResolverCtx);
		when(itemStackMock.hasItemMeta()).thenReturn(true);
		when(itemPluralNameRetrieverMock.retrieve(itemStackMock)).thenReturn(Optional.of("item plural name"));

		// Act
		String result = resolver.resolve(itemStackMock);

		// Assert
		assertEquals("item plural name", result);

		// Verify
		verify(itemStackMock, atLeastOnce()).hasItemMeta();
		verify(itemPluralNameRetrieverMock, atLeastOnce()).retrieve(itemStackMock);
	}


	@Test
	void resolve_returns_valid_display_name_as_fallback()
	{
		// Arrange
		NameResolverCtx nameResolverCtx = new NameResolverCtx(itemNameRetrieverMock, itemDisplayNameRetrieverMock, itemPluralNameRetrieverMock);
		BukkitItemPluralNameResolver resolver = new BukkitItemPluralNameResolver(nameResolverCtx);
		when(itemStackMock.hasItemMeta()).thenReturn(true);
		when(itemPluralNameRetrieverMock.retrieve(itemStackMock)).thenReturn(Optional.empty());
		when(itemDisplayNameRetrieverMock.retrieve(itemStackMock)).thenReturn(Optional.of("item display name"));

		// Act
		String result = resolver.resolve(itemStackMock);

		// Assert
		assertEquals("item display name", result);

		// Verify
		verify(itemStackMock, atLeastOnce()).hasItemMeta();
		verify(itemPluralNameRetrieverMock, atLeastOnce()).retrieve(itemStackMock);
		verify(itemDisplayNameRetrieverMock, atLeastOnce()).retrieve(itemStackMock);
	}


	@Test
	void resolve_returns_valid_item_name_as_fallback()
	{
		// Arrange
		NameResolverCtx nameResolverCtx = new NameResolverCtx(itemNameRetrieverMock, itemDisplayNameRetrieverMock, itemPluralNameRetrieverMock);
		BukkitItemPluralNameResolver resolver = new BukkitItemPluralNameResolver(nameResolverCtx);
		when(itemStackMock.hasItemMeta()).thenReturn(true);
		when(itemPluralNameRetrieverMock.retrieve(itemStackMock)).thenReturn(Optional.empty());
		when(itemDisplayNameRetrieverMock.retrieve(itemStackMock)).thenReturn(Optional.empty());
		when(itemNameRetrieverMock.retrieve(itemStackMock)).thenReturn(Optional.of("item name"));

		// Act
		String result = resolver.resolve(itemStackMock);

		// Assert
		assertEquals("item name", result);

		// Verify
		verify(itemStackMock, atLeastOnce()).hasItemMeta();
		verify(itemPluralNameRetrieverMock, atLeastOnce()).retrieve(itemStackMock);
		verify(itemDisplayNameRetrieverMock, atLeastOnce()).retrieve(itemStackMock);
		verify(itemNameRetrieverMock, atLeastOnce()).retrieve(itemStackMock);
	}

}
