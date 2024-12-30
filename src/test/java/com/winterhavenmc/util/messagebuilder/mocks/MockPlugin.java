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

package com.winterhavenmc.util.messagebuilder.mocks;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class MockPlugin {

	private final static File DATA_DIR;
	public final static String LANGUAGE_EN_US_YML = "language/en-US.yml";
	public final static String AUTO_INSTALL_TXT = "language/auto_install.txt";

	// create a temporary data directory for the mock plugin
	static {
		try {
			DATA_DIR = createTempDataDir();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	public static void setup() { }


	/**
	 * Getter for temporary data directory
	 * @return {@code File} the temporary data directory
	 */
	public static File getDataFolder() {
		return DATA_DIR;
	}


	/**
	 * Create a temporary folder
	 * @throws IOException if directory cannot be created
	 */
	private static File createTempDataDir() throws IOException {
		String tempDataDirectoryPath = Files.createTempDirectory("PluginData").toFile().getAbsolutePath();
		File tempDir = new File(tempDataDirectoryPath);
		@SuppressWarnings("unused") boolean success = tempDir.mkdirs();
		if (!tempDir.isDirectory()) {
			throw new IOException("the temporary directory could not be created.");
		} else {
			tempDir.deleteOnExit();
		}
		return tempDir;
	}


	public static InputStream getResourceStream(final String name) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
	}

	public static File getResourceFile(final String name) throws URISyntaxException {
		return Paths.get(Thread.currentThread().getContextClassLoader().getResource(name).toURI()).toAbsolutePath().toFile();
	}


	@SuppressWarnings("UnusedReturnValue")
	public static boolean installResource(final String name) throws IOException {

		boolean success = false;

		InputStream inputStream = MockPlugin.getResourceStream(name);

		if (inputStream == null) {
			throw new IOException("InputStream for '" + name + "' resource is null.");
		}

		// create subdirectory if it doesn't already exist
		try {
			createSubdirectory(name);
		}
		catch (FileAlreadyExistsException exception) {
			Logger.getLogger("MockPlugin").warning("The subdirectory '" + name + "' could not be created because it already exists.");
		}

		long bytesCopied = Files.copy(inputStream, Paths.get(DATA_DIR.getPath(), name));
		if (bytesCopied > 0) {
			success = true;
		}

		return success;
	}


	/**
	 * create a directory in the temporary plugin data directory with the given name
	 *
	 * @param name the name for the directory
	 * @return {@code true} if the directory was successfully created, {@code false) if it was not
	 * @throws IOException if an error occurs when creating the directory
	 */
	private static boolean createDirectory(final String name) throws IOException {
		File directory = new File(DATA_DIR, name);
		boolean success = directory.mkdirs();
		if (!directory.isDirectory()) {
			throw new IOException("the directory could not be created.");
		}
		return success;
	}


	/**
	 * Create a temporary folder
	 * @throws IOException if directory cannot be created
	 */
	@SuppressWarnings("UnusedReturnValue")
	private static File createSubdirectory(String name) throws IOException {

		Path parent = Paths.get(name).getParent();

		File subdirectory = new File(DATA_DIR, parent.toString());

		System.out.println(subdirectory);

		@SuppressWarnings("unused") boolean success = subdirectory.mkdirs();
		if (!subdirectory.isDirectory()) {
			throw new IOException();
		}
		return subdirectory;
	}


	public static void verifyTempDir() {
		System.out.println("Temporary plugin data directory successfully created: " + DATA_DIR.isDirectory());
	}

	public static void verifyLangDir() {
		System.out.println("Language directory successfully created: " + new File(getDataFolder(), "language").isDirectory());
	}

}
