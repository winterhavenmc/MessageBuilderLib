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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml;

enum YamlLanguageSetting
{
	CONFIG_LANGUAGE_KEY("language"),
	CONFIG_LOCALE_KEY("locale"),
	DEFAULT_LANGUAGE_TAG("en-US"),
	RESOURCE_AUTO_INSTALL("auto_install.txt"),
	RESOURCE_SUBDIRECTORY("language"),
	RESOURCE_LANGUAGE_EN_US_YML("language/en-US.yml"),
	;

	private final String value;

	YamlLanguageSetting(final String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

}
