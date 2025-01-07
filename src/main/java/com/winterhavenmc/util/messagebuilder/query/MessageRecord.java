/*
 * Copyright (c) 2024 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.query;


public record MessageRecord(
		String messageKey,
		boolean enabled,
		String message,
		long repeatDelay,
		String title,
		int titleFadeIn,
		int titleStay,
		int titleFadeOut,
		String subtitle) {


	/**
	 * Enum of MessageRecord fields and their corresponding keyPath. This enum is the source of truth for
	 * message record field constants and their corresponding keyPaths. Other field metadata may be
	 * encapsulated in this enum in the future.
	 */
	enum Field {
		ENABLED("enabled"),
		MESSAGE("message"),
		REPEAT_DELAY("repeat-delay"),
		TITLE("title"),
		TITLE_FADE_IN("title-fade-in"),
		TITLE_STAY("title-stay"),
		TITLE_FADE_OUT("title-fade-out"),
		SUBTITLE("subtitle"),
		;

		Field(final String key) {
			this.key = key;
		} // class constructor
		private final String key; // key field
		public String toKey() {
			return key;
		} // getter for key
	}

	// constant for messages section top level key
	private final static String MESSAGE_SECTION = "MESSAGES";


	/**
	 * A static method to retrieve a message record.
	 *
	 * @param messageId the {@link MessageId} for the message to be retrieved from the language file
	 * @param messageSection the message section containing the messages
	 * @return a MessageRecord if an entry could be found for the {@code MessageId}, otherwise an empty Optional.
	 * @param <MessageId> an enum constant that serves as a key to a message entry in the language file
	 */
	public static // scope
	<MessageId extends Enum<MessageId>> // parameter type
	Optional<MessageRecord> // return type
	getMessageRecord(final MessageId messageId, final ConfigurationSection messageSection) {
		if (messageId == null) { throw new IllegalArgumentException(Error.Parameter.NULL_MESSAGE_ID.getMessage()); }
		if (messageSection == null) { throw new IllegalArgumentException(Error.Parameter.NULL_SECTION_MESSAGES.getMessage()); }


		// check if messageSection is MESSAGES section of configuration
		if (messageSection.getName().equals(MESSAGE_SECTION)) {
			throw new IllegalArgumentException("The configuration section is NOT the MESSAGES section of the language configuration!");
		}

		// get entry for messageId
		ConfigurationSection messageEntry = messageSection.getConfigurationSection(messageId.toString());
		if (messageEntry == null) { return Optional.empty(); }

		return Optional.of(new MessageRecord(messageId.toString(),
				messageEntry.getBoolean(MessageRecord.Field.ENABLED.toKey()),
				messageEntry.getString(MessageRecord.Field.MESSAGE.toKey()),
				messageEntry.getLong(MessageRecord.Field.REPEAT_DELAY.toKey()),
				messageEntry.getString(MessageRecord.Field.TITLE.toKey()),
				messageEntry.getInt(MessageRecord.Field.TITLE_FADE_IN.toKey()),
				messageEntry.getInt(MessageRecord.Field.TITLE_STAY.toKey()),
				messageEntry.getInt(MessageRecord.Field.TITLE_FADE_OUT.toKey()),
				messageEntry.getString(MessageRecord.Field.SUBTITLE.toKey())));
	}

}
