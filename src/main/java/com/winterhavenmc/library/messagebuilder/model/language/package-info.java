/**
 * Provides interfaces and data models that represent entries from the plugin's
 * language YAML file. These records abstract the three top-level sections of the
 * file: {@code MESSAGES}, {@code ITEMS}, and {@code CONSTANTS}.
 *
 * <h2>Overview</h2>
 * <p>
 * Each section is represented by a sealed interface:
 * <ul>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.model.language.MessageRecord}</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.model.language.ItemRecord}</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.model.language.ConstantRecord}</li>
 * </ul>
 *
 * These interfaces have two core implementations:
 * <ul>
 *   <li><b>Valid</b> record types (e.g., {@code ValidMessageRecord}) that contain fully parsed, structured data</li>
 *   <li><b>Invalid</b> record types (e.g., {@code InvalidItemRecord}) that represent missing or invalid entries</li>
 * </ul>
 * <p>
 * The use of sealed interfaces ensures that all variants are accounted for and
 * safely handled throughout the MessageBuilder pipeline.
 *
 * <h2>Design Notes</h2>
 * <ul>
 *   <li>All types follow the <em>data-driven design pattern</em>: validation occurs at construction time.</li>
 *   <li>Factory methods such as {@code from(...)} are used to ensure all instances are safe and complete.</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.keys.RecordKey LegacyRecordKey} is used as a strongly typed identifier
 *       across all sections.</li>
 * </ul>
 *
 * <h2>Typical Usage</h2>
 * {@snippet lang="java":
 * 	ItemRecord itemRecord = queryHandler.getItemRecord(MY_ITEM_KEY);
 * 	if (itemRecord instanceof ValidItemRecord valid) {
 * 		String name = valid.nameSingular();
 * 	}
 *  }
 *
 * @see com.winterhavenmc.library.messagebuilder.query.QueryHandler QueryHandler
 * @see com.winterhavenmc.library.messagebuilder.model.language.MessageRecord MessageRecord
 * @see com.winterhavenmc.library.messagebuilder.model.language.ItemRecord ItemRecord
 * @see com.winterhavenmc.library.messagebuilder.model.language.ConstantRecord ConstantRecord
 */
package com.winterhavenmc.library.messagebuilder.model.language;
