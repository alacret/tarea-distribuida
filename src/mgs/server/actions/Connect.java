package mgs.server.actions;

import java.net.Socket;
import java.util.Map;

import mgs.server.GroupMember;
import mgs.server.Server;

public class Connect implements ServerAction {

	private Map<String, String> params;
	private Socket socket;

	@Override
	public boolean doAction(Server server) {
		String ip = socket.getInetAddress().getHostAddress();
		int clientPort = Integer.valueOf(params.get("listenPort"));
		String group = params.get("group");
		server.addMember(group, new GroupMember(ip, clientPort));
		return true;
	}

	@Override
	public void set(Map<String, String> params, Socket socket) {
		this.params = params;
		this.socket = socket;
	}

}
