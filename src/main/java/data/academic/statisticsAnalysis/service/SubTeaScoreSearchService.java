package data.academic.statisticsAnalysis.service;

import java.util.HashMap;
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
public class SubTeaScoreSearchService extends AbstractService {
   
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
	 * @Title: getGradeAndClassByLoginName
	 * @Description: 根据登录名得到用户所带年级和班级
	 * @author zhaohuanhuan
	 * @date 2016年9月8日 
	 * @param loginName
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getGradeAndClassByLoginName(String loginName) {
		return selectList("getInfoByLoginName.getGradeAndClassByLoginName", loginName);
	}
	
	/**
	 * @Title: getCourseByLoginName
	 * @Description: 根据登录名得到用户所带科目
	 * @author zhaohuanhuan
	 * @date 2016年9月8日 
	 * @param loginName
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getCourseByLoginName(String loginName) {
		return selectList("getInfoByLoginName.getCourseByLoginName", loginName);
	}
	
	/**
	 * @Title: getClassByGrade
	 * @Description: 根据年级得到用户所带班级
	 * @author zhaohuanhuan
	 * @date 2016年9月8日 
	 * @param loginName
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getClassByGrade(String gradeId) {
		return selectList("subTeacher.getClassByGrade", gradeId);
	}
	
	/**
	 * @Title: getClassAvgByClassId
	 * @Description: 根据班级得到各班级某科目的平均分
	 * @author zhaohuanhuan
	 * @date 2016年9月9日 
	 * @param course
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getClassAvgByClassId(String course){
		return selectList("subTeacher.getClassAvgByClassId", course);
	}
	
	
	/**
	 * @Title: getAvgByCourseAndGradeId
	 * @Description: 根据科目和年级id得到科目平均分（得到某年级各班某各科目的平均分）
	 * @author zhaohuanhuan
	 * @date 2016年9月12日 
	 * @param course
	 * @param gradeId
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getAvgByCourseAndGradeId(String course,String gradeId){
		Map<String, Object> params=new HashMap<>();
		params.put("course", course);
		params.put("gradeId", gradeId);
		return selectList("subTeacher.getAvgByCourseAndGradeId", params);
	}
	
	/**
	 * @Title: getAvgByCourseAndGradeIdAndSchoolCode
	 * @Description: 根据科目，年级，学校code得到平均分，（得到全校某年级某科科目的平均分）
	 * @author zhaohuanhuan
	 * @date 2016年9月12日 
	 * @param course
	 * @param gradeId
	 * @param schoolCode
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getAvgByCourseAndGradeIdAndSchoolCode(String course,String gradeId,String schoolCode){
		Map<String, Object> params=new HashMap<>();
		params.put("course", course);
		params.put("gradeId", gradeId);
		params.put("schoolCode",schoolCode);
		return selectList("subTeacher.getAvgByCourseAndGradeIdAndSchoolCode", params);
	}
	
	/**
	 * @Title: getGradeNameByGradeId
	 * @Description: 根据年级id得到年级名称
	 * @author zhaohuanhuan
	 * @date 2016年9月12日 
	 * @param gradeId
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getGradeNameByGradeId(String gradeId){
		Map<String, Object> params=new HashMap<>();
		params.put("gradeId", gradeId);
		return selectList("subTeacher.getGradeNameByGradeId", params);
	}
	
	/**
	 * @Title: getSiLv
	 * @Description: 得到某学科的四率
	 * @author zhaohuanhuan
	 * @date 2016年9月12日 
	 * @param course
	 * @param schoolCode
	 * @param classId
	 * @param gradeId
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getSiLv(String course,String schoolCode,String classId,String gradeId){
		Map<String, Object> params=new HashMap<>();
		params.put("course", course);
		params.put("schoolCode", schoolCode);
		params.put("classId", classId);
		params.put("gradeId", gradeId);
		return selectList("subTeacher.getSiLv", params);
	}
	
	/**
	 * @Title: getSiLv
	 * @Description: 班主任查看班级四率
	 * @author xiahuajun
	 * @date 2016年9月12日 
	 * @param course
	 * @param schoolCode
	 * @param classId
	 * @param gradeId
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getSiLvForClassRoomTeacher(Map<String, Object> param){
		return selectList("subTeacher.getSiLvForClassRoomTeacher", param);
	}
	
	
	/**
	 * @Title: selectSchoolCodeByLoginName
	 * @Description: 根据登录名得到学校code
	 * @author zhaohuanhuan
	 * @date 2016年9月12日 
	 * @param username
	 * @return
	 * @return String
	 */
	public String selectSchoolCodeByLoginName(String username) {
		return selectOne("teacher.selectSchoolCodeByLoginName", username);
	}
	
}
