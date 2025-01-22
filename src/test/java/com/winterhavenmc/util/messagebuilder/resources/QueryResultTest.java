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

package com.winterhavenmc.util.messagebuilder.resources;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryResultTest {

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void getObject() {
		// Arrange
		QueryResult queryResult = new QueryResult("object", String.class, QueryResultStatus.SUCCESS);

		// Act
		Object object = queryResult.getObject();

		// Assert
		assertNotNull(object);
		assertInstanceOf(String.class, object);
	}

	@Test
	void getObjectClass() {
		// Arrange
		QueryResult queryResult = new QueryResult("object", String.class, QueryResultStatus.SUCCESS);

		// Act
		Class<?> objectClass = queryResult.getObjectClass();

		// Assert
		assertNotNull(objectClass);
		assertEquals(String.class, objectClass);
	}

	@Test
	void getStatus() {
		// Arrange
		QueryResult queryResult = new QueryResult("object", String.class, QueryResultStatus.SUCCESS);

		// Act
		QueryResultStatus status = queryResult.getStatus();

		// Assert
		assertNotNull(status);
		assertEquals(QueryResultStatus.SUCCESS, status);
	}

}
