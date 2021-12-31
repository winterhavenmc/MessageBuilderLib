package com.winterhavenmc.util.messagebuilder;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.bukkit.configuration.Configuration;
import org.junit.jupiter.api.*;

import java.util.HashSet;
import java.util.Set;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileLoaderTests {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private ServerMock server;
    private PluginMain plugin;
    private FileLoader fileLoader;

    @BeforeAll
    public void setUp() {
        // Start the mock server
        server = MockBukkit.mock();

        // start the mock plugin
        plugin = MockBukkit.load(PluginMain.class);

        // start file loader
        fileLoader = new FileLoader(plugin);

    }

    @AfterAll
    public void tearDown() {
        // Stop the mock server
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("file loader not null.")
    void FileLoaderNotNull() {
        Assertions.assertNotNull(fileLoader);
    }

    @Test
    @DisplayName("file loader operational.")
        void FileLoaderTest() {
        Configuration configuration = fileLoader.getMessages();
        Assertions.assertNotNull(configuration);
        Assertions.assertNotNull(configuration.getString("MESSAGES.CHEST_SUCCESS"));
    }

    @Test
    @DisplayName("file loader get current filename not null.")
    void GetCurrentFilename() {
        Assertions.assertNotNull(fileLoader.getCurrentFilename(plugin, "en-US"));
        Assertions.assertTrue(fileLoader.getCurrentFilename(plugin, "en-US").endsWith("language/en-US.yml"));
    }


    @Nested
    class MatchMessageKeysTest {

        private Set<String> getConfigMessageNames() {
            return fileLoader.getMessages().getKeys(false);
        }

        private Set<String> getEnumMessageNames() {
            Set<String> enumMessageNames = new HashSet<>();
            for (MessageId messageId : MessageId.values()) {
                enumMessageNames.add(messageId.toString());
            }
            return enumMessageNames;
        }


        @Test
        @DisplayName("Match all MessageId enum members names against config keys.")
        void EnumContainsAllConfigMessages() {

            // check each enum member has corresponding key in message config file
            for (String messageName : getConfigMessageNames()) {
                Assertions.assertTrue(getEnumMessageNames().contains(messageName),
                        messageName + " is contained in MessageId enum.");
            }
        }


        @Test
        @DisplayName("Match all message config keys against MessageId enum member names.")
        void ConfigContainsAllEnumMessages() {

            // check each message config key has corresponding MessageId enum member
            for (String messageName : getConfigMessageNames()) {
                Assertions.assertTrue(getConfigMessageNames().contains(messageName),
                        messageName + " is contained in messages config file.");
            }
        }
    }


    @Test
    @DisplayName("Test language manager reload method.")
    void reload() {
        plugin.messageBuilder.reload();
        Assertions.assertNotNull(plugin.messageBuilder.languageHandler, "language manager not null after reload.");
    }


}
