/*
 * Copyright (c) 2024-2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section;

import com.winterhavenmc.util.messagebuilder.resources.RecordKey;

import java.time.Duration;
import java.util.Optional;


/**
 * A data object record for message information contained in the language file. This class also contains
 * an enum of fields with their corresponding path key, and a static method for retrieving a record.
 *
 * @param key the key for the message
 * @param enabled the enabled setting for the message
 * @param message the raw message string, with placeholders
 * @param repeatDelay the repeat delay setting for the message
 * @param title the raw title string, with placeholders
 * @param titleFadeIn the title fade in setting for the message
 * @param titleStay the title stay setting for the message
 * @param titleFadeOut the title fade out setting for the message
 * @param subtitle the subtitle for the message
 */
public record ValidMessageRecord(
		RecordKey key,
		boolean enabled,
		String message,
		Duration repeatDelay,
		String title,
		int titleFadeIn,
		int titleStay,
		int titleFadeOut,
		String subtitle,
		String finalMessageString,
		String finalTitleString,
		String finalSubTitleString) implements SectionRecord
{
	/**
	 * Create a duplicate record with the final message string fields populated
	 *
	 * @param newFinalMessageString final message string
	 * @param newFinalTitleString final title string
	 * @param newFinalSubTitleString final subtitle string
	 * @return a new {@code ValidMessageRecord} with the final message string fields populated
	 */
	public Optional<ValidMessageRecord> withFinalStrings(final String newFinalMessageString,
														 final String newFinalTitleString,
														 final String newFinalSubTitleString)
	{
		return Optional.of(new ValidMessageRecord(
				this.key,
				this.enabled,
				this.message,
				this.repeatDelay,
				this.title,
				this.titleFadeIn,
				this.titleStay,
				this.titleFadeOut,
				this.subtitle,
				newFinalMessageString,
				newFinalTitleString,
				newFinalSubTitleString)
		);
	}

}
