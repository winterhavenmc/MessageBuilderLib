package com.winterhavenmc.util.messagebuilder;

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
