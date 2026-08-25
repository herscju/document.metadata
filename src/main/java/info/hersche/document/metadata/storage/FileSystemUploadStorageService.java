/**
 * Copyright (c) 2024 Jürg Hersche (Green@rt)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package info.hersche.document.metadata.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.apache.commons.io.FileDeleteStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 */
@Service
public class FileSystemUploadStorageService implements StorageServiceable
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemUploadStorageService.class);

	/**
	 * 
	 */
	private final Path directory;

	/**
	 * Constructor
	 * 
	 * @param storageProperties
	 */
	public FileSystemUploadStorageService(UploadStorageProperties storageProperties)
	{
		if (storageProperties.directory().trim().length() == 0)
		{
			throw new StorageException("Storage location can not be empty.");
		}

		this.directory = Paths.get(storageProperties.directory());
	}

	/**
	 *
	 */
	@Override
	public void delete(String filename)
	{
		if (filename.isEmpty())
		{
			throw new StorageException("Failed to store empty file.");
		}

		Path target = this.resolvePath(filename);
		if (!target.getParent().equals(this.directory.toAbsolutePath()))
		{
			// This is a security check
			throw new StorageException("Cannot store file outside current location.");
		}

		try
		{
			FileDeleteStrategy.FORCE.delete(target.toFile());
			LOGGER.trace("File '{}' removed successfully from directory '{}'.", filename, target);
		}
		catch (IOException e)
		{
			LOGGER.error("Couldn't open or delete file!", e);
		}

	}

	/**
	 *
	 */
	@Override
	public void deleteAll()
	{
		FileSystemUtils.deleteRecursively(this.directory.toFile());
	}

	/**
	 *
	 */
	@Override
	public void init()
	{
		try
		{
			Files.createDirectories(this.directory);
			LOGGER.trace("Storage location '{}' created successfully.", this.directory);
		}
		catch (IOException e)
		{
			throw new StorageException("Could not initialize storage.", e);
		}
	}

	/**
	 *
	 */
	@Override
	public Path load(String filename)
	{
		return this.directory.resolve(filename);
	}

	/**
	 *
	 */
	@Override
	public Stream<Path> loadAll()
	{
		try
		{
			return Files.walk(this.directory, 1).filter(path -> !path.equals(this.directory)).map(this.directory::relativize);
		}
		catch (IOException e)
		{
			throw new StorageException("Failed to read stored files.", e);
		}

	}

	/**
	 *
	 */
	@Override
	public Resource loadAsResource(String filename)
	{
		try
		{
			Path file = this.load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable())
			{
				return resource;
			}
			else
			{
				throw new StorageFileNotFoundException("Could not read stored file: " + filename + ".");

			}
		}
		catch (MalformedURLException e)
		{
			throw new StorageFileNotFoundException("Could not read stored file: " + filename + ".", e);
		}
	}

	/**
	 * @param filename
	 * @return
	 */
	private Path resolvePath(String filename)
	{
		return this.directory.resolve(Paths.get(filename)).normalize().toAbsolutePath();
	}

	/**
	 *
	 */
	@Override
	public void store(MultipartFile file)
	{
		try
		{
			if (file.isEmpty())
			{
				throw new StorageException("Failed to store empty file.");
			}

			Path target = this.resolvePath(file.getOriginalFilename());
			if (!target.getParent().equals(this.directory.toAbsolutePath()))
			{
				// This is a security check
				throw new StorageException("Cannot store file outside current location.");
			}

			/**
			 * Create a new image on the given target destination
			 */
			try (InputStream inputStream = file.getInputStream())
			{
				Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e)
		{
			throw new StorageException("Failed to store file.", e);
		}
	}
}
