package data.academic.schoolQualityAnalysis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;
@Service
public class ScoreBetweenClassScoreService extends AbstractService{
	
	/**
	 * 
	 * @Title: getCourseByTeacher
	 * @Description: 得到老师教的课程 一个老师对应一门课
	 * @author chenteng
	 * @date 2017年8月29日 
	 * @return String
	 * @param loginName
	 * @return
	 */
	public String getCourseByTeacher(String loginName) {
		return selectOne("schoolScore.getCourseByTeacher", loginName);
	}
	/**
	 * 
	 * @Title: getClassesScoreList
	 * @Description: 得到一门考试一个课程一个班所有学生的成绩
	 * @author chenteng
	 * @date 2017年8月29日 
	 * @return List<HashMap<String,String>>
	 * @param requestMap
	 * @return
	 */
	public List<Map<String,Object>> getClassesScoreList(Map<String, Object> requestMap) {
		return selectList("schoolScore.getClassesScoreList",requestMap);
	}
	/**
	 * 
	 * @Title: getExamCodeListBySchool
	 * @Description: 得到该年级发布过的考试
	 * @author chenteng
	 * @date 2017年8月29日 
	 * @return List<HashMap<String,String>>
	 * @param gradeCode
	 * @return
	 */
	public List<HashMap<String,String>> getExamCodeListBySchool(String gradeCode) {
		HashMap<String,String> requestMap = new HashMap<>();
		requestMap.put("gradeCode", gradeCode);
		return selectList("schoolScore.getExamCodeListBySchool",requestMap);
	}

	/**
	 * 
	 * @Title: getOneClassScoreList
	 * @Description: 得到一个班多场考试的平均分（考试顺序有时间保证）
	 * @author chenteng
	 * @date 2017年8月29日 
	 * @return List<String>
	 * @param requestMap
	 * @return
	 */
	public List<String> getOneClassScoreList(Map<String, Object> requestMap) {
		return selectList("schoolScore.getOneClassScoreList",requestMap);
	}
	/**
	 * 
	 * @Title: getExamZf
	 * @Description: 得到一门考试的总分
	 * @author chenteng
	 * @date 2017年8月29日 
	 * @return String
	 * @param courseCode
	 * @param examCode
	 * @return
	 */
	public String getExamZf(String courseCode, String examCode) {
		HashMap<String,String> requestMap = new HashMap<>();
		requestMap.put("courseCode", courseCode);
		requestMap.put("examCode", examCode);
		return selectOne("schoolScore.getExamZf",requestMap);
	}
	public List<HashMap<String, String>> getClassNameAndTeacherName(Map<String, Object> requestMap) {
		return selectList("schoolScore.getClassNameAndTeacherName",requestMap);
	}
	
	
}