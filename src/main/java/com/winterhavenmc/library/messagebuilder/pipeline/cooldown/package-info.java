/**
 * Provides support for message rate limiting through a cooldown mechanism.
 * <p>
 * This package is responsible for ensuring that certain messages—typically
 * those tied to player actions such as block interactions—are not shown
 * too frequently. This helps prevent message spam when a player holds
 * down a mouse button or repeatedly triggers the same event.
 * <p>
 * The {@link com.winterhavenmc.library.messagebuilder.pipeline.cooldown.Cooldown} interface defines
 * the contract for determining whether a message is currently cooling down.
 * The concrete {@link com.winterhavenmc.library.messagebuilder.pipeline.cooldown.CooldownMap} implementation
 * manages expiration timestamps for messages on a per-recipient basis.
 *
 * <h2>Design and Behavior</h2>
 * <ul>
 *   <li>Cooldowns are keyed by a {@code CooldownKey}, which typically includes both
 *   the player's {@code UUID} and the {@code MessageId}.</li>
 *   <li>Message delay durations are defined in the message YAML file under each message entry,
 *   giving server operators full control over cooldown durations per message.</li>
 *   <li>The {@code putExpirationTime} method sets a cooldown for a specific message and player.</li>
 *   <li>The {@code notCooling} method returns {@code true} if the cooldown has expired
 *   or was never set, meaning the message may be shown again.</li>
 *   <li>The {@code removeExpired()} method removes expired cooldowns from memory,
 *   which may be useful in long-running servers or diagnostic tooling.</li>
 * </ul>
 *
 * <h2>Typical Use Case</h2>
 * Used by the {@code MessageSender} and {@code SpigotTitleSender} implementations in the
 * {@code sender} package to throttle message frequency, especially in cases
 * where user interaction may trigger rapid repeated events.
 */
package com.winterhavenmc.library.messagebuilder.pipeline.cooldown;
