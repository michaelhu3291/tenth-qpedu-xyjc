package data.academic.schoolHoliday.entity;

import java.io.Serializable;


public class SchoolCalendar  implements Serializable{

	/**
	 * 校历实体类
	 */
	private static final long serialVersionUID = 1L;
	
	private String Id;
	private String Last_Begin_Time;
	private String Last_End_Time;
	private String Next_Begin_Time;
	private String Next_End_Time;
	private String Create_Person;
	private String Create_Time;
	private String Update_Person;
	private String Update_Time;
	private String School_Year;
	private String School_Id;
	private String Winter_Begin_Time;
	private String Winter_End_Time;
	private String Summer_Begin_Time;
	private String Summer_End_Time;
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getLast_Begin_Time() {
		return Last_Begin_Time;
	}
	public void setLast_Begin_Time(String last_Begin_Time) {
		Last_Begin_Time = last_Begin_Time;
	}
	public String getLast_End_Time() {
		return Last_End_Time;
	}
	public void setLast_End_Time(String last_End_Time) {
		Last_End_Time = last_End_Time;
	}
	public String getNext_Begin_Time() {
		return Next_Begin_Time;
	}
	public void setNext_Begin_Time(String next_Begin_Time) {
		Next_Begin_Time = next_Begin_Time;
	}
	public String getNext_End_Time() {
		return Next_End_Time;
	}
	public void setNext_End_Time(String next_End_Time) {
		Next_End_Time = next_End_Time;
	}
	public String getCreate_Person() {
		return Create_Person;
	}
	public void setCreate_Person(String create_Person) {
		Create_Person = create_Person;
	}
	public String getCreate_Time() {
		return Create_Time;
	}
	public void setCreate_Time(String create_Time) {
		Create_Time = create_Time;
	}
	public String getUpdate_Person() {
		return Update_Person;
	}
	public void setUpdate_Person(String update_Person) {
		Update_Person = update_Person;
	}
	public String getUpdate_Time() {
		return Update_Time;
	}
	public void setUpdate_Time(String update_Time) {
		Update_Time = update_Time;
	}
	public String getSchool_Year() {
		return School_Year;
	}
	public void setSchool_Year(String school_Year) {
		School_Year = school_Year;
	}
	public String getSchool_Id() {
		return School_Id;
	}
	public void setSchool_Id(String school_Id) {
		School_Id = school_Id;
	}
	public String getWinter_Begin_Time() {
		return Winter_Begin_Time;
	}
	public void setWinter_Begin_Time(String winter_Begin_Time) {
		Winter_Begin_Time = winter_Begin_Time;
	}
	public String getWinter_End_Time() {
		return Winter_End_Time;
	}
	public void setWinter_End_Time(String winter_End_Time) {
		Winter_End_Time = winter_End_Time;
	}
	public String getSummer_Begin_Time() {
		return Summer_Begin_Time;
	}
	public void setSummer_Begin_Time(String summer_Begin_Time) {
		Summer_Begin_Time = summer_Begin_Time;
	}
	public String getSummer_End_Time() {
		return Summer_End_Time;
	}
	public void setSummer_End_Time(String summer_End_Time) {
		Summer_End_Time = summer_End_Time;
	}
}
