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

import com.winterhavenmc.util.TimeUnit;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unused")
public final class Message<MessageId extends Enum<MessageId>, Macro extends Enum<Macro>> {

	// reference to plugin main class
	private final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(this.getClass());

	private final static String UNKNOWN_STRING = "???";

	// required parameters
	private final CommandSender recipient;
	private final MessageId messageId;
	private final LanguageHandler languageHandler;

	// optional parameters
	private final Map<Macro, Object> macroObjectMap = new HashMap<>();
	private int quantity = 1;

	private String altMessage;
	private String altTitle;
	private String altSubtitle;


	/**
	 * Class constructor
	 *
	 * @param recipient message recipient
	 * @param messageId message identifier
	 */
	public Message(final CommandSender recipient, final MessageId messageId, final LanguageHandler languageHandler) {
		this.recipient = recipient;
		this.messageId = messageId;
		this.languageHandler = languageHandler;
	}


	/**
	 * Set alternate message string
	 *
	 * @param altMessage the alternative message string
	 * @return this message object with alternative message string set
	 */
	public Message<MessageId, Macro> setAltMessage(final String altMessage) {
		if (altMessage != null) {
			this.altMessage = altMessage;
		}
		return this;
	}


	/**
	 * Set alternate title string
	 *
	 * @param altTitle the alternate title string
	 * @return this message object with alternate title string set
	 */
	public Message<MessageId, Macro> setAltTitle(final String altTitle) {
		if (altTitle != null) {
			this.altTitle = altTitle;
		}
		return this;
	}


	/**
	 * Set alternate subtitle string
	 *
	 * @param altSubtitle the alternate subtitle string
	 * @return this message object with alternate title string set
	 */
	public Message<MessageId, Macro> setAltSubtitle(final String altSubtitle) {
		if (altSubtitle != null) {
			this.altSubtitle = altSubtitle;
		}
		return this;
	}


	/**
	 * set macro for message replacements
	 *
	 * @param macro token for placeholder
	 * @param value object that contains value that will be substituted in message
	 * @return this message object with macro value set in map
	 */
	public Message<MessageId, Macro> setMacro(final Macro macro, final Object value) {

		if (value instanceof Optional<?> optionalValue) {

			optionalValue.ifPresentOrElse(
				unwrappedValue -> macroObjectMap.put(macro, unwrappedValue),
				() -> macroObjectMap.put(macro, UNKNOWN_STRING)
			);
		}
		else {
			macroObjectMap.put(macro, value);
		}
		return this;
	}


	/**
	 * Final step of message builder, performs replacements and sends message to recipient
	 */
	public void send() {

		// if message is not enabled in messages file, do nothing and return
		if (!languageHandler.isEnabled(messageId)) {
			return;
		}

		// get cooldown instance
		MessageCooldown<MessageId> messageCooldown = MessageCooldown.getInstance(plugin);

		// if message is not cooled, do nothing and return
		if (messageCooldown.isCooling(recipient, messageId, languageHandler.getRepeatDelay(messageId))) {
			return;
		}

		// get message string
		String messageString;
		if (altMessage != null && !altMessage.isEmpty()) {
			messageString = doMacroReplacements(altMessage);
		}
		else {
			messageString = doMacroReplacements(languageHandler.getMessage(messageId));
		}

		// send message to player
		if (!messageString.isEmpty()) {
			recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', messageString));
		}

		// if titles enabled in config, do title process
		if (plugin.getConfig().getBoolean("titles-enabled")) {

			if (recipient instanceof Player) {

				// get title string
				String titleString;
				if (altTitle != null && !altTitle.isEmpty()) {
					titleString = doMacroReplacements(altTitle);
				}
				else {
					titleString = doMacroReplacements(languageHandler.getTitle(messageId));
				}

				// get subtitle string
				String subtitleString;
				if (altSubtitle != null && !altSubtitle.isEmpty()) {
					subtitleString = doMacroReplacements(altSubtitle);
				}
				else {
					subtitleString = doMacroReplacements(languageHandler.getSubtitle(messageId));
				}

				// only send title if either title string or subtitle string is not empty
				if (!titleString.isEmpty() || !subtitleString.isEmpty()) {

					// get title timing values
					int titleFadeIn = languageHandler.getTitleFadeIn(messageId);
					int titleStay = languageHandler.getTitleStay(messageId);
					int titleFadeOut = languageHandler.getTitleFadeOut(messageId);

					// if title string is empty, add format code, else it won't display with subtitle only
					if (titleString.isEmpty()) {
						titleString = "&r";
					}

					// convert formatting codes
					titleString = ChatColor.translateAlternateColorCodes('&', titleString);
					subtitleString = ChatColor.translateAlternateColorCodes('&', subtitleString);

					// cast recipient to player
					Player player = (Player) recipient;

					// send title to player
					player.sendTitle(titleString, subtitleString, titleFadeIn, titleStay, titleFadeOut);
				}
			}
		}

		// if message repeat delay value is greater than zero, add entry to messageCooldownMap
		if (languageHandler.getRepeatDelay(messageId) > 0) {
			if (recipient instanceof Entity) {
				messageCooldown.put(messageId, (Entity) recipient);
			}
		}
	}


