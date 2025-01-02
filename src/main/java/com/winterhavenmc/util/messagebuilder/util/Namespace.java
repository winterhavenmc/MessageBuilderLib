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

public class Namespace {


	/**
	 * This class contains enums that are used as a component to create a unique namespace,
	 * It is primarily used in the formation of unique keys for use in &lt;K,V&gt; data structures, such as maps.
 	 */
	private Namespace() { }


	/**
	 * This enum represents the top-level categories that must be used as the root of the keyDomain.
	 * They are subject to change at this time, and will likely be reduced to an appropriate set as necessary.
	 * I selected a wide array of categories here until I narrow down those I find useful.
	 */
	public enum Domain {

		ITEMS, // values supplied by the yaml language file, from the root level section 'ITEMS'
		CONSTANTS, //values supplied by the yaml language file, from the root level section 'CONSTANTS'
		MACRO, // values passed in by calls to the setMacro method

		// potential future domains
//		ENV, // environment variables, such as those made available by the operating system
//		RUNTIME, // runtime variables, such as those made available by the Java Runtime Environment
//		SERVER, // server variables, made available by the bukkit server instance running the plugin
//		PLUGIN, // plugin variables (could use subcategories to hold info about other plugins)
//		LIBRARY, // library variables, those specific to this library
//		RESERVED, // not for you variables?
	}

}
