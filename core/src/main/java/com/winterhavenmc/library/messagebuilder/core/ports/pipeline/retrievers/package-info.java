/**
 * Defines the entry point to the message pipeline, responsible for retrieving
 * {@link com.winterhavenmc.library.messagebuilder.models.language.MessageRecord MessageRecord}
 * instances from a configuration-backed query system.
 *
 * <p>This package encapsulates the "retrieval" stage, where a {@code LegacyRecordKey}
 * is used to obtain a structured message definition from a YAML language file or
 * other backing store.
 *
 * <h2>Key Components</h2>
 * <ul>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.retrievers.MessageRetriever}
 *       – A functional interface that abstracts the retrieval of message records.</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.core.pipeline.retrievers.MessageRetriever}
 *       – Default implementation that uses a
 *       {@code com.winterhavenmc.library.messagebuilder.query.QueryHandler QueryHandler}
 *       and guarantees a non-null fallback result.</li>
 * </ul>
 *
 * <p>This stage enables downstream processors to work exclusively with structured
 * {@code MessageRecord} instances, insulating the rest of the pipeline from
 * missing or malformed entries.
 *
 * @see com.winterhavenmc.library.messagebuilder.core.ports.pipeline.retrievers.MessageRetriever MessageRetriever
 * @see com.winterhavenmc.library.messagebuilder.core.pipeline.retrievers.MessageRetriever MessageRetriever
 * @see com.winterhavenmc.library.messagebuilder.models.language.MessageRecord MessageRecord
 */
package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.retrievers;
