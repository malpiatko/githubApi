package github.server;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import github.client.GreetingService;
import github.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	/*
	 * TODO: maybe catch the exception instead
	 */
	public String greetServer(String input) throws IllegalArgumentException, IOException{
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}
		
		Collection<GHRepository> reps = getRepositories(input);
		return getMostCommonLanguage(reps);
	}
	
	private Collection<GHRepository> getRepositories(String name) throws IOException{
		GitHub github = GitHub.connectAnonymously();
		GHUser user = github.getUser(name);
		return user.getRepositories().values();
	}
	/*
	 * TODO: what if empty repo
	 */
	private String getMostCommonLanguage(Collection<GHRepository> reps){
		Map<String, Integer> languages = new HashMap<String, Integer>();
		int max = 0;
		String result = "";
		for(GHRepository rep : reps){
			String lang = rep.getLanguage();
			Integer count;
			if(languages.get(lang) == null){
				count = 0;
			} else {
				count = languages.get(lang) + 1;
			}
			languages.put(lang, count);
			if(count > max){
				max = count;
				result = lang;
			}
		}
		return result;
		
	}

}
