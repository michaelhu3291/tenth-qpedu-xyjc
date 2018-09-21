package data.academic.examInfo.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import data.framework.support.AbstractService;
/**
 * @Title: ExamInfoSchoolService
 * @Description: 学校考试管理服务层
 * @author zhaohuanhuan
 * @date 2016年10月18日 下午3:34:40
 */
@Service
public class ExamInfoSchoolService extends AbstractService{
	
	/**
	 * @Title: getClassBySchoolAndGrade
	 * @Description: 根据子节点得到父节点id
	 * @author zhaohuanhuan
	 * @date 2016年10月18日 
	 * @param params
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getClassBySchoolAndGrade(Map<String, Object> params) {
		return selectList("examInfoSchool.getClassBySchoolAndGrade",params);
	}
	
	
	
	/**
	 * @Title: getExamNumberByNotSchoolCode
	 * @Description: 得到学校发布的考试的编号的流水号的最大值
	 * @author zhaohuanhuan
	 * @date 2016年10月18日 
	 * @param schoolCode
	 * @return
	 * @return String
	 */
	public String getExamNumberByNotSchoolCode(String schoolCode) {
		return selectOne("examInfoSchool.getExamNumberByNotSchoolCode",schoolCode);
	}
	
	/**
	 * @Title: addExamCalssCourse
	 * @Description: 添加考试编号班级科目
	 * @author zhaohuanhuan
	 * @date 2016年10月18日 
	 * @param params
	 * @return void
	 */
	public void addExamCalssCourse(Map<String, Object> params){
		insert("examInfoSchool.addExamCalssCourse", params);
	}
	
	/**
	 * @Title: deleteCourseClassByExamCode
	 * @Description: 删除与该考试相关的班级和科目
	 * @author zhaohuanhuan
	 * @date 2016年10月19日 
	 * @param examCode
	 * @return void
	 */
	public void deleteCourseClassByExamCode(List<String> examCode){
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put("examCode", examCode);
		delete("examInfoSchool.deleteCourseClassByExamCode", param);
		
	} 
	
	/**
	 * @Title: getClassIdByExamCode
	 * @Description: 根据考试编号得到与之相关的班级
	 * @author zhaohuanhuan
	 * @date 2016年10月19日 
	 * @param examCode
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getClassIdByExamCode(String examCode){
		return selectList("examInfoSchool.getClassIdByExamCode", examCode);
	}
	
	/**
	 * @Title: getCourserByExamCodeAndClass
	 * @Description: 根据考试编号和班级得到与之相关的学科
	 * @author zhaohuanhuan
	 * @date 2016年10月19日 
	 * @param params
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getCourserByExamCodeAndClass(Map<String, Object> params){
		return selectList("examInfoSchool.getCourserByExamCodeAndClass", params);
	}
	
	
	/**
	 * @Title: selectCoursesByExamCode
	 * @Description: 根据考试编号的到
	 * @author zhaohuanhuan
	 * @date 2016年11月10日 
	 * @param params
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> selectCoursesByExamCode (Map<String, Object> params){
		return selectList("examInfoSchool.selectCoursesByExamCode", params);
	}
	
	
	/**
	 * @Title: updateExamRefCourseByExamCodeAndCourse
	 * @Description: 修改学科（关系表）
	 * @author zhaohuanhuan
	 * @date 2016年11月11日 
	 * @param paramMap
	 * @return void
	 */
	public void updateExamRefCourseByExamCodeAndCourse(Map<String, Object> paramMap){
		update("examInfoSchool.updateExamRefCourseByExamCodeAndCourse", paramMap);
	}
	
	/**
	 * @Title: getCourseStartTimeAndEndTime
	 * @Description: 得到某次考试下面的科目考试的开始时间和结束时间
	 * @author zhaohuanhuan
	 * @date 2016年12月21日 
	 * @param paramMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getCourseStartTimeAndEndTime(Map<String, Object> paramMap){
		return selectList("examInfoSchool.getCourseStartTimeAndEndTime", paramMap);
	}


	//得到考试的分数段
	public List<Integer> searchScoreSegement(String scoreDictionaryName) {
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put("scoreDictionaryName", scoreDictionaryName);
		return selectList("examInfoSchool.searchScoreSegement", param);
	}
	
	/**
	 * @Title: getClassInfoByClassPk
	 * @Description: 根据班级pk得到相应班级信息
	 * @author zhaohuanhuan
	 * @date 2017年8月29日 
	 * @return List<Map<String,Object>>
	 * @param classPk
	 * @return
	 */
	public List<Map<String, Object>> getClassInfoByClassPk(String classPk){
		return selectList("examInfoSchool.getClassInfoByClassPk", classPk);
	}
}
