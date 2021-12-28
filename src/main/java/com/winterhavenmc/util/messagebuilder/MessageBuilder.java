package com.winterhavenmc.util.messagebuilder;

import org.bukkit.command.CommandSender;

@SuppressWarnings("unused")
public class MessageBuilder<MessageId extends Enum<MessageId>, Macro extends Enum<Macro>> {

	public Message<MessageId, Macro> build(final CommandSender recipient, final MessageId messageId) {
		return new Message<>(recipient, messageId);
	}

}
