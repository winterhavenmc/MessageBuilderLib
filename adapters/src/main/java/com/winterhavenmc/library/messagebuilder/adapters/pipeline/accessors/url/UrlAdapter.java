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
import com.winterhavenmc.library.messagebuilder.core.context.AccessorCtx;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.url.UrlAddressable;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import java.net.URI;
import java.util.Optional;


public class UrlAdapter implements Accessor
{
	public UrlAdapter(final AccessorCtx ctx) { }


	@Override
	public Optional<UrlAddressable> adapt(final Object obj)
	{
		return switch (obj)
		{
			case UrlAddressable urlAddressable -> Optional.of(urlAddressable);
			case Plugin plugin -> Optional.of(() -> getPluginURI(plugin));
			case Server server -> Optional.of(() -> getServerURI(server));
			case null, default -> Optional.empty();
		};
	}


	private static String getPluginURI(final Plugin plugin)
	{
		String website = plugin.getDescription().getWebsite();

		return website != null && !website.isBlank()
				? URI.create(website).toASCIIString()
				: URI.create(UNKNOWN_VALUE).toASCIIString();
	}


	private static String getServerURI(final Server server)
	{
		String serverIp = (!server.getIp().isBlank()) ? server.getIp() : "0.0.0.0";
		if (serverIp.contains(":"))
		{
			serverIp = "[" + serverIp + "]";
		}
		String serverPort = (server.getPort() != 0) ? String.valueOf(server.getPort()) : "25565";

		return URI.create("minecraft://" + serverIp + ":" + serverPort).toASCIIString();
	}

}
