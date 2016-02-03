package mgs.server;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.MaskFormatter;

final public class ServerFactory extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ServerFactory server;
	private JTextArea jTextArea;
	private List<Server> serversList = new ArrayList<Server>();

	public static synchronized ServerFactory getInstance()
			throws ParseException {
		if (null == server) {
			server = new ServerFactory();
		}
		return server;
	}

	private ServerFactory() throws ParseException {
		setTitle("Server");
		setSize(550, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		final JFormattedTextField puerto = new JFormattedTextField(
				new MaskFormatter("####"));
		puerto.setValue("8000");

		Box horizontalBox = Box.createHorizontalBox();

		horizontalBox.add(new JLabel("Puerto"));
		horizontalBox.add(puerto);

		JButton botonStart = new JButton("Arrancar");
		botonStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				int p = Integer.valueOf(puerto.getValue().toString());
				arrancarServidor(p);
			}
		});

		JButton botonParar = new JButton("Parar todo");
		botonParar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stopServers();
			}
		});

		JButton botonMiembros= new JButton("Miembros");
		botonMiembros.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (Server server : serversList) {
					Map<String, List<GroupMember>> groups = server.getGroups();
					Set<String> keySet = groups.keySet();
					for (String string : keySet) {
						System.out.println("Group: " + string);
						List<GroupMember> list = groups.get(string);
						for (GroupMember groupMember : list) {
							System.out.println(groupMember);
						}
					}
				}
			}
		});

		Box horizontalBox2 = Box.createHorizontalBox();

		horizontalBox2.add(botonStart);
		horizontalBox2.add(botonParar);
		horizontalBox2.add(botonMiembros);

		Box verticalBox = Box.createVerticalBox();

		verticalBox.add(Box.createVerticalStrut(10));
		verticalBox.add(new JLabel("Servidor"));
		verticalBox.add(Box.createVerticalStrut(10));
		verticalBox.add(horizontalBox);
		verticalBox.add(Box.createVerticalStrut(10));
		verticalBox.add(horizontalBox2);
		verticalBox.add(Box.createVerticalStrut(10));

		jTextArea = new JTextArea();
		jTextArea.setSize(250, 300);
		jTextArea.setEditable(false);
		jTextArea.setLineWrap(true);
		jTextArea.setWrapStyleWord(true);
		JScrollPane jScrollPane = new JScrollPane(jTextArea);
		jScrollPane.setPreferredSize(new Dimension(250, 300));

		verticalBox.add(jScrollPane);

		JPanel contentPane = (JPanel) getContentPane();

		contentPane.add(verticalBox);

	}

	final private void stopServers() {
		for (Server server : serversList) {
			server.stopServer();
			try {
				Socket socket = new Socket("127.0.0.1", server.getPort());
				OutputStreamWriter out = new OutputStreamWriter(
						socket.getOutputStream());
				out.write("stop");
				out.flush();
				out.close();
			} catch (UnknownHostException e1) {
				flush(e1.getMessage());
			} catch (IOException e1) {
				flush(e1.getMessage());
			}
			server.interrupt();
		}
		flush("All Servers Stopped");

	}

	final private void arrancarServidor(int p) {
		Server serverAsAthread = new Server(this, p);
		serversList.add(serverAsAthread);
		serverAsAthread.start();
	}

	final public synchronized void flush(String string) {
		jTextArea.append(string + "\n");
	}

	final public void open() {
		super.setVisible(true);
	}

}
