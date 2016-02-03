package mgs.client.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import mgs.client.Client;
import mgs.server.GroupMember;

public class Message implements ClientAction {

	private Map<String, String> params;

	@Override
	public void set(Map<String, String> params, Socket socket) {
		this.params = params;
	}

	@Override
	public boolean doAction(Client client) {
		String message = params.get("message");
		int time = Integer.parseInt(params.get("clockCount"));
		String ip = String.valueOf(params.get("ip"));
		int port = Integer.parseInt(params.get("port"));

		AtomicInteger localTime = client.getTime();

		synchronized (client) {
			int localSyncTime = localTime.get();

			if (time > localSyncTime)
				localTime.set(time++);

			client.getMessageQueque().add(
					new mgs.utils.Message(time, message, new GroupMember(ip,
							port),client.getPeers().size()-1));
		}
		List<GroupMember> peersList = client.getPeers();
		
		for (GroupMember groupMember : peersList) 
			sendAck(groupMember.getIp(), groupMember.getPort(), time);
			
		return true;
	}

	private void sendAck(String ip, int port, int time) {
		PrintWriter writer;
		try {
			Socket socket = new Socket(ip, port);
			writer = new PrintWriter(socket.getOutputStream(), true);
			writer.println("<mgs><action>ack</action><clockCount>" + time
					+ "</clockCount>" + "<ip>" + ip + "</ip>" + "<port>" + port
					+ "</port></mgs>");
			writer.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
