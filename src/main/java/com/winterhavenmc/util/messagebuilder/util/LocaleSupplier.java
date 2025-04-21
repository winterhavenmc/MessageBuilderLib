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

package com.winterhavenmc.util.messagebuilder.util;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.LanguageTag;
import org.bukkit.plugin.Plugin;

import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;


/**
 * A supplier of {@link Locale} that converts a language tag string into a {@link Locale} instance.
 * If the supplied language tag is null, blank, or invalid, it falls back to {@link Locale#getDefault()}.
 */
public class LocaleSupplier implements Supplier<Locale>
{
	private final Supplier<LanguageTag> languageTagSupplier;


	/**
	 * Constructs a LocaleSupplier from a supplier of IETF language tags (e.g., "en-US", "de", etc.).
	 *
	 * @param languageTagSupplier A supplier that returns the current language tag string
	 */
	private LocaleSupplier(Supplier<LanguageTag> languageTagSupplier)
	{
		this.languageTagSupplier = Objects.requireNonNull(languageTagSupplier, "languageTagSupplier");
	}


	public static LocaleSupplier getLocaleSupplier(final Plugin plugin)
	{
		LanguageTag languageTag = LanguageTag.of(plugin.getConfig().getString(LocaleField.LOCALE.toString()))
				.orElse(LanguageTag.of(plugin.getConfig().getString(LocaleField.LANGUAGE.toString()))
						.orElse(LanguageTag.getDefault()));

		return new LocaleSupplier(() -> languageTag);
	}


	@Override
	public Locale get()
	{
		Locale locale = languageTagSupplier.get().getLocale();

		if (locale == null)
		{
			return Locale.getDefault();
		}

		try
		{
			if (locale.getLanguage().isEmpty())
			{
				return Locale.getDefault(); // invalid tag
			}
			return locale;
		} catch (Exception e)
		{
			return Locale.getDefault(); // fallback on error
		}
	}

}
