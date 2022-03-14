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

package com.winterhavenmc.util.messagebuilder;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * provides common methods for the installation and management of
 * localized language files for bukkit plugins.
 */
final class YamlLanguageHandler implements LanguageHandler {

	// reference to main plugin
	private final JavaPlugin plugin;

	// configuration object for messages file
	private Configuration messages;


	/**
	 * class constructor
	 */
	YamlLanguageHandler(final JavaPlugin plugin) {

		this.plugin = plugin;

		// load messages
		reload();
	}


	/**
	 * Get all keys for a message section of the language file. This method is only used for unit testing.
	 *
	 * @return Collection of String containing the keys for a message section of the language file
	 */
	@Override
	public Collection<String> getMessageKeys() {
		return Objects.requireNonNull(messages.getConfigurationSection("MESSAGES")).getKeys(false);
	}


	/**
	 * Check if message is enabled. If an 'enabled' key does not exist for a valid message,
	 * and a default does not exist, returns true so that the enabled setting is optional
	 * in otherwise valid messages that contain a 'string' key.
	 *
	 * @param <MessageId> parameterized type enum member for messageId
	 * @param messageId   message identifier to check
	 * @return true if message is enabled, false if not
	 */
	@Override
	public <MessageId extends Enum<MessageId>> boolean isEnabled(final MessageId messageId) {

		// check for null parameter
		if (messageId == null) {
			return false;
		}

		// if enabled setting for a message does not exist and no default is set,
		// and message string does exist, default to true
		if (messages.getString("MESSAGES." + messageId + ".enabled") == null
				&& messages.getString("MESSAGES." + messageId + ".string") != null) {
			return true;
		}

		// return boolean value of message enabled setting
		return messages.getBoolean("MESSAGES." + messageId + ".enabled");
	}


	/**
	 * get message repeat delay from language file
	 *
	 * @param <MessageId> parameterized type enum member for messageId
	 * @param messageId   message identifier to retrieve message delay
	 * @return int message repeat delay in seconds
	 */
	@Override
	public <MessageId extends Enum<MessageId>> long getRepeatDelay(final MessageId messageId) {

		// check for null parameter
		if (messageId == null) {
			return 0L;
		}

		return messages.getLong("MESSAGES." + messageId + ".repeat-delay");
	}


	/**
	 * get message text from language file
	 *
	 * @param <MessageId> parameterized type enum member for messageId
	 * @param messageId   message identifier to retrieve message text string
	 * @return String message text, or empty string if no message string found
	 */
	@Override
	public <MessageId extends Enum<MessageId>> String getMessage(final MessageId messageId) {

		// check for null parameter
		if (messageId == null) {
			return "";
		}

		// check for message string at .message
		String string = messages.getString("MESSAGES." + messageId + ".message");

		// if null, check for message at .string
		if (string == null) {
			string = messages.getString("MESSAGES." + messageId + ".string");
		}

		// if null, set to empty string
		if (string == null) {
			string = "";
		}

		return ChatColor.translateAlternateColorCodes('&', string);
	}


	/**
	 * get title text from language file
	 *
	 * @param <MessageId> parameterized type enum member for messageId
	 * @param messageId   message identifier to retrieve title text string
	 * @return String title text, or empty string if no title string found
	 */
	@Override
	public <MessageId extends Enum<MessageId>> String getTitle(final MessageId messageId) {

		// check for null parameter
		if (messageId == null) {
			return "";
		}

		String string = messages.getString("MESSAGES." + messageId + ".title", "");

		return ChatColor.translateAlternateColorCodes('&', string);
	}


	/**
	 * get subtitle text from language file
	 *
	 * @param <MessageId> parameterized type enum member for messageId
	 * @param messageId   message identifier to retrieve subtitle text string
	 * @return String subtitle text, or empty string if no subtitle string found
	 */
	@Override
	public <MessageId extends Enum<MessageId>> String getSubtitle(final MessageId messageId) {

		// check for null parameter
		if (messageId == null) {
			return "";
		}

		String string = messages.getString("MESSAGES." + messageId + ".subtitle", "");

		return ChatColor.translateAlternateColorCodes('&', string);
	}


	@Override
	public <MessageId extends Enum<MessageId>> int getTitleFadeIn(final MessageId messageId) {

		// check for null parameter
		if (messageId == null || !messages.isInt("MESSAGES." + messageId + ".title-fade-in")) {
			return 10;
		}
		return messages.getInt("MESSAGES." + messageId + ".title-fade-in");
	}


	@Override
	public <MessageId extends Enum<MessageId>> int getTitleStay(final MessageId messageId) {

		// check for null parameter
		if (messageId == null || !messages.isInt("MESSAGES." + messageId + ".title-stay")) {
			return 70;
		}
		return messages.getInt("MESSAGES." + messageId + ".title-stay");
	}


