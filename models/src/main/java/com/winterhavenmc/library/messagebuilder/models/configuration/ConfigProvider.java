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


/**
 * Represents a functional interface for retrieving dynamic configuration values.
 * <p>
 * A {@code ConfigProvider} wraps a {@link java.util.function.Supplier Supplier}, enabling
 * deferred and always up-to-date access to individual settings defined in the plugin’s
 * {@code config.yml} file.
 * </p>
 *
 * <p>
 * Typical usage includes injecting implementations such as {@code LanguageProvider}
 * or {@code LocaleProvider} into dependent classes that require access to language
 * tags, locales, or other server-defined options.
 * </p>
 *
 * <p>
 * This abstraction decouples configuration access from implementation details,
 * promoting testability and runtime flexibility.
 * </p>
 *
 * @param <T> the type of the configuration value provided
 */
@FunctionalInterface
public interface ConfigProvider<T>
{
	T get();
}
