package student;

import java.io.Serializable;
import java.util.regex.Pattern;

public class Student implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Pattern EGNPattern = Pattern.compile("d\\{10}");
	private static final Pattern facultyNumberPattern = Pattern.compile("d\\{9}");
	private String EGN;
	private String facultyNumber;

	public Student(String EGN, String facultyNumber) throws StudentException {
		if (!EGNPattern.matcher(EGN).matches() || !facultyNumberPattern.matcher(facultyNumber).matches()) {
			throw new StudentException("Invalid format");
		}
		this.EGN = EGN;
		this.facultyNumber = facultyNumber;
	}

	public String getEGN() {
		return EGN;
	}

	public void setEGN(String eGN) {
		EGN = eGN;
	}

	public String getFacultyNumber() {
		return facultyNumber;
	}

	public void setFacultyNumber(String facultyNumber) {
		this.facultyNumber = facultyNumber;
	}

}
