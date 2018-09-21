package data.academic.statisticsAnalysis.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;

/**
 * @Title: ScoreSearchService
 * @Description: 成绩查询服务层
 * @author zhaohuanhuan
 * @date 2016年8月16日
 */
@Service
public class ScoreSearchService extends AbstractService {

	/**
	 * @Title: ScoreSearch
	 * @Description: 分页查询成绩
	 * @author zhaohuanhuan
	 * @date 2016年8月16日
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
			sortField = "Grade_Id,Class_Id";
		}
		if (StringUtils.isBlank(sort)) {
			sort = "ASC";
		} 
		return selectPaging("scoreSearch.selectScoreSearchPaging", requestMap, sortField, sort, currentPage, pageSize);
	}

	/**
	 * @Title: getSchoolCode
	 * @Description: 得到学校code
	 * @author zhaohuanhuan
	 * @date 2016年8月16日
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getSchoolCode() {
		return selectList("scoreSearch.selectSchool");
	}
	
	/**
	 * @Title: getCourseName
	 * @Description: 得到科目的名称
	 * @author zhaohuanhuan
	 * @date 2016年9月5日 
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getCourseName() {
		return selectList("scoreSearch.selectCourse");
	}
	
	/**
	 * @Title: getCoursesByCourseId
	 * @Description: 通过科目id关联查询科目
	 * @author zhaohuanhuan
	 * @date 2016年9月5日 
	 * @param courseId
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getCoursesByCourseId(String courseId) {
		return selectList("scoreSearch.selectCoursesByCourseId", courseId);
	}
	
	/**
	 * @Title: getSchoolsByGrade
	 * @Description: 查询关联年级所属的学校
	 * @author xiahuajun
	 * @date 2016年10月27日 
	 * @param schoolType
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getSchoolsByGrade(Map<String, Object> map) {
		return selectList("scoreSearch.getSchoolsByGrade", map);
	}
	
	/**
	 * @Title: searchScoreForInstructor
	 * @Description: TODO
	 * @author xiahuajun
	 * @date 2016年10月27日 
	 * @param requestMap
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	public PagingResult<Map<String, Object>> searchScoreForInstructor(Map<String, Object> requestMap, String sortField,
			String sort, int currentPage, int pageSize) {
		if (StringUtils.isBlank(sortField)) {
			sortField = "Grade_Id,Class_Id";
		}
		if (StringUtils.isBlank(sort)) {
			sort = "ASC";
		} 
		return selectPaging("scoreSearch.searchScoreForInstructor", requestMap, sortField, sort, currentPage, pageSize);
	}
	
	/**
	 * @Title: getAllSchoolsByForqpAdmin
	 * @Description: 青浦教育局加载所有学校
	 * @author xiahuajun
	 * @date 2016年10月27日 
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getAllSchoolsByForqpAdmin() {
		return selectList("scoreSearch.getAllSchoolsByForqpAdmin");
	}
	
	/**
	 * @Title getTotalDistrict
	 * @Description  查询参加考试的区级总人数
	 * @author wangchaofa
	 * @CreateDate Nov 17,2016
	 * @param map
	 * @return
	 */
	public String getTotalDistrict(Map<String, Object> map){
		return selectOne("scoreSearch.getTotalDistrict", map);
	}
	
	/**
	 * @Description 参加考试的区级排名
	 */
	public String getOrderDistrict(Map<String, Object> map){
		return selectOne("scoreSearch.getOrderDistrict", map);
	}
	
	/**
	 * @Title getTotalSchool
	 * @Description  查询参加考试的年级总人数
	 * @author wangchaofa
	 * @CreateDate Nov 17,2016
	 * @param map
	 * @return
	 */
	public String getTotalSchool(Map<String, Object> map){
		return selectOne("scoreSearch.getTotalSchool", map);
	}
	
	
	public String getTotalGrade(Map<String, Object> map){
		return selectOne("scoreSearch.getTotalGrade", map);
	}
	
	/**
	 * @Title getTotalClass
	 * @Description  查询参加考试的班级总人数
	 * @author wangchaofa
	 * @CreateDate Nov 17,2016
	 * @param map
	 * @return
	 */
	public String getTotalClass(Map<String ,Object> map){
		return selectOne("scoreSearch.getTotalClass", map);
	}
	
	
	public String getTotalClassTec(Map<String ,Object> map){
		return selectOne("scoreSearch.getTotalClassTec", map);
	}
	
