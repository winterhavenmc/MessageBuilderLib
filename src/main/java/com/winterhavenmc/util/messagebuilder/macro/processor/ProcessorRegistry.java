/*
 * Copyright (c) 2022 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.macro.processor;

import java.util.EnumMap;


/**
 * This class implements a map of unique macro processor instances using ProcessorType Enum members as keys.
 * As such, it is backed by an EnumMap.
 */
public class ProcessorRegistry {

	// the backing store EnumMap
	private final EnumMap<ProcessorType, Processor> macroProcessorMap = new EnumMap<>(ProcessorType.class);


	/**
	 * This method inserts an entry into the map
	 * @param type the macro processor type, an Enum member, used as the key
	 * @param macroProcessor the macro processor instance, used as the value
	 */
	public void put(final ProcessorType type, final Processor macroProcessor) {
		this.macroProcessorMap.put(type, macroProcessor);
	}

	/**
	 * This method retrieves a macro processor instance from the map by the MacroProcessorType key
	 * @param macroProcessorType the macro processor type key
	 * @return The macro processor instance stored in the map that is referenced by the key
	 */
	public Processor get(final ProcessorType macroProcessorType) {
		return macroProcessorMap.get(macroProcessorType);
	}

}
