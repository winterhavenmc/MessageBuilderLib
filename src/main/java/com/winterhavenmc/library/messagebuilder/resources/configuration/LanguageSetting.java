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

import org.jetbrains.annotations.NotNull;


/**
 * Represents a configured language or locale identifier.
 * <p>
 * This record encapsulates a string such as {@code en-US} or {@code fr-FR},
 * typically sourced from the plugin's {@code config.yml}. It is used by
 * localization components such as {@link LanguageProvider} and downstream
 * language or formatting tools to select appropriate resource files or
 * locale-sensitive formatters.
 * </p>
 *
 * <p>
 * The {@code name} field is expected to follow the IETF BCP 47 language tag format
 * (e.g., {@code en-US}, {@code de-DE}), although enforcement is deferred to consumers.
 * </p>
 *
 * @param name the raw language or locale tag string, never {@code null}
 */
public record LanguageSetting(@NotNull String name)
{
	@Override @NotNull
	public String toString()
	{
		return name;
	}
}
