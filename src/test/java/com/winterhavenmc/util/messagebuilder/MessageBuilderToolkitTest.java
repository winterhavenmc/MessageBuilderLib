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

package com.winterhavenmc.util.messagebuilder;

import com.winterhavenmc.util.messagebuilder.macro.MacroHandler;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.query.LanguageQueryHandler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class MessageBuilderToolkitTest {

	@Mock private MessageBuilder<MessageId, Macro> messageBuilderMock;
	@Mock private LanguageQueryHandler queryHandlerMock;

	private MessageBuilderToolkit<MessageId, Macro> toolkit;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		when(messageBuilderMock.getQueryHandler()).thenReturn(queryHandlerMock);
		when(queryHandlerMock.getString("some-key")).thenReturn(Optional.of("SOME_VALUE"));
		when(queryHandlerMock.getStringList("TEST_LIST")).thenReturn(List.of("item 1", "item 2", "item 3"));

		toolkit = new MessageBuilderToolkit<>(messageBuilderMock);
	}

	@AfterEach
	void tearDown() {
		messageBuilderMock = null;
		toolkit = null;
	}


	@Test
	void constructorTest_null_parameter() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
				new MessageBuilderToolkit<MessageId, Macro>(null));
		assertEquals("The messageBuilder parameter cannot be null.", exception.getMessage());
	}


	@Nested
	class DelimiterTests {
		@Test
		void setDelimitersTest_same() {
			toolkit.setDelimiters('#');
			assertEquals('#', MacroHandler.MacroDelimiter.LEFT.toChar());
			assertEquals('#', MacroHandler.MacroDelimiter.RIGHT.toChar());
			// these must be reset back manually or they persist across tests.
			toolkit.setDelimiters('%');
			assertEquals('%', MacroHandler.MacroDelimiter.LEFT.toChar());
			assertEquals('%', MacroHandler.MacroDelimiter.RIGHT.toChar());
		}

		@Test
		void setDelimitersTest_different() {
			toolkit.setDelimiters('L', 'R');
			assertEquals('L', MacroHandler.MacroDelimiter.LEFT.toChar());
			assertEquals('R', MacroHandler.MacroDelimiter.RIGHT.toChar());
			// these must be reset manually or they persist across tests.
			toolkit.setDelimiters('%');
			assertEquals('%', MacroHandler.MacroDelimiter.LEFT.toChar());
			assertEquals('%', MacroHandler.MacroDelimiter.RIGHT.toChar());
		}
	}

	@Test
	void getQueryHandlerTest() {
		assertNotNull(toolkit.getQueryHandler());
	}

	@Test
	void getStringTest() {
		Optional<String> optString = toolkit.getString("some-key");
		assertTrue(optString.isPresent());
		assertEquals("SOME_VALUE", optString.get());
		verify(queryHandlerMock, atLeastOnce()).getString("some-key");
	}

	@Test
	void getStringListTest() {
		List<String> stringList = toolkit.getStringList("TEST_LIST");
		assertFalse(stringList.isEmpty());
		assertEquals(List.of("item 1", "item 2", "item 3"), stringList);
		verify(queryHandlerMock, atLeastOnce()).getStringList("TEST_LIST");
	}

	@Test
	void getSpawnDisplaynameTest() {
		when(queryHandlerMock.getString("SPAWN.DISPLAY_NAME")).thenReturn(Optional.of("Spawn"));
		Optional<String> optString = toolkit.getSpawnDisplayName();
		assertTrue(optString.isPresent());
		assertEquals("Spawn", optString.get());
		verify(queryHandlerMock, atLeastOnce()).getString("SPAWN.DISPLAY_NAME");
	}

	@Test
	void getHomeDisplaynameTest() {
		when(queryHandlerMock.getString("HOME.DISPLAY_NAME")).thenReturn(Optional.of("Home"));
		Optional<String> optString = toolkit.getHomeDisplayName();
		assertTrue(optString.isPresent());
		assertEquals("Home", optString.get());
		verify(queryHandlerMock, atLeastOnce()).getString("HOME.DISPLAY_NAME");
	}

}
