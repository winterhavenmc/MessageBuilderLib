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

import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;

import java.util.EnumMap;


/**
 * This class implements a map of unique macro processor instances using ProcessorType Enum members as keys.
 * As such, it is backed by an EnumMap.
 */
public class ProcessorRegistry {

	private final EnumMap<ProcessorType, MacroProcessor<?>> macroProcessorMap;


	/**
	 * Class constructor
	 *
	 * @param languageQueryHandler the language handler to be passed to macro processor constructors
	 */
	public ProcessorRegistry(final LanguageQueryHandler languageQueryHandler) {
		if (languageQueryHandler == null) { throw new LocalizedException(LocalizedException.MessageKey.PARAMETER_NULL, "languageQueryHandler"); }

		macroProcessorMap = new EnumMap<>(ProcessorType.class);
	}


	/**
	 * This method retrieves a macro processor instance from the map by the MacroProcessorType key
	 *
	 * @param processorType the macro processor type key
	 * @return The macro processor instance stored in the map that is referenced by the key
	 */
	public MacroProcessor<?> get(final ProcessorType processorType) {
		macroProcessorMap.computeIfAbsent(processorType, ProcessorType::create);
		return macroProcessorMap.get(processorType);
	}

}
