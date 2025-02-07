package user;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Server {

	private ServerSocket server;
	private Object usersLock;
	private static final String usersFileName = "users.bin";

	public Server() {
		usersLock = new Object();
	}

	public void saveUsers(HashMap<User, List<String>> usersMap) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(usersFileName))) {
			out.writeObject(usersMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<User, List<String>> loadUsers() {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(usersFileName))) {
			return (HashMap<User, List<String>>) in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void start() {
		try {
			server = new ServerSocket(8080);
			while (true) {
				Socket client = server.accept();
				Thread clientThread = new Thread(() -> {
					Scanner sc = null;
					PrintStream out = null;
					try {
						sc = new Scanner(client.getInputStream());
						out = new PrintStream(client.getOutputStream());
						usersMenu(sc, out);
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (sc != null) {
							sc.close();
						}
						if (out != null) {
							out.close();
						}
					}
				});
				clientThread.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private void usersMenu(Scanner sc, PrintStream out) {
		out.println("Choose Register or Create : R / C");
		String answer = sc.nextLine();
		while(true) {
			switch (answer) {
			case "R": {
				out.println("Enter username");
				String username = sc.nextLine();
				out.println("Enter password");
				String password = sc.nextLine();
				registerUser(sc, out, username, password);
				
				
			}
			case "C": {
				out.println("Enter username");
				String username = sc.nextLine();
				out.println("Enter password");
				String password = sc.nextLine();
				User user = loginUser(sc, out, username, password);
				out.println("Choose Upload or Download: U / D");
				String option = sc.nextLine();
				switch(option) {
					case "U":{
						
					}
					case "D":{
						out.println("Enter name of File");
						String fileName = sc.nextLine();
						synchronized(usersLock) {
							HashMap <User, List<String>> maps = loadUsers();
							for(int i=0; i<maps.get(user).size(); i++) {
								if(fileName.equals(maps.get(user).get(i))) {
									out.println(maps.get(user).get(i));
								}
							}
					}
					}
					default :{
						return;
					}
				}
					
			}
			default:
				return;
			}

		}
		}
		
	@SuppressWarnings("unused")
	private User loginUser(Scanner sc, PrintStream out, String username, String password) {
		try {
			User user = new User(username, password);
			synchronized(usersLock) {
				HashMap <User, List<String>> map = loadUsers();
				if(map.containsKey(user)) {
					out.println("Logged in");
					return user;
				}
			}
		}
		catch(UserException e) {
			out.println(e.getMessage());
		}
		return null;
	}
	private void registerUser(Scanner sc, PrintStream out, String username, String password) {
		try {
			User user = new User(username, password);
			synchronized(usersLock) {
				HashMap <User, List<String>> map = loadUsers();
				map.put(user, null);
				saveUsers(map);
			}
		}
		catch(UserException e) {
			out.println(e.getMessage());
		}
		
	}
}
