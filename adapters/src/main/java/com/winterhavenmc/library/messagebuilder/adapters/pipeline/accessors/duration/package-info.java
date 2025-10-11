/**
 * Provides an adapter for objects that expose a {@link java.time.Duration}.
 *
 * <p>This package defines the {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.duration.Durationable}
 * interface, which should be implemented by any object that can expose a duration value for use in macro substitution.
 * Macros of the form {@code {OBJECT.DURATION}} are supported by extracting and formatting the value returned from
 * {@code getDuration()}.
 *
 * <p>The {@link com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.duration.DurationAdapter} is the
 * corresponding {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.Accessor Accessor}
 * implementation that adapts objects implementing {@code Durationable}.
 *
 * <p>Formatting of duration values is handled by the
 * {@link com.winterhavenmc.library.messagebuilder.core.ports.pipeline.formatters.duration.DurationFormatter DurationFormatter}
 * chain, allowing for localized and human-readable output.
 *
 * <p>This adapter does not perform adaptation for any Bukkit-provided types by default. Only objects explicitly
 * implementing the {@code Durationable} interface will be adapted.
 *
 * @see com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.duration.Durationable
 * @see com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.duration.DurationAdapter
 */
package com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.duration;
