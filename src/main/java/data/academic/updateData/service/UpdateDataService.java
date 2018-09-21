package data.academic.updateData.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import data.framework.support.AbstractService;
/**
 * @Title: UpdateDataService
 * @Description: 对更新接口的数据进行增删改操作服务层
 * @author zhaohuanhuan
 * @date 2016年8月4日 
 */
@Service
public class UpdateDataService extends AbstractService {
    
	
	/**
	 * @Title: batchInsertTeacher
	 * @Description: 批量添加更新的教师数据
	 * @author zhaohuanhuan
	 * @date 2016年8月4日
	 * @param teacherList
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public void batchInsertTeacher(Map<String, Object> teacherMap) {
		insert("updateTeaData.batchInsertTeacher", teacherMap);
	}
	/**
	 * @Title: batchUpdateTeacher
	 * @Description: 批量更新教师数据
	 * @author zhaohuanhuan
	 * @date 2016年8月4日 
	 * @param teacherList
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public void batchUpdateTeacher(Map<String, Object> teacherMap) {
		update("updateTeaData.batchUpdateTeacher",teacherMap);
}
	
	
	/**
	 * @Title: remove
	 * @Description: 批量删除教师数据
	 * @author zhaohuanhuan
	 * @date 2016年8月4日 
	 * @param teacherList
	 * @return
	 * @return int
	 */
	public int remove(String pk) {
        return delete("updateTeaData.batchDeleteTeacher", pk );
	
	}
	
	/**
	 * @Title: batchInsertTeacher
	 * @Description: 批量添加更新的学生数据
	 * @author zhaohuanhuan
	 * @date 2016年8月9日 
	 * @param studentList
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public void  batchInsertStudent(Map<String, Object> studentMap) {
		insert("updateStuData.batchInsertStu", studentMap);
	}
	
	/**
	 * @Title: batchUpdateTeacher
	 * @Description: 批量更新学生数据
	 * @author zhaohuanhuan
	 * @date 2016年8月9日 
	 * @param studentList
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public void batchUpdateStudent(Map<String, Object> studentMap) {
		update("updateStuData.batchUpdateStu", studentMap);
	}
	
	/**
	 * @Title: removeStudent
	 * @Description: 批量删除学生数据
	 * @author zhaohuanhuan
	 * @date 2016年8月9日 
	 * @param studentList
	 * @return
	 * @return int
	 */
	public int removeStudent(String pk) {
        return delete("updateStuData.batchDeleteStu", pk );
	
	}
	
	/**
	 * @Title: batchInsertUnit
	 * @Description: 批量添加更新的教育单位数据
	 * @author zhaohuanhuan
	 * @date 2016年8月9日 
	 * @param unitList
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public void batchInsertUnit(Map<String, Object> unitMap) {
		insert("updateUnitData.batchInsertUnit", unitMap);
	}
	
	/**
	 * @Title: batchUpdateUnit
	 * @Description: 批量更新教育单位数据
	 * @author zhaohuanhuan
	 * @date 2016年8月9日 
	 * @param unitList
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public void batchUpdateUnit(Map<String, Object> unitMap) {
		update("updateUnitData.batchUpdateUnit", unitMap);
	}
	
	/**
	 * @Title: removeUnit
	 * @Description: 批量删除教育单位数据
	 * @author zhaohuanhuan
	 * @date 2016年8月9日 
	 * @param unitList
	 * @return
	 * @return int
	 */
	public int removeUnit(String pk) {
        return delete("updateUnitData.batchDeleteUnit", pk );
	}

	/**
	 * @Title: insertUpdateStudent
	 * @Description: 批量插入学生更新接口数据
	 * @author zhaohuanhuan
	 * @date 2017年8月28日 
	 * @return void
	 * @param studentList
	 */
	public void insertUpdateStudent(List<Map<String, Object>> studentList){
		insert("updateStuData.addUpdateStudents", studentList);
	}
	
	/**
	 * @Title: insertUpdateTeacher
	 * @Description:  批量插入教师更新接口数据
	 * @author zhaohuanhuan
	 * @date 2017年8月28日 
	 * @return void
	 * @param teacherList
	 */
	public void insertUpdateTeacher(List<Map<String, Object>> teacherList){
		insert("updateTeaData.addUpdateTeachers", teacherList);
	}
	
	/**
	 * @Title: insertUpdateUnit
	 * @Description: 批量插入教育单位更新接口数据
	 * @author zhaohuanhuan
	 * @date 2017年8月28日 
	 * @return void
	 * @param unitList
	 */
	public void insertUpdateUnit(List<Map<String, Object>> unitList){
		insert("updateUnitData.addUpdateSchools", unitList);
	}
	
	/**
	 * @Title: batchInsertSchoolByBua
	 * @Description: 获取更新接口添加的数据同步到bua
	 * @author zhaohuanhuan
	 * @date 2017年8月29日 
	 * @return void
	 * @param unitMap
	 */
	public void batchInsertSchoolByBua(Map<String, Object> unitMap){
		insert("updateDataByBua.batchInsertSchoolByBua", unitMap);
	}
	
	/**
	 * @Title: batchInsertUserForBua
	 * @Description: 获取更新接口添加的数据同步到bua用户表
	 * @author zhaohuanhuan
	 * @date 2017年8月29日 
	 * @return void
	 * @param userMap
	 */
	public void batchInsertUserForBua(Map<String, Object> userMap){
		insert("updateDataByBua.batchInsertUserByBua", userMap);
	}
	
	/**
	 * @Title: batchDeleteByBua
	 * @Description: 删除bua中更新接口需要删除的数据
	 * @author zhaohuanhuan
	 * @date 2017年8月29日 
	 * @return int
	 * @param userId
	 * @return
	 */
	public int batchDeleteByBua(String userId){
		return delete("updateDataByBua.batchDeleteUserByBua", userId);
	}
	
	/**
	 * @Title: batchUpdateByBua
	 * @Description: 更新bua中  青浦更新用户接口需要更新的数据
	 * @author zhaohuanhuan
	 * @date 2017年8月29日 
	 * @return void
	 * @param userMap
	 */
	public void batchUpdateUserByBua(Map<String, Object> userMap){
		update("updateDataByBua.batchUpdateUserByBua", userMap);
	}
	
	/**
	 * @Title: batchUpdateUnitForBua
	 * @Description:  更新bua中  青浦更新教育单位接口需要更新的数据
	 * @author zhaohuanhuan
	 * @date 2017年8月29日 
	 * @return void
	 * @param unitMap
	 */
	public void batchUpdateUnitForBua(Map<String, Object> unitMap){
		update("updateDataByBua.batchUpdateUnitForBua", unitMap);
	}
	
	/**
	 * @Title: batchDeleteUnitForBua
	 * @Description: 删除 青浦接口删除的数据
	 * @author zhaohuanhuan
	 * @date 2017年8月29日 
	 * @return int
	 * @param orgId
	 * @return
	 */
	public int batchDeleteUnitForBua(String orgId){
		return delete("updateDataByBua.batchDeleteUnitForBua", orgId);
	}
}
