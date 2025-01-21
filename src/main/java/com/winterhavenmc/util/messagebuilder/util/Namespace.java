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

package com.winterhavenmc.util.messagebuilder.util;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.constants.ConstantSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.items.ItemSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.time.TimeSectionQueryHandler;

/**
 * This class contains enums that are used as a component to create a unique namespace,
 * It is primarily used in the formation of unique keys for use in &lt;K,V&gt; data structures, such as maps.
 */
public class Namespace {

	private Namespace() { /* private constructor to prevent instantiation */ }

	/**
	 * This enum represents the top-level domains that must be used as the root of the keyDomain.
	 * They are subject to change at this time, and will likely be reduced to an appropriate set as necessary.
	 * I selected a wide array of domains here until I narrow down those I find useful.
	 * <p>
	 * NOTE: THIS ENUM REPRESENTS THE SOURCE OF TRUTH FOR DOMAIN VALUES, USED IN PRODUCTION. IT MAY BE MOVED TO
	 * A MORE APPROPRIATE LOCATION IN THE FUTURE
	 * </P>
	 */
	public enum Domain {
		CONSTANTS(ConstantSectionQueryHandler.class), // values supplied by the yaml language file, from the root level section 'CONSTANTS'
		ITEMS(ItemSectionQueryHandler.class), // values supplied by the yaml language file, from the root level section 'ITEMS'
		MACRO(null), // values passed in by calls to the setMacro method
		MESSAGES(MessageSectionQueryHandler.class), // values supplied by the yaml language file. from the root level section 'MESSAGES'
		TIME(TimeSectionQueryHandler.class), // string values for time units supplied by the yaml language file, from the root level section 'TIME'
		;

		// placeholder prefix to prevent name collisions between domains
		private final Class<?> queryHandler;

		Domain(final Class<?> queryHandler) {
			this.queryHandler = queryHandler;
		}
		public Class<?> getQueryHandler() {
			return this.queryHandler;
		}
	}

}
