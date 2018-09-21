package data.academic.statisticsAnalysis.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;

/**
 * @Title: SubTeaScoreSearchService
 * @Description:科任老师查询成绩服务层
 * @author zhaohuanhuan
 * @date 2016年9月6日 上午11:21:40
 */
@Service
public class SchoolPlainAdminScoreSearchService extends AbstractService {
   
	/**
	 * @Title: ScoreSearch
	 * @Description: 分页显示科任老师查询的成绩
	 * @author zhaohuanhuan
	 * @date 2016年9月8日 
	 * @param requestMap
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	public PagingResult<Map<String, Object>> ScoreSearch(Map<String, Object> requestMap, String sortField, String sort,
			int currentPage, int pageSize) {
		if (StringUtils.isBlank(sortField)) {
			sortField = "Exam_Number";
		}
		if (StringUtils.isBlank(sort)) {
			sort = "ASC";
		} 
		return selectPaging("subTeacher.selectSubTeaScoreSearchPaging", requestMap, sortField, sort, currentPage, pageSize);
	}
	
	/**
	 * @Title: findSchoolCodeAndASchoolName
	 * @Description: 查询学校code和学校名称
	 * @author xiahuajun
	 * @date 2016年9月17日 
	 * @param schoolCode
	 * @return void
	 */
	public Map<String,Object> findSchoolCodeAndASchoolName(String schoolCode) {
		return selectOne("platformDataDictionary.selectSchoolNameBySchoolCode",schoolCode);
		
	}
	
	/**
	 * @Title: serachSlAvgPaging
	 * @Description: 学校普通管理员查询四率及平均分
	 * @author xiahuajun
	 * @date 2016年9月17日 
	 * @param requestMap
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	/*public PagingResult<Map<String, Object>> serachSlAvgPaging(Map<String, Object> requestMap, String sortField,
			String sort, int currentPage, int pageSize) {
		return selectPaging("platformUser.getSiLvTableForSchoolPlainAdmin", requestMap, sortField, sort, currentPage, pageSize);
			
	}*/
	
	/**
	 * @Title: findSchoolTypeBySchoolCode
	 * @Description:查询学校类型
	 * @author xiahuajun
	 * @date 2016年9月17日 
	 * @param schoolCode
	 * @return
	 * @return Map<String,Object>
	 */
	public String findSchoolTypeBySchoolCode(String schoolCode) {
		return selectOne("platformUser.selectSchoolSequenceBySchoolCode",schoolCode);
	}
	
	/**
	 * @Title: serachAllStudentsScorePaging
	 * @Description:教导处查询所有学生成绩 
	 * @author xiahuajun
	 * @date 2016年9月18日 
	 * @param requestMap
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	public PagingResult<Map<String, Object>> serachAllStudentsScorePaging(Map<String, Object> requestMap,
			String sortField, String sort, int currentPage, int pageSize) {
		if (StringUtils.isBlank(sortField)) {
			sortField = "Exam_Number";
		}
		if (StringUtils.isBlank(sort)) {
			sort = "DESC";
		} 
		return selectPaging("platformUser.selectAllStudentsScore", requestMap, sortField, sort, currentPage, pageSize);
	}
	
	/**
	 * @Title: selectAvgScore
	 * @Description: 教导处查询平均分
	 * @author xiahuajun
	 * @date 2016年9月18日 
	 * @param map
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> selectAvgScore(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return selectList("platformUser.selectAvgScore",map);
	}
	
	/**
	 * @Title: selectIntervalStuCountForSchoolPlainAdmin
	 * @Description: 教导处查询分数段学生人数
	 * @author xiahuajun
	 * @date 2016年9月18日 
	 * @param map
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> selectIntervalStuCountForSchoolPlainAdmin(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return selectList("platformUser.selectIntervalStuCountforSchoolPlainAdmin",map);
	}
	
	/**
	 * @Title: selectExportScoreDataForSchoolAdmin
	 * @Description: 教导员查询成绩导出excel
	 * @author xiahuajun
	 * @date 2016年11月5日 
	 * @param requestMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> selectExportScoreDataForSchoolAdmin(Map<String, Object> requestMap) {
		return selectList("scoreSearch.selectAllStudentsScoreForSchoolAdmin",requestMap);
	}
	
	
	
	
}
