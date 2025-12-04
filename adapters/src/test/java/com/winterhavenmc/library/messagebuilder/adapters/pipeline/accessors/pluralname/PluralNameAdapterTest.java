package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.pluralname;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.itemname.BukkitItemPluralNameResolver;
import com.winterhavenmc.library.messagebuilder.core.context.AccessorCtx;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.pluralname.PluralNameable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PluralNameAdapterTest
{
	@Mock AccessorCtx accessorCtxMock;
	@Mock BukkitItemPluralNameResolver itemPluralNameResolverMock;
	@Mock ItemStack itemStackMock;


	@Test @DisplayName("adapt with valid ItemStack")
	void getPluralName_with_valid_ItemStack()
	{
		// Arrange
		when(accessorCtxMock.itemPluralNameResolver()).thenReturn(itemPluralNameResolverMock);
		when(itemPluralNameResolverMock.resolve(itemStackMock)).thenReturn("Resolved ItemStack Plural Name");

		// Act
		Optional<PluralNameable> adapter = new PluralNameAdapter(accessorCtxMock).adapt(itemStackMock);
		Optional<String> pluralName = adapter.map(PluralNameable::getPluralName);

		// Assert
		assertTrue(pluralName.isPresent());
		assertEquals("Resolved ItemStack Plural Name", pluralName.get(), "The adapter should return the displayName from the ItemStack.");
	}


	@Test @DisplayName("adapt with valid custom ItemStack")
	void getPluralName_with_ItemStack()
	{
		// Arrange
		ItemStack itemStack = new ItemStack(Material.STONE);
		when(accessorCtxMock.itemPluralNameResolver()).thenReturn(itemPluralNameResolverMock);
		when(itemPluralNameResolverMock.resolve(itemStack)).thenReturn("Resolved ItemStack Plural Name");

		// Act
		Optional<PluralNameable> adapter = new PluralNameAdapter(accessorCtxMock).adapt(itemStack);
		Optional<String> pluralName = adapter.map(PluralNameable::getPluralName);

		// Assert
		assertTrue(pluralName.isPresent());
		assertEquals("Resolved ItemStack Plural Name", pluralName.get(), "The adapter should return the displayName from the ItemStack.");
	}


	@Test
	void getPluralName_returns_string_given_implementation()
	{
		// Arrange
		class TestObject implements PluralNameable
		{
			public String getPluralName()
			{
				return "Test Objects";
			}
		}

		// Act
		Optional<PluralNameable> adapter = new PluralNameAdapter(accessorCtxMock).adapt(new TestObject());
		Optional<String> pluralName = adapter.map(PluralNameable::getPluralName);

		// Assert
		assertTrue(pluralName.isPresent());
		assertEquals("Test Objects", pluralName.get());
	}


	@Test @DisplayName("adapt with null parameter")
	void getPluralName_with_regular_ItemStack()
	{
		// Arrange & Act
		Optional<PluralNameable> adapter = new PluralNameAdapter(accessorCtxMock).adapt(null);
		Optional<String> pluralName = adapter.map(PluralNameable::getPluralName);

		// Assert
		assertTrue(pluralName.isEmpty());
	}

}
