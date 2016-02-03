package mgs.client.actions;

import java.net.Socket;
import java.util.Map;

import mgs.client.Client;

public class Response implements ClientAction {

	private Map<String, String> params;

	@Override
	public void set(Map<String, String> params, Socket socket) {
		this.params = params;
	}

	@Override
	public boolean doAction(Client client) {
		if (params.get("value").equals("ok"))
			return true;
		return false;
	}

}
