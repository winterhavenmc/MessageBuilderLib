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

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import javax.lang.model.type.NullType;
import java.time.Duration;


public enum ProcessorType {

	ENTITY(Entity.class),
	COMMAND_SENDER(CommandSender.class),
	ITEM_STACK(ItemStack.class),
	LOCATION(Location.class),
	DURATION(Duration.class),
	NUMBER(Number.class),
	OFFLINE_PLAYER(OfflinePlayer.class),
	WORLD(World.class),
	STRING(String.class),
	OBJECT(Object.class),
	NULL(NullType.class),
	;

	private final Class<?> expectedType;


	/**
	 * Enum constructor
	 *
	 * @param expectedType the expected type of value for each processor type
	 */
	ProcessorType(Class<?> expectedType) {
		this.expectedType = expectedType;
	}


	/**
	 * Retrieve the expected type of value for a processor type
	 *
	 * @return {@code Class} the class of the expected type for a processor type
	 */
	public Class<?> getExpectedType() {
		return this.expectedType;
	}


	/**
	 * Static method that returns an appropriate {@code ProcessorType} for a given object. Null objects
	 * return a NULL processor type, and unmatched objects fall through to the default case and return
	 * an OBJECT processor type.
	 *
	 * @param object the object to match to a ProcessorType
	 * @return {@code ProcessorType} the matching processor type for the object
	 */
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


	/**
	 * Static method that returns an instance of a macro processor for the given {@code ProcessorType}
	 *
	 * @param processorType the ype of processor to instantiate
	 * @return a new instantiation of a macro processor for the given {@code ProcessorType}
	 */
	public static MacroProcessor<?> create(final ProcessorType processorType) {
		return switch (processorType) {
			case COMMAND_SENDER -> new CommandSenderProcessor<>();
			case DURATION -> new DurationProcessor<>();
			case ENTITY -> new EntityProcessor<>();
			case ITEM_STACK -> new ItemStackProcessor<>();
			case LOCATION -> new LocationProcessor<>();
			case NULL -> new NullProcessor<>();
			case NUMBER -> new NumberProcessor<>();
			case OBJECT -> new ObjectProcessor<>();
			case OFFLINE_PLAYER -> new OfflinePlayerProcessor<>();
			case STRING -> new StringProcessor<>();
			case WORLD -> new WorldProcessor<>();
		};
	}

}
