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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static com.winterhavenmc.util.messagebuilder.util.Error.*;


/**
 * This is a class that implements name-spaced String keys for use in the ContextMap and the ResultMap.
 * <p>
 * This key is composed of a keyDomain, which includes a mandatory domain, typically derived from the
 * name of an enum constant, and any number of optional subcategories, and a keyPath, which is a dot-delimited
 * string representing a full key path from a configuration object.
 * <p>
 * The name-spaced keys are divided into left and right components,
 * which are referred to as the keyDomain and the keyPath respectively. The key path is a dot delimited
 * String of path components, as returned from a yaml configuration section. They are fully name spaced
 * by their nature of being valid yaml. The left side of the key, or keyDomain, is composed of a colon-delimited
 * main Domain, and zero or more subcategories, which are strings passed in as var args in the static create
 * method. Finally, the keyDomain and the keyPath are themselves separated with a colon, unless one or the other
 * side of the key is not present. This should allow for sufficient name-spacing during the transition and moving
 * forward.
 * <p>
 * Key Domain: The portion of the key to the left of the | delimiter.
 * Includes the domain and optional subcategories, separated by :.
 * Represents the logical or hierarchical "ownership" of the Key Path.
 * <p>
 * Key Path: The portion of the key to the right of the | delimiter.
 * Dot-delimited structure representing the configuration or resource path.
 * <p>
 * Note: If the source of the value represented by this key is a yaml configuration file,
 * the keyPath SHALL be identical to the yaml key path used to retrieve the value.
 * <pre>
 * rule: all NameSpaceKeys MUST have at least a Domain as the root of their key domain.
 *       If you do not choose a domain, one will be chosen for you.
 *
 * rule: all NameSpaceKeys MUST have some token in the key path portion of the key.
 * </pre>
 */
public class NamespaceKey implements ContextKey {

	private final static String KEY_DOMAIN_DELIMITER = ":";
	private final static String KEY_BOUNDARY_DELIMITER = "|";
	private final static String KEY_PATH_DELIMITER = ".";

	private final String keyPath;
	private final Namespace.Domain keyDomain;
	private final List<String> subdomains;


	/**
	 * Class constructor
	 * <p>
	 *     creates a NamespaceKey with the supplied domain and keyPath. This key will have no
	 *     subdomains upon creation.
	 *</p>
	 * @param keyPath the String key or macroName to be used as the fully name-spaced key
	 * @param keyDomain  the domain that forms the root of the keyDomain
	 */
	NamespaceKey(final String keyPath, Namespace.Domain keyDomain) {
		this.keyPath = keyPath;
		this.keyDomain = keyDomain;
		this.subdomains = new ArrayList<>();
	}


	/**
	 * Class constructor
	 * <p>
	 *     Creates a namespaceKey with the supplied keyPath, domain, and subdomains.
	 *     Any number of subdomains may be passed to the constructor.
	 * </p>
	 *
	 * @param keyPath the String key or macroName to be used as the fully name-spaced key
	 * @param keyDomain  the domain that forms the root of the keyDomain
	 */
	NamespaceKey(final String keyPath, Namespace.Domain keyDomain, String... subdomains) {
		this.keyPath = keyPath;
		this.keyDomain = keyDomain;
		this.subdomains = new ArrayList<>(Arrays.stream(subdomains).toList());
	}


	/**
	 * Get the String representation of this key, composed of its domain and path with delimiters
	 *
	 * @return a String representation of this key
	 */
	@Override
	public String getKey() {
		return String.join(KEY_BOUNDARY_DELIMITER, getFullDomain(), keyPath);
	}


	/**
	 * Get the full domain component of this key as a String composed of the domain and subdomains with delimiters
	 *
	 * @return a String representation of the domain component of this key
	 */
	public String getFullDomain() {
		return String.join(KEY_DOMAIN_DELIMITER, keyDomain.name(), String.join(KEY_DOMAIN_DELIMITER, subdomains));
	}


	/**
	 * Get the domain component of this key, as a Namespace.Domain enum constant.
	 *
	 * @return the Namespace.Domain enum constant of this key
	 */
	public Namespace.Domain getKeyDomain() {
		return keyDomain;
	}


	/**
	 * Get the subdomains of the key, as a List of Strings
	 *
	 * @return List of Strings containing the subdomains of this key
	 */
	public List<String> getSubdomains() {
		return subdomains;
	}


	/**
	 * Get the path portion of the key
	 *
	 * @return the key path
	 */
	public String getKeyPath() {
		return keyPath;
	}


	/**
	 * Get the components of the key path as a List of Strings
	 *
	 * @return a List of Strings containing the separate key path elements
	 */
	public List<String> getKeyPathComponents() {
		String regex = '\\' + KEY_PATH_DELIMITER;
		return new ArrayList<>(Arrays.stream(keyPath.split(regex)).toList());
	}


