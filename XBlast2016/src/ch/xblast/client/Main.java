package ch.xblast.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Main {
	
	private static int MAX_BUFFER_SIZE = 409;
	public static void main(String[] args) throws IOException {
		String hostName = (args.length <= 0) ? "localhost" : args[0];

		DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET);
		SocketAddress hostAdress = new InetSocketAddress(hostName, 2016);
		
		ByteBuffer buffer = ByteBuffer.allocate(MAX_BUFFER_SIZE);
		
		
		

	}

}
