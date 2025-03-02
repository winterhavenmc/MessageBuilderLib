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

package com.winterhavenmc.util.messagebuilder.pipeline;

import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.time.Duration;
import java.util.UUID;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CooldownMapTest {

	@Mock Plugin pluginMock;
	@Mock Server serverMock;
	@Mock PluginManager pluginManagerMock;
	@Mock Player playerMock;

	CooldownMap cooldownMap;
	MessageRecord messageRecord;

	@BeforeEach
	void setUp() {
		cooldownMap = new CooldownMap();

		messageRecord = new MessageRecord(
				MessageId.ENABLED_MESSAGE.name(),
				true,
				"this is a message.",
				Duration.ofSeconds(3),
				"this is a title.",
				20,
				40,
				30,
				"this is a subtitle.",
				"this is a final message string",
				"this is a final title string",
				"this is a final subtitle string"
		);
	}

	@AfterEach
	void tearDown() {
		pluginMock = null;
		serverMock = null;
		pluginManagerMock = null;
		playerMock = null;
		cooldownMap = null;
		messageRecord = null;
	}

	@Nested
	@DisplayName("putExpirationTime Tests")
	class PutExpirationTimeTests {
		@Test
		@DisplayName("Test putExpirationTime with valid parameters")
		void testPutExpirationTime() {
			// Arrange
			when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());

			// Act
			cooldownMap.putExpirationTime(playerMock, messageRecord);

			// Assert
			assertFalse(cooldownMap.notCooling(new CooldownKey(playerMock, MessageId.ENABLED_MESSAGE.name())));

			// Verify
			verify(playerMock, atLeastOnce()).getUniqueId();
		}

		@Test
		@DisplayName("Test putExpirationTime with null recipient")
		void testPutExpirationTime_parameter_null_recipient() {
			// Arrange & Act
			ValidationException exception = assertThrows(ValidationException.class,
					() -> cooldownMap.putExpirationTime(null, messageRecord));

			// Assert
			assertEquals("The parameter 'recipient' cannot be null.", exception.getMessage());
		}

		@Test
		@DisplayName("Test putExpirationTime with null messageRecord")
		void testPutExpirationTime_parameter_null_messageRecord() {
			// Arrange & Act
			ValidationException exception = assertThrows(ValidationException.class,
					() -> cooldownMap.putExpirationTime(playerMock, null));

			// Assert
			assertEquals("The parameter 'messageRecord' cannot be null.", exception.getMessage());
		}

		@Test
		@DisplayName("Test putExpirationTime when already cooling")
		void testPutExpirationTime_already_cooling() {
			// Arrange
			when(playerMock.getUniqueId()).thenReturn(new UUID(42, 17));

			// Act
			cooldownMap.putExpirationTime(playerMock, messageRecord);
			// put second time
			cooldownMap.putExpirationTime(playerMock, messageRecord);

			// Assert TODO: test that second put did not overwrite first entry
			assertFalse(cooldownMap.notCooling(new CooldownKey(playerMock, MessageId.ENABLED_MESSAGE.name())));

			// Verify
			verify(playerMock, atLeast(2)).getUniqueId();
		}
	}


	@Nested
	@DisplayName("isCooling Tests")
	class isCoolingTests {
		@Test
		@DisplayName("Test isCooling with valid parameters")
		void testIsCooling() {
			// Arrange
			when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());

			// Act
			cooldownMap.putExpirationTime(playerMock, messageRecord);

			// assert
			assertFalse(cooldownMap.notCooling(new CooldownKey(playerMock, MessageId.ENABLED_MESSAGE.name())));

			// Verify
			verify(playerMock, atLeastOnce()).getUniqueId();
		}

		@Test
		@DisplayName("Test isCooling with null key")
		void testIsCooling_parameter_null_key() {
			ValidationException exception = assertThrows(ValidationException.class,
					() -> cooldownMap.notCooling(null));

			assertEquals("The parameter 'key' cannot be null.", exception.getMessage());
		}
	}


	@Nested
	@DisplayName("removeExpired Tests")
	class RemoveExpiredTests {
		@Test
		@DisplayName("Test removeExpired with empty map")
		void testRemoveExpired_empty_map() {
			// Arrange & Act
			int count = cooldownMap.removeExpired();

			// Assert
			assertEquals(0, count);
		}

		@Test
		@DisplayName("Test removeExpired")
		void testRemoveExpired() {
			// Arrange
			when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
			cooldownMap.putExpirationTime(playerMock, messageRecord);

			// Act
			int count = cooldownMap.removeExpired();

			// Assert
			assertEquals(0, count);
		}

		@Test
		@DisplayName("Test removeExpired with expired entry")
		void testRemoveExpired_expired() {
			// Arrange
			when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
			messageRecord = new MessageRecord(
					MessageId.ENABLED_MESSAGE.name(),
					true,
					"this is a message.",
					Duration.ofSeconds(-10),
					"this is a title.",
					20,
					40,
					30,
					"this is a subtitle.",
					"this is a final message string",
					"this is a final title string",
					"this is a final subtitle string"
			);

			cooldownMap.putExpirationTime(playerMock, messageRecord);

			// Act
			int count = cooldownMap.removeExpired();

			// Assert
			assertEquals(1, count);
		}
	}

}
