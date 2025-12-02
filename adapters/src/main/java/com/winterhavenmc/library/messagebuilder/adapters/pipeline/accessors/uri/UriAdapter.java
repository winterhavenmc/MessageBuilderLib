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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.uri;

import com.winterhavenmc.library.messagebuilder.core.context.AccessorCtx;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.uri.UriAddressable;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import java.net.URI;
import java.util.Optional;


public class UriAdapter implements Accessor
{
	public UriAdapter(final AccessorCtx ctx) { }


	@Override
	public Optional<UriAddressable> adapt(final Object obj)
	{
		return switch (obj)
		{
			case UriAddressable uriAddressable -> Optional.of(uriAddressable);
			case Plugin plugin -> Optional.of(() -> getPluginURI(plugin));
			case Server server -> Optional.of(() -> getServerURI(server));
			case URI uri -> Optional.of(() -> uri);
			case null, default -> Optional.empty();
		};
	}


	private static URI getPluginURI(final Plugin plugin)
	{
		String website = plugin.getDescription().getWebsite();

		return website != null && !website.isBlank()
				? URI.create(website)
				: URI.create(UNKNOWN_VALUE);
	}


	private static URI getServerURI(final Server server)
	{
		String serverIp = (!server.getIp().isBlank()) ? server.getIp() : "0.0.0.0";
		String serverPort = (server.getPort() != 0) ? String.valueOf(server.getPort()) : "25565";

		if (serverIp.contains(":"))
		{
			serverIp = "[" + serverIp + "]";
		}

		return URI.create("minecraft://" + serverIp + ":" + serverPort);
	}

}
