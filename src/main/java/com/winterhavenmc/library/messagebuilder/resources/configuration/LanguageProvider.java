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

package com.winterhavenmc.library.messagebuilder.resources.configuration;

import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;


public class LanguageProvider implements ConfigProvider<LanguageSetting>
{
	private final Supplier<LanguageSetting> languageSettingSupplier;

	private static final String FALLBACK_NAME = "en-US";


	private enum LanguageField
	{
		LANGUAGE("language"),
		LOCALE("locale");

		private final String key;
		LanguageField(String key) { this.key = key; }
		@Override public String toString() { return key; }
	}


	private LanguageProvider(Supplier<LanguageSetting> supplier)
	{
		this.languageSettingSupplier = supplier;
	}


	public static LanguageProvider create(final Plugin plugin)
	{
		return new LanguageProvider(() -> {
			var config = plugin.getConfig();

			return Stream.of(LanguageField.values())
					.map(LanguageField::toString)
					.map(config::getString)
					.filter(Objects::nonNull)
					.map(LanguageSetting::new)
					.findFirst()
					.orElse(new LanguageSetting(FALLBACK_NAME));
		});
	}


	@Override
	public LanguageSetting get()
	{
		return languageSettingSupplier.get();
	}


	public String getName()
	{
		return languageSettingSupplier.get().name();
	}

}
