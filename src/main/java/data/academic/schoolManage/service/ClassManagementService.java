package data.academic.schoolManage.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;
/** 
* @ClassName: ClassManagementService 
* @Description: 班级管理服务层
* @author zhaohuanhuan
* @date 2017年1月12日
*  
*/
@Service
public class ClassManagementService extends AbstractService {
	
	/** 
	* @Title: searchAllClassPaging 
	* @Description: 分页显示班级
	* @param @param params
	* @param @param sortField
	* @param @param sort
	* @param @param currentPage
	* @param @param pageSize
	* @param @return 
	* @author zhaohuanhuan
	* @return PagingResult<Map<String,Object>>   
	* @throws 
	*/
	public PagingResult<Map<String,Object>> searchAllClassPaging(Map<String,Object> params, String sortField, String sort, int currentPage, int pageSize ){
		if( StringUtils.isBlank( sortField ) )
            sortField = "Grade_No,Class_No" ;
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging( "classManagement.searchAllClassPaging", params, sortField, sort, currentPage, pageSize ) ;
    }
	
	public List<Map<String,Object>> searchAllClassPagingImport(Map<String,Object> params){
        return selectList( "classManagement.searchAllClassPagingImport", params) ;
    }
	
	/** 
	* @Title: updateClassTypeByClassId 
	* @Description: 修改班级是否为新疆班
	* @param @param paramMap 
	* @author zhaohuanhuan
	* @return void   
	* @throws 
	*/
	public void updateClassTypeByClassId(Map<String, Object> paramMap){
		update("classManagement.updateClassTypeByClassId", paramMap);
	}
	
	/** 
	* @Title: searchStudentByClass 
	* @Description: 得到某个班级下面的所有学生
	* @param @param paramMap
	* @param @return 
	* @author zhaohuanhuan
	* @return List<Map<String,Object>>   
	* @throws 
	*/
	public List<String> searchStudentByClass(Map<String, Object> paramMap){
		return selectList("classManagement.searchStudentByClass", paramMap);
	}
	
	/** 
	* @Title: searchStudentByClass 
	* @Description: TODO
	* @param @param sfzjh
	* @param @return 
	* @author zhaohuanhuan
	* @return String   
	* @throws 
	*/
	public String searchStudentByClass(String sfzjh){
		return selectOne("classManagement.getStuPkBySfzjh", sfzjh);
	}
	
	/** 
	* @Title: getStudentByStuRefSchool 
	* @Description: TODO
	* @param @param paramMap
	* @param @return 
	* @author zhaohuanhuan
	* @return List<Map<String,Object>>   
	* @throws 
	*/
	public List<Map<String,Object>> getStudentByStuRefSchool(Map<String, Object> paramMap){
		return selectList("classManagement.getStudentByStuRefSchool", paramMap);
	}
	
}
