package github.client;

import java.io.IOException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	/**
	 * @param name - Username of a GitHub user.
	 * @return Best guess of user's favourite programming language.
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	String greetServer(String name) throws IllegalArgumentException, IOException;
}
