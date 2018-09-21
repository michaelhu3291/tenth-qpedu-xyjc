package data.academic.scoreManage.service;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;

@Service
public class HistoryScoreService extends AbstractService{

	public PagingResult<Map<String, Object>> serachAllStudentsScorePaging(Map<String, Object> requestMap,
			String sortField, String sort, int currentPage, int pageSize) {
		if (StringUtils.isBlank(sortField)) {
			sortField = "School_Code,Grade_Id,Class_Id";
		}
		if (StringUtils.isBlank(sort)) {
			sort = "ASC";
		} 
		return selectPaging("historyScore.selectScorePagging", requestMap, sortField, sort, currentPage, pageSize);
	}
	
	/**
	 * @Title: getclassesByGradeCode
	 * @Description: 选择年级查询班级
	 * @author xiahuajun
	 * @date 2016年10月29日 
	 * @param requestMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getclassesByGradeCode(Map<String, Object> requestMap) {
		return selectList("historyScore.getclassesByGradeCode",requestMap);
	}
	
	/**
	 * @Title: selectExportHistoryScoreForTeacherAndAdmin
	 * @Description: 老师和学校管理员查询历史成绩导出excel
	 * @author xiahuajun
	 * @date 2016年11月6日 
	 * @param requestMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> selectExportHistoryScoreForTeacherAndAdmin(Map<String, Object> requestMap) {
		return selectList("historyScore.selectExportHistoryScoreForTeacherAndAdmin",requestMap);
	}
	
	/**
	 * @Title: selectExportHistoryScoreForqpAdminAndInstrutor
	 * @Description: 教研员和青浦超级管理员查询历史成绩导出excel
	 * @author xiahuajun
	 * @date 2016年11月7日 
	 * @param requestMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> selectExportHistoryScoreForqpAdminAndInstrutor(Map<String, Object> requestMap) {
		return selectList("historyScore.selectExportHistoryScoreForqpAdminAndInstrutor",requestMap);
	}

	
	
	
}
