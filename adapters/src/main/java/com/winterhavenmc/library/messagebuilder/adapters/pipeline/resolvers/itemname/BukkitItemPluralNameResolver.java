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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.itemname;

import com.winterhavenmc.library.messagebuilder.adapters.resources.language.YamlItemRepository;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname.ItemPluralNameResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ItemRepository;
import com.winterhavenmc.library.messagebuilder.models.Delimiter;

import com.winterhavenmc.library.messagebuilder.models.keys.ValidItemKey;
import com.winterhavenmc.library.messagebuilder.models.language.ValidItemRecord;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;
import java.util.regex.Pattern;


public class BukkitItemPluralNameResolver implements ItemPluralNameResolver
{
	private final ItemRepository itemRepository;


	public BukkitItemPluralNameResolver(final ItemRepository itemRepository)
	{
		this.itemRepository = itemRepository;
	}


	public String resolve(final ItemStack itemStack)
	{
		MiniMessage miniMessage = MiniMessage.miniMessage();

		return Optional.ofNullable(itemStack)
				.filter(ItemStack::hasItemMeta)
				.flatMap(item ->
						resolveCustomItem(item, miniMessage)
								.or(() -> resolveDisplayName(item))
								.or(() -> resolveItemName(item))
				)
				.orElse("");
	}


	private Optional<String> resolveCustomItem(ItemStack stack, MiniMessage miniMessage) {
		if (!itemRepository.isItem(stack)) return Optional.empty();

		return getValidKey(stack)
				.flatMap(this::getValidRecord)
				.map(record -> deserializePluralName(record, stack, miniMessage));
	}


	private Optional<ValidItemKey> getValidKey(ItemStack itemStack)
	{
		return Optional.ofNullable(itemRepository.key(itemStack))
				.filter(ValidItemKey.class::isInstance)
				.map(ValidItemKey.class::cast);
	}


	private Optional<ValidItemRecord> getValidRecord(ValidItemKey key)
	{
		return itemRepository.record(key)
				.filter(ValidItemRecord.class::isInstance)
				.map(ValidItemRecord.class::cast);
	}


	private String deserializePluralName(ValidItemRecord record, ItemStack stack, MiniMessage miniMessage)
	{
		String pluralString = record.pluralName().replaceAll(
				Pattern.quote(Delimiter.OPEN + "QUANTITY" + Delimiter.CLOSE),
				String.valueOf(stack.getAmount())
		);

		Component component = miniMessage.deserialize(
				pluralString,
				Formatter.choice("choice", stack.getAmount())
		);

		return YamlItemRepository.LEGACY_SERIALIZER.serializeOr(component, "");
	}


	private Optional<String> resolveDisplayName(ItemStack itemStack)
	{
		return Optional.ofNullable(itemStack.getItemMeta())
				.filter(ItemMeta::hasDisplayName)
				.map(ItemMeta::getDisplayName)
				.map(ChatColor::stripColor);
	}


	private Optional<String> resolveItemName(ItemStack itemStack)
	{
		return Optional.ofNullable(itemStack.getItemMeta())
				.filter(ItemMeta::hasItemName)
				.map(ItemMeta::getItemName)
				.map(ChatColor::stripColor);
	}

}
