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

package com.winterhavenmc.library.messagebuilder.model.language;

import com.winterhavenmc.library.messagebuilder.keys.RecordKey;

import java.time.Duration;
import java.util.Optional;


/**
 * A {@link MessageRecord} representing a fully constructed, macro-resolved message
 * ready for delivery to a recipient.
 * <p>
 * This record is typically derived from a {@link ValidMessageRecord} using the
 * {@link ValidMessageRecord#withFinalStrings(String, String, String)} method.
 * It retains all original message metadata while attaching resolved versions of
 * the chat message, title, and subtitle strings.
 *
 * <h2>Resolved Fields</h2>
 * The following fields contain the final rendered output after macro resolution:
 * <ul>
 *   <li>{@code finalMessageString} – The resolved chat message</li>
 *   <li>{@code finalTitleString} – The resolved title (if any)</li>
 *   <li>{@code finalSubtitleString} – The resolved subtitle (if any)</li>
 * </ul>
 * These are provided as {@link Optional} values to distinguish between
 * unresolved and empty content.
 *
 * @param key the original message key
 * @param enabled whether this message is active
 * @param message the original raw message string (pre-resolution)
 * @param repeatDelay the repeat delay setting for this message
 * @param title the raw title string
 * @param titleFadeIn title fade-in time in ticks
 * @param titleStay title stay time in ticks
 * @param titleFadeOut title fade-out time in ticks
 * @param subtitle the raw subtitle string
 * @param finalMessageString the resolved chat message string, if available
 * @param finalTitleString the resolved title string, if available
 * @param finalSubtitleString the resolved subtitle string, if available
 *
 * @see ValidMessageRecord#withFinalStrings(String, String, String)
 * @see MessageRecord
 * @see com.winterhavenmc.library.messagebuilder.pipeline.MessagePipeline MessagePipeline
 */
public record FinalMessageRecord(
		RecordKey key,
		boolean enabled,
		String message,
		Duration repeatDelay,
		String title,
		int titleFadeIn,
		int titleStay,
		int titleFadeOut,
		String subtitle,
		Optional<String> finalMessageString,
		Optional<String> finalTitleString,
		Optional<String> finalSubtitleString) implements MessageRecord { }
