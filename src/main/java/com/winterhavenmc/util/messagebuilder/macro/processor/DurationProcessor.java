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

import com.winterhavenmc.util.TimeUnit;
import com.winterhavenmc.util.messagebuilder.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.SectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.time.TimeSectionQueryHandler;

import java.time.Duration;


public class DurationProcessor extends MacroProcessorTemplate implements MacroProcessor {

	public DurationProcessor(LanguageQueryHandler queryHandler) {
		super(queryHandler);
	}

	@Override
	public <T> ResultMap resolveContext(final String keyPath, final ContextMap contextMap, final T value) {

		ResultMap resultMap = new ResultMap();

		if (value instanceof Duration duration) {
			TimeSectionQueryHandler timeQueryHandler = Section.TIME.getQueryHandler(queryHandler.getConfigurationSupplier());
			resultMap.put(keyPath, timeQueryHandler.getTimeString(duration.toMillis(), TimeUnit.MINUTES));
		}
		return resultMap;
	}

}
