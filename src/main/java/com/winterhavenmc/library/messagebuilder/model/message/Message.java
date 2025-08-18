/*
 * Copyright (c) 2025 Tim Savage.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.winterhavenmc.library.messagebuilder.model.message;

import com.winterhavenmc.library.messagebuilder.keys.ValidMessageKey;
import com.winterhavenmc.library.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroObjectMap;

import java.time.Duration;
import java.time.temporal.ChronoUnit;


/**
 * A sealed interface representing a message that can be composed using macros
 * and sent to a valid recipient.
 * <p>
 * Messages are constructed dynamically using the fluent API exposed by
 * {@link com.winterhavenmc.library.messagebuilder.MessageBuilder}, and are
 * resolved at runtime using a combination of a message template, a macro object map,
 * and a validated {@link com.winterhavenmc.library.messagebuilder.model.recipient.Recipient}.
 *
 * <p>The {@code Message} interface has two implementations:
 * <ul>
 *   <li>{@link ValidMessage} – A fully-constructed message with a resolvable template and a valid recipient</li>
 *   <li>{@link InvalidMessage} – A placeholder or sentinel used when the message cannot be composed</li>
 * </ul>
 *
 * <h2>Fluent Macro Assignment</h2>
 * Macros can be added to the message using the {@code setMacro} methods. These allow
 * rich contextual data such as strings, durations, quantities, and domain objects to be bound
 * to placeholders in the final rendered message.
 *
 * <h2>Sending</h2>
 * Once composed, the {@link #send()} method dispatches the message to the resolved recipient,
 * if one is available and valid.
 *
 * @see ValidMessage
 * @see InvalidMessage
 * @see com.winterhavenmc.library.messagebuilder.MessageBuilder
 * @see com.winterhavenmc.library.messagebuilder.model.recipient.Recipient
 * @see com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroObjectMap
 * @see ValidMessageKey
 */
public sealed interface Message permits ValidMessage, InvalidMessage
{
    /**
     * Associates a macro string with a value for later substitution during message rendering.
     *
     * @param macro the macro string to assign
     * @param value the value to associate with the macro
     * @param <K> the enum type of the macro
     * @param <V> the value type
     * @return the same {@code Message} instance, for fluent chaining
     */
    <K extends Enum<K>, V> Message setMacro(K macro, V value);


    /**
     * Associates a macro string with a value and explicit quantity, useful for pluralization or
     * contextual substitution that depends on numeric counts.
     *
     * @param quantity the quantity associated with the value
     * @param macro the macro string
     * @param value the object to associate
     * @param <K> the enum type of the macro
     * @param <V> the value type
     * @return the same {@code Message} instance, for fluent chaining
     */
    <K extends Enum<K>, V> Message setMacro(int quantity, K macro, V value);


    /**
     * Associates a duration with the given macro string, using a defined precision.
     *
     * @param macro the macro string
     * @param duration the {@link Duration} to be formatted and resolved
     * @param precision the {@link ChronoUnit} to control formatting granularity
     * @param <K> the enum type of the macro
     * @return the same {@code Message} instance, for fluent chaining
     */
    <K extends Enum<K>> Message setMacro(K macro, Duration duration, ChronoUnit precision);


    /**
     * Sends the composed message to the resolved recipient, if valid.
     * <p>
     * No-op if the message is invalid or the recipient is missing or unsupported.
     */
    void send();


    /**
     * Returns the {@link ValidMessageKey} identifying the message template used in composition.
     *
     * @return the message string
     */
    ValidMessageKey getMessageKey();


    /**
     * Returns the {@link Recipient.Sendable} to whom the message will be sent.
     *
     * @return the message recipient
     */
    Recipient.Sendable getRecipient();


    /**
     * Returns the {@link MacroObjectMap} of macro values bound to this message.
     *
     * @return the macro object map
     */
    MacroObjectMap getObjectMap();


    /**
     * Returns an empty {@code Message} instance that performs no action when sent.
     * Used as a fallback when message construction fails.
     *
     * @return an {@link InvalidMessage} representing an inert or failed message
     */
    static Message empty()
    {
        return InvalidMessage.empty();
    }

}
