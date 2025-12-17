package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.worldname;

import org.bukkit.World;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DefaultWorldNameRetrieverTest
{
	@Mock World worldMock;


	@Test
	void getWorldName_returns_empty_optional_given_null_world()
	{
		// Act
		DefaultWorldNameRetriever worldNameRetriever = new DefaultWorldNameRetriever();
		Optional<String> result = worldNameRetriever.getWorldName(null);

		// Assert
		assertTrue(result.isEmpty());
	}


	@Test
	void getWorldName_returns_optional_string_given_valid_world()
	{
		// Arrange
		when(worldMock.getName()).thenReturn("test world name");

		// Act
		DefaultWorldNameRetriever worldNameRetriever = new DefaultWorldNameRetriever();
		Optional<String> result = worldNameRetriever.getWorldName(worldMock);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("test world name", result.get());
	}

}
