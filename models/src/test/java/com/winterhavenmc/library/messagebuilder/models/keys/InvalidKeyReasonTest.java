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

package com.winterhavenmc.library.messagebuilder.models.keys;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;


class InvalidKeyReasonTest
{
	@Test
	void getDefaultMessage_returns_valid_defaultMessage()
	{
		assertEquals("The parameter 'key' was null.", InvalidKeyReason.KEY_NULL.getDefaultMessage());
		assertEquals("The parameter 'key' was blank.", InvalidKeyReason.KEY_BLANK.getDefaultMessage());
		assertEquals("The parameter 'key' was invalid.", InvalidKeyReason.KEY_INVALID.getDefaultMessage());
	}

	@Test
	void getLocalizedMessage_returns_valid_bundle_message()
	{
		assertEquals("The parameter ''key'' was null.", InvalidKeyReason.KEY_NULL.getLocalizedMessage(Locale.US));
		assertEquals("The parameter ''key'' was blank.", InvalidKeyReason.KEY_BLANK.getLocalizedMessage(Locale.US));
		assertEquals("The parameter ''key'' was invalid.", InvalidKeyReason.KEY_INVALID.getLocalizedMessage(Locale.US));
	}

	@Test
	void getLocalizedMessage_with_object_parameter_returns_valid_bundle_message()
	{
		assertEquals("The parameter 'key' was null.", InvalidKeyReason.KEY_NULL.getLocalizedMessage(Locale.US, "object1", "object2"));
		assertEquals("The parameter 'key' was blank.", InvalidKeyReason.KEY_BLANK.getLocalizedMessage(Locale.US, "object1", "object2"));
		assertEquals("The parameter 'key' was invalid.", InvalidKeyReason.KEY_INVALID.getLocalizedMessage(Locale.US, "object1", "object2"));
	}
}

// KEY_NULL = The parameter ''key'' was null.
// KEY_BLANK = The parameter ''key'' was blank.
// KEY_INVALID = The parameter ''key'' was invalid.
