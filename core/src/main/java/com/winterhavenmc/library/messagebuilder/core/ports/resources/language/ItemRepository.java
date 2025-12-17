package com.winterhavenmc.library.messagebuilder.core.ports.resources.language;

import com.winterhavenmc.library.messagebuilder.models.keys.ItemKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidItemKey;
import com.winterhavenmc.library.messagebuilder.models.language.item.ItemRecord;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;


/**
 * The item repository provides utility methods for creating custom items
 * as defined in the language file. Additional methods are provided for
 * examining an ItemStack as being a custom item.
 * <p>
 * A custom item created using the item repository will have its name and
 * lore applied from the language file settings, and will have a persistent
 * data structure attached, permanently identifying the resulting ItemStack
 * as a keyed custom item of the implementing plugin.
 * <p>
 * The item repository and its methods are always accessed via an instance
 * of MessageBuilder.
 * <p>
 * <i>Examples:</i>
{@snippet :
ItemStack customItem = messageBuilder.items().createItem(validItemKey);
ItemStack customItem = messageBuilder.items().createItem(validItemKey, quantity);
ItemStack customItem = messageBuilder.items().createItem(validItemKey, quantity, stringReplacementMap);
}
{@snippet :
Optional<String> name(ValidItemKey validItemKey);
}
{@snippet :
Optional<String> displayName(ValidItemKey validItemKey);
}
 {@snippet :
		 boolean isItem = messageBuilder.items().isCustomItem(itemStack);
		 }
 *
 */
public interface ItemRepository
{
	/**
	 * Retrieves an ItemKey from an ItemStack
	 *
	 * @return the ItemKey
	 */
	ItemKey key(ItemStack itemStack);

	/**
	 * Retrieves an item record corresponding to a valid item key
	 *
	 * @param validItemKey the valid item key
	 * @return the item record referenced by the valid item key, or null if no item record found for key
	 */
	ItemRecord record(ValidItemKey validItemKey);


	/**
	 * Retrieve an Optional of ItemRecord corresponding to a valid item key
	 *
	 * @param validItemKey the alid item key
	 * @return an Optional of itemRecord, or an empty Optional if no item record found for key
	 */
	Optional<ItemRecord> recordOpt(ValidItemKey validItemKey);

	Optional<ItemStack> createItem(ValidItemKey validItemKey);

	Optional<ItemStack> createItem(ValidItemKey validItemKey, int quantity);

	Optional<ItemStack> createItem(ValidItemKey validItemKey, int quantity, Map<String, String> replacements);


	/**
	 * Retrieves an Optional of String containing the name defined in the language file for the valid item key
	 *
	 * @param validItemKey the valid item key to be queried
	 * @return an Optional of String containing the item display name, or an empty Optional if no item record could
	 * be found for the valid item key
	 */
	Optional<String> name(ValidItemKey validItemKey);


	/**
	 * Retrieves an Optional of String containing the display name defined in the language file for the valid item key
	 *
	 * @param validItemKey the valid item key
	 * @return Optional String containing the custom item display name, or an empty Optional if no item could be found
	 * for the valid item key, or the item found does not have a valid display name
	 */
	Optional<String> displayName(ValidItemKey validItemKey);


	/**
	 * Test if an ItemStack is a custom item
	 *
	 * @param itemStack the ItemStack to test
	 * @return true if the ItemStack is a custom item, false if not
	 */
	boolean isItem(ItemStack itemStack);
}
