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

package com.winterhavenmc.util.messagebuilder;

import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.validation.LogLevel;

import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static com.winterhavenmc.util.messagebuilder.validation.ValidationHandler.logging;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;

public final class InvalidMessage implements Message
{
    private final String reason;

    public InvalidMessage(String reason) {
        this.reason = reason;
    }

    @Override
    public <K extends Enum<K>, V> Message setMacro(K macro, V value) {
        return this;
    }

    @Override
    public <K extends Enum<K>, V> Message setMacro(int quantity, K macro, V value) {
        return this;
    }

    @Override
    public void send() {
        validate(null, Objects::isNull, logging(LogLevel.WARN, PARAMETER_INVALID, RECIPIENT));
    }

    @Override
    public RecordKey getMessageKey() {
        return null;
    }

    @Override
    public ValidRecipient getRecipient() {
        return null;
    }

    @Override
    public ContextMap getContextMap() {
        return null;
    }

    public String reason() {
        return reason;
    }
}