	@Override
	public <MessageId extends Enum<MessageId>> int getTitleFadeOut(final MessageId messageId) {

		// check for null parameter
		if (messageId == null || !messages.isInt("MESSAGES." + messageId + ".title-fade-out")) {
			return 20;
		}
		return messages.getInt("MESSAGES." + messageId + ".title-fade-out");
	}


	/**
	 * Get item name from language specific messages file, with translated color codes
	 *
	 * @return String ITEM_NAME, or empty string if key not found
	 */
	@Override
	public String getItemName() {

		String string = messages.getString("ITEM_INFO.ITEM_NAME");

		if (string == null) {
			string = "";
		}

		return ChatColor.translateAlternateColorCodes('&', string);
	}


	/**
	 * Get configured plural item name from language file
	 *
	 * @return the formatted plural display name of an item, or empty string if key not found
	 */
	@Override
	public String getItemNamePlural() {

		String string = messages.getString("ITEM_INFO.ITEM_NAME_PLURAL");

		if (string == null) {
			string = "";
		}

		return ChatColor.translateAlternateColorCodes('&', string);
	}


	/**
	 * Get configured inventory item name from language file
	 *
	 * @return the formatted inventory display name of an item, or empty string if key not found
	 */
	@Override
	public String getInventoryItemName() {

		String string = messages.getString("ITEM_INFO.INVENTORY_ITEM_NAME");

		if (string == null) {
			string = "";
		}

		return ChatColor.translateAlternateColorCodes('&', string);
	}


	/**
	 * Get item lore from language specific messages file, with translated color codes
	 *
	 * @return List of strings, one string for each line of lore, or empty list if key not found
	 */
	@Override
	public List<String> getItemLore() {

		List<String> configLore = messages.getStringList("ITEM_INFO.ITEM_LORE");
		List<String> coloredLore = new ArrayList<>();
		for (String line : configLore) {
			coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
		}
		return coloredLore;
	}


	/**
	 * Get spawn display name from language file
	 *
	 * @return the formatted display name for the world spawn, or empty string if key not found
	 */
	@Override
	public String getSpawnDisplayName() {

		String string = messages.getString("ITEM_INFO.SPAWN_DISPLAY_NAME");

		if (string == null) {
			string = "";
		}

		return ChatColor.translateAlternateColorCodes('&', string);
	}


	/**
	 * Get home display name from language file
	 *
	 * @return the formatted display name for home, or empty string if key not found
	 */
	@Override
	public String getHomeDisplayName() {

		String string = messages.getString("ITEM_INFO.HOME_DISPLAY_NAME");

		if (string == null) {
			string = "";
		}

		return ChatColor.translateAlternateColorCodes('&', string);
	}


	/**
	 * Format the time string with days, hours, minutes and seconds as necessary
	 *
	 * @param duration a time duration in milliseconds
	 * @return formatted time string
	 */
	@Override
	public String getTimeString(final long duration) {
		return getTimeString(duration, TimeUnit.SECONDS);
	}


