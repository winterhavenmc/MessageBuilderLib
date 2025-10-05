/**
 * Contains interfaces and classes responsible for replacing macros in message strings
 * using resolved values from a {@link com.winterhavenmc.library.messagebuilder.core.maps.MacroObjectMap}.
 *
 * <p>This package represents the final stage in the message-building pipeline,
 * where macros such as {@code %PLAYER_NAME%} or {@code [LOCATION.X}} are substituted
 * with their corresponding string values.
 *
 * <h2>Key Components</h2>
 * <ul>
 *   <li>{@link com.winterhavenmc.library.messagebuilder.core.ports.replacers.MacroReplacer} –
 *       The abstraction for any macro replacement engine.</li>
 *   <li>{@code MacroReplacer} –
 *       The default implementation that uses regex-based detection and a {@code ValueResolver}
 *       to perform context-aware substitution.</li>
 * </ul>
 *
 * <p>Macro resolution depends on objects provided earlier in the pipeline via
 * {@code Message.setMacro(Enum, Object)}.
 * These objects are transformed into strings by the {@link com.winterhavenmc.library.messagebuilder.core.ports.resolvers.macro.ValueResolver}
 * system before replacement occurs.
 *
 * <p>Unresolvable macros are left untouched in the final message, ensuring robustness
 * and avoiding runtime failures due to incomplete context.
 *
 * @see com.winterhavenmc.library.messagebuilder.core.ports.replacers.MacroReplacer MacroReplacer
 * @see com.winterhavenmc.library.messagebuilder.core.ports.resolvers.macro.ValueResolver ValueResolver
 * @see com.winterhavenmc.library.messagebuilder.core.maps.MacroObjectMap MacroObjectMap
 */
package com.winterhavenmc.library.messagebuilder.core.ports.replacers;
