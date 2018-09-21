package data.academic.transfer.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;
@Service
public class TeacherTransferService extends AbstractService{
	
	/**
	 * @Title: serachPaging
	 * @Description: 分页查询待调动的老师
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
		return  selectPaging( "teacherTransfer.selectPaging", requestMap, sortField, sort, currentPage, pageSize ) ;
	}
	
	/**
	 * @Title: selectAllSchools
	 * @Description: 查询所有的学校
	 * @author xiahuajun
	 * @date 2016年9月20日 
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> selectAllSchools(Map<String,Object> requestMap) {
		return selectList("teacherTransfer.selectAllSchools",requestMap);
	}
	
	/**
	 * @Title: submitTransferInfo
	 * @Description: 提交调动 
	 * @author xiahuajun
	 * @date 2016年9月20日 
	 * @param requestMap
	 * @return void
	 */
	public void submitTransferInfo(Map<String, Object> requestMap) {
		// TODO Auto-generated method stub
		insert("teacherTransfer.addrefTeacherSchool",requestMap);
	}
	
	/**
	 * @Title: updateTeacherState
	 * @Description: 修改被调动老师在原学校的状态 state=2(调出)
	 * @author xiahuajun
	 * @date 2016年9月20日 
	 * @param requestMap
	 * @return void
	 */
	public void updateTeacherState(Map<String, Object> requestMap) {
		update("teacherTransfer.updataTeacherState",requestMap);
	}
	
	/**
	 * @Title: selectTeacherSchoolCode
	 * @Description: 查询code校验教师是不是调回原校籍
	 * @author xiahuajun
	 * @date 2016年10月10日 
	 * @param string
	 * @return
	 * @return String
	 */
	public String selectTeacherSchoolCode(String teacherPk) {
		// TODO Auto-generated method stub
		return selectOne("teacherTransfer.selectTeacherSchoolCode",teacherPk);
	}
	
	/**
	 * @Title: deleterefTeacherSchool
	 * @Description: 删除该老师和其它学校的关系表数据
	 * @author xiahuajun
	 * @date 2016年10月10日 
	 * @param string
	 * @return void
	 */
	public void deleterefTeacherSchool(String teacherPk) {
		delete("teacherTransfer.deleterefTeacherSchool",teacherPk);
	}
	
	/**
	 * @Title: insertRefTeacherSchool
	 * @Description: 添加一条状态为0的新数据
	 * @author xiahuajun
	 * @date 2016年10月10日 
	 * @param requestMap
	 * @return void
	 */
	public void insertRefTeacherSchool(Map<String, Object> requestMap) {
		insert("teacherTransfer.insertStateRefTeacherSchool",requestMap);
		
	}
	
	/**
	 * @Title: deleteRefTeaCourse
	 * @Description: 调动成功后清除老师关联的科目
	 * @author xiahuajun
	 * @date 2016年11月1日 
	 * @param requestMap
	 * @return void
	 */
	public void deleteRefTeaCourse(Map<String, Object> requestMap) {
		delete("teacherTransfer.deleteRefTeaCourse",requestMap);
	}
	
	/**
	 * @Title: deleteRefTeaClass
	 * @Description: 调动成功后清除老师关联的班级
	 * @author xiahuajun
	 * @date 2016年11月1日 
	 * @param requestMap
	 * @return void
	 */
	public void deleteRefTeaClass(Map<String, Object> requestMap) {
		delete("teacherTransfer.deleteRefTeaClass",requestMap);
	}
	
	
	public PagingResult<Map<String, Object>> searchTeaHistoryPaging(Map<String, Object> map,String sortField, String sort,int currentPage, int pageSize){
		if( StringUtils.isBlank(sortField)){
			sortField = "Apply_Time" ;
		}
        if( StringUtils.isBlank( sort )){
        	sort = "desc" ;
        }
        return selectPaging("studentTransfer.getTransferHistory", map, sortField, sort, currentPage, pageSize);
	}
	
	
	public void insertHistory(Map<String, Object> requesMap){
		insert("studentTransfer.insertHistory", requesMap);
	}
	
	
	public String getSchoolByState(Map<String, Object> requesMap){
		return selectOne("teacherTransfer.getTeaSchoolByState",requesMap);
	}
	
	
	public String getStuSchoolByState(Map<String, Object> requesMap){
		return selectOne("studentTransfer.getStuSchoolByState",requesMap);
	}
	
	
	
}