	/**
	 * @Title getOrderClass
	 * @Description 查询参加考试的班级排名
	 * @author wangchaofa
	 * @CreateDate Nov 17,2016
	 * @param map
	 * @return
	 */
	public String getOrderClass(Map<String, Object> map){
		return selectOne("scoreSearch.getOrderClass", map);
	}
	
	/**
	 * @Description 查询参加考试的学校的年级排名
	 * @param map
	 * @return
	 */
	public String getOrderGrade(Map<String, Object> map){
		return selectOne("scoreSearch.getOrderGrade", map);
	}
	
	public String getOrderGradeTec(Map<String, Object> map){
		return selectOne("scoreSearch.getOrderGradeTec", map);
	}
	
	/**
	 * @Description 科目总人数
	 * @param map
	 * @return
	 */
	public String getTotalCourse(Map<String, Object> map){
		return selectOne("scoreSearch.getTotalCourse", map);
	}
	
    //科目-语文-排名
	public String getOrderCourseYw(Map<String, Object> map){		
		return selectOne("scoreSearch.getOrderCourseByYw", map);
	}
	//科目-数学-排名
	public String getOrderCourseSx(Map<String, Object> map){			
		return selectOne("scoreSearch.getOrderCourseBySx", map);
	}
	//科目-外语-排名
	public String getOrderCourseYy(Map<String, Object> map){			
		return selectOne("scoreSearch.getOrderCourseByYy", map);
	}
	//科目-物理-排名
	public String getOrderCourseWl(Map<String, Object> map){			
		return selectOne("scoreSearch.getOrderCourseByWl", map);
	}
	//科目-化学-排名
	public String getOrderCourseHx(Map<String, Object> map){			
		return selectOne("scoreSearch.getOrderCourseByHx", map);
	}
	//科目-历史-排名
	public String getOrderCourseLs(Map<String, Object> map){			
		return selectOne("scoreSearch.getOrderCourseByLs", map);
	}
	//科目-科学-排名
	public String getOrderCourseKx(Map<String, Object> map){			
		return selectOne("scoreSearch.getOrderCourseByKx", map);
	}
	//科目-美术-排名
	public String getOrderCourseMs(Map<String, Object> map){			
		return selectOne("scoreSearch.getOrderCourseByMs", map);
	}
	//科目-体育-排名
	public String getOrderCourseTy(Map<String, Object> map){			
		return selectOne("scoreSearch.getOrderCourseByTy", map);
	}
	//科目-音乐-排名
	public String getOrderCourseYyue(Map<String, Object> map){			
		return selectOne("scoreSearch.getOrderCourseByYyue", map);
	}
	//科目-生物-排名
	public String getOrderCourseSw(Map<String, Object> map){			
		return selectOne("scoreSearch.getOrderCourseBySw", map);
	}
	//科目-自然-排名
	public String getOrderCourseZr(Map<String, Object> map){			
		return selectOne("scoreSearch.getOrderCourseByZr", map);
	}
	//科目-思想政治-排名
	public String getOrderCourseSxzz(Map<String, Object> map){			
		return selectOne("scoreSearch.getOrderCourseBySxzz", map);
	}
	//科目-信息科技-排名
	public String getOrderCourseXxkj(Map<String, Object> map){			
		return selectOne("scoreSearch.getOrderCourseByXxkj", map);
	}
	//科目-研究型课程-排名
	public String getOrderCourseYjxkc(Map<String, Object> map){			
		return selectOne("scoreSearch.getOrderCourseByYjxkc", map);
	}
	//科目-拓展型课程-排名
	public String getOrderCourseTzxkc(Map<String, Object> map){			
		return selectOne("scoreSearch.getOrderCourseByTzxkc", map);
	}
	//科目-牛津英语-排名
	public String getOrderCourseNjyy(Map<String, Object> map){			
		return selectOne("scoreSearch.getOrderCourseByNjyy", map);
	}
	//科目-高中新世纪英语-排名
	public String getOrderCourseXsjyy(Map<String, Object> map){			
		return selectOne("scoreSearch.getOrderCourseByXsjyy", map);
	}

		
	
