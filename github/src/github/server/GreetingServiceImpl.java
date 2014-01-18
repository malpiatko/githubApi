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
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	private static final String NO_LANGUAGES = "No known languages for the given user.";
	private static final String NO_REPOSITORIES = "Given user has no repositories.";
	private static final String NO_USERNAME = "No user with the given username.";

	/* (non-Javadoc)
	 * @see github.client.GreetingService#greetServer(java.lang.String)
	 */
	public String greetServer(String input) throws IOException{
		GitHub github = GitHub.connectAnonymously();
		GHUser user;
		try{
			user = github.getUser(input);
		} catch(FileNotFoundException exception) {
			return NO_USERNAME;
		}
		Collection<GHRepository> reps = getRepositories(user);
		if(reps.isEmpty()) {
			return NO_REPOSITORIES;
		}
		String lang = getMostCommonLanguage(reps);
		if(lang == null) {
			return NO_LANGUAGES;
		}
		return lang;
	}
	
	/**
	 * @param user - The username of a github user.
	 * @return - collection of repositories for the given user
	 * @throws IOException
	 */
	private Collection<GHRepository> getRepositories(GHUser user) throws IOException{
		return user.getRepositories().values();
	}
	
	/**
	 * @param reps -  Collection of repositories.
	 * @return The language which occurs most in the repositories. 
	 */
	private String getMostCommonLanguage(Collection<GHRepository> reps){
		Map<String, Integer> languages = new HashMap<String, Integer>();
		int max = 0;
		String result = null;
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
				if( count > max){
					max = count;
					result = lang;
				}
			}
		}
		return result;
		
	}

}
