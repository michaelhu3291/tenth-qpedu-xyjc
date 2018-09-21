package data.academic.transfer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;
@Service
public class StudentTransferService extends AbstractService{
	
	/**
	 * @Title: serachPaging
	 * @Description: 分页查询待调动的 学生
	 * @author xiahuajun
	 * @date 2016年9月21日 
	 * @param requestMap
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	public PagingResult<Map<String, Object>> serachPaging(Map<String, Object> requestMap, String sortField, String sort,
			int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		if( StringUtils.isBlank(sortField)){
			sortField = "Create_Time" ;
		}
        if( StringUtils.isBlank( sort )){
        	sort = "desc" ;
        }
		return  selectPaging( "studentTransfer.selectPaging", requestMap, sortField, sort, currentPage, pageSize ) ;
	}
	
	public List<Map<String, Object>> serachImportPaging(Map<String, Object> requestMap,
			String state) {
		if("1".equals(state)){//学生
			return  selectList( "studentTransfer.selectImportPaging", requestMap) ;
		}else{//老师
			return  selectList( "teacherTransfer.selectImportPaging", requestMap) ;
		}
		
	}
//	
//	/**
//	 * @Title: submitApply
//	 * @Description: 本校管理员提交调度学生
//	 * @author xiahuajun
//	 * @date 2016年9月20日 
//	 * @param paramMap
//	 * @return void
//	 */
//	public void submitApply(List<Map<String, Object>> paramList) {
//		insert("studentTransfer.addrefStudentSchool", paramList);
//	}
//	
//	/**
//	 * @Title: updateStudentStateForStuTbl
//	 * @Description:修改学生状态 
//	 * @author xiahuajun
//	 * @date 2016年9月22日 
//	 * @param paramMap
//	 * @return void
//	 */
//	public void updateStudentStateForStuTbl(Map<String, Object> paramMap) {
//		// TODO Auto-generated method stub
//		update("studentTransfer.updateStudentStateForStuTable", paramMap);
//	}
//	
//	/**
//	 * @Title: findIsSubmitApply
//	 * @Description: 撤销前先查询有没有未提交的调度
//	 * @author xiahuajun
//	 * @date 2016年9月20日 
//	 * @param paramMap
//	 * @return
//	 * @return List<Map<String,Object>>
//	 */
//	public List<Map<String, Object>> findIsSubmitApply(Map<String, Object> paramMap) {
//		// TODO Auto-generated method stub
//		return selectList("studentTransfer.selectIsSubmit",paramMap);
//	}
//	
//	/**
//	 * @Title: cancelSubmitApply
//	 * @Description: 撤销调度申请
//	 * @author xiahuajun
//	 * @date 2016年9月20日 
//	 * @param paramMap
//	 * @return void
//	 */
//	public void cancelSubmitApply(Map<String, Object> paramMap) {
//		update("studentTransfer.cancelSubmitApply", paramMap);
//	}
//	
//	/**
//	 * @Title: addCreatePerson
//	 * @Description: 添加申请人和申请时间
//	 * @author xiahuajun
//	 * @date 2016年9月20日 
//	 * @param paramMap
//	 * @return void
//	 */
//	public void addCreatePerson(Map<String, Object> paramMap) {
//		
//		update("teacher.addCreatePerson", paramMap);
//	}
//	
//	/**
//	 * @Title: findIsAlreadyTransfer
//	 * @Description: 调动前先查询此老师有没有被调走
//	 * @author xiahuajun
//	 * @date 2016年9月20日 
//	 * @param paramMap
//	 * @return
//	 * @return List<Map<String,Object>>
//	 */
//	public List<Map<String, Object>> findIsAlreadyTransfer(Map<String, Object> paramMap) {
//		// TODO Auto-generated method stub
//		return selectList("studentTransfer.selectIsAlreadyTransfer",paramMap);
//	}
//	
//	
//	/**
//	 * @Title: findIsAlreadyApply
//	 * @Description: 防止重复申请
//	 * @author xiahuajun
//	 * @date 2016年9月21日 
//	 * @param paramMap
//	 * @return
//	 * @return List<Map<String,Object>>
//	 */
//	public List<Map<String, Object>> findIsAlreadyApply(Map<String, Object> paramMap) {
//		// TODO Auto-generated method stub
//		return selectList("studentTransfer.selectIsAlreadyApply",paramMap);
//	}
//	
//	/**
//	 * @Title: cancelApplyForRefStuSchool
//	 * @Description: 撤销调度申请(学生学校关系表)
//	 * @author xiahuajun
//	 * @date 2016年9月21日 
//	 * @param paramMap
//	 * @return void
//	 */
//	public void cancelApplyForRefStuSchool(Map<String, Object> paramMap) {
//		update("studentTransfer.updateRefStuTeacherState",paramMap);
//	}
//	
//	/**
//	 * @Title: submitTransferInfo
//	 * @Description: 为调动的学生添加与新学校的状态关系
//	 * @author xiahuajun
//	 * @date 2016年9月22日 
//	 * @param paramList
//	 * @return void
//	 */
//	public void submitTransferInfo(List<Map<String, Object>> paramList) {
//		// TODO Auto-generated method stub
//		insert("studentTransfer.addrefStudentState",paramList);
//	}
//	
//	/**
//	 * @Title: selectStuNameByStuPk
//	 * @Description: 根据Stu_Pk查询学生的名字
//	 * @author xiahuajun
//	 * @date 2016年9月22日 
//	 * @param str
//	 * @return
//	 * @return String
//	 */
//	public List<Map<String,Object>> selectStuInfoByStuPk(String str) {
//		// TODO Auto-generated method stub
//		return selectList("studentTransfer.selectStuInfoByStuPk",str);
//	}
//	
//	/**
//	 * @Title: updateTeacherState
//	 * @Description: 修改被调动学生在原学校的状态 state=2(调出)
//	 * @author xiahuajun
//	 * @date 2016年9月22日 
//	 * @param requestMap
//	 * @return void
//	 */
//	public void updateStudentState(Map<String, Object> requestMap) {
//		// TODO Auto-generated method stub
//		update("studentTransfer.updataStudentState",requestMap);
//	}
	
