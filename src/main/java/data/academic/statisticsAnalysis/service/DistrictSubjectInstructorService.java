package data.academic.statisticsAnalysis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;

/**
 * @Title: DistrictSubjectInstructorService
 * @Description: 学科教研员服务层
 * @author zhaohuanhuan
 * @date 2016年9月14日 上午9:34:09
 */
@Service
public class DistrictSubjectInstructorService extends AbstractService {

	
	/**
	 * @Title: courseScoreSearch
	 * @Description: 学科教研员查询成绩分页显示
	 * @author zhaohuanhuan
	 * @date 2016年9月18日 
	 * @param requestMap
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	public PagingResult<Map<String, Object>> courseScoreSearch(Map<String, Object> requestMap, String sortField, String sort,
			int currentPage, int pageSize) {
		if (StringUtils.isBlank(sortField)) {
			sortField = "School_Code,Class_Id";
		}
		if (StringUtils.isBlank(sort)) {
			sort = "ASC";
		} 
		return selectPaging("districtSubjectInstructor.getCourseScoreByCourse", requestMap, sortField, sort, currentPage, pageSize);
	}
	

	
	

	/**
	 * @Title: getSubjectInstrutorAvg
	 * @Description: 得到教研员学科平均分
	 * @author zhaohuanhuan
	 * @date 2016年9月19日 
	 * @param param
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getSubjectInstrutorAvg(Map<String, Object> param){
		return selectList("districtSubjectInstructor.getSubjectInstrutorAvg", param);
	}

	public List<Map<String, Object>> getSubjectInstrutorsAvg(Map<String, Object> param){
		return selectList("districtSubjectInstructor.getSubjectInstrutorsAvg", param);
	}
	
	
	/**
	 * @Title: getCourseByLoginName
	 * @Description: 根据当前用户得到用户的学科
	 * @author zhaohuanhuan
	 * @date 2016年9月18日 
	 * @param loginName
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getCourseByLoginName(String loginName){
		Map<String, Object> params=new HashMap<>();
		params.put("loginName", loginName);
		return selectList("districtSubjectInstructor.getCourseByLoginName", params);
	}
	/**
	 * @Title: getPeriodByLoginName
	 * @Description: 根据登录名得到学段
	 * @author zhaohuanhuan
	 * @date 2016年9月19日 
	 * @param data
	 * @param out
	 * @return void
	 */
	public List<Map<String, Object>> getPeriodByLoginName(String loginName) {
		return selectList("districtSubjectInstructorInfo.getPeriodByLoginName", loginName);
	}
	
	/**
	 * @Title: getSubjectInstrutorSiLv
	 * @Description: 得到教研员学科四率
	 * @author zhaohuanhuan
	 * @date 2016年9月19日 
	 * @param params
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getSubjectInstrutorSiLv(Map<String, Object> params){
		return selectList("districtSubjectInstructor.getSubjectInstrutorSiLv", params);
	}
	
	public List<Map<String, Object>> getSubjectInstrutorsSiLv(Map<String, Object> params){
		return selectList("districtSubjectInstructor.getSubjectInstrutorsSiLv", params);
	}


	/**
	 * @Title: getSubjectPoliticalInstrutorsAvg
	 * @Description: 学校教导员查询平均分
	 * @author xiahuajun
	 * @date 2016年10月9日 
	 * @param requestMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getSubjectPoliticalInstrutorsAvg(Map<String, Object> requestMap) {
		// TODO Auto-generated method stub
		return selectList("politicalInstructor.getPoliticalInstructorAvg", requestMap);
	}

	/**
	 * @Title: getSubjectPoliticalInstrutorsSiLv
	 * @Description:学校教导员查询四率 
	 * @author xiahuajun
	 * @date 2016年10月9日 
	 * @param requestMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getSubjectPoliticalInstrutorsSiLv(Map<String, Object> requestMap) {
		return  selectList("politicalInstructor.getPoliticalInstructorSiLv", requestMap);
	}

	/**
	 * @Title: selectGradesByLoginName
	 * @Description: 根据登录名查询当前教研员所关联的年级
	 * @author xiahuajun
	 * @date 2016年10月16日 
	 * @param loginName
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> selectGradesByLoginName(String loginName) {
		// TODO Auto-generated method stub
		return selectList("politicalInstructor.selectGradesByLoginName", loginName);
	}
}
