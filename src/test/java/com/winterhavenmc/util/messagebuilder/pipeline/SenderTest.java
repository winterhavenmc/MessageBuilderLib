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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SenderTest {

	@Test
	void testTypeValues() {
		Sender.Type[] values = Sender.Type.values();
		assertEquals(Sender.Type.MESSAGE, values[0]);
		assertEquals(Sender.Type.TITLE, values[1]);
	}

	@Test
	void testTypeValueOf() {
		assertEquals(Sender.Type.MESSAGE, Sender.Type.valueOf("MESSAGE"));
		assertEquals(Sender.Type.TITLE, Sender.Type.valueOf("TITLE"));
	}

}
