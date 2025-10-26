package com.winterhavenmc.library.messagebuilder.core.context;

import com.winterhavenmc.library.messagebuilder.core.message.ValidMessage;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.Pipeline;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMessageKey;
import org.bukkit.plugin.Plugin;


public record MessageCtx(Plugin plugin,
						 ValidMessage validMessage,
						 ValidMessageKey validMessageKey,
						 Pipeline messagePipeline) { }
