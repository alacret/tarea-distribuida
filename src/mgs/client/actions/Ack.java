package mgs.client.actions;

import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

import mgs.client.Client;
import mgs.server.GroupMember;
import mgs.utils.Message;

public class Ack implements ClientAction {

	private Map<String, String> params;

	@Override
	public void set(Map<String, String> params, Socket socket) {
		this.params = params;
	}

	@Override
	public boolean doAction(Client client) {
		int time = Integer.parseInt(params.get("clockCount"));
		String ip = String.valueOf(params.get("ip"));
		int port = Integer.parseInt(params.get("port"));

		PriorityQueue<Message> messageQueque = client.getMessageQueque();
		for (Message message : messageQueque) 
			if (message.getMember().equals(new GroupMember(ip, port)))
				if (message.getTime() == time)
					synchronized (client) {
						message.increaseAckNumber();
					}
			

		client.checkMessages();
		return true;
	}

}
