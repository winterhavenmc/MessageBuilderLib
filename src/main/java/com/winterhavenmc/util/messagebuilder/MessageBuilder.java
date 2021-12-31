package com.winterhavenmc.util.messagebuilder;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class MessageBuilder<MessageId extends Enum<MessageId>, Macro extends Enum<Macro>> {

	LanguageHandler languageHandler;


	public MessageBuilder(final JavaPlugin plugin) {
		this.languageHandler = new LanguageHandlerImpl(plugin);
	}


	public Message<MessageId, Macro> build(final CommandSender recipient, final MessageId messageId) {
		return new Message<>(recipient, messageId, languageHandler);
	}


	public void reload() {
		languageHandler.reload();
	}


	public boolean isEnabled(MessageId messageId) {
		return languageHandler.isEnabled(messageId);
	}


	public long getRepeatDelay(MessageId messageId) {
		return languageHandler.getRepeatDelay(messageId);
	}


	public String getMessage(MessageId messageId) {
		return languageHandler.getMessage(messageId);
	}


	public String getItemName() {
		return languageHandler.getItemName();
	}


	public String getItemNamePlural() {
		return languageHandler.getItemNamePlural();
	}


	public String getInventoryItemName() {
		return languageHandler.getInventoryItemName();
	}


	public List<String> getItemLore() {
		return languageHandler.getItemLore();
	}


	public String getSpawnDisplayName() {
		return languageHandler.getSpawnDisplayName();
	}


	public String getHomeDisplayName() {
		return languageHandler.getHomeDisplayName();
	}


	public String getTimeString(long duration) {
		return languageHandler.getTimeString(duration);
	}


	public String getTimeString(long duration, TimeUnit timeUnit) {
		return languageHandler.getTimeString(duration, timeUnit);
	}

}
