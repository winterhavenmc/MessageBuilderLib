# Macro Naming Conventions for MessageBuilderLib

When defining `Macro` constants for your messages in **MessageBuilderLib**, it's essential to use clear and concise names. These constants correspond to placeholders in your message strings and should prioritize readability and usability for both developers and server operators. This guide outlines best practices for naming your macros and provides examples to help you get started.

---

## Best Practices for Naming Macros

### 1. Context-Driven Names
Name your macros based on their role or meaning within the message, not based on their type. Focus on what the macro represents in the context of the message.

- **Good Example**: `%PLAYER_NAME%` for a placeholder representing a player's name.
- **Avoid**: `%STRING_1%`, which is too vague.

### 2. Short and Meaningful
Keep macro names concise while ensuring they remain descriptive. Avoid overly long or complex names, as they can clutter the message string.

- **Good Example**: `%ITEM%` instead of `%ITEM_NAME_OR_DESCRIPTION%`.

### 3. Avoid Type Details in Names
Your macro names should focus on functionality or purpose rather than implementation details like type.

- **Good Example**: `%COORDS%` to represent a location's coordinates.
- **Avoid**: `%LOCATION_XYZ%`.

### 4. Namespace Common Terms
To prevent naming collisions, especially with common terms, consider adding a prefix or suffix that reflects the context.

- **Good Example**: `%SHOP_ITEM%`, `%INVENTORY_ITEM%`.
- **Avoid**: `%ITEM%` for highly specific subsystems.

### 5. Use Uppercase for Readability
By convention, macro placeholders are written in **UPPERCASE** to distinguish them from regular text in message strings.

---

## Example Placeholder Naming Table

| Placeholder   | Description                            | Good Examples         | Avoid        |
|---------------|----------------------------------------|------------------------|--------------|
| Player Name   | Displays the player's name            | `%PLAYER_NAME%`       | `%PNAME%`    |
| Score         | Represents the player's current score | `%CURRENT_SCORE%`     | `%SCORE%`    |
| Location      | Describes the player's coordinates    | `%LOCATION%`, `%COORDS%` | `%LOC%`     |
| Items         | Represents an item in an inventory    | `%ITEM%`, `%ITEM_NAME%` | `%ITM%`     |

---

## Conventions in Action

When defining messages and macros, follow these conventions to create meaningful, maintainable placeholders. For example:

**Message YAML:**
```yaml
WELCOME_MESSAGE: "Welcome %PLAYER_NAME%! You are in %WORLD% at %COORDS%."
```
**Macro enum:**
```java
public enum Macro {
    PLAYER_NAME,
    WORLD,
    COORDS;
}
```
**usage:**
```java
messageBuilder.compose(recipient, MessageId.WELCOME_MESSAGE)
    .setMacro(Macro.PLAYER_NAME, player.getName())
    .setMacro(Macro.WORLD, player.getWorld().getName())
    .setMacro(Macro.COORDS, location.toString())
    .send();
```

With this approach:

The message is easy to understand for plugin developers.
Macros are intuitive for server operators customizing YAML files.
Players receive polished, meaningful messages in-game.
By adhering to these conventions, you can ensure that your macros remain clear, concise, and practical for both developers and end-users.