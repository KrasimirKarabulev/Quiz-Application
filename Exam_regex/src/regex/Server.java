package regex;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Server {

	private ServerSocket server;
	private final String fileName = "regex.bin";
	private Object usersLock;

	public Server() {
		usersLock = new Object();
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
						userMenu(sc, out);

					} catch (IOException e) {

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

	public void saveUsers(List<Regex> regexes) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
			out.writeObject(regexes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Regex> loadUsers() {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
			return (List<Regex>) in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unused")
	private void createRegex(Scanner sc, PrintStream out) {
		out.println("Enter pattern");
		String pattern = sc.nextLine();
		out.println("Enter description");
		String description = sc.nextLine();
		String[] array = new String[100];
		String answer = "";
		String test = "";
		int count = 0;
		Regex regex = new Regex(pattern, description);

		while (!test.equals("exit")) {
			out.println("Enter next test");
			test = sc.nextLine();
			array[count] = test;
			count++;
		}
		out.println(RegexTester.test(regex, array));
		out.println("Add or No");
		answer = sc.nextLine();
		if (answer.equals("Yes")) {
			synchronized (usersLock) {
				List<Regex> regexes = loadUsers();
				regexes.add(regex);
				saveUsers(regexes);
			}
		}
		return;

	}

	@SuppressWarnings("unused")
	private void searchRegex(Scanner sc, PrintStream out) {
		out.println("Enter key word");
		String key = sc.nextLine();
		synchronized (usersLock) {
			List<Regex> regexes = loadUsers();
			List<Regex> sortedRegexes = regexes.stream().filter(c -> c.getDescription().contains(key))
					.sorted(Comparator.comparingInt(Regex::getRating).reversed())
					.collect(Collectors.toList());
			

			out.println(sortedRegexes);
			out.println("choose id");
			int id = sc.nextInt();
			
			for (int i = 0; i < sortedRegexes.size(); i++) {
				if(sortedRegexes.get(i).getId()==id) {
					String answer ="";
					String []array = new String [100];
					int count =0;
					while (!answer.equals("exit")) {
						out.println("Enter next test");
						answer = sc.nextLine();
						array[count] = answer;
						count++;
					}
					out.println(RegexTester.test(sortedRegexes.get(i), array));
					out.println("1: for a positive review; 2: for a negative review");
					answer = sc.nextLine();
					if(answer.equals("1")) {
						sortedRegexes.get(i).setRating(sortedRegexes.get(i).getRating()+1);
					}
					else if(answer.equals("2")) {
						sortedRegexes.get(i).setRating(sortedRegexes.get(i).getRating()-1);
					}
					saveUsers(sortedRegexes);
				}
				
			}
		}
	}

	private void userMenu(Scanner sc, PrintStream out) {

		out.println("Choose 1: Create Regex or 2: Search:");
		String answer = sc.nextLine();
		if (answer.equals("1")) {
			createRegex(sc, out);
		} else if (answer.equals("2")) {
			searchRegex(sc, out);
		} else {
			out.println("Invalid Answer");
			return;
		}

	}

}
