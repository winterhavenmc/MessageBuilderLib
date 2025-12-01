# Item Repository

The item repository provides utility methods for creating custom items
as defined in the language file. Additional methods are provided for
examining an ItemStack as being a custom item.

A custom item created using the item repository will have its name and
lore applied from the language file settings, and will have a persistent
data structure attached, permanently identifying the resulting ItemStack
as a keyed custom item of the implementing plugin.

The item repository and its methods are always accessed via an instance
of MessageBuilder. 

*Examples:*
```java
ItemStack customItem = messageBuilder.items().createItem(validItemKey);
ItemStack customItem = messageBuilder.items().createItem(validItemKey, quantity);
ItemStack customItem = messageBuilder.items().createItem(validItemKey, quantity, stringReplementMap);
```

```java
boolean isItem = messageBuilder.items().isCustomItem(itemStack);
```

```java
Optional<String> name(ValidItemKey validItemKey);
```

```java
Optional<String> displayName(ValidItemKey validItemKey);
```
