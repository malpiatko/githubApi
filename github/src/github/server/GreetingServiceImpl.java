package github.server;

import java.io.FileNotFoundException;
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


	public String greetServer(String input) throws IOException{
		GitHub github = GitHub.connectAnonymously();
		GHUser user;
		try{
			user = github.getUser(input);
		} catch(FileNotFoundException exception) {
			return "No user with the given username.";
		}
		Collection<GHRepository> reps = getRepositories(user);
		if(reps.isEmpty()) {
			return "Given user has no repositories.";
		}
		System.out.printf("Heeeere!1");
		String lang = getMostCommonLanguage(reps);
		System.out.printf("Heeeere!2");
		if(lang == null) {
			return "No known languages for the given user.";
		}
		return lang;
	}
	
	private Collection<GHRepository> getRepositories(GHUser user) throws IOException{
		return user.getRepositories().values();
	}
	
	private String getMostCommonLanguage(Collection<GHRepository> reps){
		Map<String, Integer> languages = new HashMap<String, Integer>();
		int max = 0;
		GHRepository result = null;
		for(GHRepository rep : reps){
			String lang = rep.getLanguage();
			if(lang != null){
				Integer count;
				if(languages.get(lang) == null){
					count = 1;
				} else {
					count = languages.get(lang) + 1;
				}
				languages.put(lang, count);
				//note that no hazard of null pointer exception
				if((count == max && rep.getSize() > result.getSize()) ||
						count > max){
					max = count;
					result = rep;
				}
			}
		}
		if(result == null){
			return null;
		}
		return result.getLanguage();
		
	}

}
