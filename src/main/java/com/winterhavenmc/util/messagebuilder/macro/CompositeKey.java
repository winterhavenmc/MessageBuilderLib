/*
 * Copyright (c) 2024 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.macro;

import com.winterhavenmc.util.messagebuilder.macro.processor.ProcessorType;

/**
 * CompositeKey is a private static class used as the key for the context map.
 * It combines a MacroProcessorType and a String (derived from the Macro constant's name).
 */
public class CompositeKey {
	private final ProcessorType type;
	private final String macroName;

	// create a new composite key from a processor type and a String
	public CompositeKey(ProcessorType type, String macroName) {
		this.type = type;
		this.macroName = macroName;
	}

	// create a new composite key from a processor type and a Macro constant name as string
	public <Macro> CompositeKey(ProcessorType type, Macro macro) {
		this.type = type;
		this.macroName = macro.toString();
	}

	// provide a getter for the processor type
	public ProcessorType getType() {
		return type;
	}

	// provide a getter for the macro name string
	public String getMacroName() {
		return macroName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CompositeKey that = (CompositeKey) o;
		return type == that.type && macroName.equals(that.macroName);
	}

	@Override
	public int hashCode() {
		return 31 * type.hashCode() + macroName.hashCode();
	}
}
