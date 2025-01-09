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

package com.winterhavenmc.util.messagebuilder;

import com.winterhavenmc.util.messagebuilder.namespace.Namespace;
import com.winterhavenmc.util.messagebuilder.query.domain.DomainQueryHandler;

import java.util.List;
import java.util.Optional;

public interface Toolkit {

	void setDelimiters(Character character);

	void setDelimiters(Character leftCharacter, Character rightCharacter);

	Optional<String> getSpawnDisplayName();

	Optional<String> getHomeDisplayName();

	Optional<String> getString(String key);

	List<String> getStringList(String key);

	DomainQueryHandler<?> getQueryHandler(Namespace.Domain domain);

}
