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

package com.winterhavenmc.library.messagebuilder.model.language;

import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import org.bukkit.configuration.ConfigurationSection;

import java.time.Duration;
import java.util.Optional;


public final class ValidMessageRecord implements MessageRecord
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
		// replace any null strings with blank strings
		this.key = key;
		this.enabled = enabled;
		this.message = message;
		this.repeatDelay = repeatDelay;
		this.title = title;
		this.titleFadeIn = titleFadeIn;
		this.titleStay = titleStay;
		this.titleFadeOut = titleFadeOut;
		this.subtitle = subtitle;
	}


	public static ValidMessageRecord create(final RecordKey key, final ConfigurationSection section)
	{
		return new ValidMessageRecord(key,
				!section.contains(Field.ENABLED.toKey()) || section.getBoolean(Field.ENABLED.toKey()), // default true if missing
				section.contains(Field.MESSAGE_TEXT.toKey()) ? section.getString(Field.MESSAGE_TEXT.toKey()) : "",
				Duration.ofSeconds(section.getLong(Field.REPEAT_DELAY.toKey())),
				section.contains(Field.TITLE_TEXT.toKey()) ? section.getString(Field.TITLE_TEXT.toKey()) : "",
				section.contains(Field.TITLE_FADE_IN.toKey()) ? section.getInt(Field.TITLE_FADE_IN.toKey()) : 10,
				section.contains(Field.TITLE_STAY.toKey()) ? section.getInt(Field.TITLE_STAY.toKey()) : 70,
				section.contains(Field.TITLE_FADE_OUT.toKey()) ? section.getInt(Field.TITLE_FADE_OUT.toKey()) : 20,
				section.contains(Field.SUBTITLE_TEXT.toKey()) ? section.getString(Field.SUBTITLE_TEXT.toKey()) : "");
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
				Optional.ofNullable(finalMessageString),
				Optional.ofNullable(finalTitleString),
				Optional.ofNullable(finalSubTitleString));
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
