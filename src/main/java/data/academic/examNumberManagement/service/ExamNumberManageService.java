/**
 * 2016年9月21日
 */
package data.academic.examNumberManagement.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;

/**
 * @Title: ExamNumberManageService
 * @Description: TODO
 * @author zhaohuanhuan
 * @date 2016年9月21日 下午9:01:43
 */
@Service
public class ExamNumberManageService extends AbstractService {

	
	/**
	 * @Title: searchPaging
	 * @Description: 分页显示考试信息
	 * @author zhaohuanhuan
	 * @date 2016年9月22日 
	 * @param params
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	public PagingResult<Map<String,Object>> searchPaging(Map<String,Object> params, String sortField, String sort, int currentPage, int pageSize ){
		if( StringUtils.isBlank( sortField ) )
            sortField = "Exam_code" ;
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging( "examNumberManage.selectExamNumberPaging", params, sortField, sort, currentPage, pageSize ) ;
    }
	
	public List<Map<String,Object>> searchPagingImport(Map<String,Object> params){
		
        return selectList( "examNumberManage.selectExamNumberPagingImport", params) ;
    }
	
	
	
	/**
	 * @Title: searchTeacherPaging
	 * @Description: 分页显示教师
	 * @author zhaohuanhuan
	 * @date 2016年11月18日 
	 * @param params
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	public PagingResult<Map<String,Object>> searchTeacherPaging(Map<String,Object> params, String sortField, String sort, int currentPage, int pageSize ){
		if( StringUtils.isBlank( sortField ) )
            sortField = "Teacher_Pk" ;
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging( "arrangement.getTeacherByCourseAndGrade", params, sortField, sort, currentPage, pageSize ) ;
    }
	
	
	/**
	 * @Title: getExamNumberInfoBySchoolCode
	 * @Description: 根据学校code得到考试信息
	 * @author zhaohuanhuan
	 * @date 2016年9月22日 
	 * @param paramMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>>  getExamNumberInfoBySchoolCode(Map<String, Object> paramMap){
		return selectList("examNumberManage.getExamNumberInfoBySchoolCode", paramMap);
	}
	
	/**
	 * @Title: getSchoolCodeByLoginName
	 * @Description: 根据登录名得到用户所在的机构id
	 * @author zhaohuanhuan
	 * @date 2016年9月22日 
	 * @param loginName
	 * @return
	 * @return String
	 */
	public String getSchoolCodeByLoginName(String loginName){
		return selectOne("examNumberManage.getSchoolCodeByLoginName", loginName);
	}
	/**
	 * @Title: getStuInfoBySchoolCodeAndStateCode
	 * @Description: 根据学校code和学籍状态得到学生信息
	 * @author zhaohuanhuan
	 * @date 2016年9月22日 
	 * @param schoolCode
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>>  getStuInfoBySchoolCodeAndStateCode(Map<String, Object> paramMap){
		return selectList("examNumberManage.getStuInfoBySchoolCodeAndStateCode", paramMap);
	}
	
	/**
	 * @Title: getStuInfoBySchoolCodeAndIsNotStateCode
	 * @Description: 根据学校code和不是选中学籍状态得到学生信息
	 * @author zhaohuanhuan
	 * @date 2016年9月26日 
	 * @param paramMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getStuInfoBySchoolCodeAndIsNotStateCode(Map<String, Object> paramMap){
		return selectList("examNumberManage.getStuInfoBySchoolCodeAndIsNotStateCode", paramMap);
	}
	
	
	
	/**
	 * @Title: getStuNumberInfoBySchoolCode
	 * @Description: 得到学生 ，学生考试信息
	 * @author zhaohuanhuan
	 * @date 2016年9月22日 
	 * @param schoolCode
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>>  getStuNumberInfoBySchoolCode(String schoolCode){
		return selectList("examNumberManage.getStuNumberInfoBySchoolCode", schoolCode);
	}

	/**
	 * @Title: insertStuCodeExamNumber
	 * @Description:  学生与考试信息的关联 
	 * @author zhaohuanhuan
	 * @date 2016年9月22日 
	 * @param paramList
	 * @return void
	 */
	public void insertStuCodeExamNumber(List<Map<String, Object>> paramList){
		insert("examNumberManage.insertStuCodeExamNumber",paramList);
	}
	
