package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.version;

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

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class VersionableTest
{
	@Mock NumberFormatter numberFormatterMock;
	@Mock ConfigRepository configRepositoryMock;
	@Mock DurationFormatter durationFormatterMock;
	@Mock WorldNameResolver worldNameResolverMock;
	@Mock ItemNameResolver itemNameResolverMock;
	@Mock ItemDisplayNameResolver itemDisplayNameResolverMock;
	@Mock ItemPluralNameResolver itemPluralNameResolverMock;


	static class TestObject implements Versionable
	{
		String string;

		TestObject(final String string)
		{
			this.string = string;
		}

		@Override
		public String getVersion()
		{
			return this.string;
		}
	}


	@Test
	void getVersion_returns_version_string()
	{
		// Arrange
		TestObject testObject = new TestObject("2.0");

		// Act
		String result = testObject.getVersion();

		// Assert
		assertEquals("2.0", result);
	}


	@Test
	void extractVersion_returns_valid_string()
	{
		// Arrange
		ValidMacroKey baseKey = MacroKey.of("TEST").isValid().orElseThrow();
		ValidMacroKey subKey = baseKey.append("VERSION").isValid().orElseThrow();
		TestObject testObject = new TestObject("2.0");
		FormatterCtx formatterCtx = new FormatterCtx(configRepositoryMock, durationFormatterMock, numberFormatterMock, MiniMessage.miniMessage());
		AccessorCtx accessorCtx = new AccessorCtx(worldNameResolverMock, itemNameResolverMock, itemDisplayNameResolverMock,
				itemPluralNameResolverMock, formatterCtx);

		// Act
		MacroStringMap result = testObject.extractVersion(baseKey, accessorCtx);

		// Assert
		assertEquals("2.0", result.get(subKey));
	}


	@Test
	void formatVersion_returns_optional_string()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject("2.0");

		// Act
		Optional<String> result = Versionable.formatVersion(testObject.getVersion());

		// Assert
		assertTrue(result.isPresent());
		assertEquals("2.0", result.get());
	}


	@Test
	void formatVersion_returns_empty_optional_given_null_version_string()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject(null);

		// Act
		Optional<String> result = Versionable.formatVersion(testObject.getVersion());

		// Assert
		assertTrue(result.isEmpty());
	}

}
