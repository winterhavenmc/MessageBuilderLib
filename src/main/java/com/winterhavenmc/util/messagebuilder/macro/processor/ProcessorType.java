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
import java.util.function.Function;


public enum ProcessorType {

	ENTITY(Entity.class, ctx -> new EntityProcessor()),
	COMMAND_SENDER(CommandSender.class, ctx -> new CommandSenderProcessor()),
	ITEM_STACK(ItemStack.class, ctx -> new ItemStackProcessor()),
	LOCATION(Location.class, ctx -> new LocationProcessor()),
	DURATION(Duration.class, ctx -> new DurationProcessor()),
	NUMBER(Number.class, ctx -> new NumberProcessor()),
	OFFLINE_PLAYER(OfflinePlayer.class, ctx -> new OfflinePlayerProcessor()),
	WORLD(World.class, ctx -> new WorldProcessor()),
	STRING(String.class, ctx -> new StringProcessor()),
	OBJECT(Object.class, ctx -> new ObjectProcessor()),
	NULL(NullType.class, ctx -> new NullProcessor()),
	;

	private final Class<?> handledType;
	private final Function<DependencyContext, MacroProcessor> creator;


	/**
	 * Enum constructor
	 *
	 * @param handledType the expected type of value for each processor type
	 */
	ProcessorType(Class<?> handledType, Function<DependencyContext, MacroProcessor> creator) {
		this.handledType = handledType;
		this.creator = creator;
	}


	/**
	 * Instantiate a macro processor for the type represented by this enum constant
	 *
	 * @param context a dependency injection container
	 * @return a newly created instance of a macro processor
	 */
	public MacroProcessor create(DependencyContext context) {
		return creator.apply(context);
	}


	/**
	 * Retrieve the expected type of value for a processor type
	 *
	 * @return {@code Class} the class of the expected type for a processor type
	 */
	public Class<?> getHandledType() {
		return this.handledType;
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

}
