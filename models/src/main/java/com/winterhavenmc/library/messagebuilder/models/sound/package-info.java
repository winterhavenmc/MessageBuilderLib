/**
 * Represents a sound record as an algebraic data type, implemented using a sealed interface
 * with permitted types of {@link com.winterhavenmc.library.messagebuilder.models.sound.ValidSoundRecord}
 * or {@link com.winterhavenmc.library.messagebuilder.models.sound.InvalidSoundRecord}.
 * <p>
 * SoundRecord instances are validated at creation by the static factory method(s) provided in the interface,
 * and return a valid or invalid type after validation.
 * <p>
 * <img src="doc-files/SoundRecord_structure.svg" alt="SoundRecord Structure">
 */
package com.winterhavenmc.library.messagebuilder.models.sound;