	/**
	 * Format the time string with days, hours, minutes and seconds as necessary, to the granularity passed
	 *
	 * @param duration a time duration in milliseconds
	 * @param timeUnit the time granularity to display (days | hours | minutes | seconds)
	 * @return formatted time string
	 * @throws NullPointerException if parameter is null
	 */
	@Override
	public String getTimeString(final long duration, final TimeUnit timeUnit) {

		// check for null parameter
		Objects.requireNonNull(timeUnit);

		// if duration is negative, return unlimited time string
		if (duration < 0) {

			String string = messages.getString("TIME_STRINGS.UNLIMITED");

			if (string == null) {
				string = "unlimited";
			}

			return ChatColor.translateAlternateColorCodes('&', string);
		}

		// return string if less than 1 of passed timeUnit remains
		String lessString = messages.getString("TIME_STRINGS.LESS_THAN_ONE");
		if (lessString == null) {
			lessString = "< 1";
		}
		if (timeUnit.equals(TimeUnit.DAYS)
				&& TimeUnit.MILLISECONDS.toDays(duration) < 1) {
			return lessString + " " + messages.getString("TIME_STRINGS.DAY");
		}
		if (timeUnit.equals(TimeUnit.HOURS)
				&& TimeUnit.MILLISECONDS.toHours(duration) < 1) {
			return lessString + " " + messages.getString("TIME_STRINGS.HOUR");
		}
		if (timeUnit.equals(TimeUnit.MINUTES)
				&& TimeUnit.MILLISECONDS.toMinutes(duration) < 1) {
			return lessString + " " + messages.getString("TIME_STRINGS.MINUTE");
		}
		if (timeUnit.equals(TimeUnit.SECONDS)
				&& TimeUnit.MILLISECONDS.toSeconds(duration) < 1) {
			return lessString + " " + messages.getString("TIME_STRINGS.SECOND");
		}


		StringBuilder timeString = new StringBuilder();

		int days = (int) TimeUnit.MILLISECONDS.toDays(duration);
		int hours = (int) TimeUnit.MILLISECONDS.toHours(duration % TimeUnit.DAYS.toMillis(1));
		int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(duration % TimeUnit.HOURS.toMillis(1));
		int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(duration % TimeUnit.MINUTES.toMillis(1));

		String dayString = messages.getString("TIME_STRINGS.DAY");
		if (dayString == null) {
			dayString = "day";
		}
		String dayPluralString = messages.getString("TIME_STRINGS.DAY_PLURAL");
		if (dayPluralString == null) {
			dayPluralString = "days";
		}
		String hourString = messages.getString("TIME_STRINGS.HOUR");
		if (hourString == null) {
			hourString = "hour";
		}
		String hourPluralString = messages.getString("TIME_STRINGS.HOUR_PLURAL");
		if (hourPluralString == null) {
			hourPluralString = "hours";
		}
		String minuteString = messages.getString("TIME_STRINGS.MINUTE");
		if (minuteString == null) {
			minuteString = "minute";
		}
		String minutePluralString = messages.getString("TIME_STRINGS.MINUTE_PLURAL");
		if (minutePluralString == null) {
			minutePluralString = "minutes";
		}
		String secondString = messages.getString("TIME_STRINGS.SECOND");
		if (secondString == null) {
			secondString = "second";
		}
		String secondPluralString = messages.getString("TIME_STRINGS.SECOND_PLURAL");
		if (secondPluralString == null) {
			secondPluralString = "seconds";
		}

		if (days > 1) {
			timeString.append(days);
			timeString.append(' ');
			timeString.append(dayPluralString);
			timeString.append(' ');
		}
		else if (days == 1) {
			timeString.append(days);
			timeString.append(' ');
			timeString.append(dayString);
			timeString.append(' ');
		}

		if (timeUnit.equals(TimeUnit.HOURS)
				|| timeUnit.equals(TimeUnit.MINUTES)
				|| timeUnit.equals(TimeUnit.SECONDS)) {
			if (hours > 1) {
				timeString.append(hours);
				timeString.append(' ');
				timeString.append(hourPluralString);
				timeString.append(' ');
			}
			else if (hours == 1) {
				timeString.append(hours);
				timeString.append(' ');
				timeString.append(hourString);
				timeString.append(' ');
			}
		}

		if (timeUnit.equals(TimeUnit.MINUTES) || timeUnit.equals(TimeUnit.SECONDS)) {
			if (minutes > 1) {
				timeString.append(minutes);
				timeString.append(' ');
				timeString.append(minutePluralString);
				timeString.append(' ');
			}
			else if (minutes == 1) {
				timeString.append(minutes);
				timeString.append(' ');
				timeString.append(minuteString);
				timeString.append(' ');
			}
		}

		if (timeUnit.equals(TimeUnit.SECONDS)) {
			if (seconds > 1) {
				timeString.append(seconds);
				timeString.append(' ');
				timeString.append(secondPluralString);
			}
			else if (seconds == 1) {
				timeString.append(seconds);
				timeString.append(' ');
				timeString.append(secondString);
			}
		}

		return ChatColor.translateAlternateColorCodes('&', timeString.toString().trim());
	}


	/**
	 * Retrieve an arbitrary string from the language file with the specified key.
	 *
 	 * @param path the message path for the string being retrieved
	 * @return the retrieved string, or null if no matching key found
	 */
	public String getString(final String path) {
		return messages.getString(path);
	}


	/**
	 * Get List of String by path in message file
	 *
	 * @param path the message path for the string list being retrieved
	 * @return List of String - the string list retrieved by path from message file
	 */
	public List<String> getStringList(final String path) {
		return messages.getStringList(path);
	}


	/**
	 * Reload messages from yaml file into Configuration object. If the yaml language file
	 * does not exist in the plugin data directory, it will be re-copied from the jar embedded resource.
	 */
	@Override
	public void reload() {

		// install message files if necessary; this will not overwrite existing files
		YamlFileInstaller installer = new YamlFileInstaller(plugin);
		Collection<String> filenames = installer.getResourceFilenames();
		installer.installResourceFiles(filenames);

		// load messages Configuration object from configured language file
		messages = new YamlFileLoader(plugin).getMessages();
	}

}
