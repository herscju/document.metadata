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
package info.hersche.document.metadata.processor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import info.hersche.document.metadata.processor.Mp4Writer.BetterByteArrayOutputStream;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;

/**
 * @author herscju
 */
public class Mp4Processor implements Mp4Processable
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Mp4Processor.class);

	/**
	 * 
	 */
	private InputStream source;

	/**
	 * 
	 */
	@Setter
	@NotNull
	private Long sizeBefore;
	@Setter
	@NotNull
	private Long sizeAfter;
	@Setter
	@NotNull
	private Long offset;

	/**
	 * Constructor
	 * 
	 * @param source
	 */
	public Mp4Processor(@NonNull InputStream source)
	{
		this.source = source;

		LOGGER.info("Class '{}' instantiated successfully.", Mp4Processor.class.getSimpleName());
	}

	/**
	 * @throws IOException
	 *
	 */
	@Override
	public void process(@NotNull Long diff, @NonNull BetterByteArrayOutputStream target) throws IOException
	{
		if (diff.longValue() != 0)
		{
			// This is not good: We have to insert bytes in the middle
			// and this costs time as it requires re-writing most of data
			try (SearchableInMemoryByteChannel channel = this.splitAndInsert(this.offset, diff))
			{
				channel.position(this.offset.longValue());
				channel.write(ByteBuffer.wrap(target.getBuffer(), 0, target.size()));
				target.write(channel.array(), 0, Math.toIntExact(channel.size()));
			}
		}
		else
		{
			// Simple overwrite of something
			try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(IOUtils.toByteArray(this.source)))
			{
				channel.position(this.offset.longValue());
				channel.write(ByteBuffer.wrap(target.getBuffer(), 0, target.size()));
				target.write(channel.array(), 0, Math.toIntExact(channel.size()));
			}
		}
	}

	/**
	 * @param pos
	 * @param length
	 * @return
	 * @throws IOException
	 * 
	 */
	private SearchableInMemoryByteChannel splitAndInsert(Long pos, Long length) throws IOException
	{
		SearchableInMemoryByteChannel write = new SearchableInMemoryByteChannel(this.sizeAfter.intValue());
		try (SearchableInMemoryByteChannel read = new SearchableInMemoryByteChannel(IOUtils.toByteArray(this.source));
				SearchableInMemoryByteChannel transfer = new SearchableInMemoryByteChannel())
		{
//			Long count = 0L;
//
//			// Get head part and transfer it to the output channel
//			read.position(0);
//			count = this.offset;
//			ByteBuffer writeBuffer = ByteBuffer.wrap(read.array(), 0, count.intValue());
//			write.write(writeBuffer);
//
//			// Get tail part and transfer it also to the end of the output channel
//			read.position(pos.longValue());
//			count = Long.valueOf(Math.subtractExact(read.size(), this.offset.longValue()));
//			ByteBuffer readBuffer = ByteBuffer.wrap(read.array(), pos.intValue(), count.intValue());
//			transfer.write(readBuffer);
//
//			Long position = Long.valueOf(Long.sum(this.offset.longValue(), length.longValue()));
//			write.position(position.longValue());
//			transfer.position(0);
//
//			Long transferred = 0L;
//			while ((transferred = Long.sum(transferred.longValue(), this.transferTo(transferred, transfer, write))) != transfer.size())
//			{
//				LOGGER.trace("'{}' Bytes transfered.", transferred);
//			}
//
//			LOGGER.trace("'{}' Bytes transfered totally.", transferred);
			
			read.position(pos);
			Long count = read.size() - pos;
			Long t1 = transfer.transferFrom(read, 0, count);
			LOGGER.info(">>>  {}", t1);
			LOGGER.info(">>>  {}", transfer.array());
		}

		return write;
	}

	/**
	 * @param transferred
	 * @param write
	 * @param transfer
	 * @return
	 * @throws IOException
	 */
	private Long transferTo(Long transferred, SeekableInMemoryByteChannel transfer, SeekableInMemoryByteChannel write) throws IOException
	{
		int size = Math.toIntExact(transfer.size());
		int length = size - transferred.intValue();
		ByteBuffer transferBuffer = ByteBuffer.wrap(transfer.array(), 0, length);
		Long value = Long.valueOf(Long.sum(transferred.longValue(), Long.valueOf(write.write(transferBuffer)).longValue()));

		return value;
	}
}
