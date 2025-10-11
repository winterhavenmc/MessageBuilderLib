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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.version;

import com.winterhavenmc.library.messagebuilder.core.context.AdapterCtx;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.version.Versionable;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import java.util.Optional;


public class VersionAdapter implements Accessor
{
	public VersionAdapter(final AdapterCtx ctx) { }


	@Override
	public Optional<Versionable> adapt(final Object obj)
	{
		return switch (obj)
		{
			case Versionable versionable -> Optional.of(versionable);
			case Plugin plugin -> Optional.of(() -> plugin.getDescription().getVersion());
			case Server server -> Optional.of(() -> server.getVersion());
			case null, default -> Optional.empty();
		};
	}

}