	/**
	 * @Title: findIsAlreadyApply
	 * @Description: 防止重复申请
	 * @author xiahuajun
	 * @date 2016年9月30日 
	 * @param paramMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> findIsAlreadyApply(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return selectList("studentTransfer.selectIsRepeatApply",paramMap);
	}
	
	/**
	 * @Title: findIsAlreadyTransfer
	 * @Description: 调动前先查询此学生有没有被调走
	 * @author xiahuajun
	 * @date 2016年9月30日 
	 * @param paramMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> findIsAlreadyTransfer(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return selectList("studentTransfer.selectIsAlreadyTransfer",paramMap);
	}
	
	/**
	 * @Title: submitApply
	 * @Description: 本校管理员提交调度学生
	 * @author xiahuajun
	 * @date 2016年9月30日 
	 * @param paramMap
	 * @return void
	 */
	public void submitApply(Map<String, Object> paramMap) {
		update("studentTransfer.updateStudentState", paramMap);
	}
	
	/**
	 * @Title: findIsSubmitApply
	 * @Description: 撤销前先查询有没有未提交的调动
	 * @author xiahuajun
	 * @date 2016年10月3日 
	 * @param paramMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> findIsSubmitApply(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return selectList("studentTransfer.selectIsSubmit",paramMap);
	}
	
	/**
	 * @Title: cancelSubmitApply
	 * @Description: 撤销调度申请
	 * @author xiahuajun
	 * @date 2016年10月3日 
	 * @param paramMap
	 * @return void
	 */
	public void cancelSubmitApply(Map<String, Object> paramMap) {
		update("studentTransfer.cancelSubmitApply", paramMap);
	}
	
	/**
	 * @Title: addCreatePerson
	 * @Description: 添加申请人和申请时间
	 * @author xiahuajun
	 * @date 2016年9月30日 
	 * @param paramMap
	 * @return void
	 */
	public void addCreatePerson(Map<String, Object> paramMap) {
		
		update("studentTransfer.addCreatePerson", paramMap);
	}
	
