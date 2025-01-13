/*
 * Copyright (c) 2024-2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.context;

import com.winterhavenmc.util.messagebuilder.macro.processor.ProcessorType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class ContextMapTest {

	@Mock private CommandSender commandSenderMock;
	private ContextMap contextMap;

	@BeforeEach
	void setUp() {
		contextMap = new ContextMap(commandSenderMock);
	}

	@Test
	void testPutAndGetContainer() {
		// Arrange
		String key = "MACRO|NUMBER";
		Integer number =42;
		ProcessorType processorType = ProcessorType.NUMBER;
		ContextContainer<Number> container = ContextContainer.of(number, processorType);

		// Act
		contextMap.put(key, container);
		Optional<ContextContainer<?>> retrievedContainer = contextMap.getContainer(key);

		// Assert
		assertTrue(retrievedContainer.isPresent(), "Container should be present");
		assertEquals(container, retrievedContainer.get(), "Retrieved container should match the original");
	}

	@Test
	void testPutDirectlyAndRetrieveContainer() {
		// Arrange
		String key = "Item::Sword";
		ItemStack itemStack = new ItemStack(Material.STONE);
		ProcessorType processorType = ProcessorType.ITEM_STACK;

		// Act
		contextMap.put(key, itemStack, processorType);
		Optional<ContextContainer<?>> retrievedContainer = contextMap.getContainer(key);

		// Assert
		assertTrue(retrievedContainer.isPresent(), "Container should be present");
		assertEquals(itemStack, retrievedContainer.get().value(), "Retrieved value should match the original");
		assertEquals(processorType, retrievedContainer.get().processorType(), "ProcessorType should match the original");
	}

	@Test
	void testGetValueWithCorrectType() {
		// Arrange
		String key = "Location::Player";
		World world = mock(World.class, "MockWorld");
		Location location = new Location(world, 10, 20, 30);
		ProcessorType processorType = ProcessorType.LOCATION;
		contextMap.put(key, location, processorType);

		// Act
		Optional<Location> retrievedValue = contextMap.getValue(key, Location.class);

		// Assert
		assertTrue(retrievedValue.isPresent(), "Value should be present");
		assertEquals(location, retrievedValue.get(), "Retrieved value should match the original");
	}

	@Test
	void testGetValueWithIncorrectType() {
		// Arrange
		String key = "Item::Sword";
		ItemStack itemStack = new ItemStack(Material.STONE);
		ProcessorType processorType = ProcessorType.ITEM_STACK;
		contextMap.put(key, itemStack, processorType);

		// Act
		Optional<Location> retrievedValue = contextMap.getValue(key, Location.class);

		// Assert
		assertFalse(retrievedValue.isPresent(), "Value should not be present for mismatched type");
	}

	@Test
	void testGetProcessorType() {
		// Arrange
		String key = "Location::Player";
		World world = mock(World.class, "MockWorld");
		Location location = new Location(world, 10, 20, 30);
		ProcessorType processorType = ProcessorType.LOCATION;
		contextMap.put(key, location, processorType);

		// Act
		Optional<ProcessorType> retrievedProcessorType = contextMap.getProcessorType(key);

		// Assert
		assertTrue(retrievedProcessorType.isPresent(), "ProcessorType should be present");
		assertEquals(processorType, retrievedProcessorType.get(), "Retrieved ProcessorType should match the original");
	}

	@Test
	void testContainsKey() {
		// Arrange
		String key = "MACRO|LOCATION";
		World world = mock(World.class, "MockWorld");
		Location location = new Location(world, 10, 20, 30);
		ProcessorType processorType = ProcessorType.LOCATION;
		contextMap.put(key, location, processorType);

		// Act & Assert
		assertTrue(contextMap.containsKey(key), "Key should be present in the map");
		assertFalse(contextMap.containsKey("NonExistentKey"), "Key should not be present in the map");
	}

	@Test
	void testEmptyMap() {
		// Act & Assert
		assertFalse(contextMap.containsKey("SomeKey"), "Empty map should not contain any keys");
		assertFalse(contextMap.getContainer("SomeKey").isPresent(), "Empty map should return empty Optional for containers");
		assertFalse(contextMap.getProcessorType("SomeKey").isPresent(), "Empty map should return empty Optional for processor type");
		assertFalse(contextMap.getValue("SomeKey", Object.class).isPresent(), "Empty map should return empty Optional for values");
	}
}
