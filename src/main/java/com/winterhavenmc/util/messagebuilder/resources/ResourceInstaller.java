/*
 * Copyright (c) 2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.resources;

import com.winterhavenmc.util.messagebuilder.resources.configuration.LanguageTag;

import java.util.Set;

public interface ResourceInstaller
{
	/**
	 * Retrieve {@link Set} of resource filenames from a plain text resource in the language directory of the plugin jar.
	 * The plain text resource name elements are currently set in the LanguageSetting enum. The valid pathname for the resource
	 * can be retrieved using the getAutoInstallResourcePath method of this class
	 *
	 * @param autoInstallPathName a {@code String} containing the resource path of the auto install plain text resource
	 * @return Set of filename strings
	 */
	Set<String> getAutoInstallResourceNames(String autoInstallPathName);


	/**
	 * sanitize resource path file paths. these are ultimately set by server operators,
	 * and may be running on servers not owned by them. It is essential that filenames
	 * in this list of files to be copied not contain the names of system resources outside
	 * the plugin's data directory.
	 * <p>
	 * Logic:
	 * <ol>
	 * <li>any whitespace characters are to be removed</li>
	 * <li>any occurrences of two or more consecutive periods (.) are to be removed</li>
	 * <li>any number of leading slashes (/) are to be removed</li>
	 * <li>Any occurrence of two or more slashes (/) are to be replaced with a single slash</li>
	 * </ol>
	 *
	 * @param resourcePath the {@code String} path name to be sanitized.
	 */
	String sanitizeResourcePath(String resourcePath);


	/**
	 * Install resources listed in auto_install.txt to the plugin data directory
	 */
	void autoInstall();


	String getAutoInstallResourcePath();


	/**
	 * Install a resource for the given language tag if not already installed in the plugin data directory
	 *
	 * @param languageTag the language tag for the resource to be installed
	 */
	InstallerStatus installIfMissing(LanguageTag languageTag);


	/**
	 * Install resource from plugin jar to plugin data directory
	 *
	 * @param resourceName {@code String} the path name of the resource to be installed
	 * @return a {@code Boolean} indicating the success or failure result of the resource installation
	 */
	InstallerStatus installByName(String resourceName);


	/**
	 * Install resource from plugin jar to plugin data directory
	 *
	 * @param languageTag {@code String} the path name of the resource to be installed
	 * @return a {@code Boolean} indicating the success or failure result of the resource installation
	 */
	InstallerStatus install(LanguageTag languageTag);


	/**
	 * Check if a resource exists
	 *
	 * @param resourceName the name of the resource
	 * @return {@code true} if the resource exists, {@code false} if it does not
	 */
	boolean resourceExists(String resourceName);


	/**
	 * Check if a resource exists
	 *
	 * @param languageTag the tag of the resource being checked for existence in the classpath
	 * @return {@code true} if the resource exists, {@code false} if it does not
	 */
	boolean resourceExists(LanguageTag languageTag);


	/**
	 * Test if resource is installed in the plugin data directory
	 *
	 * @param filename the name of the file being verified
	 * @return {@code true} if a file with the filename exists in the plugin data directory, {@code false} if not
	 */
	boolean isInstalled(String filename);


	/**
	 * Test if resource is installed in the plugin data directory
	 *
	 * @param languageTag the language tag of the file being verified as installed in the plugin data directory
	 * @return {@code true} if a file with the filename exists in the plugin data directory, {@code false} if not
	 */
	boolean isInstalledForTag(LanguageTag languageTag);


	enum InstallerStatus
	{
		UNAVAILABLE,
		FILE_EXISTS,
		SUCCESS,
		FAIL,
	}
}
