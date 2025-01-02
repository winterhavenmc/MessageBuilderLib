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
 * A class representing a fully qualified namespace key, used to uniquely identify entries in
 * various contexts, such as macros, configurations, or other domain-specific resources.
 * <p>
 * The namespace key is composed of a {@link Namespace.Domain} (e.g., MACRO, CONFIG), a
 * {@code keyPath}, and an optional sequence of subcategories. The {@code keyPath} is a
 * dot-separated string, while subcategories provide additional hierarchical context.
 * </p>
 * <p>
 * Static factory methods are provided to facilitate the creation of namespace keys with varying
 * levels of detail, ensuring that invalid inputs are rejected with appropriate validation and
 * warnings. Utility methods are included to validate, construct, and parse components of the
 * namespace key.
 * </p>
 * <p>
 * Guard clauses ensure that invalid inputs such as {@code null} or empty strings are rejected
 * at runtime, with detailed exception messages provided to aid debugging.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * NamespaceKey key = new NamespaceKey("test.key", Namespace.Domain.MACRO, "sub1", "sub2");
 * System.out.println(key.getKey()); // Outputs: MACRO:sub1:sub2|test.key
 *
 * String createdKey = NamespaceKey.create("test.key", Namespace.Domain.CONFIG);
 * System.out.println(createdKey); // Outputs: CONFIG|test.key
 * }
 * </pre>
 * </p>
 * <p>
 * Validation utilities are included to assist in ensuring the integrity of namespace keys:
 * <ul>
 *     <li>{@link #isValidKeyDomain(String)}</li>
 *     <li>{@link #isValidKeyPath(String)}</li>
 * </ul>
 * Warnings are logged for invalid keys, but the keys are never modified, in order to prevent
 * any potential mismatch, even if the key is malformed.
 * </p>
 */
public class NamespaceKey implements ContextKey {

	private final static String KEY_DOMAIN_DELIMITER = ":";
	private final static String KEY_BOUNDARY_DELIMITER = "|";
	private final static String KEY_PATH_DELIMITER = "\\."; // escaped dot

	private final Namespace.Domain domain;
	private final List<String> subdomains;
	private final List<String> keyPathComponents;


	/**
	 * Class constructor
	 * <p>
	 *     creates a NamespaceKey with the supplied keyPath and domain. This key will have no
	 *     subdomains upon creation.
	 *</p>
	 * @param keyPath a String key to be used as a component the fully name-spaced key
	 * @param domain  the domain that forms the root of the keyDomain
	 */
	NamespaceKey(final String keyPath, Namespace.Domain domain) {
		this.domain = domain;
		this.subdomains = new ArrayList<>();
		this.keyPathComponents = List.of(keyPath.split(KEY_PATH_DELIMITER));
	}


	/**
	 * Class constructor
	 * <p>
	 *     Creates a namespaceKey with the supplied keyPath, domain, and subdomains.
	 *     Any number of subdomains may be passed to the constructor.
	 * </p>
	 *
	 * @param keyPath the String key to be used as a component in the fully name-spaced key
	 * @param domain  the domain that forms the root of the keyDomain
	 */
	NamespaceKey(final String keyPath, Namespace.Domain domain, String... subdomains) {
		this.domain = domain;
		this.subdomains = new ArrayList<>(Arrays.stream(subdomains).toList());
		this.keyPathComponents = List.of(keyPath.split(KEY_PATH_DELIMITER));
	}


	/**
	 * Get the String representation of this key, composed of its domain and keyPath with delimiters
	 *
	 * @return a String representation of this key
	 */
	@Override
	public String getKey() {
		return String.join(KEY_BOUNDARY_DELIMITER, getFullDomain(), getKeyPath());
	}


	/**
	 * Get the full domain component of this key as a String composed of the domain and subdomains with delimiters
	 *
	 * @return a String representation of the domain component of this key
	 */
	public String getFullDomain() {
		return String.join(KEY_DOMAIN_DELIMITER, domain.name(), String.join(KEY_DOMAIN_DELIMITER, subdomains));
	}


	/**
	 * Get the domain component of this key, as a Namespace.Domain enum constant.
	 *
	 * @return the Namespace.Domain enum constant of this key
	 */
	public Namespace.Domain getDomain() {
		return domain;
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
		return String.join(".", keyPathComponents);
	}


	/**
	 * Get the components of the key path as a List of Strings
	 *
	 * @return a List of Strings containing the separate key path elements
	 */
	public List<String> getKeyPathComponents() {
		return keyPathComponents;
	}


	/**
	 * Static method to generate a key with a Macro constant name as the keyPath
	 * and the Domain constant MACRO as the keyDomain.
	 *
	 * @param macro the Macro constant whose name will be used as the keyPath (required).
	 * @return A proper namespaced String key.
	 */
	public static <Macro> String create(final Macro macro) {
		if (macro == null) { throw new IllegalArgumentException(Parameter.NULL_MACRO.getMessage()); }

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
		if (macro == null) { throw new IllegalArgumentException(Parameter.NULL_MACRO.getMessage()); }
		if (domain == null) { throw new IllegalArgumentException(Parameter.NULL_DOMAIN.getMessage()); }

		return domain.name() + KEY_BOUNDARY_DELIMITER + macro;
	}


	/**
	 * Static method to generate a nameSpacedKey with optional subcategories.
	 *
	 * @param keyPath        The keyPath portion of the new key (required).
	 * @param domain         The top-level domain that forms the root of the keyDomain (required).
	 * @param subcategories  Optional subcategories to be added to the keyDomain.
	 * @return A fully-formed namespaced String key.
	 */
	public static String create(String keyPath, Namespace.Domain domain, String... subcategories) {
		if (keyPath == null) { throw new IllegalArgumentException(Parameter.NULL_KEY_PATH.getMessage()); }
		if (keyPath.isEmpty()) { throw new IllegalArgumentException(Parameter.EMPTY_KEY_PATH.getMessage()); }
		if (domain == null) { throw new IllegalArgumentException(Parameter.NULL_DOMAIN.getMessage()); }
		if (subcategories == null) { throw new IllegalArgumentException(Parameter.NULL_SUBDOMAINS.getMessage()); }

		StringBuilder fullKey = new StringBuilder(domain.name());
		for (String subcategory : subcategories) {
			if (subcategory == null) {
				throw new IllegalArgumentException(Parameter.NULL_SUBDOMAIN_ELEMENT.getMessage());
			}
			if (subcategory.isEmpty()) {
				throw new IllegalArgumentException(Parameter.EMPTY_SUBDOMAIN_ELEMENT.getMessage());
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
			Logger.getLogger("NamespaceKey").warning("Key domain '" + keyDomain + "' does not conform to the allowed naming convention.");
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


	@Override
	public boolean equals(Object object) {
		if (object == null || getClass() != object.getClass()) {
			return false;
		}

		NamespaceKey that = (NamespaceKey) object;
		return domain == that.domain && subdomains.equals(that.subdomains) && keyPathComponents.equals(that.keyPathComponents);
	}


	@Override
	public int hashCode() {
		int result = domain.hashCode();
		result = 31 * result + subdomains.hashCode();
		result = 31 * result + keyPathComponents.hashCode();
		return result;
	}

}
