package data.academic.initData.service;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import data.framework.support.AbstractService;

/**
 * @Title: InitDataService
 * @Description: 初始化数据服务层
 * @author zhaohuanhuan
 * @date 2016年8月2日
 */
@Service
public class InitDataService extends AbstractService {

	/**
	 * @Title: insertTeacher
	 * @Description: 导入教师基本数据
	 * @author zhaohuanhuan
	 * @date 2016年8月2日
	 * @param teacherList
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public void insertTeacher(List<Map<String, Object>> teacherList) {
		  insert("initData.addTeachers", teacherList);
	}
	
	/**
	 * @Title: insertUserRole
	 * @Description: 给教师赋予角色权限
	 * @author zhaohuanhuan
	 * @date 2016年11月10日 
	 * @param teacherList
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public void insertUserRoleForBua(List<Map<String,Object>> teacherList){
		  insert("initDataForBua.addUserRole", teacherList);
	}
	/**
	 * @Title: insertStuentForBua
	 * @Description: 同步学生数据到bua
	 * @author zhaohuanhuan
	 * @date 2017年8月29日 
	 * @return void
	 * @param userList
	 */
	public void insertStuentForBua(List<Map<String, Object>> userList){
		insert("initDataForBua.addStudent", userList);
	}
	/**
	 * @Title: removeUsersAndOrgs
	 * @Description: 同步数据时删除bua的所有相关用户和机构
	 * @author zhaohuanhuan
	 * @date 2017年8月25日 
	 * @return int
	 * @return
	 */
	public int removeUsersAndOrgs(){
		return delete("initDataForBua.removeUsersAndOrgs");
	}
	/**
	 * @Title: removeUsersForBua
	 * @Description: 批量删除bua老师或者学生数据
	 * @author zhaohuanhuan
	 * @date 2017年8月29日 
	 * @return int
	 * @param identifier
	 * @return
	 */
	public int removeUsersForBua(String identifier){
		return delete("initDataForBua.removeUserForBua",identifier);
	}
	/**
	 * @Title: removeTeacher
	 * @Description: 删除数据
	 * @author zhaohuanhuan
	 * @date 2016年8月2日
	 * @return
	 * @return int
	 */
	public int removeTeacher() {
		return delete("initData.deleteTeacher");
	}
	
	/**
	 * @Title: removeStudentRefSchool
	 * @Description: 批量删除学生学校关系表数据
	 * @author zhaohuanhuan
	 * @date 2016年12月23日
	 * @return
	 * @return int
	 */
	public int removeStudentRefSchool() {
		return delete("initData.deleteStudentRefSchool");
	}
	
	
	/**
	 * @Title: removeTeacherRefSchool
	 * @Description: 批量删除老师学校关系表数据
	 * @author zhaohuanhuan
	 * @date 2016年12月23日
	 * @return
	 * @return int
	 */
	public int removeTeacherRefSchool() {
		return delete("initData.deleteTeacherRefSchool");
	}

	/**
	 * @Title: insertTeaRefSchool
	 * @Description: 导入教师学校关系表数据
	 * @author zhaohuanhuan
	 * @date 2016年8月2日
	 * @param teaRefSchoolList
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public void insertTeaRefSchool(List<Map<String, Object>> teaRefSchoolList) {
		insert("initData.insertTeaRefSchool", teaRefSchoolList);
	}
 
	/**
	 * @Title: insertStudentRefClass
	 * @Description: 导入学生班级关系表数据
	 * @author zhaohuanhuan
	 * @date 2017年1月3日
	 * @param stuRefClassList
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public void insertStudentRefClass(List<Map<String,Object>> stuRefClassList){
		 insert("initData.addStudentRefClass",stuRefClassList);
	}
	/**
	 * @Title: insertSchool
	 * @Description: 导入教育单位基本数据
	 * @author zhaohuanhuan
	 * @date 2016年8月3日
	 * @param schoolList
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public void insertSchool(List<Map<String, Object>> schoolList) {
		insert("initData.addSchools", schoolList);
	}

	/**
	 * @Title: removeSchool
	 * @Description: 删除所有学校数据
	 * @author zhaohuanhuan
	 * @date 2016年8月3日
	 * @return
	 * @return int
	 */
	public int removeSchool() {
		return delete("initData.deleteSchool");
	}

