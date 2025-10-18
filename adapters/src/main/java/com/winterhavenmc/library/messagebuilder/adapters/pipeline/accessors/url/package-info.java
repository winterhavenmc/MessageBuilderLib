/**
 * Provides an adapter for objects that expose a {@link com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.url}.
 *
 * <p>This package defines the {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.url.UrlAddressable}
 * interface, which should be implemented by any object that can expose a url string value for use in macro substitution.
 * Macros of the form {@code {OBJECT.URL}} are supported by extracting and formatting the value returned from
 * {@code getUrl()}.
 *
 * <p>The {@link com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.url.UrlAdapter} is the
 * corresponding {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor Accessor}
 * implementation that adapts objects implementing {@code UrlAddressable}.
 *
 * <p>This adapter does not perform adaptation for any Bukkit-provided types by default. Only objects explicitly
 * implementing the {@code UrlAddressable} interface will be adapted.
 *
 * @see com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.url.UrlAddressable
 * @see com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.url.UrlAdapter
 */
package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.url;
