[![Codacy Badge](https://app.codacy.com/project/badge/Grade/b252ef119c624bfb9eb05971e7919726)](https://app.codacy.com/gh/winterhavenmc/MessageBuilderLib/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/b252ef119c624bfb9eb05971e7919726)](https://app.codacy.com/gh/winterhavenmc/MessageBuilderLib/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage)
[![Spigot Version](https://img.shields.io/badge/spigot--api-1.21.11-yellow)](https://www.gnu.org/licenses/gpl-3.0)
&thinsp;[![License](https://img.shields.io/badge/license-GPLv3-blue)](https://www.gnu.org/licenses/gpl-3.0)
&thinsp;[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://winterhavenmc.github.io/MessageBuilderLib/javadoc/)

# MessageBuilderLib

## Overview
MessageBuilderLib is a Java library designed for Bukkit Minecraft server plugin developers to streamline the creation and customization of dynamic messages. By employing a builder pattern, the library allows for flexible text replacement using a robust macro processing system. This tool simplifies message handling, making it easier to manage localized content and dynamic data.

## Key Features

- **Builder Pattern**: Compose messages with an intuitive chaining mechanism.
- **Macro Replacement**: Use macros for dynamic text substitution, supporting standard Java objects and Bukkit-specific types.
- **Localization Support**: Automatically manage language files using IETF standard language tags (e.g., `en-US`), with fallback mechanisms for missing translations.
- **Flexible Data Sources**: Messages and custom item strings, among other constant placeholders, are stored in
a YAML `Configuration` file, and may be edited by server operators, within the limits imposed by the plugin.
- **Context Aware Placeholders**: Macros may rely on values from other objects placed in the context map 
to effect their output. For instance, singular or plural names will be displayed for custom items, if an
associated quantity exists.
- **Multi-Field Processors**: New placeholders may be created by the processor to hold multiple values. For example, the 
world name and coordinates for a location object.
- Planned features: More MacroProcessor types for dates, durations and others object types. Locale setting for localized
date and number formatting.

## Usage

### Basic Setup
You must first create two enums in your plugin. You can choose any name for these enums,
but it is recommended to name them 'MessageId' and 'Macro' for the sake of convention. The 
MessageId enum constants correlate directly to the message keys in the language yaml file,
and should be an exact string match for the constant name, as returned by the .name() method.

example:
```java
public enum MessageId {
    COMMAND_INVALID,
    COMMAND_SUCCESS
}
```

The Macro enum corresponds directly to any placeholders that may be contained in
any of the messages, and also must be an exact string match for the Macro enum constant names.

example:
```java
public enum Macro {
    PLAYER_KILLS,
    TEAM_NAME,
    PLAYER,
    PLUGIN_TOOL_ITEM,
    ANOTHER_ITEM,
    ANOTHER_ITEM_QUANTITY, // allows ANOTHER_ITEM to choose plurality
    WORLD,
    LOCATION
}
```

In your plugin's `onEnable` method, initialize the `MessageBuilder` using the
enums as parameterized types:

```java
@Override
public void onEnable() {
    messageBuilder = new MessageBuilder.create(this);
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
Language files are stored in the `language` resource folder and can be installed automatically if they are listed
in a 'auto_install.txt' file within the same folder. Server operators can customize the messages, or delete them 
and reload the plugin to re-install any auto-install specified files from their resources. The auto install process
runs when the MessageBuilder is instantiated in the plugin onEnable method, and whenever the reload method is called.


### Querying Records
#### Fetching Message Records
Retrieve message data from the `MESSAGES` section of the YAML configuration:

```java
Optional<MessageRecord> messageRecord = queryHandler.getRecord("WELCOME_MESSAGE");
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
Manages installation and loading of language files, adhering to IETF standard language tags for
specifying the language to be used. A planned feature is to be locale aware,
so that date and number formatting will be based on the locale selected in the plugin's config.yml file.

### ContextMap
Collects and stores macro data for all macros before any replacements occur so that
macro processors may refer to values from other sources in determining the string replacement logic.

### Record Classes
- **MessageRecord**: Holds data for a message, including content, title, subtitle, and timings.
- **ItemRecord**: Holds data for an item, including names and lore.

### MacroProcessor System
Processes macros according to type, replacing message placeholders with appropriate values. Each MacroProcessor instance 
is keyed to a `MacroProcessorType` constant for ensuring type safety, and future processor types can be easily added
to future versions the library, if necessary.

## Example YAML Layout
The language yaml file is broken into top-level sections, including Settings, Items, and Messages

### Settings
```yaml
SETTINGS:
  DELIMITERS:
    LEFT: '{'
    RIGHT: '}'
```
The settings section contains various settings related to the language file.
Currently, the DELIMITERS section is the only available setting.


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

### Messages
```yaml
MESSAGES:
  WELCOME_MESSAGE:
    enabled: true
    message: "Welcome, %PLAYER_NAME%!"
    REPEAT_DELAY: 0
    title: "Welcome!"
    title-fade-in: 10
    title-stay: 70
    title-fade-out: 20
    subtitle: "Enjoy your stay at %LOCATION_WORLD%!"
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
