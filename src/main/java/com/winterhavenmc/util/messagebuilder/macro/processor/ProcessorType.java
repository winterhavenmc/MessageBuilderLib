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

	ENTITY(Entity.class) {
		@Override
		MacroProcessor create(final LanguageQueryHandler queryHandler) {
			return new EntityProcessor(queryHandler);
		}
	},
	COMMAND_SENDER(CommandSender.class) {
		@Override
		MacroProcessor create(final LanguageQueryHandler queryHandler) {
			return new CommandSenderProcessor(queryHandler);
		}
	},
	ITEM_STACK(ItemStack.class) {
		@Override
		MacroProcessor create(final LanguageQueryHandler queryHandler) {
			return new ItemStackProcessor(queryHandler);
		}
	},
	LOCATION(Location.class) {
		@Override
		MacroProcessor create(final LanguageQueryHandler queryHandler) {
			return new LocationProcessor(queryHandler);
		}
	},
	DURATION(Duration.class) {
		@Override
		MacroProcessor create(final LanguageQueryHandler queryHandler) {
			return new DurationProcessor(queryHandler);
		}
	},
	NUMBER(Number.class) {
		@Override
		MacroProcessor create(final LanguageQueryHandler queryHandler) {
			return new NumberProcessor(queryHandler);
		}
	},
	OFFLINE_PLAYER(OfflinePlayer.class) {
		@Override
		MacroProcessor create(LanguageQueryHandler queryHandler) {
			return new OfflinePlayerProcessor(queryHandler);
		}
	},
	WORLD(World.class) {
		@Override
		MacroProcessor create(final LanguageQueryHandler queryHandler) {
			return new WorldProcessor(queryHandler);
		}
	},
	STRING(String.class) {
		@Override
		MacroProcessor create(final LanguageQueryHandler queryHandler) {
			return new StringProcessor(queryHandler);
		}
	},
	OBJECT(Object.class) {
		@Override
		MacroProcessor create(final LanguageQueryHandler queryHandler) {
			return new ObjectProcessor(queryHandler);
		}
	},
	NULL(NullType.class) {
		@Override
		MacroProcessor create(final LanguageQueryHandler queryHandler) {
			return new NullProcessor(queryHandler);
		}
	};

	private final Class<?> expectedType;


	ProcessorType(Class<?> expectedType) {
		this.expectedType = expectedType;
	}

	abstract MacroProcessor create(final LanguageQueryHandler queryHandler);


	public void register(final LanguageQueryHandler queryHandler,
	                     final ProcessorRegistry macroProcessorRegistry,
	                     final ProcessorType type) {
		macroProcessorRegistry.put(type, type.create(queryHandler));
	}


	public static ProcessorType matchType(final Object object) {
		return switch (object) {
			case Entity ignored -> ENTITY;
			case CommandSender ignored -> COMMAND_SENDER;
			case OfflinePlayer ignored -> OFFLINE_PLAYER;
			case ItemStack ignored -> ITEM_STACK;
			case Location ignored -> LOCATION;
			case World ignored -> WORLD;
			case Number ignored -> NUMBER;
			case String ignored -> STRING;
			case null -> NULL;
			default -> OBJECT;
		};
	}

	public Class<?> getExpectedType() {
		return this.expectedType;
	}

}
