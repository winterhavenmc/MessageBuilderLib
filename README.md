[![Codacy Badge](https://app.codacy.com/project/badge/Grade/b252ef119c624bfb9eb05971e7919726)](https://app.codacy.com/gh/winterhavenmc/MessageBuilderLib/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/b252ef119c624bfb9eb05971e7919726)](https://app.codacy.com/gh/winterhavenmc/MessageBuilderLib/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage)
[![Spigot Version](https://badgen.net/static/spigot-api/1.21.4?color=yellow)](https://spigotmc.org)
&nbsp;[![License](https://badgen.net/static/license/GPLv3)](https://www.gnu.org/licenses/gpl-3.0)

# MessageBuilderLib

## Overview
MessageBuilderLib is a Java library designed for Bukkit Minecraft server plugin developers to streamline the creation and customization of dynamic messages. By employing a builder pattern, the library allows for flexible text replacement using a robust macro processing system. This tool simplifies message handling, making it easier to manage localized content and dynamic data.

## Key Features

- **Builder Pattern**: Compose messages with an intuitive chaining mechanism.
- **Macro Replacement**: Use macros for dynamic text substitution, supporting standard Java objects and Bukkit-specific types.
- **Localization Support**: Automatically manage language files using IETF standard language tags (e.g., `en-US`), with fallback mechanisms for missing translations.
- **Flexible Data Sources**: Fetch messages and items from YAML-backed `ConfigurationSection`s.
- **Customizable Context**: Support for multiple entries of the same macro type using composite keys.
- **Extensibility**: Easily add new `MacroProcessorType` and `Processor` implementations.

## Usage

### Basic Setup
In your plugin's `onEnable` method, initialize the `MessageBuilder`:

```java
@Override
public void onEnable() {
    messageBuilder = new MessageBuilder<>(this);
}
```

### Composing a Message

```java
messageBuilder.compose(player, MessageId.WELCOME)
              .setMacro(Macro.PLAYER_NAME, player.getName())
              .setMacro(Macro.LOCATION, player.getLocation())
              .send();
```

### Adding Localization Files
Language files are stored in the `language` resource folder and are managed automatically. Server operators can customize or delete them to refresh from defaults.

### Querying Records
#### Fetching Message Records
Retrieve message data from the `MESSAGES` section of the YAML configuration:

```java
Optional<MessageRecord> messageRecord = queryHandler.getMessageRecord("WELCOME_MESSAGE");
```

#### Fetching Item Records
Retrieve item data from the `ITEMS` section of the YAML configuration:

```java
Optional<ItemRecord> itemRecord = queryHandler.getItemRecord("DEFAULT_ITEM");
```

## Components

### MessageBuilder
The primary entry point for the library, enabling message composition and macro replacement.

### QueryHandler
Provides access to localized message and item records stored in YAML configuration files.

### LanguageHandler
Manages installation and loading of language files, adhering to IETF standard language tags.

### ContextMap
Stores macro data using composite keys to allow multiple entries of the same type.

### Record Classes
- **MessageRecord**: Holds data for a message, including content, title, subtitle, and timings.
- **ItemRecord**: Holds data for an item, including names and lore.

### MacroProcessor System
Processes macros dynamically, replacing placeholders with appropriate values. MacroProcessor instances are tied to `MacroProcessorType` constants for efficient access.

## Example YAML Layout

### Messages
```yaml
MESSAGES:
  WELCOME_MESSAGE:
    messageKey: "WELCOME_MESSAGE"
    enabled: true
    message: "Welcome, %PLAYER_NAME%!"
    repeatDelay: 0
    title: "Welcome!"
    titleFadeIn: 10
    titleStay: 70
    titleFadeOut: 20
    subtitle: "Enjoy your stay at %LOCATION_WORLD%!"
```

### Items
```yaml
ITEMS:
  DEFAULT_ITEM:
    NAME:
      SINGULAR: 'Default Item'
      PLURAL: 'Default Items'
    INVENTORY_NAME:
      SINGULAR: 'Default Inventory Item'
      PLURAL:  'Default Inventory Items'
    ITEM_LORE:
      - '&edefault lore line 1'
      - '&edefault lore line 2'
```

## Requirements
- Java 21 or higher
- Bukkit API (Spigot-1.21.4-R0.1-SNAPSHOT)

## Installation
1. Add the library JAR as a dependency in your Bukkit plugin project.
2. Include the library in your plugin's classpath.
3. Initialize the `MessageBuilder` in your plugin's `onEnable` method.

## License
This project is licensed under the GPLv3 License. See the LICENSE file for details.

## Contributions
Contributions are welcome! Feel free to submit a pull request or open an issue for feature requests or bug reports.

## Contact
For questions or support, reach out through the GitHub repository’s issue tracker.