	/**
	 * @Title: submitTransferInfo
	 * @Description: 提交调动 
	 * @author xiahuajun
	 * @date 2016年10月5日 
	 * @param requestMap
	 * @return void
	 */
	public void submitTransferInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> requestList = new ArrayList<>();
		requestList.add(map);
		insert("studentTransfer.addrefStudentSchool",requestList);
	}
	
	/**
	 * @Title: updateTeacherState
	 * @Description: 修改被调动学生在原学校的状态 state=2(调出)
	 * @author xiahuajun
	 * @date 2016年10月5日 
	 * @param requestMap
	 * @return void
	 */
	public void updateStudentState(String str) {
		Map<String,List<String>> requestMap = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<>();
		list.add(str);
		requestMap.put("studentPk", list);
		update("studentTransfer.updataStudentState",requestMap);
	}
	
	/**
	 * @Title: selectTeacherSchoolCode
	 * @Description: 查询code校验学生是不是调回原校籍
	 * @author xiahuajun
	 * @date 2016年10月10日 
	 * @param string
	 * @return
	 * @return String
	 */
	public String selectTeacherSchoolCode(String studentPk) {
		// TODO Auto-generated method stub
		return selectOne("studentTransfer.selectStudentSchoolCode",studentPk);
	}
	
	/**
	 * @Title: deleterefTeacherSchool
	 * @Description: 删除该学生和其它学校的关系表数据
	 * @author xiahuajun
	 * @date 2016年10月10日 
	 * @param string
	 * @return void
	 */
	public void deleterefStudentSchool(String studentPk) {
		delete("studentTransfer.deleterefStudentSchool",studentPk);
	}
	
	/**
	 * @Title: insertRefTeacherSchool
	 * @Description: 添加一条状态为0的新数据
	 * @author xiahuajun
	 * @date 2016年10月10日 
	 * @param requestMap
	 * @return void
	 */
	public void insertRefStudentSchool(Map<String, Object> requestMap) {
		insert("studentTransfer.insertStateRefStudentSchool",requestMap);
		
	}
	

	public PagingResult<Map<String, Object>> searchStuHistoryPaging(Map<String, Object> map,String sortField, String sort,int currentPage, int pageSize){
		if( StringUtils.isBlank(sortField)){
			sortField = "Apply_Time" ;
		}
        if( StringUtils.isBlank( sort )){
        	sort = "desc" ;
        }
        return selectPaging("studentTransfer.getTransferHistory", map, sortField, sort, currentPage, pageSize);
	}
	
	public List<Map<String, Object>> importStuHistoryPaging(Map<String, Object> map){
		
        return selectList("studentTransfer.importTransferHistory", map);
	}
	
	public String getSchoolName(Map<String,Object> map){
		return selectOne("studentTransfer.getSchoolName", map);
	}	

	/**
	 * @Title: getStuAndTeacherTrans
	 * @Description: 得到待调动的学生和老师 
	 * @author zhaohuanhuan
	 * @date 2016年12月6日 
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public  List<Map<String,Object>> getStuAndTeacherTrans(){
		return selectList("studentTransfer.getStuAndTeacherTrans");
	}
	
	public String getStudentByPid(Map<String, Object> parameter){
		return selectOne("studentTransfer.getStudentByPid", parameter);
	}
	
	public String getTeacherByPid(Map<String, Object> parameter){
		return selectOne("studentTransfer.getTeacherByPid", parameter);
	}
	
	public void insertTaskRamking(Map<String, Object> map) {
		 insert("studentTransfer.insertTaskRamking",map);
	}
	
	public void removeTransferPson(Map<String, Object> map) {
		delete("studentTransfer.removeTransferPson",map);
	}
	
	/**   
	* @Title:getIdCardByStuPk
	* @Description: 得到调动学生的身份证件号
	* @author zhaohuanhuan
	* @date 2017年1月4日 
	*  
	*/
	public  List<Map<String, Object>>  getIdCardByStuPk(Map<String, Object> requesMap){
		return selectList("stu.getIdCardByStuPk",requesMap);
	}
}
