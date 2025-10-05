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

package com.winterhavenmc.library.messagebuilder.core.ports.resourcemanagers;


/**
 * An interface that describes classes that are responsible for installing resources
 * from the jar to the plugin data directory.
 */
public interface ResourceInstaller
{
	/**
	 * Install resources listed in auto_install.txt to the plugin data directory
	 */
	void autoInstall();


	enum InstallerStatus
	{
		UNAVAILABLE,
		FILE_EXISTS,
		SUCCESS,
		FAIL,
	}
}
