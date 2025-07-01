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

package com.winterhavenmc.library.messagebuilder.macro.processor;

import com.winterhavenmc.library.messagebuilder.LanguageHandler;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;


public enum ProcessorType {

	STRING() {
		@Override
		Processor create(final LanguageHandler languageHandler) {
			return new StringProcessor(languageHandler);
		}
	},
	ENTITY() {
		@Override
		Processor create(final LanguageHandler languageHandler) {
			return new EntityProcessor(languageHandler);
		}
	},
	COMMAND_SENDER() {
		@Override
		Processor create(final LanguageHandler languageHandler) {
			return new CommandSenderProcessor(languageHandler);
		}
	},
	ITEM_STACK() {
		@Override
		Processor create(final LanguageHandler languageHandler) {
			return new ItemStackProcessor(languageHandler);
		}
	},
	LOCATION() {
		@Override
		Processor create(final LanguageHandler languageHandler) {
			return new LocationProcessor(languageHandler);
		}
	},
	NUMBER() {
		@Override
		Processor create(final LanguageHandler languageHandler) {
			return new NumberProcessor(languageHandler);
		}
	},
	OFFLINE_PLAYER() {
		@Override
		Processor create(final LanguageHandler languageHandler) {
			return new OfflinePlayerProcessor(languageHandler);
		}
	},
	WORLD() {
		@Override
		Processor create(final LanguageHandler languageHandler) {
			return new WorldProcessor(languageHandler);
		}
	},
	OBJECT() {
		@Override
		Processor create(final LanguageHandler languageHandler) {
			return new ObjectProcessor(languageHandler);
		}
	},
	NULL() {
		@Override
		Processor create(final LanguageHandler languageHandler) {
			return new NullProcessor(languageHandler);
		}
	};

	abstract Processor create(final LanguageHandler languageHandler);


	public void register(final LanguageHandler languageHandler,
	                     final ProcessorRegistry macroProcessorRegistry,
	                     final ProcessorType type) {
		macroProcessorRegistry.put(type, type.create(languageHandler));
	}


	public static ProcessorType matchType(final Object object) {
		return switch (object) {
			case String ignored -> STRING;
			case Entity ignored -> ENTITY;
			case CommandSender ignored -> COMMAND_SENDER;
			case OfflinePlayer ignored -> OFFLINE_PLAYER;
			case ItemStack ignored -> ITEM_STACK;
			case Location ignored -> LOCATION;
			case World ignored -> WORLD;
			case Number ignored -> NUMBER;
			case null -> NULL;
			default -> OBJECT;
		};
	}

}
