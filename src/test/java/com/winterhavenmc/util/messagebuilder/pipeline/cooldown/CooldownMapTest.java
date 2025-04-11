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
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.FinalMessageRecord;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.MessageRecord;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.ValidMessageRecord;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.UUID;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
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
	ValidMessageRecord validMessageRecord;
	FinalMessageRecord finalMessageRecord;
	ConfigurationSection section;
	RecordKey recordKey;


	@BeforeEach
	void setUp() {
		recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();
		cooldownMap = new CooldownMap();

		section = new MemoryConfiguration();
		section.set(MessageRecord.Field.ENABLED.toKey(), true);
		section.set(MessageRecord.Field.MESSAGE_TEXT.toKey(), "this is a test message");
		section.set(MessageRecord.Field.REPEAT_DELAY.toKey(), 11);
		section.set(MessageRecord.Field.TITLE_TEXT.toKey(), "this is a test title");
		section.set(MessageRecord.Field.TITLE_FADE_IN.toKey(), 22);
		section.set(MessageRecord.Field.TITLE_STAY.toKey(), 33);
		section.set(MessageRecord.Field.TITLE_FADE_OUT.toKey(), 44);
		section.set(MessageRecord.Field.SUBTITLE_TEXT.toKey(), "this is a test subtitle");

		validMessageRecord = ValidMessageRecord.from(recordKey, section);

		finalMessageRecord = new FinalMessageRecord(
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
				"this is a final subtitle string");
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
			cooldownMap.putExpirationTime(recipient, finalMessageRecord);

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
			cooldownMap.putExpirationTime(recipient, finalMessageRecord);
			// put second time
			cooldownMap.putExpirationTime(recipient, finalMessageRecord);

			// Assert TODO: test that second put did not overwrite first entry
			RecordKey recordKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
			CooldownKey cooldownKey = CooldownKey.of(recipient, recordKey).orElseThrow();

			assertFalse(cooldownMap.notCooling(cooldownKey));

			// Verify
			verify(playerMock, atLeast(2)).getUniqueId();
		}
	}


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
		cooldownMap.putExpirationTime(recipient, finalMessageRecord);

		// assert
		assertFalse(cooldownMap.notCooling(cooldownKey));

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
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

			cooldownMap.putExpirationTime(recipient, finalMessageRecord);

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

			section.set(MessageRecord.Field.REPEAT_DELAY.toKey(), -10);

			validMessageRecord = ValidMessageRecord.from(recordKey, section);

			FinalMessageRecord expiredMessageRecord = validMessageRecord.withFinalStrings(
					"this is a final message.",
					"this is a finale title.",
					"this is a final subtitle.");

			cooldownMap.putExpirationTime(recipient, expiredMessageRecord);

			// Act
			int count = cooldownMap.removeExpired();

			// Assert
			assertEquals(1, count);
		}
	}

}
