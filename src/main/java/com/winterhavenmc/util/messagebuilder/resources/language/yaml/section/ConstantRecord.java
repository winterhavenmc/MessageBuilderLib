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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section;

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.KEY;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.SECTION;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;

public record ConstantRecord(String key, Object obj) implements SectionRecord
{
    public static Optional<ConstantRecord> getRecord(final String key, final ConfigurationSection section)
    {
        validate(key, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, KEY));
        validate(key, String::isBlank, () -> new ValidationException(PARAMETER_EMPTY, KEY));
        validate(section, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, SECTION));

        return Optional.ofNullable(section)
                .map(s -> s.get(key))
                .map(value -> new ConstantRecord(key, value));
    }

}
