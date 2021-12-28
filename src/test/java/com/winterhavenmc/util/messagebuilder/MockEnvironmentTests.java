package com.winterhavenmc.util.messagebuilder;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.junit.jupiter.api.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MockEnvironmentTests {

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
    @DisplayName("Test mock objects.")
    class Mocking {

        @Test
        @DisplayName("mock server not null.")
        void MockServerNotNull() {
            Assertions.assertNotNull(server);
        }

        @Test
        @DisplayName("mock plugin not null.")
        void MockPluginNotNull() {
            Assertions.assertNotNull(plugin);
        }
    }

}
