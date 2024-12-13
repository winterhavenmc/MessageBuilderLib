/*
 * Copyright (c) 2022 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.winterhavenmc.util.TimeUnit;
import com.winterhavenmc.util.messagebuilder.macro.MacroProcessorHandler;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import org.bukkit.World;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static com.winterhavenmc.util.TimeUnit.*;
import static com.winterhavenmc.util.TimeUnit.SECONDS;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageBuilderTest {

	private ServerMock server;
	@SuppressWarnings("FieldCanBeLocal")
	private PluginMain plugin;
	private MessageBuilder<MessageId, Macro> messageBuilder;

	@BeforeEach
	public void setUp() {
		// Start the mock server
		server = MockBukkit.mock();

		// start the mock plugin
		plugin = MockBukkit.load(PluginMain.class);

		messageBuilder = new MessageBuilder<>(plugin);
	}

	@AfterEach
	public void tearDown() {
		// Stop the mock server
		MockBukkit.unmock();

		// explicitly destroy messageBuilder
		messageBuilder = null;
	}


	@Test
	void composeTest() {
		Message<MessageId, Macro> message = messageBuilder.compose(server.getConsoleSender(), MessageId.ENABLED_MESSAGE);
		assertEquals("This is an enabled message", message.toString());
	}

	@Test
	void isEnabledTest() {
		assertTrue(messageBuilder.isEnabled(MessageId.ENABLED_MESSAGE));
		assertFalse(messageBuilder.isEnabled(MessageId.DISABLED_MESSAGE));
	}

	@Test
	void getRepeatDelayTest() {
		assertEquals(10, messageBuilder.getRepeatDelay(MessageId.REPEAT_DELAYED_MESSAGE));
	}

	@Test
	void getMessageTest() {
		assertEquals("This is an enabled message", messageBuilder.getMessage(MessageId.ENABLED_MESSAGE));
	}

	@Test
	void getItemNameTest() {
		assertEquals("§aTest Item", messageBuilder.getItemName().orElse("fail"));
	}

	@Test
	void getItemNamePluralTest() {
		assertEquals("§aTest Items", messageBuilder.getItemNamePlural().orElse("fail"));
	}

	@Test
	void getInventoryItemNameTest() {
		assertEquals("§aInventory Item", messageBuilder.getInventoryItemName().orElse("fail"));
	}

	@Test
	void getItemLoreTest() {
		assertEquals(List.of("§elore line 1", "§elore line 2"), messageBuilder.getItemLore());
	}

	@Test
	void getSpawnDisplayNameTest() {
		assertEquals("§aSpawn", messageBuilder.getSpawnDisplayName().orElse("fail"));
	}

	@Test
	void getHomeDisplayNameTest() {
		assertEquals("§aHome", messageBuilder.getHomeDisplayName().orElse("fail"));
	}

	@Test
	void getTimeStringTest_with_singular_units() {
		long duration = DAYS.toMillis(1) + HOURS.toMillis(1) + MINUTES.toMillis(1) + SECONDS.toMillis(1);
		assertEquals("1 day 1 hour 1 minute 1 second", messageBuilder.getTimeString(duration));
	}

	@Test
	void getTimeStringTest_with_plural_units() {
		long duration = DAYS.toMillis(2) + HOURS.toMillis(2) + MINUTES.toMillis(2) + SECONDS.toMillis(2);
		assertEquals("2 days 2 hours 2 minutes 2 seconds", messageBuilder.getTimeString(duration));
	}

	@Test
	void getTimeStringTest_with_unlimited_time() {
		assertEquals("unlimited time", messageBuilder.getTimeString(-1));
	}

	@Test
	void getTimeStringTest_two_parameter_with_singular_units() {
		long duration = DAYS.toMillis(1) + HOURS.toMillis(1) + MINUTES.toMillis(1) + SECONDS.toMillis(1);
		assertEquals("1 day 1 hour 1 minute 1 second", messageBuilder.getTimeString(duration, TimeUnit.SECONDS));
	}

	@Test
	void getStringTest() {
		assertEquals("an arbitrary string", messageBuilder.getString("ARBITRARY_STRING").orElse("fail"));
	}

	@Test
	void getStringListTest() {
		assertTrue(messageBuilder.getStringList("ARBITRARY_STRING_LIST").containsAll(List.of("item 1", "item 2", "item 3")));
	}

	@Test
	void setDelimitersTest_same() {
		messageBuilder.setDelimiters('#');
		assertEquals('#', MacroProcessorHandler.MacroDelimiter.LEFT.toChar());
		assertEquals('#', MacroProcessorHandler.MacroDelimiter.RIGHT.toChar());
		// these must be reset back manually or they persist across tests. nested enum superpower perhaps?
		messageBuilder.setDelimiters('%');
		assertEquals('%', MacroProcessorHandler.MacroDelimiter.LEFT.toChar());
		assertEquals('%', MacroProcessorHandler.MacroDelimiter.RIGHT.toChar());
	}

	@Test
	void setDelimitersTest_different() {
		messageBuilder.setDelimiters('L','R');
		assertEquals('L', MacroProcessorHandler.MacroDelimiter.LEFT.toChar());
		assertEquals('R', MacroProcessorHandler.MacroDelimiter.RIGHT.toChar());
		// these must be reset manually or they persist across tests. nested enum superpower perhaps?
		messageBuilder.setDelimiters('%');
		assertEquals('%', MacroProcessorHandler.MacroDelimiter.LEFT.toChar());
		assertEquals('%', MacroProcessorHandler.MacroDelimiter.RIGHT.toChar());
	}

	@Test
	void getWorldNameTest_mockito() {
		World world = mock(World.class);
		when(world.getName()).thenReturn("world");

		assertNotNull(world, "Mock world 'world' is null.");
		assertTrue(messageBuilder.getWorldName(world).isPresent(), "Returned value is an empty Optional.");
		assertEquals(Optional.of("world"), messageBuilder.getWorldName(world), "Returned world name is not 'world'.");
	}

	@Test
	void reloadTest() {
		messageBuilder.reload();
		assertEquals("This is an enabled message", messageBuilder.getMessage(MessageId.ENABLED_MESSAGE));
	}

}
