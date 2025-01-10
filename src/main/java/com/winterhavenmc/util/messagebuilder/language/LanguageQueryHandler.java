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

package com.winterhavenmc.util.messagebuilder.language;

import com.winterhavenmc.util.messagebuilder.language.section.Section;
import com.winterhavenmc.util.messagebuilder.language.section.SectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.items.ItemRecord;
import com.winterhavenmc.util.messagebuilder.language.section.messages.MessageRecord;
import org.bukkit.World;

import java.util.Optional;


public interface LanguageQueryHandler {

	/**
	 * Get a query type handler based on the namespaced Section passed
	 *
	 * @param section the namespace Section that dictates the query handler type to be returned
	 * @return a query handler of the type dictated by the passed namespace Section
	 */
	SectionQueryHandler<?> getQueryHandler(Section section);

	Optional<ItemRecord> getItemRecord(final String itemKey);

	<MessageId extends Enum<MessageId>> Optional<MessageRecord> getMessageRecord(MessageId messageId);

	//TODO: Everything below needs to go somewhere else

	Optional<String> getWorldName(final World world);

}
