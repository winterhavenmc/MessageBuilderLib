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

package com.winterhavenmc.util.messagebuilder.macro;

import com.winterhavenmc.util.messagebuilder.macro.processor.ProcessorType;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContextContainerTest {

	@Test
	void testToString() {
		ContextContainer<Number> container = ContextContainer.of(42, ProcessorType.NUMBER);
		assertEquals("ContextContainer{value=42, processorType=NUMBER}", container.toString());
	}

	@Test
	void of() {
		ContextContainer<Number> container = ContextContainer.of(42, ProcessorType.NUMBER);
		assertEquals("ContextContainer{value=42, processorType=NUMBER}", container.toString());
	}

	@Test
	void value() {
		ContextContainer<Number> container = ContextContainer.of(42, ProcessorType.NUMBER);
		assertEquals(42, container.value());
	}

	@Test
	void processorType() {
		ContextContainer<Number> container = ContextContainer.of(42, ProcessorType.NUMBER);
		assertEquals(ProcessorType.NUMBER, container.processorType());
	}

}
