package mgs.client.actions;

public class ClientActionFactory {
	private ClientActionFactory() {
		throw new UnsupportedOperationException(
				"this class it can't be instansiated...");
	}

	public static ClientAction getAction(String action) {
		if (action.equals("addGroupMember"))
			return new AddGroupMember();
		else if (action.equals("addGroupMembers"))
			return new AddGroupMembers();
		else if (action.equals("response"))
			return new Response();
		else if (action.equals("message"))
			return new Message();
		else if (action.equals("ack"))
			return new Ack();
		else
			throw new IllegalArgumentException("Action no supported");
	}
}
