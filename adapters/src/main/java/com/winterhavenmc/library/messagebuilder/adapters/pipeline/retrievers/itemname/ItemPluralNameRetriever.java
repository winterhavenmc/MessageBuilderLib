package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers.itemname;

import com.winterhavenmc.library.messagebuilder.adapters.resources.language.YamlItemRepository;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ItemRepository;
import com.winterhavenmc.library.messagebuilder.models.Delimiter;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidItemKey;
import com.winterhavenmc.library.messagebuilder.models.language.item.ValidItemRecord;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.regex.Pattern;

public class ItemPluralNameRetriever implements NameRetriever
{
	private final ItemRepository itemRepository;
	private final MiniMessage miniMessage;


	public ItemPluralNameRetriever(final ItemRepository itemRepository, final MiniMessage miniMessage)
	{
		this.itemRepository = itemRepository;
		this.miniMessage = miniMessage;
	}


	public Optional<String> retrieve(final ItemStack itemStack)
	{
		if (!itemRepository.isItem(itemStack)) return Optional.empty();

		return getValidKey(itemStack)
				.flatMap(this::getValidRecord)
				.map(record -> deserializePluralName(record, itemStack, miniMessage));
	}


	Optional<ValidItemKey> getValidKey(final ItemStack itemStack)
	{
		return Optional.ofNullable(itemRepository.key(itemStack))
				.filter(ValidItemKey.class::isInstance)
				.map(ValidItemKey.class::cast);
	}


	Optional<ValidItemRecord> getValidRecord(final ValidItemKey key)
	{
		return itemRepository.getRecordOpt(key)
				.filter(ValidItemRecord.class::isInstance)
				.map(ValidItemRecord.class::cast);
	}


	String deserializePluralName(final ValidItemRecord record, final ItemStack itemStack, final MiniMessage miniMessage)
	{
		String pluralString = record.pluralName().replaceAll(
				Pattern.quote(Delimiter.OPEN + "QUANTITY" + Delimiter.CLOSE),
				String.valueOf(itemStack.getAmount())
		);

		Component component = miniMessage.deserialize(
				pluralString,
				Formatter.choice("choice", itemStack.getAmount())
		);

		return YamlItemRepository.LEGACY_SERIALIZER.serializeOr(component, "");
	}
}
