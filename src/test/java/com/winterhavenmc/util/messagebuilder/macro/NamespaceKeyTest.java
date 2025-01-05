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

package com.winterhavenmc.util.messagebuilder.macro;

import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.util.Namespace;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class NamespaceKeyTest {

    @Test
    void testConstructor_WithDomainAndKeyPath() {
        NamespaceKey key = new NamespaceKey("test.key", Namespace.Domain.MACRO);
        assertEquals("test.key", key.getPath());
        assertEquals(Namespace.Domain.MACRO, key.getDomain());
        assertTrue(key.getSubdomains().isEmpty());
    }

    @Test
    void testConstructor_WithDomainKeyPathAndSubdomains() {
        NamespaceKey key = new NamespaceKey("test.key", Namespace.Domain.MACRO, "sub1", "sub2");
        assertEquals("test.key", key.getPath());
        assertEquals(Namespace.Domain.MACRO, key.getDomain());
        assertEquals(List.of("sub1", "sub2"), key.getSubdomains());
    }

    @Test
    void testGetFullDomain() {
        NamespaceKey key = new NamespaceKey("test.key", Namespace.Domain.MACRO, "sub1", "sub2");
        assertEquals("MACRO:sub1:sub2", key.getFullDomain());
    }

    @Test
    void testGetKey() {
        NamespaceKey key = new NamespaceKey("test.key", Namespace.Domain.MACRO, "sub1", "sub2");
        assertEquals("MACRO:sub1:sub2|test.key", key.getKey());
    }

    @Test
    void testGetPathComponents() {
        NamespaceKey key = new NamespaceKey("test.key.path", Namespace.Domain.MACRO);
        assertEquals(List.of("test", "key", "path"), key.getPathComponents());
    }

    @Test
    void testStaticCreate_WithMacro() {
        String key = NamespaceKey.create(Macro.TOOL);
        assertEquals("MACRO|TOOL", key);
    }

    @Test
    void testStaticCreate_WithMacro_null() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> NamespaceKey.create(null));
        assertEquals("macro cannot be null.", exception.getMessage());
    }

    @Test
    void testStaticCreate_WithMacroAndDomain() {
        String key = NamespaceKey.create("MACRO_NAME", Namespace.Domain.MACRO);
        assertEquals("MACRO|MACRO_NAME", key);
    }

    @Test
    void testStaticCreate_WithKeyPathDomainAndSubcategories() {
        String key = NamespaceKey.create("test.key", Namespace.Domain.MACRO, "sub1", "sub2");
        assertEquals("MACRO:sub1:sub2:test.key", key);
    }

    @Test
    void testIsValidKeyDomain_Valid() {
        assertTrue(NamespaceKey.isValidKeyDomain("MACRO:sub1:sub2"));
    }

    @Test
    void testIsValidKeyDomain_Invalid() {
        assertFalse(NamespaceKey.isValidKeyDomain("MACRO:sub1::sub2"));
    }

    @Test
    void testLogInvalid_InvalidDomainLogsWarning() {
        // Capture console output to validate the warning
        NamespaceKey.logInvalidDomainPath("MACRO:sub1::sub2");
        // Validate warning message (mock Logger for better assertions if needed)
    }

    @Test
    void testIsValidKeyPath_Valid() {
        assertTrue(NamespaceKey.isValidKeyPath("test.key.path"));
    }

    @Test
    void testIsValidKeyPath_Invalid() {
        assertFalse(NamespaceKey.isValidKeyPath("test.key..path"));
    }

    @Test
    void testLogInvalidKeyPath_InvalidDomainLogsWarning() {
        // Use a mock logger or capture console output for assertion
        NamespaceKey.logInvalidKeyPath("test.key..path");
    }


    @Nested
    class StaticCreateTests {
        @Test
        void testStaticCreate_2_param_null_macro() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> NamespaceKey.create(null, Namespace.Domain.MACRO));
            assertEquals("macro cannot be null.", exception.getMessage());
        }

        @Test
        void testStaticCreate_2_param_null_domain() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> NamespaceKey.create(Macro.TOOL, null));
            assertEquals("The domain parameter cannot be null.", exception.getMessage());
        }

        @Test
        void testStaticCreate_3_param_null_keyPath() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> NamespaceKey.create(null, Namespace.Domain.MACRO, "sub1", "sub2"));
            assertEquals("The keyPath parameter cannot be null.", exception.getMessage());
        }

        @Test
        void testStaticCreate_3_param_null_domain() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> NamespaceKey.create("some.key.path", null, "sub1", "sub2"));
            assertEquals("The domain parameter cannot be null.", exception.getMessage());
        }

        @Test
        void testStaticCreate_3_param_null_subdomain() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> NamespaceKey.create("some.key.path", Namespace.Domain.MACRO, (String) null));
            assertEquals("Subdomains cannot be null.", exception.getMessage());
        }

        @Test
        void testStaticCreate_3_param_empty_subdomain() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> NamespaceKey.create("some.key.path", Namespace.Domain.MACRO, ""));
            assertEquals("Subdomains cannot be empty.", exception.getMessage());
        }
    }


	@Nested
	class EqualsTests {
		@Test
		public void testEquals_sameObject() {
			NamespaceKey key = new NamespaceKey("test.key", Namespace.Domain.CONSTANTS, "sub1", "sub2");
			assertEquals(key, key, "A NamespaceKey should equal itself.");
		}

		@Test
		public void testEquals_nullObject() {
			NamespaceKey key = new NamespaceKey("test.key", Namespace.Domain.CONSTANTS, "sub1", "sub2");
			assertNotEquals(null, key, "A NamespaceKey should not equal null.");
		}

		@Test
		public void testEquals_differentClass() {
			NamespaceKey key = new NamespaceKey("test.key", Namespace.Domain.CONSTANTS, "sub1", "sub2");
			assertNotEquals("Some String", key,  "A NamespaceKey should not equal an object of a different class.");
		}

		@Test
		public void testEquals_equalObjects() {
			NamespaceKey key1 = new NamespaceKey("test.key", Namespace.Domain.CONSTANTS, "sub1", "sub2");
			NamespaceKey key2 = new NamespaceKey("test.key", Namespace.Domain.CONSTANTS, "sub1", "sub2");

			assertEquals(key1, key2, "NamespaceKeys with the same fields should be equal.");
			assertEquals(key1.hashCode(), key2.hashCode(), "Equal NamespaceKeys should have the same hashCode.");
		}

		@Test
		public void testEquals_differentDomains() {
			NamespaceKey key1 = new NamespaceKey("test.key", Namespace.Domain.CONSTANTS, "sub1", "sub2");
			NamespaceKey key2 = new NamespaceKey("test.key", Namespace.Domain.MACRO, "sub1", "sub2");

			assertNotEquals(key1, key2, "NamespaceKeys with different domains should not be equal.");
		}

		@Test
		public void testEquals_differentSubdomains() {
			NamespaceKey key1 = new NamespaceKey("test.key", Namespace.Domain.CONSTANTS, "sub1", "sub2");
			NamespaceKey key2 = new NamespaceKey("test.key", Namespace.Domain.CONSTANTS, "bus1", "bus2");

			assertNotEquals(key1, key2, "NamespaceKeys with different subdomains should not be equal.");
		}

		@Test
		public void testEquals_differentPathElements() {
			NamespaceKey key1 = new NamespaceKey("test.key1", Namespace.Domain.CONSTANTS, "sub1", "sub2");
			NamespaceKey key2 = new NamespaceKey("test.key2", Namespace.Domain.CONSTANTS, "sub1", "sub2");

			assertNotEquals(key1, key2, "NamespaceKeys with different path elements should not be equal.");
		}
	}


    //TODO: THESE SHOULD ALL THROW. disable test if it does, and try to work around for testing null. Null parameter checks should already be done above
