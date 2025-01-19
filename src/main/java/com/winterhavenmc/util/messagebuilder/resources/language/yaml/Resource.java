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

import java.io.File;

import static com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageResourceInstaller.SUBDIRECTORY;

public class Resource {

	private final String languageTag;


	public Resource(final String languageTag) {
		this.languageTag = languageTag;
	}

	public String getLanguageTag() {
		return languageTag;
	}

	public String getName() {
		return String.join("/", SUBDIRECTORY, languageTag).concat(".yml");
	}

	public String getFileName() {
		return String.join(File.separator, SUBDIRECTORY, languageTag).concat(".yml");
	}
}
