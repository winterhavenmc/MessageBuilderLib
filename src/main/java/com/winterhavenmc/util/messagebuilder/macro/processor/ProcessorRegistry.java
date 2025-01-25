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

	private final EnumMap<ProcessorType, MacroProcessor> macroProcessorMap;
	private final DependencyContext context;


	/**
	 * Class constructor
	 *
	 * @param languageQueryHandler the language handler to be passed to macro processor constructors
	 */
	public ProcessorRegistry(final DependencyContext context, final LanguageQueryHandler languageQueryHandler) {
		if (context == null) { throw new LocalizedException(LocalizedException.MessageKey.PARAMETER_NULL, "context"); }
		if (languageQueryHandler == null) { throw new LocalizedException(LocalizedException.MessageKey.PARAMETER_NULL, "languageQueryHandler"); }

		this.context = context;
		macroProcessorMap = new EnumMap<>(ProcessorType.class);
	}


	// Get a processor, creating it lazily if necessary
	public MacroProcessor get(ProcessorType type) {
		return macroProcessorMap.computeIfAbsent(type, t -> t.create(context));
	}

}
