# PRELIMINARY DOCUMENTATION! DO NOT RELY ON THIS DOCUMENT FOR ACCURACY AT THIS TIME!


# Namespace Class

The `Namespace` class is a centralized container for managing standardized field names across different record types. It is designed to ensure consistency and clarity when working with context map keys, particularly for hierarchical structures like YAML.

## Structure

The `Namespace` class contains static inner classes, each named after a logical record type. These inner classes define an enum, consistently named `Field`, which provides the standardized field names and any associated metadata or methods.

### Example Structure

```java
public class Namespace {

   public static class Location {
      public enum Field {
         WORLD, X, Y, Z, FORMATTED;

         // Fields and methods specific to Location.Field
      }
   }

   public static class Item {
      public enum Field {
         ITEM_KEY, DISPLAY_NAME, QUANTITY, DESCRIPTION;

         // Fields and methods specific to Item.Field
      }
   }

   public static class Recipient {
      public enum Field {
         NAME, UUID, LOCATION, LOCATION_WORLD, LOCATION_X, LOCATION_Y, LOCATION_Z;

         // Fields and methods specific to Recipient.Field
      }
   }

   // Add other types as needed
}
```

## Standardized Location Fields
   Using upper snake case for field naming while keeping subfields as dot-delimited keys under the location namespace:

### Field Definitions:

#### Base Key:
{NAMESPACE}.LOCATION

#### Fields:

|Key| Description                |
|---|----------------------------|
|{NAMESPACE}.LOCATION.FORMATTED| The full formatted string. |
|{NAMESPACE}.LOCATION.WORLD| The name of the world.     |
|{NAMESPACE}.LOCATION.X| The X-coordinate.          |
|{NAMESPACE}.LOCATION.Y| The Y-coordinate.          |
|{NAMESPACE}.LOCATION.Z| The Z-coordinate.          |


Enum for Location Fields

This enum defines all standardized location fields, their default names (e.g., FORMATTED), and metadata for documentation or dynamic lookups.

```java
public enum LocationField {

    FORMATTED("location.formatted", "String", "The full formatted location string"),
    WORLD("location.world", "String", "The world where the object is located"),
    X("location.x", "Double", "The X-coordinate of the location"),
    Y("location.y", "Double", "The Y-coordinate of the location"),
    Z("location.z", "Double", "The Z-coordinate of the location");

    private final String yamlPath; // Dot-delimited field nameSingular
    private final String type;     // Data type
    private final String description; // Field description

    LocationField(String yamlPath, String type, String description) {
        this.yamlPath = yamlPath;
        this.type = type;
        this.description = description;
    }

    public String getYamlPath() {
        return yamlPath;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String withNamespace(String namespace) {
        return namespace + "." + yamlPath; // Generate namespaced key
    }
}
```
