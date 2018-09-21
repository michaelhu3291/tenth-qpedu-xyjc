
package data.academic.examInfo.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;

/**
 * @Title: MarkingArrangementService
 * @Description: TODO
 * @author zhaohuanhuan
 * @date 2016年10月24日 下午6:18:16
 */
@Service
public class MarkingArrangementService extends AbstractService{

	/**
	 * @Title: serachCoursePaging
	 * @Description: 得到该考试下的所有科目用于分配阅卷人
	 * @author zhaohuanhuan
	 * @date 2016年10月24日 
	 * @param map
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	public PagingResult<Map<String, Object>> serachCoursePaging(Map<String, Object> map, String sortField, String sort,
			int currentPage, int pageSize) {
		if( StringUtils.isBlank(sortField)){
			sortField = "SortNumber" ;
		}
        if( StringUtils.isBlank( sort )){
        	sort = "desc" ;
        }
		return  selectPaging( "arrangement.getCoursesByExamCode", map, sortField, sort, currentPage, pageSize ) ;
	}
	
	
	/**
	 * @Title: getCourseByParentDictionary
	 * @Description:  根据父节点得到子节点  用于得到学科 
	 * @author zhaohuanhuan
	 * @date 2016年10月12日 
	 * @param paramMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public  PagingResult<Map<String, Object>> getCourseByParentDictionaryserachCoursePaging(Map<String, Object> map, String sortField, String sort,
			int currentPage, int pageSize) {
		if( StringUtils.isBlank(sortField)){
			sortField = "DictionaryCode" ;
		}
        if( StringUtils.isBlank( sort )){
        	sort = "desc" ;
        }
		return  selectPaging( "examInfo.getCourseByParentDictionary", map, sortField, sort, currentPage, pageSize ) ;
	}
	
	
	/**
	 * @Title: showArrangementByExam
	 * @Description: 显示相关考试科目的阅卷人
	 * @author zhaohuanhuan
	 * @date 2016年10月28日 
	 * @param paramMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> showArrangementByExam(Map<String, Object> paramMap){
		 return selectList( "examInfo.getCourseByParentDictionary", paramMap);
	}
	
	
	   /**
	 * @Title: getTeacherByCourseAndGrade
	 * @Description: 根据科目和年级加载出教师信息
	 * @author zhaohuanhuan
	 * @date 2016年10月25日 
	 * @param paramMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getTeacherByCourseAndGrade (Map<String, Object> paramMap){
			return selectList("arrangement.getTeacherByCourseAndGrade", paramMap);
	}
	
	/**
	 * @Title: addTeacherRefExam
	 * @Description: 添加阅卷人
	 * @author zhaohuanhuan
	 * @date 2016年10月25日 
	 * @param paramMap
	 * @return void
	 */
	public void addTeacherRefExam(Map<String, Object> paramMap){
		 insert("arrangement.addTeacherRefExam", paramMap);
	}
	
	/**
	 * @Title: deleteTeacherRefExam
	 * @Description: 删除阅卷人
	 * @author zhaohuanhuan
	 * @date 2016年10月25日 
	 * @param examCode
	 * @return
	 * @return int
	 */
	public int deleteTeacherRefExam(Map<String, Object> params){
		return delete("arrangement.deleteTeacherRefExam", params);
	}
	
