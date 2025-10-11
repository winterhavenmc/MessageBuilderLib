# The Laws of the Library

## The First Law of the Library

All `SectionQueryHandlers` SHALL have a corresponding constant in the `Section` enum. This law enforces in code the
immutable contract between the `Section` enum constants and the `SectionQueryHandlers`, ensuring that each top-level
section of the YAML language file is properly represented and handled. Sections of the YAML language file that do not
have a corresponding `Section` and `SectionQueryHandler` will not be accessible from within the library 
or plugins that use it.

This design aligns with the philosophy of The Library by:
1. Ensuring a clear and consistent relationship between the sections defined in the language file and their corresponding handlers, facilitating easier maintenance and understanding of the codebase.
2. Promoting type safety and reducing the risk of runtime errors, as the mapping between sections and handlers is explicitly defined and checked at compile time.
3. Enhancing readability and clarity of the code, making it easier for future developers to understand the structure and purpose of the library.

Enforcement of this law is achieved through the architecture of the library, where the creation of `SectionQueryHandlers` is directly linked to the `Section` enum constants. Any addition of new sections requires the implementation of a corresponding handler, maintaining the integrity of the library's design.
 
---

# The Laws of the Language File

## The First Law of the Language File: Consistent Keys Law

1. All keys in the language file **SHALL** use upper snake case, consisting only of alphanumeric characters and underscores.
2. This naming convention aligns with the naming of Java constants, particularly enum constants, to which they are always mapped.
3. While the library will attempt to use non-conforming keys without manipulation, it **MAY** log warnings to encourage adherence.
4. Responsibility for maintaining proper key management in the language file lies solely with the plugin developer.

## The Second Law of the Language File: The Holder of Truth Law

1. The **LanguageConfigurationSupplier** shall always hold the most current and accurate version of the language configuration.
2. The **LanguageHandler** is responsible for instantiating the `LanguageConfigurationSupplier` using the configuration returned by the **LanguageLoader**.
3. On a reload command, the **LanguageHandler** shall:
    - Obtain a new configuration object from the **LanguageLoader**.
    - Create a new `LanguageConfigurationSupplier` using this updated configuration.
4. The **LanguageConfigurationSupplier** shall serve as the definitive source of truth for the language configuration, ensuring consistency and accuracy across the library and any plugins utilizing it.



