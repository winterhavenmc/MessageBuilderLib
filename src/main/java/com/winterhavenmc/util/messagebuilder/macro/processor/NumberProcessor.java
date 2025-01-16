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
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.time.TimeSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.context.ContextMap;


public class NumberProcessor extends MacroProcessorTemplate implements MacroProcessor {

	private final LanguageQueryHandler queryHandler;

	public NumberProcessor(LanguageQueryHandler queryHandler) {
		super(queryHandler);
		this.queryHandler = queryHandler;
	}

	@Override
	public <T> ResultMap resolveContext(final String key, final ContextMap contextMap, final T value) {

		ResultMap resultMap = new ResultMap();

		if (value == null) {
			return resultMap;
		}

//		if (value instanceof Long longVar) {
//
//			// put string value of longVar in result map, to be overwritten if macroName ends a Duration string match
//			resultMap.put(key, String.valueOf(longVar));
//
//			for (DurationSuffix durationSuffix : DurationSuffix.values()) {
//				String[] keyComponents = key.split(":", 2);
//				String category = keyComponents[0];
//				if (category.endsWith(durationSuffix.name())) {
//
//					// get TimeSectionQueryHandler from SectionQueryFactory
//					TimeSectionQueryHandler timeSectionQueryHandler = (TimeSectionQueryHandler) queryHandler.getSectionQueryHandler(Section.TIME);
//					if (timeSectionQueryHandler != null) {
//						resultMap.put(key, timeSectionQueryHandler.getTimeString(longVar, durationSuffix.getTimeUnit()));
//					}
//				}
//			}
//		}

		resultMap.put(key, String.valueOf(value));

		return resultMap;
	}


	/**
	 * An enum of macro name suffixes that will result in a TimeStringOld being returned for the corresponding TimeUnit.
	 * This allows iterating over the suffixes for a matching macro name suffix, and passing the associated TimeUnit to
	 * {@code queryHandler.getTimeString()}.
	 */
	enum DurationSuffix {
		DURATION(TimeUnit.SECONDS),
		DURATION_SECONDS(TimeUnit.SECONDS),
		DURATION_MINUTES(TimeUnit.MINUTES),
		DURATION_HOURS(TimeUnit.HOURS),
		DURATION_DAYS(TimeUnit.DAYS),
		;

		private final TimeUnit timeUnit;

		// constructor
		DurationSuffix(TimeUnit timeUnit) {
			this.timeUnit = timeUnit;
		}

		TimeUnit getTimeUnit() {
			return this.timeUnit;
		}
	}

}
