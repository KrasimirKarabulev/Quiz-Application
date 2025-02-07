package student;

import java.io.Serializable;

public class Question implements Serializable{
	private int id;
	private String questionText;
	private Answer answer;
	
	public enum Answer{
		A, B, C, D, EMPTY;
	}
	
	public Question(int id, String questionText, Answer answer) {
		this.id=id;
		this.questionText=questionText;
		this.answer=answer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}
}
