/**
 * 2016年9月14日
 */
package data.academic.statisticsAnalysis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;

/**
 * @Title: DistrictSubjectInstructorInfoService
 * @Description: 全区学科教研员信息服务层
 * @author zhaohuanhuan
 * @date 2016年9月14日 下午2:12:53
 */
@Service
public class DistrictSubjectInstructorInfoService extends AbstractService{

	/**
	 * @Title: selectDistrictSubInstructorPaging
	 * @Description: 分页显示学科教研员
	 * @author zhaohuanhuan
	 * @date 2016年9月20日 
	 * @param requestMap
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	public PagingResult<Map<String, Object>> selectDistrictSubInstructorPaging(Map<String, Object> requestMap, String sortField,
			String sort, int currentPage, int pageSize) {
		 if(StringUtils.isBlank( sortField )){
			 //sortField = "Exam_Number" ;
			 sortField = "USER_UID";
		 }
	     if(StringUtils.isBlank( sort )){
	    	 sort = "DESC";
	     }else{
	    	 sort = "DESC";
	     }
	     return selectPaging( "districtSubjectInstructorInfo.selectDistrictSubInstructorPaging", requestMap, sortField, sort, currentPage, pageSize ) ;
	}
	
	
	/**
	 * @Title: addUser
	 * @Description: 添加教研员
	 * @author zhaohuanhuan
	 * @date 2016年9月21日 
	 * @param paramMap
	 * @return void
	 */
	public void addUser(Map<String, Object> paramMap) {
		insert("districtSubjectInstructorInfo.insertDistrictSubjectInstructorInfo", paramMap);
	}
	
	/**
	 * @Title: updateSubjectInstructor
	 * @Description: 修改学科教研员
	 * @author zhaohuanhuan
	 * @date 2016年9月20日 
	 * @param paramMap
	 * @return void
	 */
	public void updateSubjectInstructor(Map<String, Object> paramMap) {
			update("districtSubjectInstructorInfo.updateDistrictSubjectInstructor",paramMap);
		
	}
	
	
	
	
	/**
	 * @Title: getPeriodByLoginName
	 * @Description: 根据用户名得到学段
	 * @author zhaohuanhuan
	 * @date 2016年9月21日 
	 * @param loginName
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getPeriodByLoginName(String loginName) {
		return selectList("districtSubjectInstructorInfo.getPeriodByLoginName", loginName);
	}
	
	/**
	 * @Title: addDistrictSubjectInstructorRole
	 * @Description: 给学科教研员赋予角色权限
	 * @author zhaohuanhuan
	 * @date 2016年9月20日 
	 * @param userId
	 * @return void
	 */
	public void addDistrictSubjectInstructorRole(String userId) {
		insert("districtSubjectInstructorInfo.addDistrictSubjectInstructorRole", userId);
	}
	
	/**
	 * @Title: addDistrictInstructorRole
	 * @Description: 给教研员赋予角色权限
	 * @author zhaohuanhuan
	 * @date 2016年10月8日 
	 * @param userId
	 * @return void
	 */
	public void addDistrictInstructorRole(String userId) {
		insert("districtSubjectInstructorInfo.addDistrictInstructorRole", userId);
	}
	
	/**
	 * @Title: loadDistrictSubjectInstructor
	 * @Description: 根据id得到加载教研员
	 * @author zhaohuanhuan
	 * @date 2016年9月20日 
	 * @param id
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> loadDistrictSubjectInstructor(String id)
	{	
		return selectList("districtSubjectInstructorInfo.loadDistrictSubjectInstructor",id);
	}
	
	
	/**
	 * @Title: getInfoByLoginName
	 * @Description: 用于校验用户名是否已存在
	 * @author zhaohuanhuan
	 * @date 2016年9月20日 
	 * @param loginName
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getInfoByLoginName(String loginName) {
		return selectList("districtSubjectInstructorInfo.getInfoByLoginName", loginName);
	}
	
	/**
	 * @Title: deleteDistrictSubjectInstructorRole
	 * @Description: 删除教研员的角色
	 * @author zhaohuanhuan
	 * @date 2016年9月20日 
	 * @param idAry
	 * @return
	 * @return int
	 */
	public int deleteDistrictSubjectInstructorRole(List<String> idAry) {
		 Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "idAry", idAry ) ;
        return delete("districtSubjectInstructorInfo.deleteDistrictSubjectInstructorRole", param );
	
	}
	
	/**
	 * @Title: deleteDistrictSubjectInstructor
	 * @Description: 删除教研员
	 * @author zhaohuanhuan
	 * @date 2016年9月20日 
	 * @param idAry
	 * @return
	 * @return int
	 */
	public int deleteDistrictSubjectInstructor(List<String> idAry) {
		 Map<String,Object> param = new HashMap<String,Object>() ;
       param.put( "idAry", idAry ) ;
       return delete("districtSubjectInstructorInfo.deleteDistrictSubjectInstructor", param );
	
	}
	
	
	public String selectUserUidByUserId(String userId) {
		return selectOne("politicalInstructor.selectUserUidByUserId", userId);
	}

	/**
	 * @Title: addRefInstractorGrade
	 * @Description: 添加教研员年级关系表
	 * @author xiahuajun
	 * @date 2016年10月16日 
	 * @param list
	 * @return void
	 */
	public void addRefInstractorGrade(List<Map<String, Object>> list) {
		insert("districtSubjectInstructorInfo.addRefInstractorGrade",list);
		
	}

	/**
	 * @Title: deleteRefInstructorGrade
	 * @Description: 清除教研员和年级关系表
	 * @author xiahuajun
	 * @date 2016年10月17日 
	 * @param paramMap
	 * @return void
	 */
	public void deleteRefInstructorGrade(Map<String, Object> paramMap) {
		delete("districtSubjectInstructorInfo.deleteRefInstructorGrade",paramMap);
		
	}
}
