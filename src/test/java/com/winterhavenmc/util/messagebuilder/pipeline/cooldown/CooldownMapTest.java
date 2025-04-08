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

package com.winterhavenmc.util.messagebuilder.pipeline.cooldown;

import com.winterhavenmc.util.messagebuilder.recipient.InvalidRecipient;
import com.winterhavenmc.util.messagebuilder.recipient.RecipientResult;
import com.winterhavenmc.util.messagebuilder.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.MessageRecord;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.UUID;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CooldownMapTest
{
	@Mock Player playerMock;

	ValidRecipient recipient;
	CooldownKey cooldownKey;
	CooldownMap cooldownMap;
	MessageRecord messageRecord;

	@BeforeEach
	void setUp() {
		cooldownMap = new CooldownMap();

		messageRecord = new MessageRecord(
				RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow(),
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


	@Nested
	@DisplayName("putExpirationTime Tests")
	class PutExpirationTimeTests {
		@Test
		@DisplayName("Test putExpirationTime with Valid parameters")
		void testPutExpirationTime()
		{
			// Arrange
			when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
			recipient = switch (RecipientResult.from(playerMock)) {
				case ValidRecipient validRecipient -> validRecipient;
				case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			};

			cooldownKey = RecordKey.of(MessageId.ENABLED_MESSAGE)
					.flatMap(recordKey -> CooldownKey.of(recipient, recordKey))
					.orElseThrow();

			// Act
			cooldownMap.putExpirationTime(recipient, messageRecord);

			// Assert
			assertFalse(cooldownMap.notCooling(cooldownKey));

			// Verify
			verify(playerMock, atLeastOnce()).getUniqueId();
		}


		@Test
		@DisplayName("Test putExpirationTime when already cooling")
		void testPutExpirationTime_already_cooling() {
			// Arrange
			when(playerMock.getUniqueId()).thenReturn(new UUID(42, 17));
			recipient = switch (RecipientResult.from(playerMock)) {
				case ValidRecipient validRecipient -> validRecipient;
				case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			};

			// Act
			cooldownMap.putExpirationTime(recipient, messageRecord);
			// put second time
			cooldownMap.putExpirationTime(recipient, messageRecord);

			// Assert TODO: test that second put did not overwrite first entry
			RecordKey recordKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
			CooldownKey cooldownKey = CooldownKey.of(recipient, recordKey).orElseThrow();

			assertFalse(cooldownMap.notCooling(cooldownKey));

			// Verify
			verify(playerMock, atLeast(2)).getUniqueId();
		}
	}


	@Nested
	@DisplayName("isCooling Tests")
	class isCoolingTests {
		@Test
		@DisplayName("Test isCooling with Valid parameters")
		void testIsCooling() {
			// Arrange
			when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
			recipient = switch (RecipientResult.from(playerMock)) {
				case ValidRecipient validRecipient -> validRecipient;
				case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			};

			RecordKey recordKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
			CooldownKey cooldownKey = CooldownKey.of(recipient, recordKey).orElseThrow();

			// Act
			cooldownMap.putExpirationTime(recipient, messageRecord);

			// assert
			assertFalse(cooldownMap.notCooling(cooldownKey));

			// Verify
			verify(playerMock, atLeastOnce()).getUniqueId();
		}


		@Test
		@DisplayName("Test isCooling with null key")
		@Disabled("not validating 'key' parameter")
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
			recipient = switch (RecipientResult.from(playerMock)) {
				case ValidRecipient validRecipient -> validRecipient;
				case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			};

			cooldownMap.putExpirationTime(recipient, messageRecord);

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
			recipient = switch (RecipientResult.from(playerMock)) {
				case ValidRecipient validRecipient -> validRecipient;
				case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			};
			messageRecord = new MessageRecord(
					RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow(),
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

			cooldownMap.putExpirationTime(recipient, messageRecord);

			// Act
			int count = cooldownMap.removeExpired();

			// Assert
			assertEquals(1, count);
		}
	}

}