	/**
	 * @Title: searchScoreForqpAdmin
	 * @Description: 青浦超级管理员分页成绩查询
	 * @author xiahuajun
	 * @date 2016年10月27日 
	 * @param requestMap
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	public PagingResult<Map<String, Object>> searchScoreForqpAdmin(Map<String, Object> requestMap, String sortField,
			String sort, int currentPage, int pageSize) {
		if (StringUtils.isBlank(sortField)) {
			sortField = "School_Code,Grade_Id,Class_Id";
		}
		if (StringUtils.isBlank(sort)) {
			sort = "ASC";
		} 
		return selectPaging("scoreSearch.searchScoreForqpAdmin", requestMap, sortField, sort, currentPage, pageSize);
	}

    public PagingResult<Map<String, Object>> searchScoreForqpAdmin2(Map<String, Object> requestMap, String sortField,
                                                                   String sort, int currentPage, int pageSize) {
        if (StringUtils.isBlank(sortField)) {
            sortField = "School_Code,Grade_Id,Class_Id";
        }
        if (StringUtils.isBlank(sort)) {
            sort = "ASC";
        }
        return selectPaging("scoreSearch.searchScoreForqpAdmin2", requestMap, sortField, sort, currentPage, pageSize);
    }
	/**
	 * @Title: searchScoreForqpAdmin
	 * @Description: 青浦超级管理员不分页成绩查询
	 * @author huchuanhu
	 * @date 2016年11月21日 
	 * @param requestMap
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	public List<Map<String, Object>> searchScoreForqpAdminPage(Map<String, Object> requestMap) {
		return selectList("scoreSearch.searchScoreForqpAdminPage", requestMap);
	}
	
	public List<Map<String, Object>> searchScoreForqpAdminPage2(Map<String, Object> requestMap) {
		return selectList("scoreSearch.searchScoreForqpAdminPage2", requestMap);
	}
	
	/**
	 * @Title: searchScoreForqpAdmin
	 * @Description: 学校老师不分页成绩查询
	 * @author huchuanhu
	 * @date 2016年11月21日 
	 * @param requestMap
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	public List<Map<String, Object>> searchScoreForTeacherPage(Map<String, Object> requestMap) {
		return selectList("scoreSearch.searchScoreForTeacherPage", requestMap);
	}
	/**
	 * @Title: selectExportScoreDataForTeacher
	 * @Description: 老师查询成绩导出excel
	 * @author xiahuajun
	 * @date 2016年11月4日 
	 * @param requestMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> selectExportScoreDataForTeacher(Map<String, Object> requestMap) {
		return selectList("scoreSearch.selectExportScoreDataForTeacher",requestMap);
	}
	
	/**
	 * @Title: selectExportScoreDataForInstructor
	 * @Description: 教导员查询成绩导出excel
	 * @author xiahuajun
	 * @date 2016年11月5日 
	 * @param requestMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> selectExportScoreDataForInstructor(Map<String, Object> requestMap) {
		return selectList("scoreSearch.selectExportScoreDataForInstructor",requestMap);
	}
	
	/**
	 * @Title: selectExportScoreDataForInstructor
	 * @Description: 通过年级来查询考试科目列表
	 * @author huchuanhu
	 * @date 2016年11月17日 
	 * @param requestMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getCoursesByGrade(Map<String, Object> requestMap) {
		return selectList("scoreSearch.selectCourseByGrade",requestMap);
	}
	
	/**
	 * @Title: selectExportScoreDataForInstructor
	 * @Description: 通过年级来查询考试名称
	 * @author huchuanhu
	 * @date 2016年11月17日 
	 * @param requestMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getExamNameByCode(Map<String, Object> requestMap) {
		return selectList("scoreSearch.selectExamNameByGrade",requestMap);
	}
	
	/**
	 * @Title: selectExportScoreDataForInstructor
	 * @Description: 通过考试编号查询科目列表
	 * @author huchuanhu
	 * @date 2016年11月17日 
	 * @param requestMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getCourseByExamNumber(Map<String, Object> requestMap) {
		return selectList("scoreSearch.selectCourseByExamNumber",requestMap);
	}

    /**
     * 取区级名次及各科区级名次(key=学籍号,val=(key=科目,val=名次))
     * @return
     */
    public List<Map<String, Object>> searchRankQj(Map<String, Object> requestMap) {
        return selectList("scoreSearch.searchRank_qj",requestMap);
    }
    /**
     * 取年级名次(key=学籍号,val=名次)
     * @return
     */
    public List<Map<String, Object>> searchRankNj(Map<String, Object> requestMap) {
        return selectList("scoreSearch.searchRank_nj",requestMap);
    }
}
