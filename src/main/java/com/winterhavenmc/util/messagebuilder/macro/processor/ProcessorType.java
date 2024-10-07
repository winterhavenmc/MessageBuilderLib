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

import com.winterhavenmc.util.messagebuilder.LanguageHandler;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public enum ProcessorType {

	STRING() {
		@Override
		Processor create(JavaPlugin plugin, LanguageHandler languageHandler) {
			return new StringProcessor(plugin, languageHandler);
		}
	},
	ENTITY() {
		@Override
		Processor create(JavaPlugin plugin, LanguageHandler languageHandler) {
			return new EntityProcessor(plugin, languageHandler);
		}
	},
	COMMAND_SENDER() {
		@Override
		Processor create(JavaPlugin plugin, LanguageHandler languageHandler) {
			return new CommandSenderProcessor(plugin, languageHandler);
		}
	},
	ITEM_STACK() {
		@Override
		Processor create(JavaPlugin plugin, LanguageHandler languageHandler) {
			return new ItemStackProcessor(plugin, languageHandler);
		}
	},
	LOCATION() {
		@Override
		Processor create(JavaPlugin plugin, LanguageHandler languageHandler) {
			return new LocationProcessor(plugin, languageHandler);
		}
	},
	NUMBER() {
		@Override
		Processor create(JavaPlugin plugin, LanguageHandler languageHandler) {
			return new NumberProcessor(plugin, languageHandler);
		}
	},
	OFFLINE_PLAYER() {
		@Override
		Processor create(JavaPlugin plugin, LanguageHandler languageHandler) {
			return new OfflinePlayerProcessor(plugin, languageHandler);
		}
	},
	WORLD() {
		@Override
		Processor create(JavaPlugin plugin, LanguageHandler languageHandler) {
			return new WorldProcessor(plugin, languageHandler);
		}
	},
	OBJECT() {
		@Override
		Processor create(JavaPlugin plugin, LanguageHandler languageHandler) {
			return new ObjectProcessor(plugin, languageHandler);
		}
	};

	Processor create(JavaPlugin plugin, LanguageHandler languageHandler) {
		return null;
	}


	public void register(final JavaPlugin plugin, final LanguageHandler languageHandler, final ProcessorRegistry macroProcessorRegistry, final ProcessorType type) {
		macroProcessorRegistry.put(type, type.create(plugin, languageHandler));
	}


	public static ProcessorType matchType(Object object) {
		if (object instanceof String) {
			return STRING;
		}
		else if (object instanceof Entity) {
			return ENTITY;
		}
		else if (object instanceof CommandSender) {
			return COMMAND_SENDER;
		}
		else if (object instanceof OfflinePlayer) {
			return OFFLINE_PLAYER;
		}
		else if (object instanceof ItemStack) {
			return ITEM_STACK;
		}
		else if (object instanceof Location) {
			return LOCATION;
		}
		else if (object instanceof World) {
			return WORLD;
		}
		else if (object instanceof Number) {
			return NUMBER;
		}
		return OBJECT;
	}

}
