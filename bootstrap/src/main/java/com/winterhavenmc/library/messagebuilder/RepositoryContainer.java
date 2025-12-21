package com.winterhavenmc.library.messagebuilder;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ConstantRepository;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ItemRepository;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.sound.SoundRepository;
import com.winterhavenmc.library.messagebuilder.models.configuration.ConfigRepository;
import com.winterhavenmc.library.messagebuilder.models.configuration.worlds.WorldRepository;

public record RepositoryContainer(ConfigRepository config,
								  ConstantRepository constants,
								  ItemRepository items,
								  SoundRepository sounds,
								  WorldRepository worlds) { }
