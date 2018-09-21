package data.academic.examInfo.entity;
/**
 * @Title ExamPaper
 * @Description 试卷上传-试卷主表
 * @author wangchaofa
 * @CreateDate Oct 26,2016
 */
import java.util.Date;

import data.framework.support.AbstractEntity;

public class EntityExamPaper extends AbstractEntity{

	private static final long serialVersionUID = 1L;
	
	private String id;                         //试卷id
	private String examId;                     //考试id
	private String examCode;                   //考试编号
	private String course;                     //课程编号
    private String fileSource;                 //文件来源
    private String fileName;                   //文件名称
    private String fileNameExtension;          //文件扩展名
    private String fileNameInServer;           //服务器文件名
    private String filePathInServer;           //服务器文件路径
    private long fileSize;                     //文件大小
    private Date fileUploadTime = new Date() ; //文件上传时间
    private String operator;                   //操作人
    private String updatePerson;               //更新人
    private Date updateTime = new Date();      //更新时间
    private String isPublic;                   //是否公开 （0-公开，1-不公开）
    private String schoolCode;
    
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	
	public String getExamCode() {
		return examCode;
	}
	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}
	
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	
	public String getFileSource() {
		return fileSource;
	}
	public void setFileSource(String fileSource) {
		this.fileSource = fileSource;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileNameExtension() {
		return fileNameExtension;
	}
	public void setFileNameExtension(String fileNameExtension) {
		this.fileNameExtension = fileNameExtension;
	}
	
	public String getFileNameInServer() {
		return fileNameInServer;
	}
	public void setFileNameInServer(String fileNameInServer) {
		this.fileNameInServer = fileNameInServer;
	}
	
	public String getFilePathInServer() {
		return filePathInServer;
	}
	public void setFilePathInServer(String filePathInServer) {
		this.filePathInServer = filePathInServer;
	}
	
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	
	public Date getFileUploadTime() {
		return fileUploadTime;
	}
	public void setFileUploadTime(Date fileUploadTime) {
		this.fileUploadTime = fileUploadTime;
	}
	
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public String getUpdatePerson() {
		return updatePerson;
	}
	public void setUpdatePerson(String updatePerson) {
		this.updatePerson = updatePerson;
	}
	
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}
	public String getSchoolCode() {
		return schoolCode;
	}
	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}
    
    
	
}
