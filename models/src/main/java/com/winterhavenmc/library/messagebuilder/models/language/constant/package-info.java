/**
 * Represents a constant record as an algebraic data type, implemented using a sealed interface
 * with permitted types of {@link com.winterhavenmc.library.messagebuilder.models.language.constant.ValidConstantRecord}
 * and {@link com.winterhavenmc.library.messagebuilder.models.language.constant.InvalidConstantRecord}.
 * <p>
 * MessageRecord instances are validated at creation by the static factory method(s) provided in the interface,
 * and return a valid or invalid type after validation.
 * <p>
 * <img src="doc-files/ConstantRecord_structure.svg" alt="ConstantRecord Structure">
 */
package com.winterhavenmc.library.messagebuilder.models.language.constant;
