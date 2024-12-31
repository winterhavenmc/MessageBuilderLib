/*
 * Copyright (c) 2024 Tim Savage.
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

import com.winterhavenmc.util.messagebuilder.util.MockUtility;
import com.winterhavenmc.util.messagebuilder.query.MessageRecord;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.*;
import static org.junit.jupiter.api.Assertions.*;


class MessageRecordTest {

	private ConfigurationSection messageSection;

	@BeforeEach
	void setUp() {
		// create new yaml configuration
		FileConfiguration fileConfiguration = new YamlConfiguration();

		// create a real configuration object from a resource
		try {
			fileConfiguration = YamlConfiguration.loadConfiguration(MockUtility.getResourceFile("language/en-US.yml"));
		} catch (IllegalArgumentException | URISyntaxException e) {
			System.out.println("a problem was encountered while trying to load the test configuration from resource");
		}

		// get messages section of configuration
		messageSection = fileConfiguration.getConfigurationSection("MESSAGES");
	}

	@AfterEach
	void tearDown() {
		messageSection = null;
	}

	//TODO: Each test should have its own distinct test entries in the language configuration resource
	// that are only used for that test, so changes to entries will not effect other tests

	@Test
	void constructorTest() {
		MessageRecord testRecord = new MessageRecord(
				ENABLED_MESSAGE.toString(),
				true,
				"this is a test message",
				11,
				"this is a test title",
				22,
				33,
				44,
				"this is a test subtitle");
		assertNotNull(testRecord, "the newly created record is null.");
	}

//	@Nested
//	class GetTests {
//		@Test
//		void getTest() {
//			assertTrue(MessageRecord.get(ENABLED_MESSAGE.toString(), messageSection).isPresent());
//		}
//
//		@Test
//		void getTest_nonexistent_entry() {
//			assertTrue(MessageRecord.get(NONEXISTENT_ENTRY.toString(), messageSection).isEmpty());
//		}
//
//		@Test
//		void getTest_null_messageKey() {
//			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//					() -> MessageRecord.get(null, messageSection));
//			assertEquals("the messageKey parameter was null.", exception.getMessage());
//		}
//
//		@Test
//		void getTest_null_messageSection() {
//			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//					() -> MessageRecord.get(ENABLED_MESSAGE.toString(), null));
//			assertEquals("the messageSection parameter was null.", exception.getMessage());
//		}
//	}

//	@Nested
//	class GetRecordTests {
//		@Test
//		void getRecordTest() {
//			assertNotNull(MessageRecord.getRecord(ENABLED_MESSAGE.toString(), messageSection));
//		}
//
//		@Test
//		void getRecordTest_nonexistent_entry() {
//			assertNull(MessageRecord.getRecord(NONEXISTENT_ENTRY.toString(), messageSection));
//		}
//
//		@Test
//		void getRecordTest_null_messageKey() {
//			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//					() -> MessageRecord.getRecord(null, messageSection));
//			assertEquals("the messageKey parameter was null.", exception.getMessage());
//		}
//
//		@Test
//		void getRecordTest_null_messageSection() {
//			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//					() -> MessageRecord.getRecord(ENABLED_MESSAGE.toString(), null));
//			assertEquals("the messageSection parameter was null.", exception.getMessage());
//		}
//	}

//	@Nested
//	class FetchEnabledTests {
//		@Test
//		void fetchEnabledTest_enabled() {
//			assertTrue(MessageRecord.fetchEnabled(getMessageEntry(ENABLED_MESSAGE, messageSection)));
//		}
//
//		@Test
//		void FetchEnabledTest_disabled() {
//			assertFalse(MessageRecord.fetchEnabled(getMessageEntry(DISABLED_MESSAGE, messageSection)));
//		}
//
//		@Test
//		void FetchEnabledTest_undefined() {
//			assertTrue(MessageRecord.fetchEnabled(getMessageEntry(UNDEFINED_FIELD_ENABLED, messageSection)));
//		}
//
//		@Test
//		void fetchEnabledTest_nonexistent() {
//			assertFalse(MessageRecord.fetchEnabled(getMessageEntry(NONEXISTENT_ENTRY, messageSection)));
//		}
//
//		@Test
//		void fetchEnabledTest_null_parameter() {
//			assertFalse(MessageRecord.fetchEnabled(null));
//		}
//	}

//	@Nested
//	class FetchMessageTests {
//		@Test
//		void fetchMessageTest_enabled_message() {
//			assertEquals("This is an enabled message",
//					MessageRecord.fetchMessage(getMessageEntry(ENABLED_MESSAGE, messageSection)));
//		}
//
//		@Test
//		void fetchMessageTest_disabled_message() {
//			assertEquals("This is a disabled message",
//					MessageRecord.fetchMessage(getMessageEntry(DISABLED_MESSAGE, messageSection)));
//		}
//
//		@Test
//		void fetchMessageTest_undefined_field() {
//			assertEquals("", MessageRecord.fetchMessage(getMessageEntry(UNDEFINED_FIELD_MESSAGE, messageSection)));
//		}
//
//		@Test
//		void fetchMessageTest_nonexistent() {
//			assertEquals("", MessageRecord.fetchMessage(getMessageEntry(NONEXISTENT_ENTRY, messageSection)));
//		}
//
//		@Test
//		void fetchMessageTest_null_parameter() {
//			assertEquals("", MessageRecord.fetchMessage(null));
//		}
//	}

//	@Nested
//	class FetchRepeatDelayTests {
//		@Test
//		void fetchRepeatDelayTest() {
//			assertEquals(10, MessageRecord.fetchRepeatDelay(getMessageEntry(REPEAT_DELAYED_MESSAGE, messageSection)));
//		}
//
//		@Test
//		void fetchRepeatDelayTest_undefined_field() {
//			assertEquals(0, MessageRecord.fetchRepeatDelay(getMessageEntry(UNDEFINED_FIELD_REPEAT_DELAY, messageSection)));
//		}
//
//		@Test
//		void fetchRepeatDelayTest_null_parameter() {
//			assertEquals(0L, MessageRecord.fetchRepeatDelay(null));
//		}
//	}

//	@Nested
//	class FetchTitleTests {
//		@Test
//		void fetchTitleTest_enabled_title() {
//			assertEquals("This is an enabled title", MessageRecord.fetchTitle(getMessageEntry(ENABLED_TITLE, messageSection)));
//		}
//
//		@Test
//		void fetchTitleTest_disabled_title() {
//			assertEquals("This is a disabled title", MessageRecord.fetchTitle(getMessageEntry(DISABLED_TITLE, messageSection)));
//		}
//
//		@Test
//		void fetchTitleTest_undefined_field() {
//			assertEquals("", MessageRecord.fetchTitle(getMessageEntry(UNDEFINED_FIELD_TITLE, messageSection)));
//		}
//
//		@Test
//		void fetchTitleTest_null_parameter() {
//			assertEquals("", MessageRecord.fetchTitle(null));
//		}
//	}

//	@Nested
//	class FetchTitleFadeInTests {
//		@Test
//		void fetchTitleFadeInTest_custom() {
//			assertEquals(20, MessageRecord.fetchTitleFadeIn(getMessageEntry(CUSTOM_FADE_TITLE, messageSection)));
//		}
//
//		@Test
//		void fetchTitleFadeInTest_undefined_field() {
//			assertEquals(10, MessageRecord.fetchTitleFadeIn(getMessageEntry(UNDEFINED_FIELD_TITLE_FADE_IN, messageSection)));
//		}
//
//		@Test
//		void fetchTitleFadeInTest_null_parameter() {
//			assertEquals(10, MessageRecord.fetchTitleFadeIn(null));
//		}
//	}

//	@Nested
//	class FetchTitleStayTests {
//		@Test
//		void fetchTitleStayTest_custom() {
//			assertEquals(140, MessageRecord.fetchTitleStay(getMessageEntry(CUSTOM_FADE_TITLE, messageSection)));
//		}
//
//		@Test
//		void fetchTitleStayTest_undefined_field() {
//			assertEquals(70, MessageRecord.fetchTitleStay(getMessageEntry(UNDEFINED_FIELD_TITLE_STAY, messageSection)));
//		}
//
//		@Test
//		void fetchTitleStayTest_null_parameter() {
//			assertEquals(70, MessageRecord.fetchTitleStay(null));
//		}
//	}

//	@Nested
//	class FetchTitleFadeOutTests {
//		@Test
//		void fetchTitleFadeOutTest_custom() {
//			assertEquals(40, MessageRecord.fetchTitleFadeOut(getMessageEntry(CUSTOM_FADE_TITLE, messageSection)));
//		}
//
//		@Test
//		void fetchTitleFadeOutTest_undefined_field() {
//			assertEquals(20, MessageRecord.fetchTitleFadeOut(getMessageEntry(UNDEFINED_FIELD_TITLE_FADE_OUT, messageSection)));
//		}
//
//		@Test
//		void fetchTitleFadeOutTest_null_parameter() {
//			assertEquals(20, MessageRecord.fetchTitleFadeOut(null));
//		}
//	}

//	@Nested
//	class FetchSubtitleTests {
//		@Test
//		void fetchSubtitleTestTest() {
//			assertEquals("This is an enabled subtitle", MessageRecord.fetchSubtitle(getMessageEntry(ENABLED_SUBTITLE, messageSection)));
//		}
//
//		@Test
//		void fetchSubtitleTest_disabled() {
//			assertEquals("This is a disabled subtitle", MessageRecord.fetchSubtitle(getMessageEntry(DISABLED_SUBTITLE, messageSection)));
//		}
//
//		@Test
//		void fetchSubtitleTest_undefined_field() {
//			assertEquals("", MessageRecord.fetchSubtitle(getMessageEntry(UNDEFINED_FIELD_SUBTITLE, messageSection)));
//		}
//
//		@Test
//		void fetchSubtitleTest_null_parameter() {
//			assertEquals("", MessageRecord.fetchSubtitle(null));
//		}
//	}


//	@Nested
//	class NullParameterTests {
//
//		@Nested
//		class MessageId {
//			@Test
//			void fetchEnabledTest_null_messageId() {
//				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//						() -> MessageRecord.fetchEnabled(getMessageEntry(null, messageSection)));
//				assertEquals("the messageKey parameter was null.", exception.getMessage());
//			}
//
//			@Test
//			void fetchMessageTest_null_messageId() {
//				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//						() -> MessageRecord.fetchMessage(getMessageEntry(null, messageSection)));
//				assertEquals("the messageKey parameter was null.", exception.getMessage());
//			}
//
//			@Test
//			void fetchRepeatDelayTest_null_messageId() {
//				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//						() -> MessageRecord.fetchRepeatDelay(getMessageEntry(null, messageSection)));
//				assertEquals("the messageKey parameter was null.", exception.getMessage());
//			}
//
//			@Test
//			void fetchTitleTest_null_messageId() {
//				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//						() -> MessageRecord.fetchTitle(getMessageEntry(null, messageSection)));
//				assertEquals("the messageKey parameter was null.", exception.getMessage());
//			}
//
//			@Test
//			void fetchTitleFadeInTest_null_messageId() {
//				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//						() -> MessageRecord.fetchTitleFadeIn(getMessageEntry(null, messageSection)));
//				assertEquals("the messageKey parameter was null.", exception.getMessage());
//			}
//
//			@Test
//			void fetchTitleStayTest_null_messageId() {
//				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//						() -> MessageRecord.fetchTitleStay(getMessageEntry(null, messageSection)));
//				assertEquals("the messageKey parameter was null.", exception.getMessage());
//			}
//
//			@Test
//			void fetchTitleFadeOutTest_null_messageId() {
//				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//						() -> MessageRecord.fetchTitleFadeOut(getMessageEntry(null, messageSection)));
//				assertEquals("the messageKey parameter was null.", exception.getMessage());
//			}
//
//			@Test
//			void fetchSubtitleTest_null_messageId() {
//				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//						() -> MessageRecord.fetchSubtitle(getMessageEntry(null, messageSection)));
//				assertEquals("the messageKey parameter was null.", exception.getMessage());
//			}
//		}

//		@Nested
//		class MessageSection {
//			@Test
//			void fetchEnabledTest_null_messageSection() {
//				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//						() -> MessageRecord.fetchEnabled(getMessageEntry(ENABLED_MESSAGE, null)));
//				assertEquals("the messageSection parameter was null.", exception.getMessage());
//			}
//
//			@Test
//			void fetchMessageTest_null_messageSection() {
//				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//						() -> MessageRecord.fetchMessage(getMessageEntry(ENABLED_MESSAGE, null)));
//				assertEquals("the messageSection parameter was null.", exception.getMessage());
//			}
//
//			@Test
//			void fetchRepeatDelayTest_null_messageSection() {
//				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//						() -> MessageRecord.fetchRepeatDelay(getMessageEntry(ENABLED_MESSAGE, null)));
//				assertEquals("the messageSection parameter was null.", exception.getMessage());
//			}
//
//
//			@Test
//			void fetchTitleTest_null_messageSection() {
//				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//						() -> MessageRecord.fetchTitle(getMessageEntry(ENABLED_MESSAGE, null)));
//				assertEquals("the messageSection parameter was null.", exception.getMessage());
//			}
//
//			@Test
//			void fetchTitleFadeInTest_null_messageSection() {
//				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//						() -> MessageRecord.fetchTitleFadeIn(getMessageEntry(ENABLED_MESSAGE, null)));
//				assertEquals("the messageSection parameter was null.", exception.getMessage());
//			}
//
//			@Test
//			void fetchTitleStayTest_null_messageSection() {
//				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//						() -> MessageRecord.fetchTitleStay(getMessageEntry(ENABLED_MESSAGE, null)));
//				assertEquals("the messageSection parameter was null.", exception.getMessage());
//			}
//
//			@Test
//			void fetchTitleFadeOutTest_null_messageSection() {
//				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//						() -> MessageRecord.fetchTitleFadeOut(getMessageEntry(ENABLED_MESSAGE, null)));
//				assertEquals("the messageSection parameter was null.", exception.getMessage());
//			}
//
//			@Test
//			void fetchSubtitleTest_null_messageSection() {
//				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//						() -> MessageRecord.fetchSubtitle(getMessageEntry(ENABLED_SUBTITLE, null)));
//				assertEquals("the messageSection parameter was null.", exception.getMessage());
//			}
//		}
//	}

}
