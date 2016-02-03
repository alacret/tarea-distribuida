package mgs.client.actions;

import java.net.Socket;
import java.util.Map;

import mgs.client.Client;

public interface ClientAction {

	public void set(Map<String, String> params, Socket socket);

	public boolean doAction(Client client);
}
