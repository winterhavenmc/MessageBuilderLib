package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.version;

import com.winterhavenmc.library.messagebuilder.core.context.AccessorCtx;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.version.Versionable;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class VersionAdapterTest
{
	@Mock Plugin pluginMock;
	@Mock Server serverMock;
	@Mock AccessorCtx accessorCtxMock;
	@Mock PluginDescriptionFile descriptionFileMock;


	@Test
	public void getVersion_with_valid_plugin()
	{
		// Arrange
		when(pluginMock.getDescription()).thenReturn(descriptionFileMock);
		when(descriptionFileMock.getVersion()).thenReturn("2.0.0-SNAPSHOT");

		// Act
		Optional<Versionable> adapter = new VersionAdapter(accessorCtxMock).adapt(pluginMock);

		String version = "";
		if (adapter.isPresent())
		{
			version = adapter.get().getVersion();
		}

		// Assert
		assertEquals("2.0.0-SNAPSHOT", version, "The adapter should return the version from the Plugin.");

		verify(pluginMock, atLeastOnce()).getDescription();
		verify(descriptionFileMock, atLeastOnce()).getVersion();
	}


	@Test
	public void getVersion_with_valid_server()
	{
		// Arrange
		when(serverMock.getVersion()).thenReturn("Some version string");

		// Act
		Optional<Versionable> adapter = new VersionAdapter(accessorCtxMock).adapt(serverMock);
		String result = adapter.get().getVersion();

		// Assert
		assertEquals("Some version string", result, "The adapter should return the version from the Server.");

		// Verify
		verify(serverMock, atLeastOnce()).getVersion();
	}


	@Test
	void adapt_returns_version_string_when_given_implementation()
	{
		// Arrange
		class TestObject implements Versionable
		{
			public String getVersion()
			{
				return "Version 2.0";
			}
		}

		TestObject testObject = new TestObject();
		VersionAdapter versionAdapter = new VersionAdapter(accessorCtxMock);

		// Act
		Optional<Versionable> result = versionAdapter.adapt(testObject);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("Version 2.0", result.get().getVersion());
	}


	@Test
	void adapt_returns_empty_optional_when_given_null()
	{
		// Arrange
		VersionAdapter versionAdapter = new VersionAdapter(accessorCtxMock);

		// Act
		Optional<Versionable> result = versionAdapter.adapt(null);

		// Assert
		assertTrue(result.isEmpty());
	}

}
