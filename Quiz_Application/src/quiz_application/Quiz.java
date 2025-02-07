package quiz_application;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Quiz implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Student student;
	private List<Question> questions;
	private int currentQuestion;

	public Quiz(Student student) {
		this.student = student;
		this.questions = loadQuestions();
		this.currentQuestion = 0;

	}

	private static final String outputFileName = "results.bin";
	private static final String inputFileName = "questions.bin";

	@SuppressWarnings("unchecked")
	public List<Question> loadQuestions(){
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(inputFileName))) {
			List <Question> questions = (List<Question>) in.readObject();
			Collections.shuffle(questions);
			return questions;
		} 
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	@SuppressWarnings("unused")
	public boolean hasNext() {
		if(this.currentQuestion < this.questions.size() - 1) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unused")
	public String getCurrentQuestion() {
		return questions.get(currentQuestion).getQuestionText();
	}
	
	public void setAnswer(String answer) {
		
		switch(answer){
			case "A":{
				this.questions.get(currentQuestion).setAnswer(Question.Answer.A);
				break;
			}
			case "B":{
				this.questions.get(currentQuestion).setAnswer(Question.Answer.B);
				break;
			}
			case "C":{
				this.questions.get(currentQuestion).setAnswer(Question.Answer.C);
				break;
			}
			case "D":{
				this.questions.get(currentQuestion).setAnswer(Question.Answer.D);
				break;
			}
			case "EMPTY":{
				this.questions.get(currentQuestion).setAnswer(Question.Answer.EMPTY);
				break;
			}
			default:
				return;
			
			
		}
	}
	
	@SuppressWarnings("unused")
	public boolean isPassed(Map <Integer, Question.Answer> sami) {
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream("answers.bin"))){
			@SuppressWarnings("unchecked")
			Map <Integer, Question.Answer> rado = (Map <Integer, Question.Answer>) in.readObject();
			int total = 0;
			for(int i=0; i<questions.size(); i++) {
				if(rado.get(i) == sami.get(i)) {
					total++;
				}
			}
			return questions.size() / 2 + 1 < total;
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public void setCurrentQuestion(int currentQuestion) {
		this.currentQuestion = currentQuestion;
	}
}
