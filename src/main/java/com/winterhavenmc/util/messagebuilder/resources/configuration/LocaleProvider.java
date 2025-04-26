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

package com.winterhavenmc.util.messagebuilder.resources.configuration;

import org.bukkit.plugin.Plugin;

import java.util.Locale;
import java.util.function.Supplier;


public class LocaleProvider
{
	private final Supplier<Locale> localeSupplier;

	public LocaleProvider(Supplier<Locale> localeSupplier)
	{
		this.localeSupplier = localeSupplier;
	}


	public static LocaleProvider create(final Plugin plugin)
	{
		return new LocaleProvider(() ->
				LanguageTag.of(plugin.getConfig().getString(LocaleField.LOCALE.toString()))
						.orElse(LanguageTag.of(plugin.getConfig().getString(LocaleField.LANGUAGE.toString()))
								.orElse(LanguageTag.getDefault())).getLocale());
	}


	public Locale getLocale()
	{
		return localeSupplier.get();
	}

}
