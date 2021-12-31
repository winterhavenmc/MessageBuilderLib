package com.winterhavenmc.util.messagebuilder;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

/**
 * The main class for SoundConfigTest plugin
 */
@SuppressWarnings("unused")
public final class PluginMain extends JavaPlugin {

//    LanguageHandler languageHandler;
    MessageBuilder<MessageId, Macro> messageBuilder;

    public PluginMain() {
        super();
    }


    private PluginMain(JavaPluginLoader loader, PluginDescriptionFile descriptionFile, File dataFolder, File file) {
        super(loader, descriptionFile, dataFolder, file);
    }


    @Override
    public void onEnable() {

        reloadConfig();

        // instantiate language handler
//        languageHandler = new LanguageHandlerImpl(this);

        // instantiate message builder
        messageBuilder = new MessageBuilder<>(this);
    }

}
