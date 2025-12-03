package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.uri;

import com.winterhavenmc.library.messagebuilder.core.context.AccessorCtx;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.uri.UriAddressable;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UriAdapterTest
{
	@Mock Plugin pluginMock;
	@Mock Server serverMock;
	@Mock AccessorCtx ctxMock;
	@Mock PluginDescriptionFile descriptionFileMock;


	@Test
	void adapt_returns_uri_string()
	{
		// Arrange
		when(pluginMock.getDescription()).thenReturn(descriptionFileMock);
		when(descriptionFileMock.getWebsite()).thenReturn("https://www.example.com");
		UriAdapter uriAdapter = new UriAdapter(ctxMock);

		// Act
		Optional<UriAddressable> result = uriAdapter.adapt(pluginMock);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("https://www.example.com", result.get().getUri().toASCIIString());

		// Verify
		verify(pluginMock, atLeastOnce()).getDescription();
		verify(descriptionFileMock, atLeastOnce()).getWebsite();
	}


	@Test
	void adapt_returns_empty_uri_when_given_plugin_with_unspecified_website()
	{
		// Arrange
		when(pluginMock.getDescription()).thenReturn(descriptionFileMock);
		when(descriptionFileMock.getWebsite()).thenReturn(null);
		UriAdapter uriAdapter = new UriAdapter(ctxMock);

		// Act
		Optional<UriAddressable> result = uriAdapter.adapt(pluginMock);

		// Assert
		assertTrue(result.isPresent());
		assertEquals(URI.create(""), result.get().getUri());

		// Verify
		verify(pluginMock, atLeastOnce()).getDescription();
		verify(descriptionFileMock, atLeastOnce()).getWebsite();
	}


	@Test
	void adapt_returns_uri_string_when_given_valid_server()
	{
		// Arrange
		when(serverMock.getIp()).thenReturn("192.168.0.1");
		when(serverMock.getPort()).thenReturn(25566);
		UriAdapter uriAdapter = new UriAdapter(ctxMock);

		// Act
		Optional<UriAddressable> result = uriAdapter.adapt(serverMock);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("minecraft://192.168.0.1:25566", result.get().getUri().toASCIIString());

		// Verify
		verify(serverMock, atLeastOnce()).getIp();
		verify(serverMock, atLeastOnce()).getPort();
	}


	@Test
	void adapt_returns_uri_when_given_valid_uri()
	{
		// Arrange
		URI uri = URI.create("https://example.com");
		UriAdapter uriAdapter = new UriAdapter(ctxMock);

		// Act
		Optional<UriAddressable> result = uriAdapter.adapt(uri);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("https://example.com", result.get().getUri().toASCIIString());
	}


	@Test
	void adapt_returns_uri_when_given_implementation()
	{
		class TestObject implements UriAddressable
		{
			public URI getUri()
			{
				return URI.create("https://example.com");
			}
		}

		// Arrange
		TestObject testObject = new TestObject();
		UriAdapter uriAdapter = new UriAdapter(ctxMock);

		// Act
		Optional<UriAddressable> result = uriAdapter.adapt(testObject);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("https://example.com", result.get().getUri().toASCIIString());
	}


	@Test
	void adapt_returns_empty_optional_when_given_null()
	{
		// Arrange
		UriAdapter uriAdapter = new UriAdapter(ctxMock);

		// Act
		Optional<UriAddressable> result = uriAdapter.adapt(null);

		// Assert
		assertTrue(result.isEmpty());
	}


	@Test
	void adapt_returns_default_uri_string_when_given_server_without_ip_or_port()
	{
		// Arrange
		when(serverMock.getIp()).thenReturn("");
		when(serverMock.getPort()).thenReturn(0);
		UriAdapter uriAdapter = new UriAdapter(ctxMock);

		// Act
		Optional<UriAddressable> result = uriAdapter.adapt(serverMock);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("minecraft://0.0.0.0:25565", result.get().getUri().toASCIIString());

		// Verify
		verify(serverMock, atLeastOnce()).getIp();
		verify(serverMock, atLeastOnce()).getPort();
	}


	@Test
	void adapt_returns_empty_optional_when_given_non_UriAddressable_object()
	{
		// Arrange
		UriAdapter uriAdapter = new UriAdapter(ctxMock);

		// Act
		Optional<UriAddressable> result = uriAdapter.adapt("string literal");

		// Assert
		assertTrue(result.isEmpty());
	}

}
