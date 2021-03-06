/*
 * Copyright (c) 2021 Felix Solcher
 * Licensed under the terms of the MIT license.
 */
package org.fnet.mcrconapi;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.fnet.mcrconapi.packet.ClientPacket;
import org.fnet.mcrconapi.packet.Packet;
import org.fnet.mcrconapi.packet.PacketType;
import org.fnet.mcrconapi.packet.ServerPacket;

final class RConTestServer implements Runnable, AutoCloseable {

	private final ServerSocket server;
	private final Thread serverThread;
	private final List<Thread> clientThreads;
	private final List<Socket> clientSockets;
	private final int port;

	public RConTestServer() throws IOException {
		serverThread = new Thread(this);
		server = new ServerSocket();
		server.bind(null);
		this.port = server.getLocalPort();
		clientThreads = new ArrayList<>();
		clientSockets = new ArrayList<>();
	}

	@Override
	public void run() {
		try {
			while (server.isBound()) {
				Socket client = server.accept();
				clientSockets.add(client);
				Thread clientThread = new Thread(() -> {
					try {
						while (client.isConnected()) {
							Packet packet = new ClientPacket(client.getInputStream());
							switch (packet.getType()) {
							case AUTH:
								Packet response = new ServerPacket(PacketType.AUTH_RESPONSE, "");
								if (packet.getPayloadAsString().equals(RConClientTest.PASSWORD)) {
									response.setRequestID(packet.getRequestID());
								} else {
									response.setRequestID(-1);
								}
								response.writeTo(client.getOutputStream());
								break;
							case COMMAND:
								if (packet.getPayloadAsString().equals(RConClientTest.SHORT_COMMAND_REQUEST)) {
									Packet crsp = new ServerPacket(PacketType.COMMAND_RESPONSE,
											RConClientTest.SHORT_COMMAND_RESPONSE);
									crsp.writeTo(client.getOutputStream());
								} else {
									Packet crsp = new ServerPacket(PacketType.COMMAND_RESPONSE, "");
									crsp.writeTo(client.getOutputStream());
								}
								break;
							default:
								System.err.println("INVALID REQUEST TYPE " + packet.getType());
								Packet errrsp = new ServerPacket(PacketType.AUTH_RESPONSE, "");
								errrsp.setRequestID(Packet.REQUEST_ID_AUTH_FAIL);
								errrsp.writeTo(client.getOutputStream());
								break;
							}
						}
					} catch (EOFException e) {
						// Ignore: this means that client closed connection
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				clientThread.start();
			}
		} catch (SocketException e) {
			// Ignore: this means that the server socket was closed
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		serverThread.start();
	}

	@Override
	public void close() throws Exception {
		for (Socket s : clientSockets)
			if (!s.isClosed())
				s.close();
		for (Thread t : clientThreads)
			if (t.isAlive())
				t.join();
		if (!server.isClosed())
			server.close();
		serverThread.join();
	}

	public int getPort() {
		return port;
	}
}