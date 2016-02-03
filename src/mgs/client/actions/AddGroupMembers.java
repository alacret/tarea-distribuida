package mgs.client.actions;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mgs.client.Client;
import mgs.server.GroupMember;

public class AddGroupMembers implements ClientAction {
	private Map<String, String> params;

	@Override
	public void set(Map<String, String> params, Socket socket) {
		this.params = params;

	}

	@Override
	public boolean doAction(Client client) {

		String peersStr = params.get("members");

		String[] peersArray = peersStr.split("http://");

		if (peersArray.length > 0) {
			List<GroupMember> peers = new ArrayList<GroupMember>();

			for (int i = 1; i < peersArray.length; i++) {
				String[] split = peersArray[i].split(":");
				String ip = split[0];
				int port = Integer.valueOf(split[1]);
				System.out.println(ip+":"+port);
				peers.add(new GroupMember(ip, port));
			}

			client.addPeers(peers);
		}

		return true;
	}

}
