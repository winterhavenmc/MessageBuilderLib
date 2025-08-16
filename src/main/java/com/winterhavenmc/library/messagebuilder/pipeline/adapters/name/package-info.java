/**
 * Provides the {@code Nameable} interface and its corresponding adapter used to extract and format
 * name-related fields from objects passed into the message pipeline.
 *
 * <p>Objects that implement {@link com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.Nameable Nameable}
 * expose a {@code getName()} method used for macro substitution in message templates. The adapter in this package
 * maps such values to a dot-notated macro string (e.g., {@code {OBJECT.NAME}}) that is automatically replaced during
 * message composition.
 *
 * <h2>Plugin Integration</h2>
 * <p>Plugin developers can opt into this system by having their own domain types implement the {@code Nameable} interface.
 * For example, a plugin-defined class:
 * {@snippet lang="java":
 * public class CustomNPC implements Nameable {
 *
 *     @Override
 *     public String getName() {
 *         return this.name;
 *     }
 * }
 * }
 *
 * <p>When a {@code CustomNPC} instance is passed into the macro context (via {@code setMacro()}),
 * the message builder will automatically resolve the placeholder {@code {CUSTOMNPC.NAME}}.
 *
 * <h2>Standard Support</h2>
 * <p>This adapter also supports built-in Bukkit types such as:
 * <ul>
 *   <li>{@link org.bukkit.command.CommandSender}</li>
 *   <li>{@link org.bukkit.OfflinePlayer}</li>
 *   <li>{@link org.bukkit.profile.PlayerProfile}</li>
 *   <li>{@link org.bukkit.World}</li>
 *   <li>{@link org.bukkit.Server}</li>
 *   <li>{@link org.bukkit.plugin.Plugin}</li>
 * </ul>
 *
 * @see com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.Nameable Nameable
 * @see com.winterhavenmc.library.messagebuilder.pipeline.adapters.name.NameAdapter NameAdapter
 * @see com.winterhavenmc.library.messagebuilder.pipeline.adapters.Adapter Adapter
 */
package com.winterhavenmc.library.messagebuilder.pipeline.adapters.name;
