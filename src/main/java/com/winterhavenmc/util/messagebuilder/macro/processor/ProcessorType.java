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

package com.winterhavenmc.util.messagebuilder.macro.processor;

import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import javax.lang.model.type.NullType;
import java.time.Duration;


public enum ProcessorType {

	ENTITY(EntityProcessor.class, Entity.class),
	COMMAND_SENDER(CommandSenderProcessor.class, CommandSender.class),
	ITEM_STACK(ItemStackProcessor.class, ItemStack.class),
	LOCATION(LocationProcessor.class, Location.class),
	DURATION(DurationProcessor.class, Duration.class),
	NUMBER(NumberProcessor.class, Number.class),
	OFFLINE_PLAYER(OfflinePlayerProcessor.class, OfflinePlayer.class),
	WORLD(WorldProcessor.class, World.class),
	STRING(StringProcessor.class, String.class),
	OBJECT(ObjectProcessor.class, Object.class),
	NULL(NullProcessor.class, NullType.class),
	;

	private final Class<? extends MacroProcessor> processorClass;
	private final Class<?> expectedType;


	ProcessorType(Class<? extends MacroProcessor> processorClass, Class<?> expectedType) {
		this.processorClass = processorClass;
		this.expectedType = expectedType;
	}


	public static ProcessorType matchType(final Object object) {
		return switch (object) {
			case Entity ignored -> ENTITY;
			case CommandSender ignored -> COMMAND_SENDER;
			case OfflinePlayer ignored -> OFFLINE_PLAYER;
			case ItemStack ignored -> ITEM_STACK;
			case Location ignored -> LOCATION;
			case World ignored -> WORLD;
			case Duration ignored -> DURATION;
			case Number ignored -> NUMBER;
			case String ignored -> STRING;
			case null -> NULL;
			default -> OBJECT;
		};
	}

	public Class<?> getExpectedType() {
		return this.expectedType;
	}


	public static MacroProcessor of(final ProcessorType processorType, final LanguageQueryHandler languageQueryHandler) {
		return switch (processorType) {
			case COMMAND_SENDER -> new CommandSenderProcessor(languageQueryHandler);
			case DURATION -> new DurationProcessor(languageQueryHandler);
			case ENTITY -> new EntityProcessor(languageQueryHandler);
			case ITEM_STACK -> new ItemStackProcessor(languageQueryHandler);
			case LOCATION -> new LocationProcessor(languageQueryHandler);
			case NULL -> new NullProcessor(languageQueryHandler);
			case NUMBER -> new NumberProcessor(languageQueryHandler);
			case OBJECT -> new ObjectProcessor(languageQueryHandler);
			case OFFLINE_PLAYER -> new OfflinePlayerProcessor(languageQueryHandler);
			case STRING -> new StringProcessor(languageQueryHandler);
			case WORLD -> new WorldProcessor(languageQueryHandler);
		};
	}

}
