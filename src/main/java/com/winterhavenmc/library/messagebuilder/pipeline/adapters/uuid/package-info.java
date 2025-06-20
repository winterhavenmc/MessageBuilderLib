/**
 * Provides support for macro replacement of UUID values from objects.
 *
 * <p>This package contains the {@link com.winterhavenmc.library.messagebuilder.pipeline.adapters.uuid.Identifiable}
 * interface and its corresponding adapter {@link com.winterhavenmc.library.messagebuilder.pipeline.adapters.uuid.UniqueIdAdapter}.
 * These components enable automatic extraction and macro substitution of UUIDs for objects such as:
 * <ul>
 *     <li>Entities</li>
 *     <li>Offline players</li>
 *     <li>Player profiles</li>
 *     <li>Worlds</li>
 *     <li>Plugin-defined types implementing {@code Identifiable}</li>
 * </ul>
 *
 * <p>When a match is found, the object's UUID will be formatted as a string and placed into the macro
 * map under the {@code {OBJECT.UUID}} placeholder.</p>
 */
package com.winterhavenmc.library.messagebuilder.pipeline.adapters.uuid;
