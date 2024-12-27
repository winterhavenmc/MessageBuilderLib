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

import com.winterhavenmc.util.messagebuilder.macro.CompositeKey;

import java.util.HashMap;
import java.util.Map;

public class ResultMap {

	private final Map<CompositeKey, String> internalResultMap = new HashMap<>();


	void put(final CompositeKey compositeKey, final String value) {
		internalResultMap.put(compositeKey, value);
	}

	String get(final CompositeKey compositeKey) {
		return internalResultMap.get(compositeKey);
	}

	public void putAll(final ResultMap resultMap) {
		for (Map.Entry<CompositeKey, String> entry : resultMap.entrySet()) {
			internalResultMap.put(entry.getKey(), entry.getValue());
		}
	}

	public boolean containsKey(final CompositeKey compositeKey) {
		return internalResultMap.containsKey(compositeKey);
	}

	public Iterable<? extends Map.Entry<CompositeKey, String>> entrySet() {
		return internalResultMap.entrySet();
	}

	public boolean isEmpty() {
		return internalResultMap.isEmpty();
	}

}
