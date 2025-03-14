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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml;


/**
 * A sealed interface representing a scalar (single value) in a YAML document.
 * Scalars include strings, numbers, booleans, and null values.
 */
sealed interface YamlScalar extends YamlNode { }

record YamlString(String value) implements YamlScalar { }
record YamlNumber(Number value) implements YamlScalar { }
record YamlBoolean(boolean value) implements YamlScalar { }
record YamlNull() implements YamlScalar { }
