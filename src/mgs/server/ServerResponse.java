package mgs.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import mgs.server.actions.ServerAction;
import mgs.server.actions.ServerActionFactory;
import mgs.utils.XMLParser;

import org.xml.sax.SAXException;

public class ServerResponse extends Thread {
	public Socket socket;
	private Server server;

	public ServerResponse(Socket socket, Server serverFactory) {
		this.socket = socket;
		this.server = serverFactory;
	}

	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String readLine = reader.readLine();
			if (readLine != null)
				parseReceivedText(readLine, socket);
		} catch (IOException e) {
			server.flush(e.getMessage());
		} catch (ParserConfigurationException e) {
			server.flush(e.getMessage());
		} catch (SAXException e) {
			server.flush(e.getMessage());
		}
	}

	private void parseReceivedText(String readLine, Socket socket)
			throws ParserConfigurationException, SAXException, IOException {

		Map<String, String> params = XMLParser.parse(readLine, server);

		if (params == null)
			return;

		ServerAction action = ServerActionFactory.getAction(params
				.get("action"));
		action.set(params, socket);
		action.doAction(server);

	}

}