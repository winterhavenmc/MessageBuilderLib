## MessageBuilderToolkit

The `MessageBuilderToolkit` is an advanced feature of the library, providing a suite of tools and utilities to complement the `MessageBuilder`. While the `MessageBuilder` focuses on composing and sending messages, the `Toolkit` offers additional methods for plugin developers to interact with the library and simplify common tasks.

### Accessing the Toolkit

You can access the `MessageBuilderToolkit` through the `MessageBuilder`:

```java
MessageBuilder<MyMessageId, MyMacro> builder = new MessageBuilder<>(plugin);
MessageBuilderToolkit toolkit = builder.getToolkit();
