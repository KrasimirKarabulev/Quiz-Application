package user;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class User {

	private String userName;
	private String password;
	private final Pattern userNamePattern = Pattern.compile("[a-z0-9]{5,}");
	private final Pattern passwordPattern = Pattern.compile("[a-z0-9]{5,}");
	private HashMap<User, List<String>> usersMap;

	public User(String userName, String password) throws UserException{
		if(!userNamePattern.matcher(userName).matches() || !passwordPattern.matcher(password).matches()) {
			throw new UserException("Invalid format");
		}
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
