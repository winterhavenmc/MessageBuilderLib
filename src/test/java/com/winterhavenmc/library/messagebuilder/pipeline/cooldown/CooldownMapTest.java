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

package com.winterhavenmc.library.messagebuilder.pipeline.cooldown;

import com.winterhavenmc.library.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.messages.MessageId;
import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.model.language.MessageRecord;
import com.winterhavenmc.library.messagebuilder.model.language.ValidMessageRecord;
import com.winterhavenmc.library.messagebuilder.validation.ValidationException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.library.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CooldownMapTest
{
	@Mock Player playerMock;

	Recipient.Valid recipient;
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

		validMessageRecord = ValidMessageRecord.create(recordKey, section);

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
				Optional.of("this is a final message string"),
				Optional.of("this is a final title string"),
				Optional.of("this is a final subtitle string"));
	}


	@Nested
	@DisplayName("putExpirationTime Tests")
	class putExpirationTimeTests {
		@Test
		@DisplayName("putExpirationTime() inserts entry in map with Valid parameters")
		void putExpirationTime_inserts_entry_in_map()
		{
			// Arrange
			when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
			recipient = switch (Recipient.of(playerMock)) {
				case Recipient.Valid valid -> valid;
				case Recipient.Proxied ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
				case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
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
		@DisplayName("putExpirationTime() does not overwrite existing entry for player/message.")
		void putExpirationTime_does_not_overwrite_existing_entry()
		{
			// Arrange
			when(playerMock.getUniqueId()).thenReturn(new UUID(42, 17));
			recipient = switch (Recipient.of(playerMock)) {
				case Recipient.Valid valid -> valid;
				case Recipient.Proxied ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
				case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
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
	@DisplayName("notCooling() returns false when entry for key exists in map.")
	void notCooling_returns_false_if_entry_with_key_exists_in_map()
	{
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		recipient = switch (Recipient.of(playerMock)) {
			case Recipient.Valid valid -> valid;
			case Recipient.Proxied ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
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


	@Test
	@DisplayName("notCooling returns true when entry for key does not exist in map.")
	void notCooling_returns_true_if_entry_with_key_does_not_exist_in_map()
	{
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
		recipient = switch (Recipient.of(playerMock)) {
			case Recipient.Valid valid -> valid;
			case Recipient.Proxied ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};

		RecordKey recordKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
		CooldownKey cooldownKey = CooldownKey.of(recipient, recordKey).orElseThrow();

		// Act
		boolean result = cooldownMap.notCooling(cooldownKey);

		// assert
		assertTrue(result);

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
	}


	@Nested
	@DisplayName("removeExpired Tests")
	class RemoveExpiredTests
	{
		@Test
		@DisplayName("removeExpired() does nothing when map is empty.")
		void removeExpired_does_nothing_when_map_is_empty() {
			// Arrange & Act
			int count = cooldownMap.removeExpired();

			// Assert
			assertEquals(0, count);
		}

		@Test
		@DisplayName("removeExpired() leaves all unexpired entries in map")
		void removeExpired_leaves_all_unexpired_entries_in_map() //TODO: test multiple expired entries
		{
			// Arrange
			when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
			recipient = switch (Recipient.of(playerMock)) {
				case Recipient.Valid valid -> valid;
				case Recipient.Proxied ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
				case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			};

			RecordKey recordKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
			CooldownKey cooldownKey = CooldownKey.of(recipient, recordKey).orElseThrow();
			cooldownMap.putExpirationTime(recipient, finalMessageRecord);

			// Act
			int result = cooldownMap.removeExpired();

			// Assert
			assertEquals(0, result);
			assertFalse(cooldownMap.notCooling(cooldownKey));
		}


		@Test
		@DisplayName("removeExpired() removes all expired entries from map")
		void removeExpired_removes_all_expired_entries_from_map()
		{
			// Arrange
			when(playerMock.getUniqueId()).thenReturn(UUID.randomUUID());
			recipient = switch (Recipient.of(playerMock))
			{
				case Recipient.Valid valid -> valid;
				case Recipient.Proxied ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
				case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			};
			RecordKey recordKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
			CooldownKey cooldownKey = CooldownKey.of(recipient, recordKey).orElseThrow();

			section.set(MessageRecord.Field.REPEAT_DELAY.toKey(), -10);
			//TODO: what is this testing? negative repeatDelay should not be inserted in the first place.

			validMessageRecord = ValidMessageRecord.create(recordKey, section);

			FinalMessageRecord expiredMessageRecord = validMessageRecord.withFinalStrings(
					"this is a final message.",
					"this is a finale title.",
					"this is a final subtitle.");

			cooldownMap.putExpirationTime(recipient, expiredMessageRecord);

			// Act
			int result = cooldownMap.removeExpired();

			// Assert
			assertEquals(1, result);
			assertTrue(cooldownMap.notCooling(cooldownKey));
		}
	}

}
