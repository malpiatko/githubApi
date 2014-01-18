package github.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GreetingServiceAsync {
	/**
	 * See: greetServer in GreetingService.java
	 */
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
}
