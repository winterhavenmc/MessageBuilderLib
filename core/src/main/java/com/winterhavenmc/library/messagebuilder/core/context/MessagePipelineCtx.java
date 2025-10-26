package com.winterhavenmc.library.messagebuilder.core.context;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.cooldown.CooldownMap;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.processors.Processor;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.retrievers.MessageRetriever;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.senders.Sender;

import java.util.List;

public record MessagePipelineCtx(MessageRetriever messageRetriever,
								 Processor messageProcessor,
								 CooldownMap cooldownMap,
								 List<Sender> senders) { }
