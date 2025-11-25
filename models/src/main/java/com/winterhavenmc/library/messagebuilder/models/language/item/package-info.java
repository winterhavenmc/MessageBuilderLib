/**
 * Represents an item record as an algebraic data type, implemented using a sealed interface
 * with permitted types of {@link com.winterhavenmc.library.messagebuilder.models.language.item.ValidItemRecord}
 * and {@link com.winterhavenmc.library.messagebuilder.models.language.item.InvalidItemRecord}.
 * <p>
 * ItemRecord instances are validated at creation by the static factory method(s) provided in the interface,
 * and return a valid or invalid type after validation.
 * <p>
 * <img src="doc-files/ItemRecord_structure.svg" alt="ItemRecord Structure">
 */
package com.winterhavenmc.library.messagebuilder.models.language.item;
