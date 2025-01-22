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

package com.winterhavenmc.util.messagebuilder.context;


/**
 * A source key shall have a Source constant and a keyPath combined to form a composite key.
 */
public class SourceKey implements ContextKey {

	private final static String DELIMITER = "|";
	private final Source source;
	private final String keyPath;

	public SourceKey(final Source source, final String keyPath) {
		this.source = source;
		this.keyPath = keyPath;
	}

	@Override
	public String toString() {
		return getKey();
	}

	public Source getSource() {
		return source;
	}

	public String getKeyPath() {
		return keyPath;
	}


	/**
	 * @return String representation of key
	 */
	@Override
	public String getKey() {
		return String.join(DELIMITER, source.name(), keyPath);
	}

}
