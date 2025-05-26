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

import com.winterhavenmc.library.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.keys.RecordKey;

import java.time.Duration;
import java.time.temporal.ChronoUnit;


public sealed interface Message permits ValidMessage, InvalidMessage {
    <K extends Enum<K>, V> Message setMacro(K macro, V value);
    <K extends Enum<K>, V> Message setMacro(int quantity, K macro, V value);
    <K extends Enum<K>> Message setMacro(K macro, Duration duration, ChronoUnit precision);
    void send();
    RecordKey getMessageKey();
    Recipient.Sendable getRecipient();
    MacroObjectMap getObjectMap();

    static Message empty()
    {
        return InvalidMessage.empty();
    }
}
