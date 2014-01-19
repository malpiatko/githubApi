package github.shared;

public class FieldVerifier {
	/*
	 * A valid GitHub username may only contain alphanumeric characters
	 * or dashes and cannot begin with a dash.
	 */
	public static boolean isValidUsername(String name) {
		return name.matches("[A-Za-z0-9][-A-Za-z-0-9]*");
	}
}
