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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section;

import com.winterhavenmc.util.messagebuilder.resources.QueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.constants.ConstantSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.items.ItemSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import java.util.EnumMap;
import java.util.Objects;
import java.util.function.Function;

import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.*;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


/**
 * An enumeration of Sections that correspond directly to each top level {@code ConfigurationSection} of the language file.
 */
public enum Section
{
	CONSTANTS(ConstantSectionQueryHandler::new, "Constant", "Constants", "CONST"),
	ITEMS(ItemSectionQueryHandler::new, "Item", "Items", "ITEM"),
	MESSAGES(MessageSectionQueryHandler::new, "Message", "Messages", "MSG"),
	;

	// Enum map serves as a cache for instances of query handlers
	private static final EnumMap<Section, QueryHandler<?>> HANDLER_MAP = new EnumMap<>(Section.class);

	private final Function<YamlConfigurationSupplier, ? extends QueryHandler<? extends Record>> handlerFactory;
	private final String singularName;
	private final String pluralName;
	private final String mnemonic;
//	private final Predicate<Class<? extends Record>> matchesSectionType;


	/**
	 * Constructor for enum constant instances
	 *
	 * @param handlerFactory the Class of {@link QueryHandler} that is immutably bound to this enum constant
	 * @param singularName   the singular name of this section (ex: Item)
	 * @param pluralName     the plural name for this section (ex: Items)
	 * @param mnemonic       the short mnemonic for this section. To be used in key generation, or other programmatic purposes.
	 */
	<R extends Record> Section(
			final Function<YamlConfigurationSupplier, QueryHandler<R>> handlerFactory,
			final String singularName,
			final String pluralName,
			final String mnemonic)
	{
		this.handlerFactory = handlerFactory;
		this.singularName = singularName;
		this.pluralName = pluralName;
		this.mnemonic = mnemonic;
//		this.matchesSectionType = type -> type.equals(this.recordType);
	}


	/**
	 * Retrieve an instance of the section query handler that is bound to this enum constant from the enum map.
	 * If the map has not been populated with an instance of its query handler, a new instance is created using
	 * reflection to call the constructor and pass the {@code ConfigurationSupplier} parameter to the constructor,
	 * which is then placed in the map for future retrievals, and returned to the caller for this use.
	 *
	 * @param configurationSupplier the Configuration supplier for the language resource
	 * @return an instance of the section query handler that is bound to the enum constant
	 * @param <R> the specific type of the section query handler being returned
	 */
	@SuppressWarnings("unchecked")
	public <R extends Record> QueryHandler<R> createHandler(YamlConfigurationSupplier configurationSupplier) {
		validate(configurationSupplier, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, CONFIGURATION_SUPPLIER));

		return (QueryHandler<R>) HANDLER_MAP.computeIfAbsent(this, section -> handlerFactory.apply(configurationSupplier));
	}


	/**
	 * Retrieve the singular, pretty formatted name for this enum constant
	 *
	 * @return the formatted singular name for this enum constant
	 */
	public String getSingularName()
	{
		return singularName;
	}

	/**
	 * Retrieve the plural, pretty formatted name for this enum constant
	 *
	 * @return the formatted plural name for this enum constant
	 */
	public String getPluralName()
	{
		return pluralName;
	}

	/**
	 * Retrieve the mnemonic abbreviation for this enum constant
	 *
	 * @return the mnemonic for this enum constant
	 */
	public String getMnemonic()
	{
		return mnemonic;
	}

}
