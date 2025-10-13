/**
 * Defines the final dispatch stage in the message pipeline, responsible for delivering
 * resolved and formatted messages to recipients.
 *
 * <p>This package abstracts different message delivery strategies, such as chat output or
 * title overlays, using the {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.senders.Sender} interface.
 * Implementations are responsible for validating recipient types, formatting message content,
 * and applying cooldown restrictions via a
 * {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.cooldown.CooldownMap CooldownMap}.
 *
 * <h2>Key Components</h2>
 * <ul>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.senders.Sender}
 *   – A functional interface for sending messages to a recipient using a defined delivery channel.</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.senders.Sender}
 *   - Sends the message string to the chat interface of the recipient.</li>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.senders.Sender}
 *   - Displays the title and subtitle of the message as an overlay (if the recipient is a
 *   {@link org.bukkit.entity.Player Player}).</li>
 * </ul>
 *
 * <p>All implementations are expected to work with fully processed
 * {@link com.winterhavenmc.library.messagebuilder.models.language.FinalMessageRecord FinalMessageRecord} instances
 * and should not perform macro resolution or placeholder detection themselves.
 *
 * @see com.winterhavenmc.library.messagebuilder.core.ports.pipeline.senders.Sender
 */
package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.senders;
