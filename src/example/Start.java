package example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import mgs.client.Client;
import mgs.client.ClientFactory;
import mgs.server.GroupMember;
import mgs.server.ServerFactory;

import org.xml.sax.SAXException;

public class Start {

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		ServerFactory.getInstance().open();

		//On localhost these are the client lists (ports list)
		int[] array = { 8081,8080 };

		for (int i = 0; i < array.length; i++) {
			final int port = array[i];
			final Client client = ClientFactory.getStandardClient("localhost",
					port);

			JButton botonTest = new JButton("Connect:" + port);
			botonTest.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						client.connect("testGroup");
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ParserConfigurationException e1) {
						e1.printStackTrace();
					} catch (SAXException e1) {
						e1.printStackTrace();
					}

				}
			});

			JButton botonSend = new JButton("Send:" + port);
			botonSend.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						client.send("" + port);
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
			});

			JButton peersButton = new JButton("Peers");
			peersButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					List<GroupMember> peers = client.getPeers();
					for (GroupMember peer : peers) {
						System.out.println(peer.getIp() + " : " + peer.getPort());
					}
				}
			});

			Box hBox = Box.createHorizontalBox();
			hBox.add(botonTest);
			hBox.add(botonSend);
			hBox.add(peersButton
				);

			JFrame testFrame = new JFrame(String.valueOf(port));
			testFrame.setSize(300, 100);
			JPanel panel = (JPanel) testFrame.getContentPane();
			panel.add(hBox);
			testFrame.setVisible(true);
		}

	}

}
