package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.url;

import com.winterhavenmc.library.messagebuilder.core.context.AccessorCtx;
import com.winterhavenmc.library.messagebuilder.core.context.FormatterCtx;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.formatters.duration.DurationFormatter;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.formatters.number.NumberFormatter;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname.ItemDisplayNameResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname.ItemNameResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname.ItemPluralNameResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameResolver;
import com.winterhavenmc.library.messagebuilder.models.configuration.ConfigRepository;
import com.winterhavenmc.library.messagebuilder.models.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
class UrlAddressableTest
{
	@Mock NumberFormatter numberFormatterMock;
	@Mock ConfigRepository configRepositoryMock;
	@Mock DurationFormatter durationFormatterMock;
	@Mock WorldNameResolver worldNameResolverMock;
	@Mock ItemNameResolver itemNameResolverMock;
	@Mock ItemDisplayNameResolver itemDisplayNameResolverMock;
	@Mock ItemPluralNameResolver itemPluralNameResolverMock;


	static class TestObject implements UrlAddressable
	{
		String string;

		TestObject(final String string)
		{
			this.string = string;
		}

		@Override
		public String getUrl()
		{
			return this.string;
		}
	}


	@Test
	void getUri()
	{
		// Arrange
		TestObject testObject = new TestObject("https://example.com");

		// Act
		String result = testObject.getUrl();

		// Assert
		assertEquals("https://example.com", result);
	}


	@Test
	void extractUri_returns_populated_map()
	{
		// Arrange
		ValidMacroKey baseKey = MacroKey.of("TEST").isValid().orElseThrow();
		ValidMacroKey subKey = baseKey.append("URL").isValid().orElseThrow();
		TestObject testObject = new TestObject("https://example.com");
		FormatterCtx formatterCtx = new FormatterCtx(configRepositoryMock, durationFormatterMock, numberFormatterMock, MiniMessage.miniMessage());
		AccessorCtx accessorCtx = new AccessorCtx(worldNameResolverMock, itemNameResolverMock, itemDisplayNameResolverMock,
				itemPluralNameResolverMock, formatterCtx);

		// Act
		MacroStringMap result = testObject.extractUrl(baseKey, accessorCtx);

		// Assert
		assertEquals("https://example.com", result.get(subKey));
	}


	@Test
	void formatUri_returns_optional_string()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject("https://example.com");

		// Act
		Optional<String> result = UrlAddressable.formatUrl(testObject.getUrl());

		// Assert
		assertTrue(result.isPresent());
		assertEquals("https://example.com", result.get());
	}


	@Test
	void formatUri_returns_empty_optional_given_null_uri_string()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject(null);

		// Act
		Optional<String> result = UrlAddressable.formatUrl(testObject.getUrl());

		// Assert
		assertTrue(result.isEmpty());
	}


	@Test
	void formatUri_returns_optional_string_given_invalid_uri_string()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject("not a valid url");

		// Act
		Optional<String> result = UrlAddressable.formatUrl(testObject.getUrl());

		// Assert
		assertTrue(result.isPresent());
		assertEquals("not a valid url", result.get());
	}

}
