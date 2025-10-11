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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.url;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor;
import com.winterhavenmc.library.messagebuilder.core.context.AdapterCtx;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.url.UrlAddressable;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import java.util.Optional;


public class UrlAdapter implements Accessor
{
	public UrlAdapter(final AdapterCtx ctx) { }


	@Override
	public Optional<UrlAddressable> adapt(final Object obj)
	{
		return switch (obj)
		{
			case UrlAddressable urlAddressable -> Optional.of(urlAddressable);
			case Plugin plugin -> Optional.of(() -> plugin.getDescription().getWebsite());
			case Server server -> Optional.of(() -> server.getIp());
			case null, default -> Optional.empty();
		};
	}

}
