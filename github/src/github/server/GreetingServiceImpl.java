package github.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.RepositoryService;

import github.client.GreetingService;
import github.shared.FieldVerifier;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	private static final String INVALID_USERNAME = "This is an invalid GitHub username.";
	private static final String NO_LANGUAGES = "No known languages for the given user.";
	private static final String NO_REPOSITORIES = "Given user has no repositories.";
	private static final String NO_USERNAME = "No user with the given username.";

	/* (non-Javadoc)
	 * @see github.client.GreetingService#greetServer(java.lang.String)
	 */
	public String greetServer(String input) throws IllegalArgumentException, IOException{
		if (!FieldVerifier.isValidUsername(input)) {
			throw new IllegalArgumentException(
					INVALID_USERNAME);
		}
		
		RepositoryService service = new RepositoryService();
		List<Repository> reps;
		try{
			reps = service.getRepositories(input);
		} catch(RequestException exception) {
			return NO_USERNAME;
		}
		if(reps.isEmpty()) {
			return NO_REPOSITORIES;
		}
		String lang = getFavouriteLanguage(reps);
		if(lang == null) {
			return NO_LANGUAGES;
		}
		return lang;
	}
	
	/**
	 * @param reps -  Collection of repositories.
	 * @return The language which occurs most in the repositories. 
	 */
	private String getFavouriteLanguage(List<Repository> reps){
		Map<String, Integer> languages = new HashMap<String, Integer>();
		int max = 0;
		String result = null;
		for(Repository rep : reps){
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
