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

import com.winterhavenmc.util.messagebuilder.validation.ValidationHandler;
import org.bukkit.command.CommandSender;

import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.validation.ExceptionMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


public class Recipient
{
    private final CommandSender wrappedSender;


    private Recipient(final CommandSender sender)
    {
        validate(sender, Objects::isNull, ValidationHandler.throwing(PARAMETER_NULL, RECIPIENT));
        this.wrappedSender = sender;
    }


    public static Optional<Recipient> of(final CommandSender sender)
    {
        return sender == null
                ? Optional.empty()
                : Optional.of(new Recipient(sender));
    }


    public CommandSender asCommandSender()
    {
        return wrappedSender;
    }


    @Override
    public String toString()
    {
        return wrappedSender.getName();
    }

}
