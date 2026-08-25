/**
 * Copyright (c) 2024 - 2026 Jürg Hersche (Green@rt)
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
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NonReadableChannelException;
import java.nio.channels.NonWritableChannelException;

import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;

/**
 * 
 */
public class SearchableInMemoryByteChannel extends SeekableInMemoryByteChannel
{
	// Maximum size to map when using a mapped buffer
	private static final long MAPPED_TRANSFER_SIZE = 8L * 1024L * 1024L;
	private static final int TRANSFER_SIZE = 8192;

	/**
	 * Constructs a new instance using a default empty buffer.
	 */
	public SearchableInMemoryByteChannel()
	{
		super();
	}

	/**
	 * Constructs a new instance from a byte array.
	 * 
	 * This constructor is intended to be used with pre-allocated buffer or when reading from a given byte array.
	 * 
	 *
	 * @param data
	 *            input data or pre-allocated array.
	 */
	public SearchableInMemoryByteChannel(final byte[] data)
	{
		super(data);
	}

	/**
	 * Constructs a new instance from a size of storage to be allocated.
	 * 
	 * Creates a channel and allocates internal storage of a given size.
	 * 
	 *
	 * @param size
	 *            size of internal buffer to allocate, in bytes.
	 */
	public SearchableInMemoryByteChannel(final int size)
	{
		this(new byte[size]);
	}

	/**
	 * Transfers bytes into this channel from the given readable byte
	 * channel.
	 *
	 * An attempt is made to read up to {@code count} bytes from the
	 * source channel and write them to this channel's file starting at the
	 * given {@code position}. An invocation of this method may or may not
	 * transfer all of the requested bytes; whether or not it does so depends
	 * upon the natures and states of the channels. Fewer than the requested
	 * number of bytes will be transferred if the source channel has fewer than
	 * {@code count} bytes remaining, or if the source channel is non-blocking
	 * and has fewer than {@code count} bytes immediately available in its
	 * input buffer. No bytes are transferred, and zero is returned, if the
	 * source has reached end-of-stream.
	 *
	 * This method does not modify this channel's position. If the given
	 * position is greater than the file's current size then no bytes are
	 * transferred. If the source channel has a position then bytes are read
	 * starting at that position and then the position is incremented by the
	 * number of bytes read.
	 *
	 * @param src
	 *            The source channel
	 *
	 * @param position
	 *            The position within the file at which the transfer is to begin;
	 *            must be non-negative
	 *
	 * @param count
	 *            The maximum number of bytes to be transferred; must be
	 *            non-negative
	 *
	 * @return The number of bytes, possibly zero,
	 *         that were actually transferred
	 *
	 * @throws IllegalArgumentException
	 *             If the preconditions on the parameters do not hold
	 *
	 * @throws NonReadableChannelException
	 *             If the source channel was not opened for reading
	 *
	 * @throws NonWritableChannelException
	 *             If this channel was not opened for writing
	 *
	 * @throws ClosedChannelException
	 *             If either this channel or the source channel is closed
	 *
	 * @throws AsynchronousCloseException
	 *             If another thread closes either channel
	 *             while the transfer is in progress
	 *
	 * @throws ClosedByInterruptException
	 *             If another thread interrupts the current thread while the
	 *             transfer is in progress, thereby closing both channels and
	 *             setting the current thread's interrupt status
	 *
	 * @throws IOException
	 *             If some other I/O error occurs
	 */
	public Long transferFrom(SearchableInMemoryByteChannel src, int position, Long count) throws IOException
	{
		if ((position < 0) || (count < 0))
		{
			throw new IllegalArgumentException();
		}

		if (position > this.size())
		{
			return Long.valueOf(0L);
		}

		long n = this.transferFromSource(src, position, count);

		if (n >= 0)
		{
			return n;
		}

		return transferFromArbitraryChannel(src, position, count);
	}

	/**
	 * Based on method 'transferFromArbitraryChannel' of class 'FileChannelImpl'
	 * 
	 * @param src
	 * @param position
	 * @param count
	 * @return
	 * @throws IOException
	 */
	private Long transferFromArbitraryChannel(SearchableInMemoryByteChannel src, int position, Long count) throws IOException
	{
		// Untrusted target: Use a newly-erased buffer
		int c = (int) Math.min(count, TRANSFER_SIZE);
		ByteBuffer bb = ByteBuffer.allocate(c);
		long tw = 0; // Total bytes written
		long pos = position;

		try
		{
			while (tw < count)
			{
				bb.limit((int) Math.min((count - tw), (long) TRANSFER_SIZE));
				// ## Bug: Will block reading src if this channel
				// ## is asynchronously closed
				int nr = src.read(bb);
				if (nr <= 0)
				{
					break;
				}
				bb.flip();
				int nw = this.write(bb, pos);
				tw += nw;
				if (nw != nr)
				{
					break;
				}
				pos += nw;
				bb.clear();
			}

			return tw;
		}
		catch (IOException e)
		{
			if (tw > 0)
			{
				return tw;
			}

			throw e;
		}
	}

	/**
	 * Based on method 'transferFromFileChannel' of class 'FileChannelImpl'
	 * 
	 * @param src
	 * @param position
	 * @param count
	 * @return
	 * @throws IOException
	 */
	private long transferFromSource(SearchableInMemoryByteChannel src, int position, Long count) throws IOException
	{
		long pos = src.position();
		long max = Math.min(count.longValue(), src.size() - pos);

		long remaining = max;
		long p = pos;

		while (remaining > 0L)
		{
			long size = Math.min(remaining, MAPPED_TRANSFER_SIZE);
			ByteBuffer buffer = ByteBuffer.allocate(4);
			int n = src.read(buffer);
			
			p += n;
			position += n;
			remaining -= n;
		}

		long nwritten = max - remaining;
		src.position(pos + nwritten);

		return nwritten;
	}

	/**
	 * @param bb
	 * @param pos
	 * @return
	 */
	private int write(ByteBuffer bb, long pos)
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
