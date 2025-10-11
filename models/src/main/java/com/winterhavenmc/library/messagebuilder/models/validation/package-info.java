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

/**
 * Provides a flexible, localized validation framework for use within the MessageBuilder library.
 *
 * <p>
 * This package defines a lightweight set of tools for validating input parameters and plugin configuration
 * in a way that supports both exception-based and logging-based workflows. All error messages are resolved
 * using structured keys from {@link com.winterhavenmc.library.messagebuilder.models.validation.ErrorMessageKey},
 * enabling localization through a {@link java.util.ResourceBundle}.
 *
 * <h2>Core Concepts</h2>
 * <ul>
 *     <li>{@link com.winterhavenmc.library.messagebuilder.models.validation.Validator}
 *     — a functional interface representing a strategy for handling invalid values (e.g., throwing or logging).</li>
 *     <li>{@link com.winterhavenmc.library.messagebuilder.models.validation.ValidationException}
 *     — a custom runtime exception for reporting validation failures with localized messages.</li>
 *     <li>{@link java.util.function.Predicate Predicate}
 *     — a funtional predicate passed as a parameter to the {@code validate} method to check against a parameter.</li>
 *     <li>{@link com.winterhavenmc.library.messagebuilder.models.validation.ErrorMessageKey}
 *     — an enum constant that corresponds to an error message string in the {@link java.util.ResourceBundle}</li>
 *     <li>{@link com.winterhavenmc.library.messagebuilder.models.validation.Parameter}
 *     — an enum of symbolic parameter names used to identify invalid arguments in error messages.</li>
 * </ul>
 *
 * <h2>Usage Pattern</h2>
 * Throwing exception:
 * {@snippet lang = "java":
 *     import com.winterhavenmc.library.messagebuilder.models.validation.ErrorMessageKey;import com.winterhavenmc.library.messagebuilder.models.validation.Parameter;validate(recipient, Objects::isNull, throwing(ErrorMessageKey.PARAMETER_NULL, Parameter.RECIPIENT));
 *}
 * Logging error and returning default value:
 * {@snippet lang = "java":
 *     import com.winterhavenmc.library.messagebuilder.models.validation.ErrorMessageKey;import com.winterhavenmc.library.messagebuilder.models.validation.LogLevel;import com.winterhavenmc.library.messagebuilder.models.validation.Parameter;String validName = validate(name, String::isBlank, logging(LogLevel.INFO, ErrorMessageKey.STRING_BLANK, Parameter.NAME)).orElse("Steve");
 *}
 * <h2>Localization</h2>
 * <p>
 * Localized messages are loaded from the {@code exception.messages} resource bundle.
 * The locale may default to {@link java.util.Locale#getDefault()}, but is designed to be
 * overridable via plugin configuration through a global locale context.
 *
 * @see com.winterhavenmc.library.messagebuilder.models.validation.Validator
 * @see com.winterhavenmc.library.messagebuilder.models.validation.ValidationException
 * @see com.winterhavenmc.library.messagebuilder.models.validation.ErrorMessageKey
 * @see com.winterhavenmc.library.messagebuilder.models.validation.Parameter
 * @see com.winterhavenmc.library.messagebuilder.models.validation.LogLevel
 */
package com.winterhavenmc.library.messagebuilder.models.validation;
