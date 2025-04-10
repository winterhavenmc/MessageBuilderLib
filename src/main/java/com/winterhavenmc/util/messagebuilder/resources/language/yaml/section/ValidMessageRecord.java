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


public final class ValidMessageRecord implements MessageRecord, SectionRecord
{
	private final RecordKey key;
	private final boolean enabled;
	private final String message;
	private final Duration repeatDelay;
	private final String title;
	private final int titleFadeIn;
	private final int titleStay;
	private final int titleFadeOut;
	private final String subtitle;


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
	private ValidMessageRecord(RecordKey key,
					   boolean enabled,
					   String message,
					   Duration repeatDelay,
					   String title,
					   int titleFadeIn,
					   int titleStay,
					   int titleFadeOut,
					   String subtitle)
	{
		this.key = key;
		this.enabled = enabled;
		this.message = message != null ? message : "";
		this.repeatDelay = repeatDelay;
		this.title = title != null ? title : "";
		this.titleFadeIn = titleFadeIn;
		this.titleStay = titleStay;
		this.titleFadeOut = titleFadeOut;
		this.subtitle = subtitle != null ? subtitle : "";
	}


	public static ValidMessageRecord of(final RecordKey key,
										final boolean enabled,
										final String message,
										final Duration repeatDelay,
										final String title,
										final int titleFadeIn,
										final int titleStay,
										final int titleFadeOut,
										final String subtitle)
	{
		return new ValidMessageRecord(key, enabled, message, repeatDelay,
				title, titleFadeIn, titleStay, titleFadeOut, subtitle);
	}


	/**
	 * Create a duplicate record with the final message string fields populated
	 *
	 * @param finalMessageString final message string
	 * @param finalTitleString final title string
	 * @param finalSubTitleString final subtitle string
	 * @return a new {@code ValidMessageRecord} with the final message string fields populated
	 */
	public FinalMessageRecord withFinalStrings(final String finalMessageString,
											   final String finalTitleString,
											   final String finalSubTitleString)
	{
		return new FinalMessageRecord(
				this.key,
				this.enabled,
				this.message,
				this.repeatDelay,
				this.title,
				this.titleFadeIn,
				this.titleStay,
				this.titleFadeOut,
				this.subtitle,
				finalMessageString,
				finalTitleString,
				finalSubTitleString);
	}


	@Override
	public RecordKey key()
	{
		return key;
	}

	public boolean enabled()
	{
		return enabled;
	}

	public String message()
	{
		return message;
	}

	public Duration repeatDelay()
	{
		return repeatDelay;
	}

	public String title()
	{
		return title;
	}

	public int titleFadeIn()
	{
		return titleFadeIn;
	}

	public int titleStay()
	{
		return titleStay;
	}

	public int titleFadeOut()
	{
		return titleFadeOut;
	}

	public String subtitle()
	{
		return subtitle;
	}

}