	/**
	 * @Title: getSelectedTeacher
	 * @Description: 得到选中的老师
	 * @author zhaohuanhuan
	 * @date 2016年10月25日 
	 * @param params
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getSelectedTeacher(Map<String, Object> params){
		return selectList("arrangement.getSelectedTeacher", params);
	}
	
	/**
	 * @Title: countArrangementNum
	 * @Description: 得到某次考试下面的科目阅卷人总数
	 * @author zhaohuanhuan
	 * @date 2016年11月17日 
	 * @param params
	 * @return
	 * @return Integer
	 */
	public Integer countArrangementNum(Map<String, Object> params){
		return selectOne("arrangement.countArrangementNum", params);
	}
	
	
	/**
	 * @Title: selectCourseBySchoolCode
	 * @Description: 根据学校code分页显示科目信息
	 * @author zhaohuanhuan
	 * @date 2016年11月17日 
	 * @param map
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	public PagingResult<Map<String, Object>> selectCourseBySchoolCode(Map<String, Object> map, String sortField, String sort,
			int currentPage, int pageSize) {
		if( StringUtils.isBlank(sortField)){
			sortField = "SortNumber" ;
		}
        if( StringUtils.isBlank( sort )){
        	sort = "desc" ;
        }
		return  selectPaging( "arrangement.selectCourseBySchoolCode", map, sortField, sort, currentPage, pageSize ) ;
	}
	
	
	/**
	 * @Title: getSchoolArrangementByExamCodeAndCourse
	 * @Description: 根据考试编号和科目分页显示学校相关阅卷人信息
	 * @author zhaohuanhuan
	 * @date 2016年11月17日 
	 * @param map
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	public PagingResult<Map<String, Object>> getSchoolArrangementByExamCodeAndCourse(Map<String, Object> map, String sortField, String sort,
			int currentPage, int pageSize) {
		if( StringUtils.isBlank(sortField)){
			sortField = "Brevity_Code" ;
		}
        if( StringUtils.isBlank( sort )){
        	sort = "desc" ;
        }
		return  selectPaging( "arrangement.getSchoolArrangementByExamCodeAndCourse", map, sortField, sort, currentPage, pageSize ) ;
	}
	
	
	/**
	 * @Title: getSchoolNotExistArrangementByExamCodePading
	 * @Description: 分页显示未安排阅卷人的学校信息
	 * @author zhaohuanhuan
	 * @date 2016年11月24日 
	 * @param map
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	public PagingResult<Map<String, Object>> getSchoolNotExistArrangementByExamCodePading(Map<String, Object> map, String sortField, String sort,
			int currentPage, int pageSize) {
		if( StringUtils.isBlank(sortField)){
			sortField = "Brevity_Code" ;
		}
        if( StringUtils.isBlank( sort )){
        	sort = "desc" ;
        }
		return  selectPaging( "arrangement.getSchoolNotExistArrangementByExamCodePading", map, sortField, sort, currentPage, pageSize ) ;
	}
	
	
	/**
	 * @Title: getSchoolNotExistArrangementByExamCodePading
	 * @Description: 分页显示已安排阅卷人的学校信息
	 * @author zhaohuanhuan
	 * @date 2016年11月24日 
	 * @param map
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	public PagingResult<Map<String, Object>> getExistArrangementPading(Map<String, Object> map, String sortField, String sort,
			int currentPage, int pageSize) {
		if( StringUtils.isBlank(sortField)){
			sortField = "Brevity_Code" ;
		}
        if( StringUtils.isBlank( sort )){
        	sort = "desc" ;
        }
		return  selectPaging( "arrangement.getExistArrangementPading", map, sortField, sort, currentPage, pageSize ) ;
	}
	
	/**
	 * @Title: getSchoolShortName
	 * @Description: 得到学校简称，用于青浦管理员查看各校的阅卷人
	 * @author zhaohuanhuan
	 * @date 2016年10月26日 
	 * @param params
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getSchoolShortName (Map<String, Object> params){
		return selectList("arrangement.getSchoolShortName", params);
	}
	
	
	
	/**
	 * @Title: getSchoolShortNameAndBrevityCode
	 * @Description: 根据学校code得到学校简称和简码
	 * @author zhaohuanhuan
	 * @date 2016年11月17日 
	 * @param schoolCode
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getSchoolShortNameAndBrevityCode (Map<String, Object> params){
		return selectList("arrangement.getSchoolShortNameAndBrevityCode", params);
	}
	
	
	/**
	 * @Title: getSchoolArrangementByExamCode
	 * @Description: 得到某次考试下面的学校
	 * @author zhaohuanhuan
	 * @date 2016年11月23日 
	 * @param map
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>>  getSchoolArrangementByExamCode(Map<String, Object> map) {
		 
		return  selectList( "arrangement.getSchoolArrangementByExamCode",map ) ;
	}
	
	/**
	 * @Title: getSchoolNotExistArrangementByExamCode
	 * @Description: 得到未安排阅卷人的学校信息
	 * @author zhaohuanhuan
	 * @date 2016年11月24日 
	 * @param map
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>>  getSchoolNotExistArrangementByExamCode(Map<String, Object> map) {
		 
		return  selectList( "arrangement.getSchoolNotExistArrangementByExamCode",map ) ;
	}
	
}
