/*
 * Copyright (c) 2022 Tim Savage.
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class MacroObjectMap {

	private final Map<String, Object> macroObjectMap = new HashMap<>();

	void put(String macro, Object object) {
		this.macroObjectMap.put(macro, object);
	}

	Object get(final String key) {
		return this.macroObjectMap.get(key);
	}

	boolean containsKey(final String key) {
		return this.macroObjectMap.containsKey(key);
	}

	Set<Map.Entry<String, Object>> entrySet() {
		return this.macroObjectMap.entrySet();
	}

}
