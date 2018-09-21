/**
 * 2016年10月21日
 */
package data.academic.examNumberManagement.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;

/**
 * @Title: AdminExamNumberService
 * @Description: 青浦管理员考号管理服务层
 * @author zhaohuanhuan
 * @date 2016年10月21日 下午3:46:58
 */
@Service
public class AdminExamNumberService extends AbstractService {

    /**
     * @Title: getSchoolByExamCode
     * @Description: 得到青浦管理员发布的考试下的学校
     * @author zhaohuanhuan
     * @date 2016年10月21日 
     * @param examCode
     * @return
     * @return List<Map<String,Object>>
     */
    public List<Map<String, Object>> getSchoolByExamCode (String examCode){
    	return selectList("adminExamNumberManage.getSchoolByExamCode", examCode);
    }
  
    
    /**
	 * @Title: testCastGeneration
	 * @Description: 学校生成考号情况分页查询（区级查看）
	 * @author zhaohuanhuan
	 * @date 2016年10月24日 
	 * @param examCode
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public PagingResult<Map<String,Object>> testCastGenerationSearchPading(Map<String,Object> params, String sortField, String sort, int currentPage, int pageSize ){
		if( StringUtils.isBlank( sortField ) )
            sortField = "Is_Exist_Exam_Number,Brevity_Code" ;
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging( "examNumberManage.testCastGeneration", params, sortField, sort, currentPage, pageSize ) ;
    }
	
	/**
	 * @Title: getSchoolCodeAndExistNumber
	 * @Description: 得到生成考号的学校code
	 * @author zhaohuanhuan
	 * @date 2016年10月26日 
	 * @param examCode
	 * @return
	 * @return List<String>
	 */
	public List<String> getSchoolCodeAndExistNumber(String examCode){
		return selectList("examNumberManage.getSchoolCodeAndExistNumber", examCode);
	}
	
	/**
	 * @Title: countSchoolByExamCode
	 * @Description: 根据考试编号的到学校
	 * @author zhaohuanhuan
	 * @date 2016年11月16日 
	 * @param params
	 * @return
	 * @return Integer
	 */
	public Integer countSchoolByExamCode(Map<String, Object> params){
		return selectOne("examNumberManage.countSchoolByExamCode", params);
	}
}
