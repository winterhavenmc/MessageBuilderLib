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

package com.winterhavenmc.util.messagebuilder.pipeline;

import com.winterhavenmc.util.messagebuilder.Message;
import com.winterhavenmc.util.messagebuilder.cooldown.CooldownKey;
import com.winterhavenmc.util.messagebuilder.cooldown.CooldownMap;
import com.winterhavenmc.util.messagebuilder.macro.MacroReplacer;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;

import java.util.Optional;
import java.util.function.Predicate;

import static com.winterhavenmc.util.messagebuilder.pipeline.MessagePredicates.IS_ENABLED;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.Parameter.MESSAGE;

public class MessageProcessor implements Processor {

	private final LanguageQueryHandler languageQueryHandler;
	private final MacroReplacer macroReplacer;
	private final CooldownMap cooldownMap;


	public MessageProcessor(final LanguageQueryHandler languageQueryHandler,
	                        final MacroReplacer macroReplacer,
	                        final CooldownMap cooldownMap)
	{
		this.languageQueryHandler = languageQueryHandler;
		this.macroReplacer = macroReplacer;
		this.cooldownMap = cooldownMap;
	}

	public <Macro extends Enum<Macro>>
	void process(final Message<Macro> message)
	{
		if (message == null) { throw new LocalizedException(PARAMETER_NULL, MESSAGE); }

		// define predicates
		Predicate<CooldownKey> isCooling = cooldownMap::isCooling;

		// Replacer replacer = new MacroReplacer();     // BiFunction (injected into class)
		Sender messageSender = new MessageSender();     // Consumer
		Sender titleSender = new TitleSender();         // Consumer

		// get optional message record
		Optional<MessageRecord> messageRecord = new MessageRetriever().getRecord(message.getMessageId(), languageQueryHandler);

		// if optional message record is present and enabled, recipient is online if player, and message is not cooling
		if (messageRecord.isPresent()
				&& IS_ENABLED.test(messageRecord.get())
				&& !isCooling.test(new CooldownKey(message.getRecipient(), message.getMessageId())))
		{
			// perform macro replacements
			Optional<MessageRecord> finalMesssageRecord = macroReplacer.replaceMacros(messageRecord.get(), message.getContextMap());

			// send message
			finalMesssageRecord.ifPresent(record -> messageSender.send(message.getRecipient(), record));
			finalMesssageRecord.ifPresent(record -> titleSender.send(message.getRecipient(), record));

			// set cooldown
			finalMesssageRecord.ifPresent(record -> cooldownMap.putExpirationTime(message.getRecipient(), record));
		}
	}

}