	/**
	 * Static method to generate a key with a Macro constant name as the keyPath
	 * and the Domain constant MACRO as the keyDomain.
	 *
	 * @param macro the Macro constant whose name will be used as the keyPath (required).
	 * @return A proper namespaced String key.
	 */
	public static <Macro> String create(final Macro macro) {
		if (macro == null) { throw new IllegalArgumentException(PARAMETER_NULL_MACRO.getMessage()); }

		return Namespace.Domain.MACRO + KEY_BOUNDARY_DELIMITER + macro;
	}


	/**
	 * Static method to generate a key with a Macro constant name as the keyPath
	 * and a Domain constant name as the keyDomain.
	 *
	 * @param macro  the Macro constant whose name will be used as the keyPath (required).
	 * @param domain a domain to use as the keyDomain (required).
	 * @return A proper namespaced String key.
	 */
	public static <Macro> String create(final Macro macro, final Namespace.Domain domain) {
		if (macro == null) { throw new IllegalArgumentException(PARAMETER_NULL_MACRO.getMessage()); }
		if (domain == null) { throw new IllegalArgumentException(PARAMETER_NULL_DOMAIN.getMessage()); }

		return domain.name() + KEY_BOUNDARY_DELIMITER + macro;
	}


	/**
	 * Static method to generate a nameSpacedKey with optional subcategories.
	 *
	 * @param keyPath       The keyPath portion of the new key (required).
	 * @param domain        The top-level domain that forms the root of the keyDomain (required).
	 * @param subcategories Optional subcategories to be added to the keyDomain.
	 * @return A fully-formed namespaced String key.
	 */
	public static String create(String keyPath, Namespace.Domain domain, String... subcategories) {
		if (keyPath == null) { throw new IllegalArgumentException(PARAMETER_NULL_KEY_PATH.getMessage()); }
		if (keyPath.isEmpty()) { throw new IllegalArgumentException(PARAMETER_EMPTY_KEY_PATH.getMessage()); }
		if (domain == null) { throw new IllegalArgumentException(PARAMETER_NULL_DOMAIN.getMessage()); }

		StringBuilder fullKey = new StringBuilder(domain.name());
		for (String subcategory : subcategories) {
			if (subcategory == null || subcategory.isEmpty()) {
				throw new IllegalArgumentException(PARAMETER_NULL_OR_EMPTY_SUBDOMAIN.getMessage());
			}
			fullKey.append(KEY_DOMAIN_DELIMITER).append(subcategory);
		}
		fullKey.append(KEY_DOMAIN_DELIMITER).append(keyPath);
		return fullKey.toString();
	}


	/**
	 * Static utility method to check the validity of a key domain.
	 * <p>
	 * Logic:<br>
	 * A keyDomain must contain at least one string of alphanumeric characters, and optionally may contain
	 * one or more additional alphanumeric strings, delimited by a colon (:)
	 * <p>
	 * Note: Because domain is derived from an enum constant name, it should be formatted in upper snake case,
	 * to adhere to the Java naming convention for constants. The subcategories are unrestricted in their
	 * case, but obviously must avoid using a colon character to avoid confusion with the domain delimiter.
	 *
	 * @param keyDomain the keyDomain to validate
	 * @return {@code true} if the keyDomain conforms to the naming convention, {@code false} if it does not.
	 */
	static boolean isValidKeyDomain(String keyDomain) {
		String keyDomainPattern = "^[a-zA-Z0-9_]+(:[a-zA-Z0-9_]+)*$";
		return keyDomain.matches(keyDomainPattern);
	}


	/**
	 * Static method to generate a warning to the log when an invalid key domain is detected
	 *
	 * @param keyDomain the key domain to check for validity
	 */
	static void validateKeyDomain(String keyDomain) {
		if (!isValidKeyDomain(keyDomain)) {
			// Log a warning without modifying the keyPath
			System.out.println("Warning: Key path '" + keyDomain + "' does not conform to the allowed naming convention.");
		}
	}


	/**
	 * Static utility method to check the validity of a key path.
	 * <p>
	 * Logic:<br>
	 * A keyPath must contain at least one string of alphanumeric characters, and optionally may contain
	 * one or more additional alphanumeric strings, delimited by a period (.)
	 * <p>
	 * If the source of this value is a yaml configuration file, the keyPath SHALL be identical to the
	 * yaml key path used to retrieve the value.
	 *
	 * @param keyPath the keyPath to validate
	 * @return {@code true} if the keyPath conforms to the naming convention, {@code false} if it does not.
	 */
	static boolean isValidKeyPath(String keyPath) {
		String keyPathPattern = "^[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*$";
		return keyPath.matches(keyPathPattern);
	}


	/**
	 * Static method to generate a warning to the log when an invalid key path is detected
	 *
	 * @param keyPath the key path to check for validity
	 */
	static void validateKeyPath(String keyPath) {
		if (!isValidKeyPath(keyPath)) {
			// Log a warning without modifying the keyPath
			Logger.getLogger("NamespaceKey").warning("Key path '" + keyPath + "' does not conform to the allowed naming convention.");
		}
	}

}
