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

package com.winterhavenmc.library.messagebuilder.models.validation;

import java.util.logging.Level;


/**
 * Represents standard log severity levels used for validation feedback
 * and internal diagnostics.
 * <p>
 * This enum serves as a semantic bridge between commonly used log level
 * names (such as {@code TRACE}, {@code WARN}, {@code ERROR}) and their
 * corresponding {@link java.util.logging.Level} constants.
 * <p>
 * It is used primarily in situations where validation failures or warnings
 * are logged rather than thrown as exceptions.
 *
 * <p>
 * Example usage:
 * {@snippet lang="java":
 *     logger.log(LogLevel.WARN.toJavaUtilLevel(), "Validation issue detected...");
 * }
 *
 * @see Validator
 */
public enum LogLevel
{
	/**
	 * The lowest level of logging, typically used for very fine-grained tracing.
	 * Maps to {@link java.util.logging.Level#FINEST}.
	 */
	TRACE(Level.FINEST),

	/**
	 * A fine-grained debug message.
	 * Maps to {@link java.util.logging.Level#FINER}.
	 */
	DEBUG(Level.FINER),

	/**
	 * A standard informational message.
	 * Maps to {@link java.util.logging.Level#INFO}.
	 */
	INFO(Level.INFO),

	/**
	 * A warning that indicates a potential issue or recoverable problem.
	 * Maps to {@link java.util.logging.Level#WARNING}.
	 */
	WARN(Level.WARNING),

	/**
	 * A serious error that should be brought to immediate attention.
	 * Maps to {@link java.util.logging.Level#SEVERE}.
	 */
	ERROR(Level.SEVERE);

	private final Level javaLevel;


	/**
	 * Constructs a {@code LogLevel} enum constant with its mapped
	 * {@link java.util.logging.Level}.
	 *
	 * @param javaLevel the corresponding {@code java.util.logging.Level}
	 */
	LogLevel(Level javaLevel)
	{
		this.javaLevel = javaLevel;
	}


	/**
	 * Returns the corresponding {@link java.util.logging.Level} for this log level.
	 *
	 * @return the mapped {@code java.util.logging.Level}
	 */
	public Level toJavaUtilLevel()
	{
		return javaLevel;
	}

}
