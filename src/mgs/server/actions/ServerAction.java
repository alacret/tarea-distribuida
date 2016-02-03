package mgs.server.actions;

import java.net.Socket;
import java.util.Map;

import mgs.server.Server;

public interface ServerAction {

	public boolean doAction(Server server);

	public void set(Map<String, String> params, Socket socket);
}
