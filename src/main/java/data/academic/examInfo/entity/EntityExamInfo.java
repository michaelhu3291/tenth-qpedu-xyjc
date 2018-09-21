package data.academic.examInfo.entity;


import java.util.ArrayList;
import java.util.List;

import data.framework.support.AbstractEntity;
import data.platform.entity.EntityPlatformFile;

public class EntityExamInfo extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String exam_time;//考试时间
	private String Exam_Type;//考试类型
	private String Exam_Name;//考试名称
	private String Create_Time;
	private String Create_Person;
	private String Update_Time;
	private String Update_Person;
	private String School_Year;
	private String Term;
	private String Exam_Number;
	private String School_Code;
	private String Grade_Code;
	private String Closing_Time;
	private String Introduced_Time;
	
	private List<EntityPlatformFile> files=new ArrayList<EntityPlatformFile>();
	
	public String getGrade_Code() {
		return Grade_Code;
	}
	public void setGrade_Code(String grade_Code) {
		Grade_Code = grade_Code;
	}
	public String getSchool_Year() {
		return School_Year;
	}
	public void setSchool_Year(String school_Year) {
		School_Year = school_Year;
	}
	public String getTerm() {
		return Term;
	}
	public void setTerm(String term) {
		Term = term;
	}
	public String getExam_Number() {
		return Exam_Number;
	}
	public void setExam_Number(String exam_Number) {
		Exam_Number = exam_Number;
	}
	public String getSchool_Code() {
		return School_Code;
	}
	public void setSchool_Code(String school_Code) {
		School_Code = school_Code;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getExam_time() {
		return exam_time;
	}
	public void setExam_time(String exam_time) {
		this.exam_time = exam_time;
	}
	
	public String getExam_Type() {
		return Exam_Type;
	}
	public void setExam_Type(String exam_Type) {
		Exam_Type = exam_Type;
	}
	public String getExam_Name() {
		return Exam_Name;
	}
	public void setExam_Name(String exam_Name) {
		Exam_Name = exam_Name;
	}
	public String getCreate_Time() {
		return Create_Time;
	}
	public void setCreate_Time(String create_Time) {
		Create_Time = create_Time;
	}
	public String getCreate_Person() {
		return Create_Person;
	}
	public void setCreate_Person(String create_Person) {
		Create_Person = create_Person;
	}
	public String getUpdate_Time() {
		return Update_Time;
	}
	public void setUpdate_Time(String update_Time) {
		Update_Time = update_Time;
	}
	public String getUpdate_Person() {
		return Update_Person;
	}
	public void setUpdate_Person(String update_Person) {
		Update_Person = update_Person;
	}
	public String getClosing_Time() {
		return Closing_Time;
	}
	public void setClosing_Time(String closing_Time) {
		Closing_Time = closing_Time;
	}
	public String getIntroduced_Time() {
		return Introduced_Time;
	}
	public void setIntroduced_Time(String introduced_Time) {
		Introduced_Time = introduced_Time;
	}
	public List<EntityPlatformFile> getFiles() {
		return files;
	}
	public void setFiles(List<EntityPlatformFile> files) {
		this.files = files;
	}


}
