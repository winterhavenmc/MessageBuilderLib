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

package com.winterhavenmc.util.messagebuilder.context;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.constants.ConstantSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.items.ItemSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;

import java.util.*;
import java.util.logging.Logger;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;


/**
 * A class representing a fully qualified namespace key, used to uniquely identify entries in
 * various contexts, such as macros, configurations, or other domain-specific resources.
 * <p>
 * The namespace key is composed of a {@link Domain} (e.g., MACRO, CONFIG), a
 * {@code keyPath}, and an optional sequence of subdomains. The {@code keyPath} is a
 * dot-separated string, while subdomains provide additional hierarchical context.
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

	private final static Character KEY_DOMAIN_DELIMITER = ':';
	private final static Character KEY_BOUNDARY_DELIMITER = '|';
	private final static Character KEY_PATH_DELIMITER = '.';

	private final Domain domain;
	private final List<String> subdomains;
	private final List<String> pathComponents;


	/**
	 * Class constructor
	 * <p>
	 *     creates a NamespaceKey with the supplied keyPath and domain. This key will have no
	 *     subdomains upon creation.
	 *</p>
	 * @param keyPath a String key to be used as a component the fully name-spaced key
	 * @param domain  the domain that forms the root of the keyDomain
	 */
	public NamespaceKey(final String keyPath, final Domain domain) {
		this.domain = domain;
		this.subdomains = new ArrayList<>();
		this.pathComponents = List.of(keyPath.split("\\" + KEY_PATH_DELIMITER));
		System.out.println(pathComponents);
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
	public NamespaceKey(final String keyPath, final Domain domain, final String... subdomains) {
		this.domain = domain;
		this.subdomains = new ArrayList<>(Arrays.stream(subdomains).toList());
		this.pathComponents = List.of(keyPath.split("\\" + KEY_PATH_DELIMITER));
	}


	/**
	 * Get the String representation of this key, composed of its domain and keyPath with delimiters
	 *
	 * @return a String representation of this key
	 */
	@Override
	public String getKey() {
		return String.join(KEY_BOUNDARY_DELIMITER.toString(), getFullDomain(), getPath());
	}


	/**
	 * Get the full domain component of this key as a String composed of the domain and subdomains with delimiters
	 *
	 * @return a String representation of the domain component of this key
	 */
	public String getFullDomain() {
		return String.join(KEY_DOMAIN_DELIMITER.toString(), domain.name(), String.join(KEY_DOMAIN_DELIMITER.toString(), subdomains));
	}


	/**
	 * Get the domain component of this key, as a Namespace.Domain enum constant.
	 *
	 * @return the Namespace.Domain enum constant of this key
	 */
	public Domain getDomain() {
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
	public String getPath() {
		return String.join(".", pathComponents);
	}


	/**
	 * Get the components of the key path as a List of Strings
	 *
	 * @return a List of Strings containing the separate key path elements
	 */
	public List<String> getPathComponents() {
		return pathComponents;
	}


	/**
	 * Static method to generate a key with a Macro constant name as the keyPath
	 * and the Domain constant MACRO as the keyDomain.
	 *
	 * @param macro the Macro constant whose name will be used as the keyPath (required).
	 * @return A proper namespaced String key.
	 */
	public static <Macro> String create(final Macro macro) {
		if (macro == null) { throw new LocalizedException(PARAMETER_NULL, "macro"); }

		return Domain.MACRO + KEY_BOUNDARY_DELIMITER.toString() + macro;
	}


	/**
	 * Static method to generate a key with a Macro constant name as the keyPath
	 * and a Domain constant name as the keyDomain.
	 *
	 * @param macro  the Macro constant whose name will be used as the keyPath (required).
	 * @param domain a domain to use as the keyDomain (required).
	 * @return A proper namespaced String key.
	 */
	public static <Macro> String create(final Macro macro, final Domain domain) {
		if (macro == null) { throw new LocalizedException(PARAMETER_NULL, "macro"); }
		if (domain == null) { throw new LocalizedException(PARAMETER_NULL, "domain"); }

		return domain.name() + KEY_BOUNDARY_DELIMITER + macro;
	}


	/**
	 * Static method to generate a nameSpacedKey with optional subdomains.
	 *
	 * @param keyPath        The keyPath portion of the new key (required).
	 * @param domain         The top-level domain that forms the root of the keyDomain (required).
	 * @param subdomains  Optional subdomains to be added to the keyDomain.
	 * @return A fully-formed namespaced String key.
	 */
	public static String create(final String keyPath,
	                            final Domain domain,
	                            final String... subdomains) {
		if (keyPath == null) { throw new LocalizedException(PARAMETER_NULL, "keyPath"); }
		if (keyPath.isBlank()) { throw new LocalizedException(PARAMETER_EMPTY, "keyPath"); }
		if (domain == null) { throw new LocalizedException(PARAMETER_NULL, "domain"); }
		if (subdomains == null) { throw new LocalizedException(PARAMETER_NULL, "subdomains"); }

		StringBuilder fullKey = new StringBuilder(domain.name());
		for (String subdomain : subdomains) {
			if (subdomain == null) {
				throw new LocalizedException(PARAMETER_NULL, "subdomain");
			}
			if (subdomain.isBlank()) {
				throw new LocalizedException(PARAMETER_EMPTY, "subdomain");
			}
			fullKey.append(KEY_DOMAIN_DELIMITER).append(subdomain);
		}
		fullKey.append(KEY_DOMAIN_DELIMITER).append(keyPath);
		return fullKey.toString();
	}


	/**
	 * Static utility method to check the validity of a key domain.
	 * <p>
	 * Logic:<br>
	 * A keyDomain must contain at least one string of alphanumeric characters, which is a string representation
	 * of a constant of the enum {@link Domain}, and optionally may contain one or more additional
	 * subdomains as alphanumeric strings, with the domain and any subdomains delimited by a colon (:)
	 * <p>
	 * Note: Because domain is derived from an enum constant name, it should be formatted in upper snake case,
	 * to adhere to the Java naming convention for constants. The subdomains are unrestricted in their
	 * case, but obviously must avoid using a colon character to avoid confusion with the domain delimiter.
	 *
	 * @param fullDomain the keyDomain to validate
	 * @return {@code true} if the keyDomain conforms to the naming convention, {@code false} if it does not.
	 */
	public static boolean isValidKeyDomain(final String fullDomain) {
		String keyDomainPattern = "^[a-zA-Z0-9_]+(:[a-zA-Z0-9_]+)*$";
		return fullDomain.matches(keyDomainPattern);
	}


	/**
	 * Static method to generate a warning to the log when an invalid key domain is detected
	 *
	 * @param fullDomain the key domain to check for validity
	 */
	public static void logInvalidDomainPath(final String fullDomain) {
		if (!isValidKeyDomain(fullDomain)) {
			// Log a warning without modifying the keyPath
			Logger.getLogger("NamespaceKey").warning("Key domain '" + fullDomain + "' does not conform to the allowed naming convention.");
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
	public static boolean isValidKeyPath(final String keyPath) {
		String keyPathPattern = "^[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*$";
		return keyPath.matches(keyPathPattern);
	}


	/**
	 * Static method to generate a warning to the log when an invalid key path is detected
	 *
	 * @param keyPath the key path to check for validity
	 */
	public static void logInvalidKeyPath(final String keyPath) {
		if (!isValidKeyPath(keyPath)) {
			// Log a warning without modifying the keyPath
			Logger.getLogger("NamespaceKey").warning("Key path '" + keyPath + "' does not conform to the allowed naming convention.");
		}
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true; // Same object
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false; // Null or different class
		}
		NamespaceKey that = (NamespaceKey) obj;

		return domain == that.domain && // Enums are compared by reference
				Objects.equals(subdomains, that.subdomains) && // Safe list comparison
				Objects.equals(pathComponents, that.pathComponents); // Safe list comparison
	}


	@Override
	public int hashCode() {
		return Objects.hash(domain, subdomains, pathComponents);
	}

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
