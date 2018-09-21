package data.academic.qualityAnalysis.entity;

import java.io.Serializable;


public class StuTempInfo  implements Serializable{

	/**
	 * 学生临时信息类
	 */
	private static final long serialVersionUID = 1L;
	private String examNumber;
	private String schoolName;
    private String classId;
    private String grade;
    private String classType;
    private String className;
    private String schoolCode;
    private String xjh;
    private String examCode;
	public String getExamCode() {
		return examCode;
	}
	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}
	public String getExamNumber() {
		return examNumber;
	}
	public void setExamNumber(String examNumber) {
		this.examNumber = examNumber;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getSchoolCode() {
		return schoolCode;
	}
	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}
	public String getXjh() {
		return xjh;
	}
	public void setXjh(String xjh) {
		this.xjh = xjh;
	}
	@Override
	public String toString() {
		return "StuTempInfo [examNumber=" + examNumber + ", schoolName=" + schoolName + ", classId=" + classId
				+ ", grade=" + grade + ", classType=" + classType + ", className=" + className + ", schoolCode="
				+ schoolCode + ", xjh=" + xjh + "]";
	}
	
}
