package data.academic.studentManagement.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.academic.qualityAnalysis.entity.StuTempInfo;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;

/**
 * @Title: StuManagementService
 * @Description: 学生管理服务层
 * @author zhaohuanhuan
 * @date 2016年7月29日
 */
@Service
public class StuManagementService extends AbstractService {
	/**
	 * 分页查询
	 */
//	public PagingResult<EntityStudent> searchPaging(Map<String, Object> param, String sortField, String sort,
//			int currentPage, int pageSize) {
//		if (StringUtils.isBlank(sortField))
//			sortField = "Grade_Id,Class_Id,School_Type_Sequence"; 
//		if (StringUtils.isBlank(sort))
//			sort = "ASC";
//		return selectPaging("stu.selectPaging", param, sortField, sort, currentPage, pageSize);
//	}
	
	/**
	 * 分页查询
	 */
	public PagingResult<Map<String,Object>> searchPaging(Map<String,Object> param, String sortField, String sort, int currentPage, int pageSize ){
		if( StringUtils.isBlank( sortField ) )
            sortField = "Grade_No,Class_No" ;
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging( "stu.selectPaging", param, sortField, sort, currentPage, pageSize ) ;
    }
	/**
	 * 
	 * @Title: searchPaging
	 * @Description: 导出
	 * @author jay zhong
	 * @date 2017年11月23日 下午7:20:13 
	 * @return PagingResult<Map<String,Object>>
	 *
	 * @param param
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String,Object>> searchPagingImport(Map<String,Object> param){
		
        return selectList( "stu.selectPagingImport", param ) ;
    }
	/**
	 * 得到其他的科目，过滤当前科目
	 */

	public List<Map<String, String>> getCourseList(List<String> courseCodeList) {
		Map<String, Object> param = new HashMap<>();
		
		param.put("courseCodeList", courseCodeList);
		return selectList("courseRefStu.getCourseList", param);
	}
	
	public List<Map<String, String>> getHasCourseList(List<String> courseCodeList) {
		Map<String, Object> param = new HashMap<>();
		
		param.put("courseCodeList", courseCodeList);
		return selectList("courseRefStu.getHasCourseList", param);
	}
	/**
	 * 根据学生id得到对应的科目
	 * 
	 * @param id
	 * @return
	 */
	public List<Map<String, String>> getCourseByXjfh(String sjfh) {
		return selectList("courseRefStu.getCourseByXjfh", sjfh);
	}
	
	/**
	 * @Title: getCousresByXjfh
	 * @Description: 根据学籍号得到学生相关科目，用于导出学生时得到相关的科目
	 * @author zhaohuanhuan
	 * @date 2016年12月5日 
	 * @param params
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getCousresByXjfh(Map<String, Object> params) {
		return selectList("courseRefStu.getCousresByXjfh", params);
	}
	
	

	//根据学籍号删除学生的科目
	public int removeCourseByXjfh(List<String> sfzjhArr) {
		return delete("courseRefStu.deleteCourseByXjfh", sfzjhArr);
	}
	
	
	/**
	 * @Title: getCourseByCourseCode
	 * @Description: 根据学科code得到学科名称
	 * @author zhaohuanhuan
	 * @date 2016年9月30日 
	 * @param courseCode
	 * @return
	 * @return String
	 */
	public String getCourseByCourseCode(String courseCode){
		return selectOne("courseRefStu.getCourseByCourseCode",courseCode);
	}
	/**
	 * 给当前学生添加科目
	 */
	public void stuReCourse(String sfzjh, String course) {
		Map<String, String> param = new HashMap<>();
		param.put("sfzjh", sfzjh);
		param.put("course", course);
		insert("courseRefStu.stuReCourse", param);
	}
	
	public void updateClassByStudentId(Map<String, Object> paramMap){
		update("stu.updateClassByStudentId", paramMap);
	}
	
	public void updateStudentClass(Map<String, Object> paramMap){
		update("stu.updateStudentClass", paramMap);
	}
	
	/**
	 * @Title: getSchoolCodeByLoginName
	 * @Description: 根据登录名得到学校code
	 * @author zhaohuanhuan
	 * @date 2016年8月10日 
	 * @param loginName
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getSchoolCodeByLoginName(String loginName){
		Map<String, String> param = new HashMap<>();
		param.put("loginName", loginName);
		return selectList("getInfoByLoginName.getSchoolCodeByLoginName", param);
	}
	
	/**
	 * @Title: selectSchoolNameBySchoolCode
	 * @Description: 根据schoolCode查询学校名
	 * @author xiahuajun
	 * @date 2016年9月28日 
	 * @param schoolCode
	 * @return
	 * @return String
	 */
	public String selectSchoolNameBySchoolCode(String schoolCode) {
		
		return selectOne("stu.getSchoolNameBySchoolCode",schoolCode);
	}
	/**
	 * 
	 * @Title: selectSchoolShortNameBySchoolCode
	 * @Description: 查询校名
	 * @author jay zhong
	 * @date 2017年11月27日 下午3:26:25 
	 * @return String
	 *
	 * @param schoolCode
	 * @return
	 */
	public String selectSchoolShortNameBySchoolCode(String schoolCode) {
		
		return selectOne("stu.getSchoolShortNameBySchoolCode",schoolCode);
	}
	