//    @Disabled
//    @Nested
//    class ThrowOnNullParameterTests {
//        @Test
//        public void testEquals_nullSubdomains() {
//            NamespaceKey key1 = new NamespaceKey("test.key", Namespace.Domain.CONSTANTS, (String[]) null);
//            NamespaceKey key2 = new NamespaceKey("test.key", Namespace.Domain.CONSTANTS, (String[]) null);
//
//            assertEquals(key1, key2, "NamespaceKeys with null subdomains but identical other fields should be equal.");
//            assertEquals(key1.hashCode(), key2.hashCode(), "Equal NamespaceKeys with null subdomains should have the same hashCode.");
//        }
//
//        @Test
//        public void testEquals_nullPathElements() {
//            NamespaceKey key1 = new NamespaceKey(null, Namespace.Domain.CONSTANTS, "sub1","sub2");
//            NamespaceKey key2 = new NamespaceKey(null, Namespace.Domain.CONSTANTS, "sub1","sub2");
//
//            assertEquals(key1, key2, "NamespaceKeys with null path elements but identical other fields should be equal.");
//            assertEquals(key1.hashCode(), key2.hashCode(), "Equal NamespaceKeys with null path elements should have the same hashCode.");
//        }
//
//        @Test
//        public void testEquals_bothNullSubdomainsAndPathElements() {
//            NamespaceKey key1 = new NamespaceKey(null, Namespace.Domain.CONSTANTS, (String[]) null);
//            NamespaceKey key2 = new NamespaceKey(null, Namespace.Domain.CONSTANTS, (String[]) null);
//
//            assertEquals(key1, key2, "NamespaceKeys with null subdomains and null path elements should be equal.");
//            assertEquals(key1.hashCode(), key2.hashCode(), "Equal NamespaceKeys with null fields should have the same hashCode.");
//        }
//
//        @Test
//        public void testEquals_oneNullSubdomains() {
//            NamespaceKey key1 = new NamespaceKey("test.key", Namespace.Domain.CONSTANTS, (String[]) null);
//            NamespaceKey key2 = new NamespaceKey("test.key", Namespace.Domain.CONSTANTS, "sub1","sub2");
//
//            assertNotEquals(key1, key2, "NamespaceKeys with one null and one non-null subdomain should not be equal.");
//        }
//
//        @Test
//        public void testEquals_oneNullPathElements() {
//            NamespaceKey key1 = new NamespaceKey(null, Namespace.Domain.CONSTANTS, "sub1","sub2");
//            NamespaceKey key2 = new NamespaceKey("test.key", Namespace.Domain.CONSTANTS, "sub1","sub2");
//
//            assertNotEquals(key1, key2, "NamespaceKeys with one null and one non-null path element should not be equal.");
//        }
//    }

}
