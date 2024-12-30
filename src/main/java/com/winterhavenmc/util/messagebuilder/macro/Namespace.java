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

public class Namespace {

	/**
	 * This enum represents the top-level categories that must be used as the root of the keyDomain.
	 * They are subject to change at this time, and will likely be reduced to an appropriate set as necessary.
	 * I selected a wide array of categories here until I narrow down those I find useful.
	 */
	public enum Category {

		// from the language file top sections we want to use
		ITEMS, // in use now
		CONSTANTS, // in use now
		MACRO, // in use now
		LOCATIONS, // could go under constants if we are just setting static text. perhaps a subcategory of constants

		TYPES, // intriguing. perhaps a method to encode type information in the key.

		// from other sources, both internal and external
		ENV, // environment variables
		RUNTIME, // runtime variables
		SERVER, // server variables
		PLUGIN, // plugin variables (could use subcategories to hold info about other plugins)
		LIBRARY, // library variables,
		RESERVED, // not for you variables?
	}


	// will likely migrate to this from Category
	public enum Source {
		// from the language file top sections we want to use
		ITEMS, // in use now
		CONSTANTS, // in use now
		MACRO, // in use now
		LOCATIONS, // could go under constants if we are just setting static text. perhaps a subcategory of constants

		// from other sources, both internal and external
//		ENV, // environment variables, such as those made available by the operating system
//		RUNTIME, // runtime variables, such as those made available by the Java Runtime Environment
//		SERVER, // server variables, made available by the bukkit server instance running the plugin
//		PLUGIN, // plugin variables (could use subcategories to hold info about other plugins)
//		LIBRARY, // library variables, those specific to this library
//		RESERVED, // not for you variables?
	}

}
