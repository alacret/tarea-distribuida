package mgs.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mgs.utils.Toilet;

public class Server extends Thread implements Toilet {

	private int port;

	public int getPort() {
		return port;
	}

	private ServerFactory factory;
	private boolean run = true;
	private Map<String, List<GroupMember>> groups;

	public Map<String, List<GroupMember>> getGroups() {
		return groups;
	}

	public Server(ServerFactory factory, int p) {
		this.factory = factory;
		this.port = p;
		groups = new HashMap<String, List<GroupMember>>();
	}

	public void flush(String msg) {
		factory.flush(msg);
	}

	@Override
	public void run() {
		factory.flush("Server started at " + port + "...");
		try {
			ServerSocket server = new ServerSocket(port);
			try {
				while (run) {
					Socket accept = server.accept();
					ServerResponse resp = new ServerResponse(accept, this);
					resp.start();
				}
			} catch (Exception e) {
				factory.flush(e.getMessage());
				server.close();
			}
			factory.flush("Stopping server at " + port + "...");
			server.close();

		} catch (IOException e) {
			factory.flush(e.getMessage());
		}
	}

	public void stopServer() {
		run = false;
	}

	public void addMember(String group, GroupMember groupMember) {
		synchronized (this) {
			if (groups.get(group) == null)
				groups.put(group, new ArrayList<GroupMember>());
			List<GroupMember> clientGroup = groups.get(group);
			clientGroup.add(groupMember);
		}

	//	deliverPeersList(group, groupMember);
		 broadCastNewMember(group, groupMember);
	}

//	private void deliverPeersList(String group, GroupMember groupMember) {
//		List<GroupMember> clientGroupSrc = groups.get(group);
//		List<GroupMember> clientGroupDest = null;
//
//		synchronized (clientGroupSrc) {
//			clientGroupDest = new ArrayList<GroupMember>(clientGroupSrc);
//		}
//
//		clientGroupDest.remove(groupMember);
//		if (clientGroupDest.size() > 0)
//			groupMember.notifyNewMember(clientGroupDest);
//
//	}

	private void broadCastNewMember(String group, GroupMember groupMember) {
		List<GroupMember> clientGroupSrc = groups.get(group);
		List<GroupMember> clientGroupDest = null;

		synchronized (this) {
			clientGroupDest = new ArrayList<GroupMember>(clientGroupSrc);
		}

		System.out.println("broadcasting new member");
		
		clientGroupDest.remove(groupMember);

		for (GroupMember client : clientGroupDest)
			client.notifyNewMember(groupMember);
		
		groupMember.notifyNewMember(clientGroupDest);
		

	}
}