/*
 * Copyright (c) 2024 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.query;

import com.winterhavenmc.util.TimeUnit;
import com.winterhavenmc.util.messagebuilder.namespace.Namespace;
import com.winterhavenmc.util.messagebuilder.query.domain.DomainQueryHandler;
import com.winterhavenmc.util.messagebuilder.query.domain.item.ItemRecord;
import com.winterhavenmc.util.messagebuilder.query.domain.message.MessageRecord;
import org.bukkit.World;

import java.util.List;
import java.util.Optional;

public interface LanguageQueryHandler {

	/**
	 * Get a query type handler based on the namespaced Domain passed
	 *
	 * @param domain the namespace Domain that dictates the query handler type to be returned
	 * @return a query handler of the type dictated by the passed namespace Domain
	 */
	DomainQueryHandler<?> getQueryHandler(Namespace.Domain domain);

	Optional<ItemRecord> getItemRecord(String itemKey);

	<MessageId extends Enum<MessageId>> Optional<MessageRecord> getMessageRecord(MessageId messageId);

	String getTimeString(long duration);

	String getTimeString(long duration, TimeUnit timeUnit);

	Optional<String> getString(final String key);

	List<String> getStringList(String key);

	//TODO: Everything below needs to go somewhere else

	Optional<String> getWorldName(final World world);

	Optional<String> getWorldAlias(final World world);

}
