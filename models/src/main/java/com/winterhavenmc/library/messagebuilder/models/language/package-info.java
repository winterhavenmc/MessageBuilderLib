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

/**
 * Provides interfaces and data models that represent entries from the plugin's
 * language YAML file. These records abstract the three top-level sections of the
 * file: {@code MESSAGES}, {@code ITEMS}, and {@code CONSTANTS}.
 *
 * <h2>Overview</h2>
 * <p>
 * Each section is represented by a sealed interface:
 * <ul>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.models.language.message.MessageRecord}</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.models.language.item.ItemRecord}</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.models.language.constant.ConstantRecord}</li>
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
 *   <li>{@code RecordKey} is used as a strongly typed identifier across all sections.</li>
 * </ul>
 *
 * <h2>Typical Usage</h2>
 * {@snippet lang = "java":
 * 	import com.winterhavenmc.library.messagebuilder.models.language.item.ItemRecord;import com.winterhavenmc.library.messagebuilder.models.language.item.ValidItemRecord;ItemRecord itemRecord = queryHandler.getItemRecord(MY_ITEM_KEY);
 * 	if (itemRecord instanceof ValidItemRecord valid) {
 * 		Section name = valid.name();
 * 	}
 *}
 *
 * @see com.winterhavenmc.library.messagebuilder.models.language.message.MessageRecord MessageRecord
 * @see com.winterhavenmc.library.messagebuilder.models.language.item.ItemRecord ItemRecord
 * @see com.winterhavenmc.library.messagebuilder.models.language.constant.ConstantRecord ConstantRecord
 */
package com.winterhavenmc.library.messagebuilder.models.language;
