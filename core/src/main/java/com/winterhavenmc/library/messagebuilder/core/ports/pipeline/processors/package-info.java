/**
 * Defines the final stage in the message processing pipeline, responsible for converting a structured
 * {@link com.winterhavenmc.library.messagebuilder.models.language.ValidMessageRecord ValidMessageRecord}
 * into a fully resolved
 * {@link com.winterhavenmc.library.messagebuilder.models.language.FinalMessageRecord FinalMessageRecord}.
 *
 * <p>At this stage, all macros within the message fields (such as the body text, title, and subtitle)
 * are replaced with their corresponding values, derived from context data stored in a
 * {@link com.winterhavenmc.library.messagebuilder.core.maps.MacroObjectMap MacroObjectMap}.
 *
 * <h2>Key Components</h2>
 * <ul>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.processors.Processor} –
 *       A functional interface that transforms message records by resolving macro placeholders.</li>
 *   <li>{@code com.winterhavenmc.library.messagebuilder.core.pipeline.processor.MessageProcessor} –
 *       The default implementation that performs macro substitution using a configured
 *       {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.replacers.MacroReplacer MacroReplacer}.</li>
 * </ul>
 *
 * <p>This stage produces the final output-ready representation of a message and completes
 * the macro resolution lifecycle.
 *
 * @see com.winterhavenmc.library.messagebuilder.core.ports.pipeline.processors.Processor Processor
 * @see com.winterhavenmc.library.messagebuilder.models.language.FinalMessageRecord FinalMessageRecord
 */
package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.processors;
