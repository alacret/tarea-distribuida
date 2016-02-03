package mgs.client.actions;

import java.net.Socket;
import java.util.Map;

import mgs.client.Client;
import mgs.server.GroupMember;

public class AddGroupMember implements ClientAction {

	private Map<String, String> params;

	@Override
	public void set(Map<String, String> params, Socket socket) {
		this.params = params;
	}

	@Override
	public boolean doAction(Client client) {
		int port = Integer.valueOf(params.get("listenPort"));
		String ip = params.get("ip");
		System.out.println(ip + ":" + port);
		client.addPeer(new GroupMember(ip, port));
		return true;
	}

}