	/**
	 * @Title: addStudent
	 * @Description: 添加随班就读学生
	 * @author xiahuajun
	 * @date 2016年9月28日 
	 * @param paramMap
	 * @return void
	 */
	public void addStudent(Map<String, Object> paramMap) {
		insert("stu.addStudent",paramMap);
		
	}
	
	/**
	 * @Title: getSequenceBySchoolCode
	 * @Description: 根据学校code得到学校标识
	 * @author zhaohuanhuan
	 * @date 2016年9月30日 
	 * @param schoolCode
	 * @return
	 * @return String
	 */
	public String getSequenceBySchoolCode(String schoolCode){
		return selectOne("courseRefStu.getSequenceBySchoolCode", schoolCode);
	}
	
	/**
	 * 根据学生id得到对应的学校
	 * @param id
	 * @return
	 */
	public List<Map<String, String>> getSchoolByStuId(String id){
		Map<String, String> param=new HashMap<String, String>();
		param.put("id", id);
		return selectList("stu.getSchoolByStuId",id);
	}
	
	/**
	 * 根据学生id得到对应的班级
	 * @param id
	 * @return
	 */
	public List<Map<String, String>> getClassByStuId(String id){
		Map<String, String> param=new HashMap<String, String>();
		param.put("id", id);
		return selectList("stu.getClassByStuId",id);
	}
	
	/**
	 * 根据学生id得到对应的科目
	 * @param id
	 * @return
	 */
	public List<Map<String, String>> getCourseByStuId(Map<String, Object> param){
		return selectList("stu.getCourseByStuId",param);
	}
	
	/**   
	* @Title: StuManagementService.java 
	* @Description: 根据身份证件号得到学生所在班级id
	* @author zhaohuanhuan
	* @date 2017年1月3日 下午2:50:41 
	* @version V1.0   
	*/
	public List<Map<String, Object>> getClassByStuSfzjh(String sfzjh){
		return selectList("stu.getClassByStuSfzjh",sfzjh);
	}
	
	/**   
	* @Title: StuManagementService.java 
	* @Description: 得到学生原来所在学校
	* @author zhaohuanhuan
	* @date 2017年1月9日 下午3:35:44 
	* @return 
	*/
	public List<Map<String, Object>> getOldClassByStuSfzjh(Map<String, Object> paramMap){
		return selectList("stu.getOldClassByStuSfzjh",paramMap);
	}
	/**
	 * @Title: addRefStudentSchool
	 * @Description: //添加随班就读学生(学生学校关系表)
	 * @author xiahuajun
	 * @date 2016年10月10日 
	 * @param paramMap
	 * @return void
	 */
	public void addRefStudentSchool(Map<String, Object> paramMap) {
		insert("stu.addRefStudentSchool",paramMap);
	}
	
	/**   
	* @Title: StuManagementService.java 
	* @Description: TODO
	* @author zhaohuanhuan
	* @date 2017年1月4日 上午10:09:23 
	* @return void
	*/
	public void addRefStudentClass(Map<String, Object> paramMap) {
		insert("stu.addRefStudentClass",paramMap);
	}
	/**   
	* @Title: getCallOutStudentById
	* @Description: 得到调出的学生
	* @author zhaohuanhuan
	* @date 2017年1月3日 下午4:01:54 
	* @version V1.0   
	*/
	public String getCallOutStudentById(Map<String, Object> paramMap) {
		return selectOne("stu.getCallOutStudentById",paramMap);
	}
	
	/** 
	* @Title: updateStuStateForStudent 
	* @Description: 更新学生学籍状态 学生表
	* @param @param paramMap 
	* @author zhaohuanhuan
	* @return void   
	* @throws 
	*/
	public void updateStuStateForStudent(Map<String, Object> paramMap){
		update("stu.updateStuStateForStudent", paramMap);
	}
	
	
	/** 
	* @Title: updateStuStateForStudentRefSchool 
	* @Description: 更新学生学籍状态 学生学校关系表
	* @param @param paramMap 
	* @author zhaohuanhuan
	* @return void   
	* @throws 
	*/
	public void updateStuStateForStudentRefSchool(Map<String, Object> paramMap){
		update("stu.updateStuStateForStudentRefSchool", paramMap);
	}
	
	/** 
	* @Title: findClassDetail 
	* @Description: 根据考号查到对应学生所在的班级和类型
	* @param @param examNumber 
	* @author chenteng
	* @return HashMap   
	* @throws 
	*/
	public HashMap<String, String> findClassDetail(String examNumber) {
		Map<String, String> param=new HashMap<String, String>();
		param.put("examNumber", examNumber);
		return selectOne("stu.findClassDetail",examNumber);
	}

	public List<StuTempInfo> findInfoList(List<String> examNumList) {
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("examNumList", examNumList);
		return selectList("stu.findInfoList",param);
	}
	/**
	 * 
	 * @Title: selectClassNameByClassCode
	 * @Description: 获取班级名称
	 * @author jay zhong
	 * @date 2017年11月27日 下午4:08:14 
	 * @return String
	 *
	 * @param formatString
	 * @return
	 */
	public String selectClassNameByClassCode(String id) {
		
		return selectOne("stu.getClassNameByClassCode",id);
	}
}
