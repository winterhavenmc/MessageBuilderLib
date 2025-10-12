package com.winterhavenmc.library.messagebuilder.core.ports.resources.sound;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.SectionProvider;
import com.winterhavenmc.library.messagebuilder.models.language.Section;

public interface SoundResourceManager
{
	SectionProvider getSoundConfigSection(Section section);
}
