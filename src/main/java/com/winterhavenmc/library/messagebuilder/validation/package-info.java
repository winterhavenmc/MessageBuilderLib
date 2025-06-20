/**
 * Provides a flexible, localized validation framework for use within the MessageBuilder library.
 *
 * <p>
 * This package defines a lightweight set of tools for validating input parameters and plugin configuration
 * in a way that supports both exception-based and logging-based workflows. All error messages are resolved
 * using structured keys from {@link com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey},
 * enabling localization through {@link java.util.ResourceBundle}.
 *
 * <h2>Core Concepts</h2>
 * <ul>
 *     <li>{@link com.winterhavenmc.library.messagebuilder.validation.ValidationException} —
 *         a custom runtime exception for reporting validation failures with localized messages.</li>
 *     <li>{@link com.winterhavenmc.library.messagebuilder.validation.Parameter} —
 *         an enum of symbolic parameter names used to identify invalid arguments in error messages.</li>
 *     <li>{@link com.winterhavenmc.library.messagebuilder.validation.Validator} —
 *         a functional interface representing a strategy for handling invalid values (e.g., throwing or logging).</li>
 *     <li>{@link com.winterhavenmc.library.messagebuilder.validation.ValidationUtility} —
 *         a static utility class for performing validations and formatting localized messages.</li>
 * </ul>
 *
 * <h2>Usage Pattern</h2>
 * <pre>{@code
 *     Optional<String> result = validate(name, String::isBlank,
 *         throwing(STRING_BLANK, Parameter.PLAYER_NAME));
 * }</pre>
 *
 * <h2>Localization</h2>
 * <p>
 * Localized messages are loaded from the {@code exception.messages} resource bundle.
 * The locale may default to {@link java.util.Locale#getDefault()}, but is designed to be
 * overridable via plugin configuration through a global locale context.
 *
 * @see com.winterhavenmc.library.messagebuilder.validation.ValidationUtility
 * @see com.winterhavenmc.library.messagebuilder.validation.Validator
 * @see com.winterhavenmc.library.messagebuilder.validation.ValidationException
 * @see com.winterhavenmc.library.messagebuilder.validation.Parameter
 * @see com.winterhavenmc.library.messagebuilder.validation.LogLevel
 */
package com.winterhavenmc.library.messagebuilder.validation;
