/**
 * Contains the sealed {@link com.winterhavenmc.library.messagebuilder.model.message.Message}
 * interface and its implementations, which represent composed messages ready for macro
 * substitution and delivery.
 * <p>
 * A {@code Message} is constructed through the library's fluent builder API, with macro
 * values bound dynamically to placeholders. Once composed, the message can be dispatched
 * using {@link com.winterhavenmc.library.messagebuilder.model.message.Message#send()}.
 *
 * <h2>Message Implementations</h2>
 * This package defines two implementations:
 *
 * <ul>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.model.message.ValidMessage} –
 *       A fully constructed and resolvable message that contains a valid recipient,
 *       a message template key, a macro object map, and a {@code MessagePipeline}</li>
 *
 *   <li>{@link com.winterhavenmc.library.messagebuilder.model.message.InvalidMessage} –
 *       A fallback or no-op message representing a failure to compose a valid message,
 *       typically due to missing or null input</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * Developers do not typically instantiate message types directly. Instead, messages are created
 * via the fluent API exposed by {@link com.winterhavenmc.library.messagebuilder.MessageBuilder}:
 *
 * <pre>{@code
 * messageBuilder.compose(sender, MessageId.LOGIN_SUCCESS)
 *               .setMacro(Macro.PLAYER, player)
 *               .send();
 * }</pre>
 *
 * If the recipient or message key is invalid, the library will return an
 * {@link com.winterhavenmc.library.messagebuilder.model.message.InvalidMessage InvalidMessage}, which safely performs no action when sent.
 *
 * @see com.winterhavenmc.library.messagebuilder.model.message.Message
 * @see com.winterhavenmc.library.messagebuilder.model.message.ValidMessage
 * @see com.winterhavenmc.library.messagebuilder.model.message.InvalidMessage
 * @see com.winterhavenmc.library.messagebuilder.model.recipient.Recipient Recipient
 * @see com.winterhavenmc.library.messagebuilder.pipeline.MessagePipeline MessagePipeline
 */
package com.winterhavenmc.library.messagebuilder.model.message;