	/**
	 * Final step of message builder, performs replacements and returns message string
	 */
	@Override
	public String toString() {

		// if message is not enabled in messages file, return empty string
		if (!languageHandler.isEnabled(messageId)) {
			return "";
		}

		// get message string from file
		String messageString = languageHandler.getMessage(messageId);

		// return message with macro replacements and color codes translated
		return ChatColor.translateAlternateColorCodes('&', doMacroReplacements(messageString));
	}


	/**
	 * Do macro replacements in message string
	 *
	 * @param messageString the message string to perform replacements on
	 * @return the modified message string post replacements
	 */
	String doMacroReplacements(final String messageString) {

		String modifiedMessageString = messageString;

		// only process macro tokens if message string contains a token marker character
		if (modifiedMessageString.contains("%")) {

			// iterate over macro map, giving special treatment to certain entries
			for (Map.Entry<Macro, Object> entry : macroObjectMap.entrySet()) {

				switch (entry.getKey().toString()) {
					case "ITEM_QUANTITY":
						// if quantity is an integer, copy value to class field
						if (entry.getValue() instanceof Integer) {
							quantity = (Integer) entry.getValue();
						}
						break;
					case "WORLD":
					case "WORLD_NAME":
						// if object is a world, attempt to replace with Multiverse alias as string
						if (entry.getValue() instanceof World) {
							entry.setValue(getWorldName((World) entry.getValue()).orElse(UNKNOWN_STRING));
						}
						// if object is an entity, attempt to replace with Multiverse alias for entity world name as string
						else if (entry.getValue() instanceof Entity) {
							entry.setValue(getWorldName((Entity) entry.getValue()).orElse(UNKNOWN_STRING));
						}
						// if object is a location, attempt to replace with Multiverse alias for location world as string
						else if (entry.getValue() instanceof Location) {
							entry.setValue(getWorldName((Location) entry.getValue()).orElse(UNKNOWN_STRING));
						}
						break;
					case "LOCATION":
						// if entry type is Entity, set value to entity's location
						if (entry.getValue() instanceof Entity) {
							entry.setValue(((Entity) entry.getValue()).getLocation());
						}
						// if entry type is location, set value to formatted location string and do message replacements
						if (entry.getValue() instanceof Location location) {
							String locWorld = getWorldName(location).orElse(UNKNOWN_STRING);
							String locX = String.valueOf(location.getBlockX());
							String locY = String.valueOf(location.getBlockY());
							String locZ = String.valueOf(location.getBlockZ());
							String locString = locWorld + " [" + locX + ", " + locY + ", " + locZ + "]";
							entry.setValue(locString);
							modifiedMessageString = modifiedMessageString.replace("%LOC_WORLD%", locWorld);
							modifiedMessageString = modifiedMessageString.replace("%LOC_X%", locX);
							modifiedMessageString = modifiedMessageString.replace("%LOC_Y%", locY);
							modifiedMessageString = modifiedMessageString.replace("%LOC_Z%", locZ);
						}
						break;
					case "PLAYER_LOCATION":
						// if entry type is Entity, set value to entity's location
						if (entry.getValue() instanceof Entity) {
							entry.setValue(((Entity) entry.getValue()).getLocation());
						}
						// if entry type is location, set value to formatted location string and do message replacements
						if (entry.getValue() instanceof Location location) {
							String locWorld = getWorldName(location).orElse(UNKNOWN_STRING);
							String locX = String.valueOf(location.getBlockX());
							String locY = String.valueOf(location.getBlockY());
							String locZ = String.valueOf(location.getBlockZ());
							String locString = locWorld + " [" + locX + ", " + locY + ", " + locZ + "]";
							entry.setValue(locString);
							modifiedMessageString = modifiedMessageString.replace("%PLAYER_LOC_WORLD%", locWorld);
							modifiedMessageString = modifiedMessageString.replace("%PLAYER_LOC_X%", locX);
							modifiedMessageString = modifiedMessageString.replace("%PLAYER_LOC_Y%", locY);
							modifiedMessageString = modifiedMessageString.replace("%PLAYER_LOC_Z%", locZ);
						}
						break;
					default:
						// if key ends in "DURATION" and value type is Number, set value to time string
						if (entry.getKey().toString().endsWith("DURATION") && entry.getValue() instanceof Number) {
							entry.setValue(languageHandler.getTimeString((Long) entry.getValue()));
						}

						// if key ends in "DURATION_MINUTES" and value type is Number, set value to time string
						else if (entry.getKey().toString().endsWith("DURATION_MINUTES") && entry.getValue() instanceof Number) {
							entry.setValue(languageHandler.getTimeString((Long) entry.getValue(), TimeUnit.MINUTES));
						}

						// if entry is CommandSender, set value to name
						else if (entry.getValue() instanceof CommandSender) {
							entry.setValue(((CommandSender) entry.getValue()).getName());
						}

						// if entry is OfflinePlayer, set value to name
						else if (entry.getValue() instanceof OfflinePlayer) {
							entry.setValue(((OfflinePlayer) entry.getValue()).getName());
						}
						break;
				}

				// replace macro tokens in message string with values as string
				String macroToken = "%" + entry.getKey().toString() + "%";
				if (entry.getValue() != null) {
					modifiedMessageString = modifiedMessageString.replace(macroToken, entry.getValue().toString());
				}
			}

			// replace %ITEM_NAME% with value declared in language file
			String itemName = languageHandler.getItemName().orElse(ChatColor.RED + "UNDEFINED" + ChatColor.RESET);
			if (quantity != 1) {
				itemName = languageHandler.getItemNamePlural().orElse(ChatColor.RED + "UNDEFINED" + ChatColor.RESET);
			}
			modifiedMessageString = modifiedMessageString.replace("%ITEM%", itemName);
			modifiedMessageString = modifiedMessageString.replace("%ITEM_NAME%", itemName);

			// replace %WORLD_NAME% with recipient world name
			modifiedMessageString = modifiedMessageString.replace("%WORLD%", getWorldName(recipient).orElse(UNKNOWN_STRING));
			modifiedMessageString = modifiedMessageString.replace("%WORLD_NAME%", getWorldName(recipient).orElse(UNKNOWN_STRING));

			// replace %PLAYER_NAME% with recipient name
			modifiedMessageString = modifiedMessageString.replace("%PLAYER%", recipient.getName());
			modifiedMessageString = modifiedMessageString.replace("%PLAYER_NAME%", recipient.getName());
		}

		return modifiedMessageString;
	}


