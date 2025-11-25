/**
 * Represents a message record as an algebraic data type, implemented using a sealed interface
 * with permitted types of {@link com.winterhavenmc.library.messagebuilder.models.language.message.ValidMessageRecord},
 * {@link com.winterhavenmc.library.messagebuilder.models.language.message.FinalMessageRecord}
 * and {@link com.winterhavenmc.library.messagebuilder.models.language.message.InvalidMessageRecord}.
 * <p>
 * MessageRecord instances are validated at creation by the static factory method(s) provided in the interface,
 * and return a valid or invalid type after validation.
 * <p>
 * <img src="doc-files/MessageRecord_structure.svg" alt="MessageRecord Structure">
 */
package com.winterhavenmc.library.messagebuilder.models.language.message;
