package mgs.client;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.ParserConfigurationException;

import mgs.server.GroupMember;
import mgs.utils.Message;
import mgs.utils.Toilet;

import org.xml.sax.SAXException;

public class Client extends Thread implements Toilet {
	private String serverIp;
	private int serverSocket;
	private PrintStream out;
	private int clientSocket;
	private boolean run = true;
	private List<GroupMember> peers;
	private boolean isConnected = false;
	private Object peersLock = new Object();
	private AtomicInteger time = new AtomicInteger();
	private PriorityQueue<Message> messageQueque = new PriorityQueue<Message>();

	public Client(String serverIp, int serverSocket, int clientSocket,
			PrintStream out) {
		this.serverIp = serverIp;
		this.serverSocket = serverSocket;
		this.clientSocket = clientSocket;
		this.out = out;
		this.peers = new ArrayList<GroupMember>();
	}

	@Override
	public void run() {
		flush("Client started at " + clientSocket + "...");
		try {
			ServerSocket server = new ServerSocket(clientSocket);
			try {
				while (run) {

					Socket accept = server.accept();
					ClientResponse response = new ClientResponse(accept, this);
					response.start();
				}
			} catch (Exception e) {
				flush(e.getMessage());
				server.close();
			}
			flush("Stopping client at " + clientSocket + "...");
			server.close();

		} catch (IOException e) {
			flush(e.getMessage());
		}
	}

	public void connect(String groupName) throws UnknownHostException,
			IOException, ParserConfigurationException, SAXException {
		if (isConnected)
			throw new UnsupportedOperationException(
					"The client is already connected to a server");
		start();
		Socket socket = new Socket(serverIp, serverSocket);
		PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
		writer.println("<mgs><action>connect</action><group>" + groupName
				+ "</group><listenPort>" + clientSocket + "</listenPort></mgs>");

		writer.close();
		socket.close();

		isConnected = true;
	}

	public void flush(String msg) {
		out.println(msg);
	}

	public void stopClient() {
		try {
			run = false;
			Socket socket = new Socket("127.0.0.1", clientSocket);
			OutputStreamWriter out = new OutputStreamWriter(
					socket.getOutputStream());
			out.write("stop");
			out.flush();
			out.close();
			socket.close();
		} catch (UnknownHostException e1) {
			flush(e1.getMessage());
		} catch (IOException e1) {
			flush(e1.getMessage());
		}
		flush("Client Stopped");

	}

	public void addPeer(GroupMember groupMember) {
		synchronized (peersLock) {
			peers.add(groupMember);
		}
	}

	public void send(String string) throws UnknownHostException, IOException {
		List<GroupMember> tmpPeers;
		synchronized (peersLock) {
			tmpPeers = new ArrayList<GroupMember>(peers);
		}

		int count = time.incrementAndGet();
		
		messageQueque.add(
				new mgs.utils.Message(count, string, new GroupMember(InetAddress.getLocalHost().getHostAddress(),
						clientSocket),peers.size()));

		for (GroupMember groupMember : tmpPeers)
			send(string, groupMember, count);

	}

	private void send(String string, GroupMember groupMember, int count)
			throws UnknownHostException, IOException {
		Socket socket = new Socket(groupMember.getIp(), groupMember.getPort());
		PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

		writer.println("<mgs><action>message</action><message>" + string
				+ "</message><clockCount>" + count + "</clockCount>" +
						"<ip>" + InetAddress.getLocalHost().getHostAddress() + "</ip>" +
								"<port>" + clientSocket + "</port></mgs>");

		writer.close();
		socket.close();
	}

	public void addPeers(List<GroupMember> peersLocal) {
		synchronized (peersLock) {
			peers.addAll(peersLocal);
		}
	}
	public List<GroupMember> getPeers(){
		return peers;
	}

	public PriorityQueue<Message> getMessageQueque() {
		return messageQueque;
	}

	public AtomicInteger getTime() {
		return time;
	}

	public void checkMessages() {
		synchronized (messageQueque) {
		Message peek = messageQueque.peek();
			if(peek.isReadyForConsume()){
				doSomething(peek);
				messageQueque.remove(peek);
			}
			
		}	
		
	}

	private void doSomething(Message peek) {
		System.out.println("Reading: " + peek.getMessage());
		
	}

}