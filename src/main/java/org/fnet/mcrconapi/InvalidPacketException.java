/*******************************************************************************
 * MIT License
 *
 * Copyright (c) 2017 fnetworks
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
package org.fnet.mcrconapi;

import java.io.IOException;

import org.fnet.mcrconapi.packet.Packet;

/**
 * Thrown when a packet is sent that shouldn't be there, e.g. the server sends a
 * packet that violates the protocol
 */
public class InvalidPacketException extends IOException {

	private static final long serialVersionUID = 7359011692721587596L;
	private Packet invalidPacket;

	/**
	 * Constructs a new InvalidPacketException, given a message and the invalid
	 * packet
	 * 
	 * @param message
	 *            the message to show
	 * @param invalidPacket
	 *            the packet that was invalid
	 */
	public InvalidPacketException(String message, Packet invalidPacket) {
		super(message);
		this.invalidPacket = invalidPacket;
	}

	/**
	 * Returns the packet that was considered invalid
	 * 
	 * @return the invalid {@link Packet}
	 */
	public Packet getInvalidPacket() {
		return invalidPacket;
	}

}
