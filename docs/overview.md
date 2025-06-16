🧾 MessageBuilderLib — Architecture Summary

🔧 Purpose
MessageBuilderLib provides a robust, extensible system for building and sending localized, macro-replaced messages in Bukkit plugins. It enables developers to define messages using language-specific YAML files and dynamically inject runtime values (macros) using a functional, fluent API.

📦 High-Level Components
1. MessageBuilder (Entry Point)

Role: Public API for plugin developers.
Usage:
messageBuilder.compose(sender, MessageId.WELCOME)
.setMacro(Macro.PLAYER, player)
.send();
Internally delegates to macro processing, message resolution, and delivery logic.
Created via static factory method (e.g., MessageBuilder.create(plugin)).
2. Macro System

Macro
Enum key used to identify placeholders (e.g., PLAYER, ITEM, LOCATION_X).
MacroProcessor
Field-based processor that maps an object to one or more placeholder-value pairs.
Handles both simple (%PLAYER%) and structured macros (%LOCATION_X%, etc.).
MacroProcessorType
Enum categorizing macro processor implementations (e.g., PLAYER, ITEM, LOCATION, etc.).
ContextMap
Internal structure that holds macro data.
Supports namespaced or compound keys using a ContextKey abstraction.
Can store multiple values per processor type if needed.
3. Message Records

MessageId
Enum used by plugin devs to reference localized messages.
MessageRecord
Immutable object containing the parsed fields of a message (text, subtitle, repeat delay, etc.).
ItemRecord
Specialized record for items with singular/plural forms and lore.
4. Configuration & Localization

LanguageResourceManager
Orchestrates installation and loading of language files.
Exposes getSectionProvider(Section) for localized queries.
LanguageResourceInstaller
Copies language files from the JAR to the plugin’s data directory, based on auto-install.txt.
Never overwrites existing files.
LanguageResourceLoader
Loads a YAML file into a Configuration object.
If the configured language file is missing, falls back to loading language/en-US.yml directly from the JAR.
5. Query System

QueryHandler
Interface for fetching MessageRecord or ItemRecord from a configuration.
Implementation (ConfigurationQueryHandler) parses a specific YAML layout.
6. Fluent API Lifecycle

compose(recipient, messageId)
└─> sets up context, loads message
└─> .setMacro(Macro, Object)
└─> routed to appropriate MacroProcessor
└─> populates ContextMap
└─> replaces placeholders in message string
└─> .send() delivers to CommandSender
⚙️ Design Strengths

✅ Extensible: Easily supports new macro types.

✅ Locale-Safe: Supports dynamic language switching without restart.

✅ Immutable Records: Safe message structure via MessageRecord and ItemRecord.

✅ Failproof Loading: Guarantees a message config is always loaded, even in failure scenarios.

✅ Unchanged External API: Maintains backward compatibility with previous usage patterns.

----------------

🧾 Component Descriptions
MessageBuilder: The primary interface used by plugin developers to compose and send messages. It interacts with macro processors and retrieves message records.
MacroProcessor: Processes objects to extract macro data, populating the ContextMap.
ContextMap: Holds the macro data used for placeholder replacements in messages.
MessageRecord / ItemRecord: Immutable representations of messages and items, containing the necessary data for message composition.
LanguageResourceManager: Manages the loading and installation of language resources, coordinating with the loader and installer.
LanguageResourceLoader: Loads language YAML files into configurations.
LanguageResourceInstaller: Handles the installation of default language files from the JAR to the plugin's data directory.
QueryHandler: Retrieves specific message records from the loaded configurations.
language/*.yml Files: The YAML configuration files containing localized messages.

