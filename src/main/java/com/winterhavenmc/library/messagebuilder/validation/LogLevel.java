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

package com.winterhavenmc.library.messagebuilder.validation;

import java.util.logging.Level;


public enum LogLevel
{
	TRACE(Level.FINEST),
	DEBUG(Level.FINER),
	INFO(Level.INFO),
	WARN(Level.WARNING),
	ERROR(Level.SEVERE);

	private final Level javaLevel;


	LogLevel(Level javaLevel)
	{
		this.javaLevel = javaLevel;
	}


	public Level toJavaUtilLevel()
	{
		return javaLevel;
	}

}
