package com.winterhavenmc.util.messagebuilder;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class YamlFileLoaderTests {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private ServerMock server;
    private Plugin plugin;
    private YamlFileLoader fileLoader;

    @BeforeEach
    public void setUp() {
        // Start the mock server
        server = MockBukkit.mock();

        // start the mock plugin
        plugin = MockBukkit.load(PluginMain.class);

        // start file loader
        fileLoader = new YamlFileLoader(plugin);

    }

    @AfterEach
    public void tearDown() {
        // Stop the mock server
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("file loader not null.")
    void FileLoaderNotNull() {
        assertNotNull(fileLoader);
    }

    @Test
    @DisplayName("file loader operational.")
        void FileLoaderTest() {
        Configuration configuration = fileLoader.getMessages();
        assertNotNull(configuration);
        assertNotNull(configuration.getString("MESSAGES.ENABLED_MESSAGE"));
    }

    @Test
    @DisplayName("file loader get current filename not null.")
    void GetLanguageFilenameTest() {
        assertNotNull(fileLoader.getLanguageFilename(plugin, "en-US"));
        assertTrue(fileLoader.getLanguageFilename(plugin, "en-US").endsWith("language" + File.separator + "en-US.yml"));
    }

    @Test
    @DisplayName("file loader get current filename non-existent.")
    void GetLanguageFilenameTest_nonexistent() {
        assertNotNull(fileLoader.getLanguageFilename(plugin, "not-a-valid-tag"));
        assertTrue(fileLoader.getLanguageFilename(plugin, "not-a-valid-tag").endsWith("language" + File.separator + "not-a-valid-tag.yml"));
    }

    @Test
    @DisplayName("languageFileExists test")
    void languageFileExistsTests_nonexistent() {
        assertNotNull(fileLoader.languageFileExists(plugin, "not-a-valid-tag"));
        assertEquals("en-US", fileLoader.languageFileExists(plugin, "not-a-valid-tag"));
    }

    @Test
    @DisplayName("getResourceName test")
    void getResourceNameTest() {
        assertNotNull(fileLoader.getResourceName("en-US"));
        assertEquals("language/en-US.yml", fileLoader.getResourceName("en-US"));
    }

    @Test
    @DisplayName("getResourceName test 2")
    void getResourceNameTest_nonexistent() {
        assertNotNull(fileLoader.getResourceName("not-a-valid-tag"));
        assertEquals("language/en-US.yml", fileLoader.getResourceName("not-a-valid-tag"));
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


        @Disabled
        @Test
        @DisplayName("Match all MessageId enum members names against config keys.")
        void EnumContainsAllConfigMessages() {

            // check each enum member has corresponding key in message config file
            for (String messageName : getConfigMessageNames()) {
                assertTrue(getEnumMessageNames().contains(messageName),
                        messageName + " not is contained in MessageId enum.");
            }
        }


        @Test
        @DisplayName("Match all message config keys against MessageId enum member names.")
        void ConfigContainsAllEnumMessages() {

            // check each message config key has corresponding MessageId enum member
            for (String messageName : getConfigMessageNames()) {
                assertTrue(getConfigMessageNames().contains(messageName),
                        messageName + " is contained in messages config file.");
            }
        }
    }

}
