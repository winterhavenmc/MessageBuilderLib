package com.winterhavenmc.util.messagebuilder;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.bukkit.ChatColor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LanguageHandlerTests {

    private ServerMock server;
    private PluginMain plugin;

    @BeforeAll
    public void setUp() {
        // Start the mock server
        server = MockBukkit.mock();

        // start the mock plugin
        plugin = MockBukkit.load(PluginMain.class);

    }

    @AfterAll
    public void tearDown() {
        // Stop the mock server
        MockBukkit.unmock();
    }

    @Nested
    @DisplayName("Test LanguageManager methods.")
    class LanguageHandler {
        @Test
        @DisplayName("language manager getItemName is valid.")
        void GetItemNameTest() {
            Assertions.assertNotNull(plugin.languageHandler.getItemName());
            Assertions.assertEquals("Item", ChatColor.stripColor(plugin.languageHandler.getItemName()));
        }

        @Test
        @DisplayName("language manager getItemNamePlural is valid.")
        void GetItemNamePluralTest() {
            Assertions.assertNotNull(plugin.languageHandler.getItemNamePlural());
            Assertions.assertEquals("Items", ChatColor.stripColor(plugin.languageHandler.getItemNamePlural()));
        }

        @Test
        @DisplayName("language manager getItemLore not null.")
        void GetItemLoreTest() {

            Assertions.assertNotNull(plugin.languageHandler.getItemLore());
        }

        @Test
        @DisplayName("language manager getSpawnDisplayName is valid.")
        void GetSpawnDisplayNameTest() {
            Assertions.assertNotNull(plugin.languageHandler.getSpawnDisplayName());
            Assertions.assertEquals("spawn", ChatColor.stripColor(plugin.languageHandler.getSpawnDisplayName()));
        }

        @ParameterizedTest
        @EnumSource(MessageId.class)
        @DisplayName("enum member messageId is contained in getConfig() keys.")
        void FileKeysContainsEnumValue(MessageId messageId) {
            Assertions.assertNotNull(plugin.languageHandler.getMessage(messageId),"Enum value '" + messageId.name() + "' not in messages file");
            System.out.println("Enum value '" + messageId.name() + "' contained in messages file");
        }

//        @Test
//        void MessageKeysIsEmpty() {
//            Assertions.assertFalse(plugin.languageHandler.getMessageKeys().isEmpty());
//        }
//
//        @Test
//        void MessageKeysIsEmpty2() {
//            Assertions.assertTrue(plugin.languageHandler.getMessageKeys().size() > 0);
//        }

        @Test
        void test1() {
            Assertions.assertNotNull(plugin.languageHandler.getMessageKeys());
            for (String key : plugin.languageHandler.getMessageKeys()) {
                System.out.println("Key: " + key);
            }
        }


    }
}
