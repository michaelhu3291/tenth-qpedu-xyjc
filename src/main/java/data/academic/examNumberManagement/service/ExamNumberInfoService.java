/**
 * 2016年9月21日
 */
package data.academic.examNumberManagement.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;

/**
 * @Title: ExamNumberInfoService
 * @Description: 
 * @author zhaohuanhuan
 * @date 2016年9月21日 下午9:49:04
 */
@Service
public class ExamNumberInfoService  extends  AbstractService{
	
	
	/**
	 * @Title: searchPaging
	 * @Description:学生考号信息列表分页查询
	 * @author zhaohuanhuan
	 * @date 2016年9月22日 
	 * @param params
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	
	public PagingResult<Map<String,Object>> searchPaging(Map<String,Object> params, String sortField, String sort, int currentPage, int pageSize ){
		if( StringUtils.isBlank( sortField ) )
            sortField = "school,Class_No,Exam_Number" ;
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging( "examNumberManage.getStuNumberInfoBySchoolCode", params, sortField, sort, currentPage, pageSize ) ;
    }
	
}
