package mgs.client;

import java.io.PrintStream;

final public class ClientFactory {

	private static final int DEFAULT_PORT = 8000;

	public static Client getClient(String serverIp, int serverSocket,
			int clientSocket, PrintStream out) {
		return new Client(serverIp, serverSocket, clientSocket, out);
	}

	public static Client getStandardClient(String serverIp, int clientSocket) {
		return new Client(serverIp, DEFAULT_PORT, clientSocket, System.out);
	}

}
