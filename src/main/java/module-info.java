module com.winterhavenmc.library.messagebuilder.api
{
	requires java.logging;
	requires net.time4j.base;
	requires org.bukkit;
	requires Multiverse.Core;

	// Public API exports: accessible to the plugin users
	exports com.winterhavenmc.library.messagebuilder; // for MessageBuilder and related utilities
	exports com.winterhavenmc.library.messagebuilder.model.message; // for Message and its implementations
}
