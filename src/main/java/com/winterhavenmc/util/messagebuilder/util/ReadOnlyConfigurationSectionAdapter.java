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

package com.winterhavenmc.util.messagebuilder.util;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReadOnlyConfigurationSectionAdapter implements ReadOnlyConfigurationSection {

	private final ConfigurationSection configurationSection;

	public ReadOnlyConfigurationSectionAdapter(ConfigurationSection configurationSection) {
		this.configurationSection = configurationSection;
	}


	/**
	 * Gets a set containing all keys in this section.
	 * <p>
	 * If deep is set to true, then this will contain all the keys within any
	 * child {@link ConfigurationSection}s (and their children, etc). These
	 * will be in a valid path notation for you to use.
	 * <p>
	 * If deep is set to false, then this will contain only the keys of any
	 * direct children, and not their own children.
	 *
	 * @param deep Whether or not to get a deep list, as opposed to a shallow
	 *             list.
	 * @return Set of keys contained within this ConfigurationSection.
	 */
	@Override
	public @NotNull Set<String> getKeys(boolean deep) {
		return configurationSection.getKeys(deep);
	}

	/**
	 * Gets a Map containing all keys and their values for this section.
	 * <p>
	 * If deep is set to true, then this will contain all the keys and values
	 * within any child {@link ConfigurationSection}s (and their children,
	 * etc). These keys will be in a valid path notation for you to use.
	 * <p>
	 * If deep is set to false, then this will contain only the keys and
	 * values of any direct children, and not their own children.
	 *
	 * @param deep Whether or not to get a deep list, as opposed to a shallow
	 *             list.
	 * @return Map of keys and values of this section.
	 */
	@Override
	public @NotNull Map<String, Object> getValues(boolean deep) {
		return configurationSection.getValues(deep);
	}

	/**
	 * Checks if this {@link ConfigurationSection} contains the given path.
	 * <p>
	 * If the value for the requested path does not exist but a default value
	 * has been specified, this will return true.
	 *
	 * @param path Path to check for existence.
	 * @return True if this section contains the requested path, either via
	 * default or being set.
	 * @throws IllegalArgumentException Thrown when path is null.
	 */
	@Override
	public boolean contains(@NotNull String path) {
		return configurationSection.contains(path);
	}

	/**
	 * Checks if this {@link ConfigurationSection} contains the given path.
	 * <p>
	 * If the value for the requested path does not exist, the boolean parameter
	 * of true has been specified, a default value for the path exists, this
	 * will return true.
	 * <p>
	 * If a boolean parameter of false has been specified, true will only be
	 * returned if there is a set value for the specified path.
	 *
	 * @param path          Path to check for existence.
	 * @param ignoreDefault Whether or not to ignore if a default value for the
	 *                      specified path exists.
	 * @return True if this section contains the requested path, or if a default
	 * value exist and the boolean parameter for this method is true.
	 * @throws IllegalArgumentException Thrown when path is null.
	 */
	@Override
	public boolean contains(@NotNull String path, boolean ignoreDefault) {
		return configurationSection.contains(path, ignoreDefault);
	}

	/**
	 * Checks if this {@link ConfigurationSection} has a value set for the
	 * given path.
	 * <p>
	 * If the value for the requested path does not exist but a default value
	 * has been specified, this will still return false.
	 *
	 * @param path Path to check for existence.
	 * @return True if this section contains the requested path, regardless of
	 * having a default.
	 * @throws IllegalArgumentException Thrown when path is null.
	 */
	@Override
	public boolean isSet(@NotNull String path) {
		return configurationSection.isSet(path);
	}

	/**
	 * Gets the path of this {@link ConfigurationSection} from its root {@link
	 * Configuration}
	 * <p>
	 * For any {@link Configuration} themselves, this will return an empty
	 * string.
	 * <p>
	 * If the section is no longer contained within its root for any reason,
	 * such as being replaced with a different value, this may return null.
	 * <p>
	 * To retrieve the single name of this section, that is, the final part of
	 * the path returned by this method, you may use {@link #getName()}.
	 *
	 * @return Path of this section relative to its root
	 */
	@Override
	public @Nullable String getCurrentPath() {
		return configurationSection.getCurrentPath();
	}

	/**
	 * Gets the name of this individual {@link ConfigurationSection}, in the
	 * path.
	 * <p>
	 * This will always be the final part of {@link #getCurrentPath()}, unless
	 * the section is orphaned.
	 *
	 * @return Name of this section
	 */
	@Override
	public @NotNull String getName() {
		return configurationSection.getName();
	}

	/**
	 * Gets the root {@link Configuration} that contains this {@link
	 * ConfigurationSection}
	 * <p>
	 * For any {@link Configuration} themselves, this will return its own
	 * object.
	 * <p>
	 * If the section is no longer contained within its root for any reason,
	 * such as being replaced with a different value, this may return null.
	 *
	 * @return Root configuration containing this section.
	 */
	@Override
	public @Nullable Configuration getRoot() {
		return configurationSection.getRoot();
	}

	/**
	 * Gets the parent {@link ConfigurationSection} that directly contains
	 * this {@link ConfigurationSection}.
	 * <p>
	 * For any {@link Configuration} themselves, this will return null.
	 * <p>
	 * If the section is no longer contained within its parent for any reason,
	 * such as being replaced with a different value, this may return null.
	 *
	 * @return Parent section containing this section.
	 */
	@Override
	public @Nullable ConfigurationSection getParent() {
		return configurationSection.getParent();
	}

	/**
	 * Gets the requested Object by path.
	 * <p>
	 * If the Object does not exist but a default value has been specified,
	 * this will return the default value. If the Object does not exist and no
	 * default value was specified, this will return null.
	 *
	 * @param path Path of the Object to get.
	 * @return Requested Object.
	 */
	@Override
	public @Nullable Object get(@NotNull String path) {
		return configurationSection.get(path);
	}

	/**
	 * Gets the requested Object by path, returning a default value if not
	 * found.
	 * <p>
	 * If the Object does not exist then the specified default value will
	 * returned regardless of if a default has been identified in the root
	 * {@link Configuration}.
	 *
	 * @param path Path of the Object to get.
	 * @param def  The default value to return if the path is not found.
	 * @return Requested Object.
	 */
	@Override
	public @Nullable Object get(@NotNull String path, @Nullable Object def) {
		return configurationSection.get(path, def);
	}

	/**
	 * Gets the requested String by path.
	 * <p>
	 * If the String does not exist but a default value has been specified,
	 * this will return the default value. If the String does not exist and no
	 * default value was specified, this will return null.
	 *
	 * @param path Path of the String to get.
	 * @return Requested String.
	 */
	@Override
	public @Nullable String getString(@NotNull String path) {
		return configurationSection.getString(path);
	}

	/**
	 * Gets the requested String by path, returning a default value if not
	 * found.
	 * <p>
	 * If the String does not exist then the specified default value will
	 * returned regardless of if a default has been identified in the root
	 * {@link Configuration}.
	 *
	 * @param path Path of the String to get.
	 * @param def  The default value to return if the path is not found or is
	 *             not a String.
	 * @return Requested String.
	 */
	@Override
	public @Nullable String getString(@NotNull String path, @Nullable String def) {
		return configurationSection.getString(path, def);
	}

	/**
	 * Checks if the specified path is a String.
	 * <p>
	 * If the path exists but is not a String, this will return false. If the
	 * path does not exist, this will return false. If the path does not exist
	 * but a default value has been specified, this will check if that default
	 * value is a String and return appropriately.
	 *
	 * @param path Path of the String to check.
	 * @return Whether or not the specified path is a String.
	 */
	@Override
	public boolean isString(@NotNull String path) {
		return configurationSection.isString(path);
	}

	/**
	 * Gets the requested int by path.
	 * <p>
	 * If the int does not exist but a default value has been specified, this
	 * will return the default value. If the int does not exist and no default
	 * value was specified, this will return 0.
	 *
	 * @param path Path of the int to get.
	 * @return Requested int.
	 */
	@Override
	public int getInt(@NotNull String path) {
		return configurationSection.getInt(path);
	}

	/**
	 * Gets the requested int by path, returning a default value if not found.
	 * <p>
	 * If the int does not exist then the specified default value will
	 * returned regardless of if a default has been identified in the root
	 * {@link Configuration}.
	 *
	 * @param path Path of the int to get.
	 * @param def  The default value to return if the path is not found or is
	 *             not an int.
	 * @return Requested int.
	 */
	@Override
	public int getInt(@NotNull String path, int def) {
		return configurationSection.getInt(path, def);
	}

	/**
	 * Checks if the specified path is an int.
	 * <p>
	 * If the path exists but is not a int, this will return false. If the
	 * path does not exist, this will return false. If the path does not exist
	 * but a default value has been specified, this will check if that default
	 * value is a int and return appropriately.
	 *
	 * @param path Path of the int to check.
	 * @return Whether or not the specified path is an int.
	 */
	@Override
	public boolean isInt(@NotNull String path) {
		return configurationSection.isInt(path);
	}

	/**
	 * Gets the requested boolean by path.
	 * <p>
	 * If the boolean does not exist but a default value has been specified,
	 * this will return the default value. If the boolean does not exist and
	 * no default value was specified, this will return false.
	 *
	 * @param path Path of the boolean to get.
	 * @return Requested boolean.
	 */
	@Override
	public boolean getBoolean(@NotNull String path) {
		return configurationSection.isBoolean(path);
	}

	/**
	 * Gets the requested boolean by path, returning a default value if not
	 * found.
	 * <p>
	 * If the boolean does not exist then the specified default value will
	 * returned regardless of if a default has been identified in the root
	 * {@link Configuration}.
	 *
	 * @param path Path of the boolean to get.
	 * @param def  The default value to return if the path is not found or is
	 *             not a boolean.
	 * @return Requested boolean.
	 */
	@Override
	public boolean getBoolean(@NotNull String path, boolean def) {
		return configurationSection.getBoolean(path, def);
	}

	/**
	 * Checks if the specified path is a boolean.
	 * <p>
	 * If the path exists but is not a boolean, this will return false. If the
	 * path does not exist, this will return false. If the path does not exist
	 * but a default value has been specified, this will check if that default
	 * value is a boolean and return appropriately.
	 *
	 * @param path Path of the boolean to check.
	 * @return Whether or not the specified path is a boolean.
	 */
	@Override
	public boolean isBoolean(@NotNull String path) {
		return configurationSection.isBoolean(path);
	}

	/**
	 * Gets the requested double by path.
	 * <p>
	 * If the double does not exist but a default value has been specified,
	 * this will return the default value. If the double does not exist and no
	 * default value was specified, this will return 0.
	 *
	 * @param path Path of the double to get.
	 * @return Requested double.
	 */
	@Override
	public double getDouble(@NotNull String path) {
		return configurationSection.getDouble(path);
	}

	/**
	 * Gets the requested double by path, returning a default value if not
	 * found.
	 * <p>
	 * If the double does not exist then the specified default value will
	 * returned regardless of if a default has been identified in the root
	 * {@link Configuration}.
	 *
	 * @param path Path of the double to get.
	 * @param def  The default value to return if the path is not found or is
	 *             not a double.
	 * @return Requested double.
	 */
	@Override
	public double getDouble(@NotNull String path, double def) {
		return configurationSection.getDouble(path, def);
	}

	/**
	 * Checks if the specified path is a double.
	 * <p>
	 * If the path exists but is not a double, this will return false. If the
	 * path does not exist, this will return false. If the path does not exist
	 * but a default value has been specified, this will check if that default
	 * value is a double and return appropriately.
	 *
	 * @param path Path of the double to check.
	 * @return Whether or not the specified path is a double.
	 */
	@Override
	public boolean isDouble(@NotNull String path) {
		return isConfigurationSection(path);
	}

	/**
	 * Gets the requested long by path.
	 * <p>
	 * If the long does not exist but a default value has been specified, this
	 * will return the default value. If the long does not exist and no
	 * default value was specified, this will return 0.
	 *
	 * @param path Path of the long to get.
	 * @return Requested long.
	 */
	@Override
	public long getLong(@NotNull String path) {
		return configurationSection.getLong(path);
	}

	/**
	 * Gets the requested long by path, returning a default value if not
	 * found.
	 * <p>
	 * If the long does not exist then the specified default value will
	 * returned regardless of if a default has been identified in the root
	 * {@link Configuration}.
	 *
	 * @param path Path of the long to get.
	 * @param def  The default value to return if the path is not found or is
	 *             not a long.
	 * @return Requested long.
	 */
	@Override
	public long getLong(@NotNull String path, long def) {
		return configurationSection.getLong(path, def);
	}

	/**
	 * Checks if the specified path is a long.
	 * <p>
	 * If the path exists but is not a long, this will return false. If the
	 * path does not exist, this will return false. If the path does not exist
	 * but a default value has been specified, this will check if that default
	 * value is a long and return appropriately.
	 *
	 * @param path Path of the long to check.
	 * @return Whether or not the specified path is a long.
	 */
	@Override
	public boolean isLong(@NotNull String path) {
		return configurationSection.isLong(path);
	}

	/**
	 * Checks if the specified path is a List.
	 * <p>
	 * If the path exists but is not a List, this will return false. If the
	 * path does not exist, this will return false. If the path does not exist
	 * but a default value has been specified, this will check if that default
	 * value is a List and return appropriately.
	 *
	 * @param path Path of the List to check.
	 * @return Whether or not the specified path is a List.
	 */
	@Override
	public boolean isList(@NotNull String path) {
		return configurationSection.isList(path);
	}

	/**
	 * Gets the requested List of String by path.
	 * <p>
	 * If the List does not exist but a default value has been specified, this
	 * will return the default value. If the List does not exist and no
	 * default value was specified, this will return an empty List.
	 * <p>
	 * This method will attempt to cast any values into a String if possible,
	 * but may miss any values out if they are not compatible.
	 *
	 * @param path Path of the List to get.
	 * @return Requested List of String.
	 */
	@Override
	public @NotNull List<String> getStringList(@NotNull String path) {
		return configurationSection.getStringList(path);
	}

	/**
	 * Gets the requested ConfigurationSection by path.
	 * <p>
	 * If the ConfigurationSection does not exist but a default value has been
	 * specified, this will return the default value. If the
	 * ConfigurationSection does not exist and no default value was specified,
	 * this will return null.
	 *
	 * @param path Path of the ConfigurationSection to get.
	 * @return Requested ConfigurationSection.
	 */
	@Override
	public @Nullable ConfigurationSection getConfigurationSection(@NotNull String path) {
		return configurationSection.getConfigurationSection(path);
	}

	/**
	 * Checks if the specified path is a ConfigurationSection.
	 * <p>
	 * If the path exists but is not a ConfigurationSection, this will return
	 * false. If the path does not exist, this will return false. If the path
	 * does not exist but a default value has been specified, this will check
	 * if that default value is a ConfigurationSection and return
	 * appropriately.
	 *
	 * @param path Path of the ConfigurationSection to check.
	 * @return Whether or not the specified path is a ConfigurationSection.
	 */
	@Override
	public boolean isConfigurationSection(@NotNull String path) {
		return configurationSection.isConfigurationSection(path);
	}

	/**
	 * Gets the requested comment list by path.
	 * <p>
	 * If no comments exist, an empty list will be returned. A null entry
	 * represents an empty line and an empty String represents an empty comment
	 * line.
	 *
	 * @param path Path of the comments to get.
	 * @return A unmodifiable list of the requested comments, every entry
	 * represents one line.
	 */
	@Override
	public @NotNull List<String> getComments(@NotNull String path) {
		return configurationSection.getComments(path);
	}

	public static ReadOnlyConfigurationSection of(ConfigurationSection configurationSection) {
		return new ReadOnlyConfigurationSectionAdapter(configurationSection);
	}

}
