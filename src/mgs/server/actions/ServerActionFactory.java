package mgs.server.actions;

public class ServerActionFactory {
	private ServerActionFactory() {
		throw new UnsupportedOperationException(
				"this class it can't be instansiated...");
	}

	public static ServerAction getAction(String action) {
		if (action.equals("connect"))
			return new Connect();
		else
			throw new IllegalArgumentException("Action no supported");
	}
}
