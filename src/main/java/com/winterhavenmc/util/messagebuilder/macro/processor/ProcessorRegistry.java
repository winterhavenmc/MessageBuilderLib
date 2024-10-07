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

import com.winterhavenmc.util.messagebuilder.macro.processor.Processor;
import com.winterhavenmc.util.messagebuilder.macro.processor.ProcessorType;

import java.util.EnumMap;

public class ProcessorRegistry {

	private final EnumMap<ProcessorType, Processor> macroProcessorMap = new EnumMap<>(ProcessorType.class);


	public void put(ProcessorType type, Processor macroProcessor) {
		this.macroProcessorMap.put(type, macroProcessor);
	}

	public Processor get(ProcessorType macroProcessorType) {
		return macroProcessorMap.get(macroProcessorType);
	}

}
