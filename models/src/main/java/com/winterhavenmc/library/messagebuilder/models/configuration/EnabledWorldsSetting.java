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

package com.winterhavenmc.library.messagebuilder.models.configuration;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;


/**
 * Represents a list of enabled worlds, derived from the 'enabled-worlds' and 'disabled-worlds' settings
 * in the plugin configuration
 *
 * @param worldUids the list of enabled world uuids
 * @since 2.0.0
 */
public record EnabledWorldsSetting(@NotNull List<UUID> worldUids) { }
