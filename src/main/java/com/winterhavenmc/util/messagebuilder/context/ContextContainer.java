/*
 * Copyright (c) 2024-2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.context;

import com.winterhavenmc.util.messagebuilder.macro.processor.ProcessorType;


public record ContextContainer<T>(T value, ProcessorType processorType) {

	@Override
	public String toString() {
		return String.format("ContextContainer{value=%s, processorType=%s}", value, processorType);
	}


	/**
	 * Utility method to create a new ContextContainer.
	 *
	 * @param value         the value to store in the container
	 * @param processorType the processor type associated with the value
	 * @param <T>           the type of the value
	 * @return a new instance of ContextContainer
	 */
	public static <T> ContextContainer<T> of(T value, ProcessorType processorType) {
		return new ContextContainer<>(value, processorType);
	}

}
