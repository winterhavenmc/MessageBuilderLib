# MessageBuilder API

## Instantiate

```(java)
MessageBuilder messageBuilder = MessageBuilder.create(plugin);
```

## Send

```(java)
messageBuilder.compose(recipient, messageId).send();
```

```(java)
messageBuilder.compose(recipient, messageId)
	.setMacro(Macro.ITEM, itemStack)
	.setMacro(Macro.ENTITY, entity)
	.send();
```

## Utilities

### Constant Repository

```(java)
List<String> stringList = messageBuilder.constants().getStringList(validConstantKey);
Optional<String> string = messageBuilder.constants().getString(validConstantKey);
Optional<int> value = messageBuilder.constants().getInt(validConstantKey);
Optional<boolean> value = messageBuilder.constants().getBoolean(validConstantKey);
```
Note: empty optional denotes non-present constant entry for key

### Item Repository

```(java)
Optional<ItemStack> customItem = messageBuilder.items().createItem(validItemKey);
Optional<ItemStack> customItem = messageBuilder.items().createItem(validItemKey, qty);
```
Note: empty optional denotes non-present or invalid item entry for key


### Sound Repository
```(java)
messageBuilder.sounds().play(player, soundId);
```

### World Repository
```(java)
boolean enabled = messageBuilder.worlds().isEnabled(worldUid);
Optional<String> worldName = messageBuilder.worlds().aliasOrName(worldUid);
Optional<Location> spawnLocation = messageBuilder.worlds().spawnLocation(worldUid);
```

### Config Repository
```(java)
Locale locale = messageBuilder.config().locale();
ZoneId timezone = messageBuilder.config().zoneId();
```
