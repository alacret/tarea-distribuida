package mgs.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import mgs.client.actions.ClientAction;
import mgs.client.actions.ClientActionFactory;
import mgs.utils.XMLParser;

import org.xml.sax.SAXException;

public class ClientResponse extends Thread {
	public Socket socket;
	private Client client;

	public ClientResponse(Socket socket, Client client) {
		this.socket = socket;
		this.client = client;
	}

	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String readLine = reader.readLine();
			System.out.println(readLine);
			if (readLine != null)
				parseReceivedText(readLine, socket);
		} catch (IOException e) {
			client.flush(e.getMessage());
		} catch (ParserConfigurationException e) {
			client.flush(e.getMessage());
		} catch (SAXException e) {
			client.flush(e.getMessage());
		}
	}

	private void parseReceivedText(String readLine, Socket socket)
			throws ParserConfigurationException, SAXException, IOException {

		Map<String, String> params = XMLParser.parse(readLine, client);

		ClientAction action = ClientActionFactory.getAction(params
				.get("action"));
		action.set(params, socket);
		action.doAction(client);

	}
}