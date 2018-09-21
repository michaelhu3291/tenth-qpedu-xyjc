package data.academic.schoolManage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;

/**
 * @Title: PoliticalInstructorService
 * @Description: 教导员服务层
 * @author zhaohuanhuan
 * @date 2016年9月20日 下午5:46:49
 */
@Service
public class PoliticalInstructorService extends AbstractService {
	
	/**
	 * @Title: searchPaging
	 * @Description:分页
	 * @author zhaohuanhuan
	 * @date 2016年9月21日 
	 * @param orgId
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	public PagingResult<Map<String,Object>> searchPaging(Map<String,Object> params, String sortField, String sort, int currentPage, int pageSize ){
		if( StringUtils.isBlank( sortField ) )
            sortField = "USER_UID" ;
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging( "politicalInstructor.selectPaging", params, sortField, sort, currentPage, pageSize ) ;
    }
	
	/**
	 * @Title: selectSchoolCodeByLoginName
	 * @Description: 根据登录名找到所属机构id
	 * @author zhaohuanhuan
	 * @date 2016年9月21日 
	 * @param username
	 * @return
	 * @return String
	 */
	public String selectOrgIdByLoginName(String username) {
		return selectOne("politicalInstructor.selectOrgIdByLoginName", username);
	}
	
	public String selectUserUidByUserId(String userId) {
		return selectOne("politicalInstructor.selectUserUidByUserId", userId);
	}
	
	/**
	 * @Title: addPoliticalInstructor
	 * @Description: 添加教导员
	 * @author zhaohuanhuan
	 * @date 2016年9月21日 
	 * @param paramMap
	 * @return void
	 */
	public void addPoliticalInstructor(Map<String, Object> paramMap) {
		insert("politicalInstructor.insertPoliticalInstructor", paramMap);
	}
	/**
	 * @Title: addPoliticalInstructorRole
	 * @Description: 给教导员赋予角色权限
	 * @author zhaohuanhuan
	 * @date 2016年9月21日 
	 * @param paramMap
	 * @return void
	 */
	public void addPoliticalInstructorRole(String userId) {
		insert("politicalInstructor.addPoliticalInstructorRole", userId);
	}
	
	/**
	 * @Title: updatePoliticalInstructor
	 * @Description: 更新教导员
	 * @author zhaohuanhuan
	 * @date 2016年9月21日 
	 * @param paramMap
	 * @return void
	 */
	public void updatePoliticalInstructor(Map<String, Object> paramMap) {
		update("politicalInstructor.updatePoliticalInstructor",paramMap);
	
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
	 * @Title: deletePoliticalInstructorRole
	 * @Description: 删除教导员角色
	 * @author zhaohuanhuan
	 * @date 2016年9月21日 
	 * @param idAry
	 * @return
	 * @return int
	 */
	public int deletePoliticalInstructorRole(List<String> idAry) {
		 Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "idAry", idAry ) ;
        return delete("districtSubjectInstructorInfo.deleteDistrictSubjectInstructorRole", param );
	
	}
	
	 
	/**
	 * @Title: deletePoliticalInstructor
	 * @Description: 删除教导员
	 * @author zhaohuanhuan
	 * @date 2016年9月21日 
	 * @param idAry
	 * @return
	 * @return int
	 */
	public int deletePoliticalInstructor(List<String> idAry) {
		 Map<String,Object> param = new HashMap<String,Object>() ;
       param.put( "idAry", idAry ) ;
       return delete("districtSubjectInstructorInfo.deleteDistrictSubjectInstructor", param );
	
	}
	
	/**
	 * @Title: loadPoliticalInstructor
	 * @Description: 根据id加载教导员
	 * @author zhaohuanhuan
	 * @date 2016年9月21日 
	 * @param id
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> loadPoliticalInstructor(String id)
	{	
		return selectList("politicalInstructor.loadPoliticalInstructor",id);
	}
	
	/**
	 * @Title: getSchoolShortNameBySchoolCode
	 * @Description: 根据当前教导员的学校code得到 学校简称
	 * @author zhaohuanhuan
	 * @date 2016年9月29日 
	 * @param schoolCode
	 * @return
	 * @return String
	 */
	public String getSchoolShortNameBySchoolCode(String schoolCode){
		return selectOne("politicalInstructor.getSchoolShortNameBySchoolCode", schoolCode);
	}
}
