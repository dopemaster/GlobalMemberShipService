package edu.illinois.cs425.mp2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class MulticastServerThread extends Thread {
	private final MulticastServer multicastServer;

	public MulticastServerThread(MulticastServer multicastServer)
			throws IOException {
		super("MulticastServerThread");
		this.multicastServer = multicastServer;
	}

	@Override
	public void run() {
		DatagramPacket packet;
		Message message;
		MulticastSocket socket = multicastServer.getMulticastListenerSocket();
		boolean keepListening = true;
		while (keepListening) {
			byte receiveMessage[] = new byte[Message.MAX_MESSAGE_LENGTH];
			try {
				packet = new DatagramPacket(receiveMessage,
						receiveMessage.length);
				socket.receive(packet);
				ByteArrayInputStream bis = new ByteArrayInputStream(
						packet.getData());
				ObjectInputStream in = null;
				in = new ObjectInputStream(bis);
				message = (Message) in.readObject();
				multicastServer.getServer().getLogger()
						.info(message.getDescription());
				message.processMessage();

			} catch (IOException e) {

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
