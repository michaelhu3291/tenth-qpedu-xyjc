package data.academic.initData.entity;

/**
 * @Title: EntityTeacherInfo
 * @Description: 教师信息实体类
 * @author zhaohuanhuan
 * @date 2016年8月1日 
 */
public class EntityTeacherInfo {

	private String id;
	private String teacherName; //教师名称
	private String teacherNumber; //教师编号
	private String teacherPosition;//教师职位
	private String createTime;
	private String createPerson;
	private String updateTime;
	private String updatePerson;
	private String SFZJH;//教师身份证号
	private String PK;//获取的教师id
	private String XM;
	private String pkSchool;
	
	
	
	public String getSFZJH() {
		return SFZJH;
	}
	public void setSFZJH(String sFZJH) {
		SFZJH = sFZJH;
	}
	public String getPK() {
		return PK;
	}
	public void setPK(String pK) {
		PK = pK;
	}
	public String getXM() {
		return XM;
	}
	public void setXM(String xM) {
		XM = xM;
	}
	public String getPkSchool() {
		return pkSchool;
	}
	public void setPkSchool(String pkSchool) {
		this.pkSchool = pkSchool;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getTeacherNumber() {
		return teacherNumber;
	}
	public void setTeacherNumber(String teacherNumber) {
		this.teacherNumber = teacherNumber;
	}
	public String getTeacherPosition() {
		return teacherPosition;
	}
	public void setTeacherPosition(String teacherPosition) {
		this.teacherPosition = teacherPosition;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreatePerson() {
		return createPerson;
	}
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdatePerson() {
		return updatePerson;
	}
	
	
	
}
