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

import com.winterhavenmc.util.messagebuilder.macro.MacroObjectMap;
import com.winterhavenmc.util.messagebuilder.macro.MacroProcessorHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

@SuppressWarnings("unused")
public final class Message<MessageId extends Enum<MessageId>, Macro extends Enum<Macro>> {

	// reference to plugin main class
	private final JavaPlugin plugin;

	// macro object map
	private final MacroObjectMap macroObjectMap = new MacroObjectMap();

	private final static String UNKNOWN_VALUE = "???";

	// required parameters
	private final CommandSender recipient;
	private final MessageId messageId;
	private final LanguageHandler languageHandler;
	private final MacroProcessorHandler macroProcessorHandler;

	// optional parameters
	private String altMessage;
	private String altTitle;
	private String altSubtitle;


	/**
	 * Class constructor
	 *
	 * @param plugin          reference to plugin main class
	 * @param languageHandler reference to language handler class
	 * @param macroProcessorHandler  reference to macro processor class
	 * @param recipient       message recipient
	 * @param messageId       message identifier
	 */
	public Message(final JavaPlugin plugin, final LanguageHandler languageHandler, final MacroProcessorHandler macroProcessorHandler, final CommandSender recipient, final MessageId messageId) {
		this.plugin = plugin;
		this.languageHandler = languageHandler;
		this.macroProcessorHandler = macroProcessorHandler;
		this.recipient = recipient;
		this.messageId = messageId;
	}


	/**
	 * set macro for message replacements
	 *
	 * @param macro token for placeholder
	 * @param value object that contains value that will be substituted in message
	 * @return this message object with macro value set in map
	 */
	@SuppressWarnings("UnusedReturnValue")
	public Message<MessageId, Macro> setMacro(final Macro macro, final Object value) {

		if (value instanceof Optional<?> optionalValue) {

			optionalValue.ifPresentOrElse(
					unwrappedValue -> macroObjectMap.put(macro.name(), unwrappedValue),
					() -> macroObjectMap.put(macro.name(), UNKNOWN_VALUE)
			);
		}
		else {
			macroObjectMap.put(macro.name(), value);
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

		// send message to player
		if (!this.toString().isEmpty()) {
			recipient.sendMessage(this.toString());
		}

		// if titles enabled in config, display titles
		if (plugin.getConfig().getBoolean("titles-enabled")) {
			displayTitle();
		}

		// if message repeat delay value is greater than zero, add entry to messageCooldownMap
		if (languageHandler.getRepeatDelay(messageId) > 0) {
			if (recipient instanceof Entity) {
				messageCooldown.put(messageId, (Entity) recipient);
			}
		}
	}


	private void displayTitle() {
		if (recipient instanceof Player) {

			// get title string
			String titleString;
			if (altTitle != null && !altTitle.isEmpty()) {
				titleString = macroProcessorHandler.replaceMacros(recipient, macroObjectMap, altTitle);
			}
			else {
				titleString = macroProcessorHandler.replaceMacros(recipient, macroObjectMap, languageHandler.getTitle(messageId));
			}

			// get subtitle string
			String subtitleString;
			if (altSubtitle != null && !altSubtitle.isEmpty()) {
				subtitleString = macroProcessorHandler.replaceMacros(recipient, macroObjectMap, altSubtitle);
			}
			else {
				subtitleString = macroProcessorHandler.replaceMacros(recipient, macroObjectMap, languageHandler.getSubtitle(messageId));
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


	/**
	 * performs replacements and returns formatted message string
	 */
	@Override
	public String toString() {

		// if message is not enabled in messages file, return empty string
		if (!languageHandler.isEnabled(messageId)) {
			return "";
		}

		// return message with macro replacements and color codes translated
		return getMessageString();
	}


	/**
	 * Get formatted message string with macros replaced, using alternate message if set
	 *
	 * @return message string to send
	 */
	private String getMessageString() {
		String messageString;
		if (altMessage != null && !altMessage.isEmpty()) {
			messageString = macroProcessorHandler.replaceMacros(recipient, macroObjectMap, altMessage);
		}
		else {
			messageString = macroProcessorHandler.replaceMacros(recipient, macroObjectMap, languageHandler.getMessage(messageId));
		}
		return ChatColor.translateAlternateColorCodes('&', messageString);
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

}
