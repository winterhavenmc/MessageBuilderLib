package com.winterhavenmc.util.messagebuilder.messages;


import com.winterhavenmc.util.messagebuilder.util.MessageIdInterface;

/**
 * An enum whose values represent the text messages displayed to users, corresponding to user
 * configured text in the language yaml files.
 */
public enum MessageId implements MessageIdInterface {

	ENABLED_MESSAGE,
	DISABLED_MESSAGE,
	REPEAT_DELAYED_MESSAGE,
	ENABLED_TITLE,
	DISABLED_TITLE,
	ENABLED_SUBTITLE,
	DISABLED_SUBTITLE,
	CUSTOM_FADE_TITLE,
	NON_INT_TITLE_FADE_VALUES,
	DURATION_MESSAGE,

	NONEXISTENT_ENTRY,
	UNDEFINED_FIELD_ENABLED,
	UNDEFINED_FIELD_MESSAGE,
	UNDEFINED_FIELD_REPEAT_DELAY,
	UNDEFINED_FIELD_TITLE,
	UNDEFINED_FIELD_TITLE_FADE_IN,
	UNDEFINED_FIELD_TITLE_STAY,
	UNDEFINED_FIELD_TITLE_FADE_OUT,
	UNDEFINED_FIELD_SUBTITLE,
}
