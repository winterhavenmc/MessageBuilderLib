/*
 * Copyright (c) 2025 Tim Savage.
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

package com.winterhavenmc.library.messagebuilder.pipeline.processor;

import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.model.language.ValidMessageRecord;


/**
 * A functional interface representing the final stage in the message pipeline: transforming a
 * {@link com.winterhavenmc.library.messagebuilder.model.language.ValidMessageRecord ValidMessageRecord}
 * into a {@link com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord FinalMessageRecord}
 * by resolving and replacing all macro placeholders.
 *
 * <p>This step is typically performed after a message record has been retrieved and
 * all necessary context objects have been registered to a {@link com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroObjectMap MacroObjectMap}.
 *
 * <p>Macro resolution is delegated internally to a {@link com.winterhavenmc.library.messagebuilder.pipeline.resolvers.Resolver Resolver},
 * and placeholder substitution is handled by a {@link com.winterhavenmc.library.messagebuilder.pipeline.replacer.Replacer Replacer}.
 *
 * @see com.winterhavenmc.library.messagebuilder.model.language.ValidMessageRecord ValidMessageRecord
 * @see com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord FinalMessageRecord
 * @see com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroObjectMap MacroObjectMap
 * @see com.winterhavenmc.library.messagebuilder.pipeline.processor.MessageProcessor MessageProcessor
 */
@FunctionalInterface
public interface Processor
{
	/**
	 * Processes a valid message record by replacing all macros using the provided macro object map.
	 *
	 * <p>This includes macro substitution in the message body, title, and subtitle fields.
	 * The result is a {@link com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord}
	 * containing the final rendered strings ready for output.
	 *
	 * @param messageRecord the message record to process
	 * @param macroObjectMap the context data used to resolve placeholder values
	 * @return a new {@code FinalMessageRecord} with all macros resolved
	 */
	FinalMessageRecord process(ValidMessageRecord messageRecord, MacroObjectMap macroObjectMap);
}