	/**
	 * get current world name of message recipient, using Multiverse alias if available
	 *
	 * @param recipient player to fetch world name
	 * @return {@link Optional} wrapped String containing recipient world name
	 */
	private Optional<String> getWorldName(final CommandSender recipient) {

		// if recipient is null, return question marks
		if (recipient == null) {
			return Optional.empty();
		}

		// get reference to plugin main class
		JavaPlugin plugin = JavaPlugin.getProvidingPlugin(Message.class);

		// get reference to Multiverse-Core if installed
		MultiverseCore mvCore = (MultiverseCore) plugin.getServer().getPluginManager().getPlugin("Multiverse-Core");

		// declare recipient world
		World world;

		// if sender is entity, set worldName to entity world name
		if (recipient instanceof Entity) {
			world = ((Entity) recipient).getWorld();
		}
		else {
			// otherwise, use server first world
			world = recipient.getServer().getWorlds().get(0);
		}

		// set result string to world name
		String resultString = world.getName();

		// if Multiverse is enabled, use Multiverse world alias if available
		if (mvCore != null && mvCore.isEnabled()) {

			// get Multiverse world object
			MultiverseWorld mvWorld = mvCore.getMVWorldManager().getMVWorld(world);

			// if Multiverse alias is not null or empty, set world name to alias if set
			if (mvWorld != null && mvWorld.getAlias() != null && !mvWorld.getAlias().isEmpty()) {
				resultString = mvWorld.getAlias();
			}
		}

		// return resultString
		return Optional.of(resultString);
	}


