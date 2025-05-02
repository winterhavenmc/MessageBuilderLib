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

package com.winterhavenmc.library.messagebuilder.resources.configuration;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Optional;


/**
 * Represents a language file setting selected from configuration.
 * Includes the raw name, the expected file location, and an optional parsed {@link LanguageTag}.
 */
public record LanguageSetting(
		@NotNull String name,
		@NotNull File file,
		@NotNull Optional<LanguageTag> tag)
{
	/**
	 * Returns whether the expected file currently exists on disk.
	 */
	@Contract(pure = true)
	public boolean exists()
	{
		return file.isFile();
	}


	/**
	 * Returns whether the name is a valid BCP 47 tag recognized by the JVM.
	 */
	@Contract(pure = true)
	public boolean isConformant()
	{
		return tag.isPresent();
	}


	@Override @NotNull @Contract(pure = true)
	public String toString()
	{
		return name;
	}

}
