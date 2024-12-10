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
import org.bukkit.World;
import org.junit.jupiter.api.*;

import java.util.List;

import static com.winterhavenmc.util.TimeUnit.*;
import static com.winterhavenmc.util.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageBuilderTest {

	@SuppressWarnings({"FieldCanBeLocal", "unused"})
	private ServerMock server;
	private PluginMain plugin;

	@BeforeEach
	public void setUp() {
		// Start the mock server
		server = MockBukkit.mock();

		// start the mock plugin
		plugin = MockBukkit.load(PluginMain.class);
	}

	@AfterEach
	public void tearDown() {
		// Stop the mock server
		MockBukkit.unmock();
	}


	@Test
	void compose() {
		MessageBuilder<MessageId, Macro> messageBuilder = new MessageBuilder<>(plugin);
		Message<MessageId, Macro> message = messageBuilder.compose(server.getConsoleSender(), MessageId.ENABLED_MESSAGE);
		assertEquals("This is an enabled message", message.toString());
	}

	@Test
	void isEnabled() {
		MessageBuilder<MessageId, Macro> messageBuilder = new MessageBuilder<>(plugin);
		assertTrue(messageBuilder.isEnabled(MessageId.ENABLED_MESSAGE));
		assertFalse(messageBuilder.isEnabled(MessageId.DISABLED_MESSAGE));
	}

	@Test
	void getRepeatDelay() {
		MessageBuilder<MessageId, Macro> messageBuilder = new MessageBuilder<>(plugin);
		assertEquals(10, messageBuilder.getRepeatDelay(MessageId.REPEAT_DELAYED_MESSAGE));
	}

	@Test
	void getMessage() {
		MessageBuilder<MessageId, Macro> messageBuilder = new MessageBuilder<>(plugin);
		assertEquals("This is an enabled message", messageBuilder.getMessage(MessageId.ENABLED_MESSAGE));
	}

	@Test
	void getItemName() {
		MessageBuilder<MessageId, Macro> messageBuilder = new MessageBuilder<>(plugin);
		assertEquals("§aTest Item", messageBuilder.getItemName().orElse("fail"));
	}

	@Test
	void getItemNamePlural() {
		MessageBuilder<MessageId, Macro> messageBuilder = new MessageBuilder<>(plugin);
		assertEquals("§aTest Items", messageBuilder.getItemNamePlural().orElse("fail"));
	}

	@Test
	void getInventoryItemName() {
		MessageBuilder<MessageId, Macro> messageBuilder = new MessageBuilder<>(plugin);
		assertEquals("§aInventory Item", messageBuilder.getInventoryItemName().orElse("fail"));
	}

	@Test
	void getItemLore() {
		MessageBuilder<MessageId, Macro> messageBuilder = new MessageBuilder<>(plugin);
		assertEquals(List.of("§elore line 1", "§elore line 2"), messageBuilder.getItemLore());
	}

	@Test
	void getSpawnDisplayName() {
		MessageBuilder<MessageId, Macro> messageBuilder = new MessageBuilder<>(plugin);
		assertEquals("§aSpawn", messageBuilder.getSpawnDisplayName().orElse("fail"));
	}

	@Test
	void getHomeDisplayName() {
		MessageBuilder<MessageId, Macro> messageBuilder = new MessageBuilder<>(plugin);
		assertEquals("§aHome", messageBuilder.getHomeDisplayName().orElse("fail"));
	}

	@Test
	void getTimeString_with_singular_units() {
		MessageBuilder<MessageId, Macro> messageBuilder = new MessageBuilder<>(plugin);

		long duration = DAYS.toMillis(1) + HOURS.toMillis(1) + MINUTES.toMillis(1) + SECONDS.toMillis(1);
		assertEquals("1 day 1 hour 1 minute 1 second", messageBuilder.getTimeString(duration));
	}

	@Test
	void getTimeString_with_plural_units() {
		MessageBuilder<MessageId, Macro> messageBuilder = new MessageBuilder<>(plugin);

		long duration = DAYS.toMillis(2) + HOURS.toMillis(2) + MINUTES.toMillis(2) + SECONDS.toMillis(2);
		assertEquals("2 days 2 hours 2 minutes 2 seconds", messageBuilder.getTimeString(duration));
	}

	@Test
	void getTimeString_with_unlimited_time() {
		MessageBuilder<MessageId, Macro> messageBuilder = new MessageBuilder<>(plugin);
		assertEquals("unlimited time", messageBuilder.getTimeString(-1));
	}

	@Test
	void getTimeString_two_parameter_with_singular_units() {
		MessageBuilder<MessageId, Macro> messageBuilder = new MessageBuilder<>(plugin);

		long duration = DAYS.toMillis(1) + HOURS.toMillis(1) + MINUTES.toMillis(1) + SECONDS.toMillis(1);
		assertEquals("1 day 1 hour 1 minute 1 second", messageBuilder.getTimeString(duration, TimeUnit.SECONDS));
	}

	@Test
	void getString() {
		MessageBuilder<MessageId, Macro> messageBuilder = new MessageBuilder<>(plugin);
		assertEquals("an arbitrary string", messageBuilder.getString("ARBITRARY_STRING").orElse("fail"));
	}

	@Test
	void getStringList() {
		MessageBuilder<MessageId, Macro> messageBuilder = new MessageBuilder<>(plugin);
		assertTrue(messageBuilder.getStringList("ARBITRARY_STRING_LIST").containsAll(List.of("item 1", "item 2", "item 3")));
	}

	@Disabled
	@Test
	void getWorldName() {
//		World world = server.addSimpleWorld("test_world");
		World world = server.getWorld("world");
		MessageBuilder<MessageId, Macro> messageBuilder = new MessageBuilder<>(plugin);
		assertNotNull(world, "Default Mock world 'world' is null.");
		assertTrue(messageBuilder.getWorldName(world).isPresent());
		assertEquals("world", messageBuilder.getWorldName(world).get());
	}


	@Test
	void reload() {
		MessageBuilder<MessageId, Macro> messageBuilder = new MessageBuilder<>(plugin);
		messageBuilder.reload();
		assertEquals("This is an enabled message", messageBuilder.getMessage(MessageId.ENABLED_MESSAGE));
	}

}
