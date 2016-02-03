package mgs.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class GroupMember {

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (!(obj instanceof GroupMember))
			return false;

		GroupMember tmp = (GroupMember) obj;

		return (tmp.ip.equals(ip) && tmp.port == port);
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	private String ip;
	private int port;

	/**
	 * 
	 * @param ip
	 *            - the clients ip
	 * @param port
	 *            - the listening port
	 */
	public GroupMember(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public void notifyNewMember(GroupMember newMember) {
		try {
			Socket socket = new Socket(ip, port);
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			writer.println("<mgs><action>addGroupMember</action><ip>" + newMember.getIp()
					+ "</ip><listenPort>" + newMember.getPort() + "</listenPort></mgs>");

			writer.close();
			reader.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void notifyNewMember(List<GroupMember> clientGroupDest) {
		try {
			Socket socket = new Socket(ip, port);
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			StringBuilder members = new StringBuilder();
			for (GroupMember member : clientGroupDest)
				members.append("<member><ip>http://" + member.getIp()
						+ "</ip><listenPort>:" + member.getPort()
						+ "</listenPort></member>");

			writer.println("<mgs><action>addGroupMembers</action><members>"
					+ members.toString() + "</members></mgs>");

			writer.close();
			reader.close();
			socket.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public String toString(){
		return "Ip: " + ip + " port: " + port;
	}

}