	/**
	 * Get world name from world object, using Multiverse alias if available
	 *
	 * @param world the world object to retrieve name
	 * @return bukkit world name or multiverse alias as {@link Optional} wrapped String
	 */
	private Optional<String> getWorldName(final World world) {

		// if world is null, return question marks
		if (world == null) {
			return Optional.empty();
		}

		// get reference to plugin main class
		JavaPlugin plugin = JavaPlugin.getProvidingPlugin(Message.class);

		// get reference to Multiverse-Core if installed
		MultiverseCore mvCore = (MultiverseCore) plugin.getServer().getPluginManager().getPlugin("Multiverse-Core");

		// get bukkit world name
		String worldName = world.getName();

		// if Multiverse is enabled, get MultiverseWorld object
		if (mvCore != null && mvCore.isEnabled()) {

			MultiverseWorld mvWorld = mvCore.getMVWorldManager().getMVWorld(world);

			// if Multiverse alias is not null or empty, set worldName to alias
			if (mvWorld != null && mvWorld.getAlias() != null && !mvWorld.getAlias().isEmpty()) {
				worldName = mvWorld.getAlias();
			}
		}

		// return the bukkit world name or Multiverse world alias
		return Optional.of(worldName);
	}


	/**
	 * Get world name for location, using Multiverse alias if available
	 *
	 * @param location the location used to retrieve world name
	 * @return bukkit world name or multiverse alias as {@link Optional} wrapped String
	 */
	private Optional<String> getWorldName(final Location location) {

		// check for null parameter
		if (location == null) {
			return Optional.empty();
		}

		// get reference to plugin main class
		JavaPlugin plugin = JavaPlugin.getProvidingPlugin(Message.class);

		// get reference to Multiverse-Core if installed
		MultiverseCore mvCore = (MultiverseCore) plugin.getServer().getPluginManager().getPlugin("Multiverse-Core");

		// declare resultString with world name for location
		String resultString;
		if (location.getWorld() != null) {
			resultString = location.getWorld().getName();
		}
		else {
			// get name of first world
			resultString = plugin.getServer().getWorlds().get(0).getName();
		}

		// if Multiverse is enabled, use Multiverse world alias if available
		if (mvCore != null && mvCore.isEnabled()) {

			// get Multiverse world object
			MultiverseWorld mvWorld = mvCore.getMVWorldManager().getMVWorld(location.getWorld());

			// if Multiverse alias is not null or empty, set world name to alias if set
			if (mvWorld != null && mvWorld.getAlias() != null && !mvWorld.getAlias().isEmpty()) {
				resultString = mvWorld.getAlias();
			}
		}

		// return resultString
		return Optional.of(resultString);
	}

}
