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

package com.winterhavenmc.util.messagebuilder;

import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * The main class for SoundConfigTest plugin
 */
public class PluginMain extends JavaPlugin {

    MessageBuilder<MessageId, Macro> messageBuilder;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        // instantiate message builder
        messageBuilder = new MessageBuilder<>(this);
    }

}
