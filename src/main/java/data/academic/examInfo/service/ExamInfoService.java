package data.academic.examInfo.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import data.academic.examInfo.entity.EntityExamInfo;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;
@Service
public class ExamInfoService extends AbstractService{
	
	public void addExam(Map<String,Object> map){
		insert("examInfo.addExam", map);
	}
	
	/**分页查询
	 * 2016年7月28日
	 * xiahuajun
	 */
	public PagingResult<Map<String, Object>> serachExamPaging(Map<String, Object> map, String sortField, String sort,
			int currentPage, int pageSize) {
		if( StringUtils.isBlank(sortField)){
			sortField = "Exam_Time" ;
		}
        if( StringUtils.isBlank( sort )){
        	sort = "desc" ;
        }
		return  selectPaging( "examInfo.selectExamPaging", map, sortField, sort, currentPage, pageSize ) ;
	}
	
	public List<Map<String, Object>> serachImportExamPaging(Map<String, Object> map) {
		
		return  selectList( "examInfo.selectImportExamPaging", map) ;
	}
	
	/**删除
	 * 2016年7月29日
	 * xiahuajun
	 */
	public int remove(List<String> idAry) {
		 Map<String,Object> param = new HashMap<String,Object>() ;
         param.put( "idAry", idAry ) ;
         return delete("examInfo.deleteExam", param );
	
	}
	
	/**
	 * @Title: deleteAccessory
	 * @Description: 根据考试编号删除考试对应的附件
	 * @author zhaohuanhuan
	 * @date 2016年10月13日 
	 * @param examNumber
	 * @return
	 * @return int
	 */
	public int deleteAccessory (List<String> associatedObjectId) {
		Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "associatedObjectId", associatedObjectId ) ;
        return delete("examInfo.deleteAccessory", param );
	
	}
	
	/**
	 * @Title: selectIdByAssociatedObjectID
	 * @Description: 根据考试编号的到附件的id
	 * @author zhaohuanhuan
	 * @date 2016年10月13日 
	 * @param param
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<String> selectIdByAssociatedObjectID(Map<String,Object> param) {
        return selectList( "examInfo.selectIdByAssociatedObjectID", param ) ;
	}
	
	/**查询实体
	 * 2016年7月29日
	 * xiahuajun
	 */
	public EntityExamInfo selectExamById(Map<String,Object> param) {
        return selectOne( "examInfo.selectExamById", param ) ;
	}
	/**查询实体判断是否有添加重复测试
	 * 2016年11月24日
	 * huchuanhu
	 */
	public List<EntityExamInfo> selectExamBySchoolCode(Map<String,Object> param) {
        return selectList( "examInfo.selectExamBySchoolCode", param ) ;
	}
	/**修改考试
	 * 2016年7月29日
	 * xiahuajun
	 */
	public void updateExamById(Map<String, Object> paramMap) {
		update("examInfo.updateExamById", paramMap);
	}
	
	/**
	 * @Title: selectExamNumberIsExist
	 * @Description: 删除考试前判断考号有无生成
	 * @author xiahuajun
	 * @date 2016年9月30日 
	 * @param paramMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> selectExamNumberIsExist(Map<String, Object> paramMap) {
		return selectList("examInfo.selectExamNumberIsExist",paramMap);
	}
	/**
	 * @Title: getExamNumberBySchoolCode
	 * @Description: 根据学校code得到考试信息表中考试编号后四位流水号最大的值 
	 * @author zhaohuanhuan
	 * @date 2016年10月14日 
	 * @param schoolCode
	 * @return
	 * @return String
	 */
	public String getExamNumberBySchoolCode(String schoolCode) {
		return selectOne("examInfo.getExamNumberBySchoolCode",schoolCode);
	}
	
	
	/**
	 * @Title: getParentDictionaryByDicCode
	 * @Description: 根据子节点得到父节点code ，用于得到科目
	 * @author zhaohuanhuan
	 * @date 2016年10月12日 
	 * @param grade
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getParentDictionaryByDicCode(String gradeCode){
		return selectList("examInfo.getParentDictionaryByDicCode", gradeCode);
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
	public List<Map<String, Object>> getCourseByParentDictionary(Map<String, Object> params) {
		return selectList("examInfo.selectExamNumberIsExist",params);
	}
	
	/**
	 * @Title: getParIdByDicCode
	 * @Description: 根据子节点得到父节点id
	 * @author zhaohuanhuan
	 * @date 2016年10月12日 
	 * @param params
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getParIdByDicCode(Map<String, Object> params) {
		return selectList("examInfo.getParIdByDicCode",params);
	}
	
	
	/**
	 * @Title: getSchoolCodeAndShoolName
	 * @Description: 根据学校类型集合得到学校code和学校姓名
	 * @author zhaohuanhuan
	 * @date 2016年10月12日 
	 * @param params
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getSchoolCodeAndShoolName(Map<String, Object> params) {
		return selectList("examInfo.getSchoolCodeAndShoolName",params);
	}
	
	/**
	 * @Title: selectSchoolByExamCode
	 * @Description: 根据当前考试编号的到当前考试编号下面的学校 
	 * @author zhaohuanhuan
	 * @date 2016年10月17日 
	 * @param examCode
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> selectSchoolByExamCode(String examCode) {
		return selectList("examInfo.selectSchoolByExamCode",examCode);
	}
	
	/**
	 * @Title: deleteExamRefSchool
	 * @Description: 删除跟此次考试相关的学校
	 * @author zhaohuanhuan
	 * @date 2016年10月17日 
	 * @param examCodeAry
	 * @return
	 * @return int
	 */
	public int deleteExamRefSchool(List<String> examCode) {
		 Map<String,Object> param = new HashMap<String,Object>() ;
         param.put( "examCode", examCode ) ;
        return delete("examInfo.deleteExamRefSchool", param );
	}
	
	
	
 
	/**
	 * @Title: deleteExamRefAccessory
	 * @Description: TODO
	 * @author zhaohuanhuan
	 * @date 2016年10月17日 
	 * @param examCodeAry
	 * @return
	 * @return int
	 */
	public int deleteExamRefAccessory(String examCode) {
       return delete("examInfo.deleteExamRefAccessory", examCode );
	}
	
	/**
	 * @Title: updateExamRefAccessory
	 * @Description: 更新与 相关的附件的考试 编号 
	 * @author zhaohuanhuan
	 * @date 2016年10月17日 
	 * @param paramMap
	 * @return void
	 */
	public void updateExamRefAccessory(Map<String, Object> paramMap) {
		update("examInfo.updateExamRefAccessory", paramMap);
	}
	
	/**
	 * @Title: getRoleByUserId
	 * @Description: 根据用户id得到用户角色
	 * @author zhaohuanhuan
	 * @date 2016年10月19日 
	 * @param userId
	 * @return
	 * @return String
	 */
	public String getRoleByUserId(String userId) {
		return selectOne("examInfo.getRoleByUserId",userId);
	}
	
	/**
	 * @Title: getCoursesByExamCode
	 * @Description: 根据考试编号得到学科
	 * @author zhaohuanhuan
	 * @date 2016年11月1日 
	 * @param params
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getCoursesByExamCode(Map<String, Object> params){
		return selectList("arrangement.getCoursesByExamCode", params);
	}
}
