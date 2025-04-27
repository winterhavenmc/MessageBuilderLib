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

import com.winterhavenmc.util.messagebuilder.model.language.Section;
import com.winterhavenmc.util.messagebuilder.model.language.constant.ConstantRecord;
import com.winterhavenmc.util.messagebuilder.model.language.item.ItemRecord;
import com.winterhavenmc.util.messagebuilder.model.language.message.MessageRecord;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageSectionResourceManager;
import com.winterhavenmc.util.messagebuilder.resources.language.SectionProvider;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class QueryHandlerFactoryTest
{
	@Mock
	LanguageSectionResourceManager languageResourceManagerMock;
	@Mock SectionProvider sectionProviderMock;


	@Test
	void getMessageQueryHandler()
	{
		// Arrange
		when(languageResourceManagerMock.getSectionProvider(Section.MESSAGES)).thenReturn(sectionProviderMock);
		QueryHandlerFactory queryHandlerFactory = new QueryHandlerFactory(languageResourceManagerMock);

		// Act
		QueryHandler<MessageRecord> queryHandler = queryHandlerFactory.getQueryHandler(Section.MESSAGES);

		// Assert
		assertInstanceOf(MessageQueryHandler.class, queryHandler);

		// Verify
		verify(languageResourceManagerMock, atLeastOnce()).getSectionProvider(Section.MESSAGES);
	}


	@Test
	void getItemQueryHandler()
	{
		// Arrange
		when(languageResourceManagerMock.getSectionProvider(Section.ITEMS)).thenReturn(sectionProviderMock);
		QueryHandlerFactory queryHandlerFactory = new QueryHandlerFactory(languageResourceManagerMock);

		// Act
		QueryHandler<ItemRecord> queryHandler = queryHandlerFactory.getQueryHandler(Section.ITEMS);

		// Assert
		assertInstanceOf(ItemQueryHandler.class, queryHandler);

		// Verify
		verify(languageResourceManagerMock, atLeastOnce()).getSectionProvider(Section.ITEMS);
	}


	@Test
	void getConstantQueryHandler()
	{
		// Arrange
		when(languageResourceManagerMock.getSectionProvider(Section.CONSTANTS)).thenReturn(sectionProviderMock);
		QueryHandlerFactory queryHandlerFactory = new QueryHandlerFactory(languageResourceManagerMock);

		// Act
		QueryHandler<ConstantRecord> queryHandler = queryHandlerFactory.getQueryHandler(Section.CONSTANTS);

		// Assert
		assertInstanceOf(ConstantQueryHandler.class, queryHandler);

		// Verify
		verify(languageResourceManagerMock, atLeastOnce()).getSectionProvider(Section.CONSTANTS);
	}


	@Test
	void getQueryHandler_null()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> new QueryHandlerFactory(null));

		// Assert
		assertEquals("The parameter 'languageResourceManager' cannot be null.", exception.getMessage());
	}

}
