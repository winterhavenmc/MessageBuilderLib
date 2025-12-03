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
### Item Keys
All MessageBuilder keys are implemented as algebraic data types, which
are validated on creation to ensure only non-null, non-blank, and 
naming-convention compliant keys are used to reference key/value pairs.
A valid key can be constructed from a string as follows:

```java
ItemKey itemKey = ItemKey.of("VALID_KEY_STRING");
if (itemKey instance of ValidItemKey validItemKey) { }
```

```java
Optional<ValidItemKey> validItemKey = ItemKey.of("VALID_KEY").isValid();
if (validItemKey.isPresent()) { }
```

```java
ValidItemKey validItemKey = ItemKey.of("VALID_KEY").isValid().orElseThrow();
```

MessageBuilder keys may also be created by passing an enum constant
as a parameter, which will use the name() method to create a string-based
key.
