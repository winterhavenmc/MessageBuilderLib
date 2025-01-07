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

package com.winterhavenmc.util.messagebuilder.macro.processor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class ResultMap {

	private final Map<String, String> internalResultMap;

	// Static nested class to hold the singleton empty ResultMap
	private static class EmptyResultMapHolder {
		private static final ResultMap EMPTY_INSTANCE =
				new ResultMap(new HashMap<>(Collections.emptyMap()));
	}


	// Public constructor for regular instantiation
	public ResultMap() {
		this.internalResultMap = new HashMap<>();
	}

	// Private constructor for the empty singleton instance
	private ResultMap(HashMap<String, String> map) {
		this.internalResultMap = map;
	}


	// Static method to return a shared, empty ResultMap
	public static ResultMap empty() {
		return EmptyResultMapHolder.EMPTY_INSTANCE;
	}

	void put(final String contextKey, final String value) {
		internalResultMap.put(contextKey, value);
	}


	String get(final String key) {
		return internalResultMap.get(key);
	}

	public void putAll(final ResultMap resultMap) {
		for (Map.Entry<String, String> entry : resultMap.entrySet()) {
			internalResultMap.put(entry.getKey(), entry.getValue());
		}
	}

	public boolean containsKey(final String compositeKey) {
		return internalResultMap.containsKey(compositeKey);
	}

	public Iterable<? extends Map.Entry<String, String>> entrySet() {
		return internalResultMap.entrySet();
	}

	public boolean isEmpty() {
		return internalResultMap.isEmpty();
	}

}
