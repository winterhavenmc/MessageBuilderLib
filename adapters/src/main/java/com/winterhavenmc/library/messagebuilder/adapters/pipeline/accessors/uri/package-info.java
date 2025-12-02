/**
 * Provides an adapter for objects that expose a {@link com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.uri uri}.
 *
 * <p>This package defines the {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.uri.UriAddressable}
 * interface, which should be implemented by any object that can expose a uri string value for use in macro substitution.
 * Macros of the form {@code {OBJECT.URL}} are supported by extracting and formatting the value returned from
 * {@code getUri()}.
 *
 * <p>The {@link com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.uri.UriAdapter} is the
 * corresponding {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor Accessor}
 * implementation that adapts objects implementing {@code UriAddressable}.
 *
 * @see com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.uri.UriAddressable
 * @see com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.uri.UriAdapter
 */
package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.uri;
