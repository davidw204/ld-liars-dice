/*
 * Copyright (C) 2002,2003,2004,2005,2006 Michal Å evÄ?enko
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * 
 * 
 * The KSMSA Project, http://virtual.cvut.cz/ksmsaWeb/main?page=about
 * Sub-project utils: miscellaneous utilities.
 * 
 * Version: 1.0.10 beta
 * File: org/ksmsa/utils/io/ByteBuilder.java
 * Release Date: 10 Jan, 2007 19:31:48
 * 
 */
/*
 * Created on 14.3.2005
 */
package org.ksmsa.utils.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UTFDataFormatException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * A class similar to <code>java.lang.StringBuffer</code> for holding
 * <code>byte</code> s instead of <code>char</code>s.
 * 
 * @author Michal Å evÄ?enko
 * @version 1.0.10 beta
 */
public class ByteBuilder {
	private byte[] buffer;

	private int length;

	public ByteBuilder() {
		allocate(10);
	}

	private void allocate(int capacity) {
		byte[] newBuffer = new byte[capacity];
		if (length > 0)
			System.arraycopy(buffer, 0, newBuffer, 0, length);
		buffer = newBuffer;
	}

	public ByteBuilder append(byte value) {
		ensureCapacity(length + 1);
		buffer[length++] = value;
		return this;
	}

	public ByteBuilder appendByte(int value) {
		if (value < 0)
			value += 128;
		ensureCapacity(length + 1);
		buffer[length++] = (byte) value;
		return this;
	}

	public ByteBuilder append(byte[] array) {
		return append(array, 0, array.length);
	}

	public ByteBuilder append(byte[] array, int len) {
		return append(array, 0, len);
	}

	public ByteBuilder append(byte[] array, int offset, int len) {
		ensureCapacity(length + len);
		System.arraycopy(array, offset, buffer, length, len);
		length += len;
		return this;
	}

	public ByteBuilder overwrite(byte[] array, int dest, int offset, int len) {
		System.arraycopy(array, offset, buffer, dest, len);
		return this;
	}

	public ByteBuilder insert(byte[] array, int dest, int offset, int len) {
		if (length + len > buffer.length) {
			int newCapacity = getNewCapacity(length + len);
			byte[] newBuffer = new byte[newCapacity];
			System.arraycopy(buffer, 0, newBuffer, 0, dest);
			System.arraycopy(array, offset, newBuffer, dest, len);
			System
					.arraycopy(buffer, dest, newBuffer, dest + len, length
							- dest);
			buffer = newBuffer;
		} else {
			System.arraycopy(buffer, dest, buffer, dest + len, length - dest);
			System.arraycopy(array, offset, buffer, dest, len);
		}
		length += len;
		return this;
	}

	public ByteBuilder delete(int startIndex, int endIndex) {
		int gap = endIndex - startIndex;
		System.arraycopy(buffer, endIndex, buffer, startIndex, length
				- endIndex);
		length -= gap;
		return this;
	}

	public ByteBuilder insert(ByteBuilder b, int dest) {
		return insert(b.getBuffer(), dest, 0, b.getLength());
	}

	public ByteBuilder appendUTF(String v) throws UTFDataFormatException, UnsupportedEncodingException {
		//byte[] bytearr = IOUtils.toUtfSmall(v);
		byte[] bytearr = v.getBytes("UTF8");
		return append(bytearr);
	}

	private byte[] writeBuffer = new byte[8];

	public ByteBuilder append(long v) {
		writeBuffer[0] = (byte) ((v >>> 56) & 0xFF);
		writeBuffer[1] = (byte) ((v >>> 48) & 0xFF);
		writeBuffer[2] = (byte) ((v >>> 40) & 0xFF);
		writeBuffer[3] = (byte) ((v >>> 32) & 0xFF);
		writeBuffer[4] = (byte) ((v >>> 24) & 0xFF);
		writeBuffer[5] = (byte) ((v >>> 16) & 0xFF);
		writeBuffer[6] = (byte) ((v >>> 8) & 0xFF);
		writeBuffer[7] = (byte) ((v >>> 0) & 0xFF);
		return append(writeBuffer, 0, 8);
	}

	public ByteBuilder append(int v) {
		writeBuffer[0] = (byte) ((v >>> 24) & 0xFF);
		writeBuffer[1] = (byte) ((v >>> 16) & 0xFF);
		writeBuffer[2] = (byte) ((v >>> 8) & 0xFF);
		writeBuffer[3] = (byte) ((v >>> 0) & 0xFF);
		return append(writeBuffer, 0, 4);
	}

	public ByteBuilder overwrite(int dest, int v) {
		writeBuffer[0] = (byte) ((v >>> 24) & 0xFF);
		writeBuffer[1] = (byte) ((v >>> 16) & 0xFF);
		writeBuffer[2] = (byte) ((v >>> 8) & 0xFF);
		writeBuffer[3] = (byte) ((v >>> 0) & 0xFF);
		return overwrite(writeBuffer, dest, 0, 4);
	}

	private void ensureCapacity(int capacity) {
		if (capacity > buffer.length)
			allocate(getNewCapacity(capacity));
	}

	private int getNewCapacity(int capacity) {
		return Math.max(capacity, length * 2 + 1);
	}

	public int getLength() {
		return length;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public ByteBuffer asByteBuffer() {
		return asByteBuffer(0);
	}

	public ByteBuffer asByteBuffer(int start) {
		return ByteBuffer.wrap(buffer, start, length);
	}

	public byte get(int index) {
		if (index < 0 || index >= length)
			throw new IndexOutOfBoundsException();
		return buffer[index];
	}

	public OutputStream getOutputStream() {
		return new OutputStream() {
			public void write(int b) throws IOException {
				append((byte) b);
			}

			public void write(byte[] b, int off, int len) throws IOException {
				append(b, off, len);
			}
		};
	}
	
	public byte[] getBytes() {
		ByteBuffer bf = asByteBuffer();
		byte[] b = new byte[1];
		if (bf.hasArray()) {
			b = new byte[bf.remaining()];
			bf.get(b);
		}
		return b;
	}
}