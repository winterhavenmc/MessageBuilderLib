/**
 * Provides an interface for objects that expose a version number as a string.
 *
 * <p>This package defines the {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.version.Versionable}
 * interface, which should be implemented by any object that can expose a url string value for use in macro substitution.
 * Macros of the form {@code {OBJECT.URL}} are supported by extracting and formatting the value returned from
 * {@code getUrl()}.
 *
 * <p>The {@code VersionAdapter} is the corresponding
 * {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor Accessor}
 * implementation that adapts objects implementing {@code Versionable}.
 *
 * @see com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.version.Versionable
 */
package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.version;
