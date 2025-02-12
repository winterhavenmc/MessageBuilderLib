/*
 * Copyright (c) 2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MultiverseHelperTest {

	@Mock World worldMock;
	@Mock PluginManager pluginManagerMock;

	static MockedStatic<Bukkit> mockStatic;

	@BeforeAll
	static void preSetup() {
		mockStatic = mockStatic(Bukkit.class);
	}

	@AfterEach
	void tearDown() {
		worldMock = null;
	}


	@Test
	void getAlias() {
		// Arrange
		mockStatic.when(Bukkit::getPluginManager).thenReturn(pluginManagerMock);

		// Act
		Optional<String> alias = MultiverseHelper.getAlias(worldMock);

		// Assert
		assertTrue(alias.isEmpty());
	}

}
