package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.url;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.uri.UriAdapter;
import com.winterhavenmc.library.messagebuilder.core.context.AccessorCtx;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.uri.UriAddressable;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.url.UrlAddressable;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UrlAdapterTest
{
	@Mock Plugin pluginMock;
	@Mock Server serverMock;
	@Mock AccessorCtx ctxMock;
	@Mock PluginDescriptionFile descriptionFileMock;


	@Test
	void adapt_returns_url_string_when_given_plugin_with_valid_website()
	{
		// Arrange
		when(pluginMock.getDescription()).thenReturn(descriptionFileMock);
		when(descriptionFileMock.getWebsite()).thenReturn("https://www.example.com");
		UrlAdapter urlAdapter = new UrlAdapter(ctxMock);

		// Act
		Optional<UrlAddressable> result = urlAdapter.adapt(pluginMock);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("https://www.example.com", result.get().getUrl());

		// Verify
		verify(pluginMock, atLeastOnce()).getDescription();
		verify(descriptionFileMock, atLeastOnce()).getWebsite();
	}


	@Test
	void adapt_returns_empty_url_string_when_given_plugin_with_unspecified_website()
	{
		// Arrange
		when(pluginMock.getDescription()).thenReturn(descriptionFileMock);
		when(descriptionFileMock.getWebsite()).thenReturn(null);
		UrlAdapter urlAdapter = new UrlAdapter(ctxMock);

		// Act
		Optional<UrlAddressable> result = urlAdapter.adapt(pluginMock);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("", result.get().getUrl());

		// Verify
		verify(pluginMock, atLeastOnce()).getDescription();
		verify(descriptionFileMock, atLeastOnce()).getWebsite();
	}


	@Test
	void adapt_returns_url_string_when_given_valid_server()
	{
		// Arrange
		when(serverMock.getIp()).thenReturn("192.168.0.1");
		when(serverMock.getPort()).thenReturn(25566);
		UrlAdapter urlAdapter = new UrlAdapter(ctxMock);

		// Act
		Optional<UrlAddressable> result = urlAdapter.adapt(serverMock);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("minecraft://192.168.0.1:25566", result.get().getUrl());

		// Verify
		verify(serverMock, atLeastOnce()).getIp();
		verify(serverMock, atLeastOnce()).getPort();
	}


	@Test
	void adapt_returns_url_when_given_implementation()
	{
		class TestObject implements UrlAddressable
		{
			public String getUrl()
			{
				return "https://example.com";
			}
		}

		// Arrange
		TestObject testObject = new TestObject();
		UrlAdapter urlAdapter = new UrlAdapter(ctxMock);

		// Act
		Optional<UrlAddressable> result = urlAdapter.adapt(testObject);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("https://example.com", result.get().getUrl());
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
	void adapt_returns_empty_optional_when_given_non_UrlAddressable_object()
	{
		// Arrange
		UrlAdapter urlAdapter = new UrlAdapter(ctxMock);

		// Act
		Optional<UrlAddressable> result = urlAdapter.adapt("string literal");

		// Assert
		assertTrue(result.isEmpty());
	}

}