	/**
	 * @Title: insertStudent
	 * @Description: 导入学生基本数据
	 * @author zhaohuanhuan
	 * @date 2016年8月3日
	 * @param studentList
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public void insertStudent(List<Map<String, Object>> studentList) {
		insert("initData.addStudents", studentList);
	}

	public void insertStudentRefSchool(List<Map<String, Object>> studentList) {
		insert("initData.addStudentRefSchool", studentList);
	}
	/**
	 * @Title: removeStudent
	 * @Description: 删除所有学生数据
	 * @author zhaohuanhuan
	 * @date 2016年8月3日
	 * @return
	 * @return int
	 */
	public int removeStudent() {
		return delete("initData.deleteStudent");
	}
	
	/**
	 * @Title: insertCourse
	 * @Description: 导入科目基本数据
	 * @author zhaohuanhuan
	 * @date 2016年8月23日 
	 * @param courseList
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public void insertCourse(List<Map<String, Object>> courseList) {
		 insert("initData.addCourses", courseList);
	}
	/**
	 * @Title: removeCourse
	 * @Description: 删除所有科目数据
	 * @author zhaohuanhuan
	 * @date 2016年8月23日 
	 * @return
	 * @return int
	 */
	public int removeCourse() {
		return delete("initData.deleteCourse");
	}
	
	
	/**
	 * @Title: insertClass
	 * @Description:  导入班级基本数据
	 * @author zhaohuanhuan
	 * @date 2016年8月10日 
	 * @param classList
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public void insertClass(List<Map<String, Object>> classList){
		 insert("initData.addClass",classList);
	}
	
	
	/**
	 * @Title: removeClass
	 * @Description: 删除所有的班级
	 * @author zhaohuanhuan
	 * @date 2016年8月10日 
	 * @return
	 * @return int
	 */
	public int removeClass(){
		return delete("initData.deleteClass");
	}
	
	
	
	/**
	 * @Title: insertUnitForBua
	 * @Description: 同步教育单位数据到bua
	 * @author zhaohuanhuan
	 * @date 2016年8月18日 
	 * @param unitBuaList
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public void insertUnitForBua(List<Map<String,Object>> unitBuaList){
		insert("initDataForBua.addUnit", unitBuaList);
	}
	
	
	/**
	 * @Title: insertTeacherForBua
	 * @Description: 同步教师数据到bua
	 * @author zhaohuanhuan
	 * @date 2016年8月18日 
	 * @param teacherBuaList
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public void insertTeacherForBua(List<Map<String,Object>> teacherBuaList){
		insert("initDataForBua.addTeacher", teacherBuaList);
	}
	
	/**
	 * @Title: removeStudentRefClass
	 * @Description: 删除学生班级关系表数据
	 * @author zhaohuanhuan
	 * @date 2017年01月03日 
	 * @return
	 * @return int
	 */
	public int removeStudentRefClass(){
		return delete("initData.deleteStudentRefClass");
	}
	
	/**
	 * @Title: insertKnowledgeData
	 * @Description: 初始化知识点数据
	 * @author zhaohuanhuan
	 * @date 2017年9月4日 
	 * @return void
	 * @param knowledgeList
	 */
	public void insertKnowledgeData(List<Map<String,Object>> knowledgeList){
		insert("initData.initKnowledgeData", knowledgeList);
	}
	
	/**
	 * @Title: removeKnowledge
	 * @Description: 删除所有的知识点
	 * @author zhaohuanhuan
	 * @date 2017年9月4日 
	 * @return int
	 * @return
	 */
	public int removeKnowledge(){
		return delete("initData.removeKnowledgeData");
	}

}
