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
 * Represents a configured language file identifier.
 * <p>
 * This record encapsulates a string such as {@code en-US} or {@code fr-FR},
 * typically sourced from the plugin's {@code config.yml}. It is used by
 * localization components such as {@link LanguageProvider} and downstream
 * language or formatting tools to select appropriate resource files, and may
 * also be queried for the locale setting. This allows server operators to include
 * only one setting for both language file and locale in the plugin {@code config.yml}
 * file, assuming the chosen setting conforms to a valid IETF language tag.
 * </p>
 *
 * <p>
 * The {@code name} field is expected to follow the IETF BCP 47 language tag format
 * (e.g., {@code en-US}, {@code de-DE}), although enforcement is deferred to consumers,
 * to allow custom language file names that do not interfere with existing or provided
 * language file names.
 * </p>
 *
 * @param name the raw language or locale tag string, never {@code null}
 */
public record LanguageSetting(@NotNull String name) { }
