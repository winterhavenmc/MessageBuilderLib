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

package com.winterhavenmc.util.messagebuilder.namespace;

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
		CONSTANTS, //values supplied by the yaml language file, from the root level section 'CONSTANTS'
		ITEMS, // values supplied by the yaml language file, from the root level section 'ITEMS'
		MACRO, // values passed in by calls to the setMacro method
		MESSAGES, // values supplied by the yaml language file. from the root level section 'MESSAGES'
		TIME, // string values for time units supplied by the yaml language file, from the root level section 'TIME'
	}


	/**
	 * NOTE: THIS ENUM IS NOT CURRENTLY IN USE. IT IS A DEMONSTRATION OF POSSIBLE SOURCE CONSTANTS
	 * THAT WERE CONSIDERED ALONG WITH DOMAIN CONSTANTS. IF IT IS NOT USED IN PRODUCTION, REMOVE IT OR MOVE TO TESTING.
	 */
	public enum Source {
		ENV, // environment variables, such as those made available by the operating system
		RUNTIME, // runtime variables, such as those made available by the Java Runtime Environment
		SERVER, // server variables, made available by the bukkit server instance running the plugin
		PLUGIN, // plugin variables (could use subdomains to hold info about other plugins)
		LIBRARY, // library variables, those specific to this library
		RESERVED, // not for you variables?
	}


	/**
	 * NOTE: THIS ENUM IS CURRENTLY IN USE FOR TESTING. IF IT IS NOT USED FOR PRODUCTION, MOVE IT TO TESTING.
	 */
	public static class Time {
		// this enum contains the default en-US singular and plural names of time units.
		// It is used only for testing at this time, to confirm the correct singular and
		// plural names are being returned by methods under test.
		public enum Unit {
			MILLISECONDS("millisecond", "milliseconds"),
			TICKS("tick", "ticks"),
			SECONDS("second", "seconds"),
			MINUTES("minute", "minutes"),
			HOURS("hour","hours"),
			DAYS("day","days"),
			WEEKS("week", "weeks"),
			MONTHS("month", "months"),
			YEARS("year", "years"),
			;

			private final String singular;
			private final String plural;

			Unit(final String singular, final String plural) {
				this.singular = singular;
				this.plural = plural;
			}

			public String getPlural() {
				return plural;
			}

			public String getSingular() {
				return singular;
			}
		}
	}



	// these should reflect the field names as they will be used as suffixes in the context keys
	// required should mean that the retrieved value may be null. if this meaning is inappropriate
	// for the field 'required', a new field should be added to positively identify nullable fields.
	//
 	// NOTE: THIS ENUM HAS BEEN SUPERSEDED IN USE BY A SIMILAR ENUM DECLARED WITHIN THE ITEM RECORD CLASS
	public static class Item {
		public enum Field {
			// fields in source
			NAME_SINGULAR("NAME.SINGULAR", String.class, true), // required
			NAME_PLURAL("NAME.PLURAL", String.class, true), // required
			INVENTORY_NAME_SINGULAR("INVENTORY_NAME.SINGULAR", String.class, false), // if not found, use NAME_SINGULAR
			INVENTORY_NAME_PLURAL("INVENTORY_NAME.PLURAL", String.class, false), // if not found, use NAME_PLURAL
			QUANTITY("QUANTITY", Integer.class, false), // if not found, use one (1)
			// synthetic fields derived from context
			NAME("NAME", String.class, false), // this will be populated with the singular/plural depending on QUANTITY
			DISPLAY_NAME("DISPLAY_NAME", String.class, false), // relies on QUANTITY to choose NAME_SINGULAR or NAME_PLURAL
			INVENTORY_DISPLAY_NAME("INVENTORY_DISPLAY_NAME", String.class, false), // relies on QUANTITY to choose INVENTORY_NAME_SINGULAR or INVENTORY_NAME_PLURAL
			;

			private final String keyPath;
			private final Class<?> type;
			private final boolean required;
			//private final boolean immutable;

			private Field(final String keyPath, final Class<?> type, final boolean required) {
				this.keyPath = keyPath;
				this.type = type;
				this.required = required;
				//this.immutable = immutable;
			}

			public String getKeyPath() {
				return keyPath;
			}

			public Class<?> getType() {
				return type;
			}

			public boolean isRequired() {
				return this.required;
			}
		}
	}


	/**
	 * ContextMap fields for a Location object. The ContextMap key for each field is derived by appending
	 * the field nameSingular to the existing key path for this Macro. The field values are derived from a Location object
	 * by the LocationProcessor.
	 */
	public static class Location {
		public enum Field {
			FORMATTED(String.class),
			WORLD(String.class),
			X(String.class),
			Y(String.class),
			Z(String.class),
			;

			private final Class<?> type;

			private Field(final Class<?> type) {
				this.type = type;
			}
		}
	}

	/**
	 * This enum serves to document the fields that will be put in place for the message recipient in the context map
	 */
	public static class Recipient {
		public enum Field {
			NAME("RECIPIENT.NAME", String.class, "The name of the recipient"),
			NAME_DISPLAYNAME("RECIPIENT.DISPLAYNAME", String.class, "The display name of the recipient (if applicable)"),
			UUID("RECIPIENT.UUID", java.util.UUID.class, "The UUID of the recipient (if applicable)"),
			LOCATION("RECIPIENT.LOCATION", Location.class, "The Location of the recipient (if applicable)"),
			LOCATION_WORLD("RECIPIENT.LOCATION.WORLD", String.class, "The world of the recipient location (if applicable)"),
			LOCATION_X("RECIPIENT.LOCATION.X", Double.class, "The X-coordinate of the recipient location (if applicable)"),
			LOCATION_Y("RECIPIENT.LOCATION.Y", Double.class, "The Y-coordinate of the recipient location (if applicable)"),
			LOCATION_Z("RECIPIENT.LOCATION.Z", Double.class, "The Z-coordinate of the recipient location (if applicable)"),
			LOCATION_FORMATTED("RECIPIENT.LOCATION.FORMATTED", String.class,
					"The Location of the recipient as a formatted String (if applicable)"),
			;

			private final String keyPath;
			private final Class<?> type;
			private final String description;

			Field(String keyPath, Class<?> type, String description) {
				this.keyPath = keyPath;
				this.type = type;
				this.description = description;
			}

			public String getKeyPath() {
				return keyPath;
			}

			public Class<?> getType() {
				return type;
			}

			public String getDescription() {
				return description;
			}
		}
	}

}
