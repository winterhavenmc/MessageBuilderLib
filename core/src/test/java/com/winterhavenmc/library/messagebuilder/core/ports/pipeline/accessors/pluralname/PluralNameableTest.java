/*
 * Copyright (c) 2025 Tim Savage.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.pluralname;

import com.winterhavenmc.library.messagebuilder.core.context.AccessorCtx;
import com.winterhavenmc.library.messagebuilder.core.context.FormatterCtx;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor;
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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;


@ExtendWith(MockitoExtension.class)
class PluralNameableTest
{
	@Mock NumberFormatter numberFormatterMock;
	@Mock ConfigRepository configRepositoryMock;
	@Mock DurationFormatter durationFormatterMock;
	@Mock WorldNameResolver worldNameResolverMock;
	@Mock ItemNameResolver itemNameResolverMock;
	@Mock ItemDisplayNameResolver itemDisplayNameResolverMock;
	@Mock ItemPluralNameResolver itemPluralNameResolver;

	static class TestObject implements PluralNameable
	{
		@Override
		public String getPluralName()
		{
			return "plural name";
		}
	}


	@Test
	void object_is_instance_of_PluralNameable()
	{
		// Arrange & Act
		TestObject testObject = new TestObject();

		// Assert
		assertInstanceOf(PluralNameable.class, testObject);
	}


	@Test
	void getUniqueId_returns_UUID()
	{
		// Arrange
		TestObject testObject = new TestObject();

		// Act
		String result = testObject.getPluralName();

		// Assert
		assertEquals("plural name", result);
	}


	@Test
	void extractPluralName_returns_populated_map()
	{
		// Arrange
		ValidMacroKey baseKey = MacroKey.of("TEST").isValid().orElseThrow();
		ValidMacroKey subKey = baseKey.append(Accessor.BuiltIn.PLURAL_NAME).isValid().orElseThrow();
		TestObject testObject = new TestObject();
		FormatterCtx formatterCtx = new FormatterCtx(configRepositoryMock, durationFormatterMock, numberFormatterMock, MiniMessage.miniMessage());
		AccessorCtx accessorCtx = new AccessorCtx(worldNameResolverMock, itemNameResolverMock,
				itemDisplayNameResolverMock, itemPluralNameResolver, formatterCtx);

		// Act
		MacroStringMap result = testObject.extractPluralName(baseKey, accessorCtx);

		// Assert
		assertEquals("plural name", result.get(subKey));
	}


	@Test
	void formatPluralName_returns_optional_string()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		Optional<String> result = PluralNameable.formatPluralName("plural name");

		// Assert
		assertEquals(Optional.of("plural name"), result);
	}


	@Test
	void formatPluralName_with_null_parameter_returns_empty_optional()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		Optional<String> result = PluralNameable.formatPluralName(null);

		// Assert
		assertEquals(Optional.empty(), result);
	}

}
