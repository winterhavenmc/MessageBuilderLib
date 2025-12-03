/**
 * Provides an adapter for objects that expose a version number as a string.
 *
 * <p>This package defines the {@link com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.version.VersionAdapter}
 * adapter class, which implements a wrapper class that provides a Versionable interface compliant accessor method
 * to retrieve a string version field from the wrapped object.
 * <p>
 * Any class that implements the
 * {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.version.Versionable Versionable}
 * interface by providing a {@code getVersion()} method that returns String will have a VERSION subfield added to
 * the string results map and available as a placeholder replacement in messages.
 * <p>
 * The {@code VersionAdapter} is the
 * corresponding {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor Accessor}
 * implementation that adapts objects implementing {@code Versionable}.
 *
 * @see com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.version.Versionable
 * @see com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.version.VersionAdapter
 */
package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.version;
