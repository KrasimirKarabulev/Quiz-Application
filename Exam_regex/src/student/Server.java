package student;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

public class Server {

	private ServerSocket server;
	private final Object usersLock;
	private final String fileName = "results.bin";
	
	public Server() {
		usersLock = new Object();
	}
	
	public void start() {
		try{
			server = new ServerSocket(8080);
			System.out.println("Server started on port 8080");
			while(true) {
				Socket client = server.accept();
				System.out.println("Client connected: " +client.getInetAddress());
				Thread clientThread = new Thread(() ->{
					Scanner sc = null;
					PrintStream out = null;
					try {
						sc = new Scanner(client.getInputStream());
						out = new PrintStream(client.getOutputStream());
						QuizMenu(sc, out);
						
					}
					catch(IOException e) {
						e.printStackTrace();
					}
				});
				clientThread.start();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void saveQuestions(Quiz quiz) {
		try(ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream(fileName))){
			out.writeObject(quiz);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void QuizMenu(Scanner sc, PrintStream out) {
		out.println("Enter EGN");
		String EGN = sc.nextLine();
		out.println("enter facultyNumber");
		String facultyNumber = sc.nextLine();
		Map <Integer, Question.Answer> userAnswers;
		
		try {
			Quiz quiz = new Quiz(new Student(EGN, facultyNumber));
			while(quiz.hasNext()) {
				out.println(quiz.getCurrentQuestion());
				out.println("Enter answer");
				String answer = sc.nextLine();
				quiz.setAnswer(answer);
			}
			synchronized(usersLock) {
				quiz.isPassed(null);
			}
			
		} catch (StudentException e) {
			e.printStackTrace();
		}
		
	}
	
}
