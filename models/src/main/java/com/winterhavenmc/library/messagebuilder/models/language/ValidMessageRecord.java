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

package com.winterhavenmc.library.messagebuilder.models.language;

import com.winterhavenmc.library.messagebuilder.models.keys.ValidMessageKey;
import com.winterhavenmc.library.messagebuilder.models.time.Tick;
import org.bukkit.configuration.ConfigurationSection;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Optional;



/**
 * A validated, immutable {@link MessageRecord} representing a single entry from the
 * {@code MESSAGES} section of a language YAML file.
 * <p>
 * This record contains the full structure of a templated message, including:
 * <ul>
 *   <li>A unique {@link ValidMessageKey}</li>
 *   <li>A plain-text message body (with placeholders)</li>
 *   <li>Optional title and subtitle text</li>
 *   <li>Title animation parameters (fade-in, stay, fade-out durations)</li>
 *   <li>An optional repeat delay for scheduled messages</li>
 * </ul>
 * <p>
 * This class is created via the {@link #create(ValidMessageKey, ConfigurationSection)} factory method,
 * which performs all necessary validation and applies default values where appropriate. Once constructed,
 * instances are considered safe and complete and can be used without additional checks.
 *
 * @see MessageRecord
 * @see ValidMessageKey
 */
public final class ValidMessageRecord implements MessageRecord
{
	private final static TemporalUnit TICKS = new Tick();

	private final ValidMessageKey key;
	private final boolean enabled;
	private final String message;
	private final Duration repeatDelay;
	private final String title;
	private final Duration titleFadeIn;
	private final Duration titleStay;
	private final Duration titleFadeOut;
	private final String subtitle;


	/**
	 * Constructs a {@code ValidMessageRecord} from fully parsed and validated data fields.
	 * <p>
	 * Use {@link #create(ValidMessageKey, ConfigurationSection)} to construct new instances.
	 *
	 * @param key the string for the message
	 * @param enabled the enabled setting for the message
	 * @param message the raw message string, with placeholders
	 * @param repeatDelay the repeat delay setting for the message
	 * @param title the raw title string, with placeholders
	 * @param titleFadeIn the title fade in setting for the message
	 * @param titleStay the title stay setting for the message
	 * @param titleFadeOut the title fade out setting for the message
	 * @param subtitle the subtitle for the message
	 */
	private ValidMessageRecord(ValidMessageKey key,
							   boolean enabled,
							   String message,
							   Duration repeatDelay,
							   String title,
							   Duration titleFadeIn,
							   Duration titleStay,
							   Duration titleFadeOut,
							   String subtitle)
	{
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


	/**
	 * Creates a {@code ValidMessageRecord} from a YAML configuration section.
	 * <p>
	 * All expected fields are validated or defaulted according to their definitions
	 * in {@link MessageRecord.Field}. If a field is missing, a sensible default is applied.
	 *
	 * @param key the record string representing this message
	 * @param section the configuration section containing the message definition
	 * @return a new, fully validated {@code ValidMessageRecord}
	 */
	public static ValidMessageRecord create(final ValidMessageKey key, final ConfigurationSection section)
	{
		// enabled defaults to true if not present
		boolean enabled = !section.contains(Field.ENABLED.toString()) || section.getBoolean(Field.ENABLED.toString());

		// defaults to empty string if not present
		String messageText = section.contains(Field.MESSAGE_TEXT.toString())
				? section.getString(Field.MESSAGE_TEXT.toString())
				: "";

		// defaults to zero, no ternary operator necessary
		Duration repeatDelay = Duration.ofSeconds(section.getLong(Field.REPEAT_DELAY.toString()));

		// defaults to empty string if not present
		String titleText = section.contains(Field.TITLE_TEXT.toString())
				? section.getString(Field.TITLE_TEXT.toString())
				: "";

		// default to Bukkit standard 10 ticks if not present
		Duration titleFadeIn = section.contains(Field.TITLE_FADE_IN.toString())
				? Duration.of(section.getLong(Field.TITLE_FADE_IN.toString()), TICKS)
				: Duration.of(10, TICKS);

		// default to Bukkit standard 70 ticks if not present
		Duration titleStay = section.contains(Field.TITLE_STAY.toString())
				? Duration.of(section.getLong(Field.TITLE_STAY.toString()), TICKS)
				: Duration.of(70, TICKS);

		// default to Bukkit standard 20 ticks if not present
		Duration titleFadeOut = section.contains(Field.TITLE_FADE_OUT.toString())
				? Duration.of(section.getLong(Field.TITLE_FADE_OUT.toString()), TICKS)
				: Duration.of(20, TICKS);

		// defaults to empty string if not present
		String subtitleText = section.contains(Field.SUBTITLE_TEXT.toString())
				? section.getString(Field.SUBTITLE_TEXT.toString())
				: "";


		return new ValidMessageRecord(key, enabled, messageText, repeatDelay,
				titleText, titleFadeIn, titleStay, titleFadeOut, subtitleText);
	}


	/**
	 * Creates a {@link FinalMessageRecord} using the current record data,
	 * combined with finalized message, title, and subtitle strings after macro resolution.
	 * <p>
	 * This is typically used as the last step in the message pipeline before dispatch.
	 *
	 * @param finalMessageString the resolved chat message string
	 * @param finalTitleString the resolved title string
	 * @param finalSubTitleString the resolved subtitle string
	 * @return a {@link FinalMessageRecord} that includes the resolved strings
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
	public ValidMessageKey key()
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


	public Duration titleFadeIn()
	{
		return titleFadeIn;
	}


	public Duration titleStay()
	{
		return titleStay;
	}


	public Duration titleFadeOut()
	{
		return titleFadeOut;
	}


	public String subtitle()
	{
		return subtitle;
	}

}
