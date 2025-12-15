package com.winterhavenmc.library.messagebuilder.core.context;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.retrievers.ItemStackNameRetriever;

public record NameResolverCtx(ItemStackNameRetriever nameRetriever, ItemStackNameRetriever displayNameRetriever, ItemStackNameRetriever pluralNameRetriever) { }