	/**
	 * @Title: insertschoolCodeExamNumber
	 * @Description: 添加学校和考试编号
	 * @author zhaohuanhuan
	 * @date 2016年10月17日 
	 * @param paramMap
	 * @return void
	 */
	public void insertschoolCodeExamNumber(Map<String, Object> paramMap){
		insert("examNumberManage.insertschoolCodeExamNumber",paramMap);
	}
	
	
	/**
	 * @Title: getIsExistExamNumberBySchoolCodeAndExamCode
	 * @Description: 根据学校code和考试编号判断该学校是否已经生成考号
	 * @author zhaohuanhuan
	 * @date 2016年10月14日 
	 * @param paramMap
	 * @return
	 * @return String
	 */
	public String getIsExistExamNumberBySchoolCodeAndExamCode(Map<String, Object> paramMap){
		return selectOne("examNumberManage.getInfoBySchoolCodeAndExamCode", paramMap);
	}
	
	/**
	 * @Title: updateNumberStateByExamNumber
	 * @Description: 更新考号状态
	 * @author zhaohuanhuan
	 * @date 2016年9月23日 
	 * @param examNumber
	 * @return void
	 */
	public void updateNumberStateByExamNumber(String examNumber){
		update("examNumberManage.updateNumberStateByExamCode", examNumber);
	}
	
	/**
	 * @Title: updateExamNumberById
	 * @Description: 根据id更新学生考号
	 * @author zhaohuanhuan
	 * @date 2016年9月23日 
	 * @param paramMap
	 * @return void
	 */
	public void updateExamNumberById(Map<String, Object> paramMap){
		update("examNumberManage.updateExamNumberById", paramMap);
	}
	
	/**
	 * @Title: getExamNumberById
	 * @Description: 根据id得到学生考号
	 * @author zhaohuanhuan
	 * @date 2016年9月28日 
	 * @param id
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>>  getExamNumberById(String id){
		return selectList("examNumberManage.getExamNumberById", id);
	}
	/**
	 * @Title: examNumberIsExist
	 * @Description: 判断学生考号是否存在
	 * @author zhaohuanhuan
	 * @date 2016年9月28日 
	 * @param examNumber
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>>  examNumberIsExist(String examNumber){
		return selectList("examNumberManage.examNumberIsExist", examNumber);
	}
	
	/**
	 * @Title: getStuInfoByParams
	 * @Description: 根据考试编号得到所有的学生信息
	 * @author zhaohuanhuan
	 * @date 2016年9月28日 
	 * @param paramMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getStuInfoByParams(Map<String, Object> paramMap){
		return selectList("examNumberManage.getStuInfoByParams",paramMap);
	}
	
	/**
	 * @Title: getClassInfoByClassId
	 * @Description: 根据classPk得到班级信息
	 * @author zhaohuanhuan
	 * @date 2017年8月29日 
	 * @return Map<String,Object>
	 * @param classId
	 * @return
	 */
	public Map<String, Object> getClassInfoByClassId(String classId){
		return selectOne("examNumberManage.getClassInfoByClassId",classId);
	}
	/**
	 * @Title: updateIntroducedStateByIntroducedTime
	 * @Description: 根据发布时间更新发布状态 
	 * @author zhaohuanhuan
	 * @date 2016年10月14日 
	 * @param paramMap
	 * @return void
	 */
	public void updateIntroducedStateByIntroducedTime(Map<String, Object> paramMap){
		update("examNumberManage.updateIntroducedStateByIntroducedTime", paramMap);
	}
	
	
	
	/**
	 * @Title: updateExamNumberStateById
	 * @Description: 更改该校是否生成考号
	 * @author zhaohuanhuan
	 * @date 2016年10月17日 
	 * @param paramMap
	 * @return void
	 */
	public void updateExamNumberStateById(Map<String, Object> paramMap) {
		update("examNumberManage.updateExamNumberStateById", paramMap);
	}
	
	/**
	 * @Title: countAllStuBySchoolCodeAndGrade
	 * @Description: 得到某个学校某年级的总人数
	 * @author zhaohuanhuan
	 * @date 2016年10月17日 
	 * @param paramMap
	 * @return
	 * @return Integer
	 */
	public Integer countAllStuBySchoolCodeAndGrade(Map<String, Object> paramMap){
		return selectOne("examNumberManage.countAllStuBySchoolCodeAndGrade", paramMap);
	}
	
