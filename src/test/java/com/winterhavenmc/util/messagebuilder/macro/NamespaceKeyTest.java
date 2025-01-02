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
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class NamespaceKeyTest {

    @Test
    void testConstructor_WithDomainAndKeyPath() {
        NamespaceKey key = new NamespaceKey("test.key", Namespace.Domain.MACRO);
        assertEquals("test.key", key.getKeyPath());
        assertEquals(Namespace.Domain.MACRO, key.getDomain());
        assertTrue(key.getSubdomains().isEmpty());
    }

    @Test
    void testConstructor_WithDomainKeyPathAndSubdomains() {
        NamespaceKey key = new NamespaceKey("test.key", Namespace.Domain.MACRO, "sub1", "sub2");
        assertEquals("test.key", key.getKeyPath());
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
    void testGetKeyPathComponents() {
        NamespaceKey key = new NamespaceKey("test.key.path", Namespace.Domain.MACRO);
        assertEquals(List.of("test", "key", "path"), key.getKeyPathComponents());
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
    void testValidateKeyDomain_InvalidLogsWarning() {
        // Capture console output to validate the warning
        NamespaceKey.validateKeyDomain("MACRO:sub1::sub2");
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
    void testValidateKeyPath_InvalidLogsWarning() {
        // Use a mock logger or capture console output for assertion
        NamespaceKey.validateKeyPath("test.key..path");
    }

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
