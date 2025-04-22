module com.winterhavenmc.util.messagebuilder.model.language {
	requires org.bukkit;
	requires net.time4j.base;
	requires Multiverse.Core;
	requires java.logging;
	exports com.winterhavenmc.util.messagebuilder.keys;
	exports com.winterhavenmc.util.messagebuilder.resources.language.yaml;
	exports com.winterhavenmc.util.messagebuilder.resources.language.yaml.section;
	exports com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.message;
	exports com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.item;
	exports com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.constant;
}
