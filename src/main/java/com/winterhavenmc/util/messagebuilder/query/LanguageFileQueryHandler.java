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
import org.bukkit.World;

import java.util.List;
import java.util.Optional;

public interface LanguageFileQueryHandler {

	List<String> getStringList(String key);

	Optional<ItemRecord> getItemRecord(String itemKey);
	<MessageId extends Enum<MessageId>> Optional<MessageRecord> getMessageRecord(MessageId messageId);

	Optional<String> getString(final String key);


	//TODO: Everything below needs to go somewhere else eventually

	Optional<String> getWorldName(final World world);
	Optional<String> getWorldAlias(final World world);

	String getTimeString(long duration);
	String getTimeString(long duration, TimeUnit timeUnit);

}