	/**
	 * @Title: countExistExamNumberStuBySchoolCodeAndGrade
	 * @Description: 得到某个学校某年级生成考号的人数 
	 * @author zhaohuanhuan
	 * @date 2016年10月17日 
	 * @param paramMap
	 * @return
	 * @return Integer
	 */
	public Integer countExistExamNumberStuBySchoolCodeAndGrade(Map<String, Object> paramMap){
		return selectOne("examNumberManage.countExistExamNumberStuBySchoolCodeAndGrade", paramMap);
	}
	
	/**
	 * @Title: selectIdByAssociatedExamNumber
	 * @Description: 根据考试编号得到其附件
	 * @author zhaohuanhuan
	 * @date 2016年10月17日 
	 * @param paramMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> selectIdByAssociatedExamNumber(Map<String, Object> paramMap){
		return selectList("examNumberManage.selectIdByAssociatedExamNumber",paramMap);
	}
	
	/**
	 * @Title: getBrevityCodeBySchoolCode
	 * @Description: 根据学校code得到学校简码（用于生成学生考号）
	 * @author zhaohuanhuan
	 * @date 2016年10月18日 
	 * @param schoolCode
	 * @return
	 * @return String
	 */
	public String getBrevityCodeBySchoolCode(String schoolCode){
		return selectOne("examNumberManage.getBrevityCodeBySchoolCode", schoolCode);
	}
	
	/**
	 * @Title: inserttSchoolCandidateNumberType
	 * @Description: 学校和生成考号类型关联关系
	 * @author zhaohuanhuan
	 * @date 2016年12月8日 
	 * @param paramList
	 * @return void
	 */
	public void inserttSchoolCandidateNumberType(Map<String, Object> map){
		insert("examNumberManage.inserttSchoolCandidateNumberType", map);
	}
	
	/**
	 * @Title: getCanNumberStateBySchoolCode
	 * @Description: TODO
	 * @author zhaohuanhuan
	 * @date 2016年12月8日 
	 * @param schoolCode
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<String> getCanNumberStateBySchoolCode(Map<String,Object> paramMap){
		return selectList("examNumberManage.getCanNumberStateBySchoolCode", paramMap);
	}
	
	
	/**   
	* @Title: insertExamNumberForNewStuInfo
	* @Description: 重新关联班级的时候，匹配是否已经生成过考号，已经生成，添加一条信息在学生考号关系表
	* @author zhaohuanhuan
	* @date 2017年1月10日 下午5:40:59 
	* @return void
	*/
	public void insertExamNumberForNewStuInfo(Map<String, Object> map){
		insert("examNumberManage.insertExamNumberForNewStuInfo", map);
	}
	
	/**   
	* @Title: getExamCodeByStuInfo
	* @Description: 根据学生信息得到考试编号
	* @author zhaohuanhuan
	* @date 2017年1月10日 下午5:41:55 
	* @return List<Map<String, Object>>
	*/
	public List<Map<String, Object>> getExamCodeByStuInfo(Map<String, Object> paramMap){
		return selectList("examNumberManage.getExamCodeByStuInfo",paramMap);
	}
	
	/** 
	* @Title: getExamNumberByStuInfo 
	* @Description: 用于判断某个学生所在学校年级班级是否已经生成考号
	* @param @param paramMap
	* @param @return 
	* @author zhaohuanhuan
	* @return int   
	* @throws 
	*/
	public int getExamNumberByStuInfo(Map<String, Object> paramMap){
		return selectOne("examNumberManage.getExamNumberByStuInfo", paramMap);
	}
	
	/** 
	* @Title: updateExamNumberInfo 
	* @Description: 修改已经存在考号的学生
	* @param @param paramMap 
	* @author zhaohuanhuan
	* @return void   
	* @throws 
	*/
	public void updateExamNumberInfo(Map<String, Object> paramMap){
		update("examNumberManage.updateExamNumberInfo", paramMap);
	}
	/** 
	* @Title: getXjbClass 
	* @Description: 得到某一学校某一年级下面所有的新疆班
	* @param @param paramMap
	* @param @return 
	* @author zhaohuanhuan
	* @return List<String>   
	* @throws 
	*/
	public List<String> getXjbClass(Map<String,Object> paramMap){
		return selectList("examNumberManage.getXjbClass", paramMap);
	}
}
