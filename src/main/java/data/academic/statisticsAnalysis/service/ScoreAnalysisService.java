package data.academic.statisticsAnalysis.service;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;


/**
 * @Title: ScoreAnalysisController
 * @Description: 成绩分析服务层
 * @author zhaohuanhuan
 * @date 2016年8月30日
 */

@Service
public class ScoreAnalysisService extends AbstractService {

	/**
	 * @Title: ScoreSearch
	 * @Description: 多条件分页分析成绩
	 * @author zhaohuanhuan
	 * @date 2016年9月5日 
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
			sortField = "sAvg";
		}
		if (StringUtils.isBlank(sort)) {
			sort = "DESC";
		} 
		return selectPaging("scoreAnalusis.getScoreByCorse", requestMap, sortField, sort, currentPage, pageSize);
	}

	
	
	/**
	 * @Title: getScoreByCourses
	 * @Description: 多条件查询得到四率
	 * @author zhaohuanhuan
	 * @date 2016年9月5日 
	 * @param requestMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getScoreByCourses(Map<String, Object> requestMap) {
		return selectList("scoreAnalusis.getScoreByCorse", requestMap);
	}
	
}
