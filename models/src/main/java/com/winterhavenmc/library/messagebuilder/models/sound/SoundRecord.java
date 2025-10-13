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

package com.winterhavenmc.library.messagebuilder.models.sound;

public sealed interface SoundRecord permits ValidSoundRecord, InvalidSoundRecord
{
	String key();

	static SoundRecord of(String key,
						  boolean enabled,
						  boolean playerOnly,
						  String bukkitSoundName,
						  float volume,
						  float pitch)
	{
		if (key == null) return new InvalidSoundRecord("ø", "The parameter 'key' was null.");
		else if (key.isBlank()) return new InvalidSoundRecord("BLANK", "The parameter 'key' was blank.");
		else if (bukkitSoundName == null) return new InvalidSoundRecord(key, "The parameter 'soundName' was null.");
		else if (bukkitSoundName.isBlank()) return new InvalidSoundRecord(key, "The parameter 'soundName' was blank.");
		else return new ValidSoundRecord(key, enabled, playerOnly, bukkitSoundName, volume, pitch);
	}
}
