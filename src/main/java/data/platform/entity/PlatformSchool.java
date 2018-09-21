package data.platform.entity ;

import java.util.Date ;

import data.framework.support.AbstractEntity ;
public class PlatformSchool extends AbstractEntity
{
   
    
    public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	
	public String getDictionaryLevel() {
		return dictionaryLevel;
	}
	public void setDictionaryLevel(String dictionaryLevel) {
		this.dictionaryLevel = dictionaryLevel;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
     

	public String getTeacherAdAccount() {
		return teacherAdAccount;
	}
	public void setTeacherAdAccount(String teacherAdAccount) {
		this.teacherAdAccount = teacherAdAccount;
	}


	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
    

	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	

	public String getRaduationYear() {
		return raduationYear;
	}
	public void setRaduationYear(String raduationYear) {
		this.raduationYear = raduationYear;
	}


	private String code ;
    private String name ;
    private String year ;
    private String parent ;
    private String teacher ;
    private String remark ;
    private String dictionaryLevel ;
    private Date createTime ;
    private Date updateTime ;
    private String teacherAdAccount;
    private String pId;
    private String grade;
    private String raduationYear;
    private static final long serialVersionUID = -5738420005617254043L ;
}