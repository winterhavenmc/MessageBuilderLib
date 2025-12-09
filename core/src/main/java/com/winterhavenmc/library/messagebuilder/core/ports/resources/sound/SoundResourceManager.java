/*
 * Copyright (c) 2022-2025 Tim Savage.
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

package com.winterhavenmc.library.messagebuilder.core.ports.resources.sound;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.ResourceManager;


/**
 * An interface that describes a resource manager that is responsible for installing resources
 * from the plugin jar file to the plugin data directory, as well as loading and reloading
 * the installed files at startup or issuance of the {@code reload} command.
 */
public interface SoundResourceManager extends ResourceManager
{
}
