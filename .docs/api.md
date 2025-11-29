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
messageBuilder.constants().get(validConstantKey);
```

### Item Repository

```(java)
ItemStack customItem = messageBuilder.items().create(validItemKey);
```

### Sound Repository
```(java)
messageBuilder.sounds().play(player, validSoundKey);
```

### World Repository
```(java)
boolean enabled = messageBuilder.worlds().isEnabled(worldUid);
Optional<String> worldName = messageBuilder.worlds().aliasOrName(worldUid);
Location spawnLocation = messageBuilder.worlds().spawnLocation(worldUid);
```

### Config Repository
```(java)
Locale locale = messageBuilder.config().locale();
ZoneId timezone = messageBuilder.config().zoneId();
```
