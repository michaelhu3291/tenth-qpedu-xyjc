/**
 * 2016年9月12日
 */
package data.academic.statisticsAnalysis.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;

/**
 * @Title: ClassRoomTeacherService
 * @Description: TODO
 * @author zhaohuanhuan
 * @date 2016年9月12日 下午1:57:47
 */
@Service
public class ClassRoomTeacherService extends AbstractService{

	public PagingResult<Map<String, Object>> ScoreSearch(Map<String, Object> requestMap, String sortField, String sort,
			int currentPage, int pageSize) {
		if (StringUtils.isBlank(sortField)) {
			sortField = "Exam_Number";
		}
		if (StringUtils.isBlank(sort)) {
			sort = "ASC";
		} 
		return selectPaging("classRoomTeacher.crtSelectScoreSearchPaging", requestMap, sortField, sort, currentPage, pageSize);
	}
}
