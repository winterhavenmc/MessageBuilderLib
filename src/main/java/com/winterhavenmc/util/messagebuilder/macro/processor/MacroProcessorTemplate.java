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
import com.winterhavenmc.util.messagebuilder.util.Error;


/**
 * This skeleton class makes the queryHandler available to any extending classes,
 * as well as the UNKNOWN_VALUE string constant.
 * Methods used by all classes may be placed here in the future.
 */
public abstract class MacroProcessorTemplate implements MacroProcessor {

	final static String UNKNOWN_VALUE = "???";

	public final LanguageQueryHandler queryHandler;


	/**
	 * Abstract class constructor
	 * @param queryHandler the LanguageQueryHandler used by the classes that extend this abstract class
	 */
	MacroProcessorTemplate(final LanguageQueryHandler queryHandler) {
		if (queryHandler == null) { throw new IllegalArgumentException(Error.Parameter.NULL_QUERY_HANDLER.getMessage()); }
		this.queryHandler = queryHandler;
	}

}
