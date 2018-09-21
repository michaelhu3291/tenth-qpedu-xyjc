package data.academic.dataManage.service;

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
public class SmallTitleService extends AbstractService{
	
	/**
	 * @Title: insertSmallTitle
	 * @Description: 导入小题分
	 * @author xiahuajun
	 * @date 2016年8月3日 
	 * @param list
	 * @return void
	 */
	public void insertSmallTitle(List<Map<String,Object>> list){
		/*for (Map<String, Object> map : list) {
			if(map.get("exam_number")!=null && !"".equals(map.get("exam_number").toString()))
			{
				insert("smallTitle.insertSmallTitle",list);
			}
		}*/
		insert("smallTitle.insertSmallTitle",list);
		//insert("smallTitle.insertSmallTitle",list);
	}
	/**
	 * @Title: insertEveryStuEveryCourseTotalScore
	 * @Description: 导入每个人的每个科目的总分数
	 * @author chenteng
	 * @date 2016年8月3日 
	 * @param list
	 * @return void
	 */
	public void insertEveryStuEveryCourseTotalScore(List<Map<String,Object>> list){
		insert("smallTitle.insertEveryStuEveryCourseTotalScore",list);
	}
	/**
	 * @Title: serachSmallTitleScore
	 * @Description: 分页查询小题分
	 * @author xiahuajun
	 * @date 2016年8月3日 
	 * @param requestMap
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	public PagingResult<Map<String, Object>> serachSmallTitleScore(Map<String, Object> requestMap, String sortField,
			String sort, int currentPage, int pageSize) {
		 if(StringUtils.isBlank( sortField )){
			 //sortField = "Exam_Number" ;
			 sortField = "Total_Score";
		 }
	        
	     if(StringUtils.isBlank( sort )){
	    	 sort = "DESC";
	     }else{
	    	 sort = "DESC";
	     }
	     return selectPaging( "smallTitle.selectSmallTitlePaging", requestMap, sortField, sort, currentPage, pageSize ) ;
	}
	public List<Map<String, Object>> serachImportSmallTitleScore(Map<String, Object> requestMap) {
		
	     return selectList( "smallTitle.selectImportSmallTitlePaging", requestMap) ;
	}
	
/*	*//**
	 * @Title: deleteSmallTitleScore
	 * @Description: 删除小题分
	 * @author xiahuajun
	 * @date 2016年8月9日 
	 * @param map
	 * @return void
	 *//*
	public void deleteSmallTitleScore(List<Map<String,String>> map) {
		// TODO Auto-generated method stub
		delete("smallTitle.deleteSmallTitleScore", map);
	}*/
	
	/**
	 * @Title: getDetailList
	 * @Description: 查看详情
	 * @author xiahuajun
	 * @date 2016年8月11日 
	 * @param paramMap
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getDetailList(Map<String, Object> paramMap) {
		return selectList("smallTitle.selectDetailList", paramMap);
	}
	/**
	 * 
	 * @Title: getScoreDetailList
	 * @Description: 获取个人试卷每小题分数和总分
	 * @author chenteng
	 * @date 2017年8月4日 
	 * @return List<Map<String,Object>>
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getScoreDetailList(Map<String, Object> paramMap) {
		return selectList("smallTitle.getScoreDetailList", paramMap);
	}
	
	/**
	 * 
	 * @Title: getOneCourseRateList
	 * @Description: 得到一门学科的实考人数 最高分 平均分 平均分排名，优秀人数 良好人数，合格人数，及格人数
	 * @author chenteng
	 * @date 2017年8月7日 
	 * @return List<Map<String,Object>>
	 * @param paramMap
	 * @return
	 */
	public HashMap<String, Object> getOneCourseRateList(Map<String, Object> paramMap) {
		return selectOne("smallTitle.getOneCourseRateList", paramMap);
	}
	/**
	 * @Title: getCurrentYear
	 * @Description: 显示学年为当前学年
	 * @author xiahuajun
	 * @date 2016年8月18日 
	 * @param data
	 * @param out
	 * @return void
	 */
	public Map<String, Object> getCurrentYearByParam(Map<String, Object> requestMap) {
		return selectOne("smallTitle.getCurrentYear", requestMap);
	}
	
	/**
	 * @Title: selectOrgCodeByLoginName
	 * @Description: 青浦超级管理员角色查询组织code
	 * @author xiahuajun
	 * @date 2016年11月14日 
	 * @param loginName
	 * @return
	 * @return String
	 */
	public String selectOrgCodeByLoginName(String loginName) {
		return selectOne("smallTitle.selectOrgCodeByLoginName", loginName);
	}
	
	/**
	 * 
	 * @Title: batchOperate
	 * @Description: 批量操作保证事务正确 先删导入过会引起重复的试卷分数 再导小题分数 再导试卷总分
	 * @author chenteng
	 * @date 2017年8月4日 
	 * @return void
	 * @param deleteMap
	 * @param paramList
	 * @param totalScoreList
	 */
	@Transactional
	public void batchOperate(HashMap<String, Object> deleteMap, List<HashMap<String, Object>> paramList,List<HashMap<String, Object>> totalScoreList) {
		//分批操作
		
		List<HashMap<String,Object>> temp = new ArrayList<HashMap<String,Object>>();
		int batchNum = 0;
		int deleteMapListSize=0;
		int paramListSize=0;
		int totalScoreListSize=0;
		ArrayList<String> examNumList =(ArrayList<String>) deleteMap.get("examNumList");
		ArrayList<String> tempExamNumList =new ArrayList<>();
		HashMap<String,Object> tMap =new HashMap<>();
		tMap.put("course", deleteMap.get("course"));
		for(int i=0;i<examNumList.size();i++){
		    if(batchNum<2000){
		    	tempExamNumList.add(examNumList.get(i));
		        batchNum++;
		    }else{
		    	tMap.put("examNumList", tempExamNumList);
		    	delete("smallTitle.deleteTotalScore", tMap);
				delete("smallTitle.deleteSmallTitleScore", tMap);
				tempExamNumList = new ArrayList<String>();
				tempExamNumList.add(examNumList.get(i));
			     batchNum=1;
		    }
		    deleteMapListSize++;
		}
		//tempExamNumList = new ArrayList<String>();
		for(int i=deleteMapListSize;i<examNumList.size();i++){
			tempExamNumList.add(examNumList.get(i));
        }
		tMap.put("examNumList", tempExamNumList);
    	delete("smallTitle.deleteTotalScore", tMap);
		delete("smallTitle.deleteSmallTitleScore", tMap);
		
		batchNum = 0;
        temp = new ArrayList<HashMap<String,Object>>();
        
		for(int i=0;i<paramList.size();i++){
		    if(batchNum<100){
		        temp.add(paramList.get(i));
		        batchNum++;
		    }else{
		    	insert("smallTitle.insertSmallTitle",temp);
		        temp = new ArrayList<HashMap<String,Object>>();
		        temp.add(paramList.get(i));
		        batchNum=1;
		    }
		    paramListSize++;
		}
        for(int i=paramListSize;i<paramList.size();i++){
        	temp.add(paramList.get(i));
        }
        insert("smallTitle.insertSmallTitle",temp);

        batchNum = 0;
        temp = new ArrayList<HashMap<String,Object>>();
        for(int i=0;i<totalScoreList.size();i++){
		    if(batchNum<100){
		        temp.add(totalScoreList.get(i));
		        batchNum++;
		    }else{
		    	 insert("smallTitle.insertEveryStuEveryCourseTotalScore",temp);
		    	 batchNum = 1;
		         temp = new ArrayList<HashMap<String,Object>>();
		         temp.add(totalScoreList.get(i));
		    }
		    totalScoreListSize++;
		}
        for(int i=totalScoreListSize;i<totalScoreList.size();i++){
        	temp.add(totalScoreList.get(i));
        }
        insert("smallTitle.insertEveryStuEveryCourseTotalScore",temp);
		
		
		
	}
	/**
	 * 
	 * @Title: 
	 * @Description: 获取试卷题目信息
	 * @author chenteng
	 * @date 2017年8月4日 
	 * @return List<HashMap<String,String>>
	 * @param requestMap
	 * @return
	 */
	public List<HashMap<String, String>> getExamQuestion(Map<String, Object> requestMap) {
		return selectList("smallTitle.getExamQuestion", requestMap);
	}
	
	/**
	 * 
	 * @Title: getDistrictRateMap
	 * @Description: 得到一个区各个学校的总分及格率 优良率 平均分 排名
	 * @author chenteng
	 * @date 2017年8月10日 
	 * @return HashMap<String,Object>
	 * @param mapForDistrict
	 * @return
	 */
	public HashMap<String, Object> getDistrictRateMap(Map<String, Object> mapForDistrict) {
		return selectOne("smallTitle.getDistrictRateMap", mapForDistrict);
	}
	
	/**
	 * 
	 * @Title: getDistrictCountMap
	 * @Description: 全区总分及格率，优良率，平均分
	 * @author chenteng
	 * @date 2017年8月10日 
	 * @return Map<String,Object>
	 * @param mapForDistrict
	 * @return
	 */
	public Map<String, Object> getDistrictCountMap(Map<String, Object> mapForDistrict) {
		return selectOne("smallTitle.getDistrictRateMap", mapForDistrict);
	}

	
	
	public List<Double> getScoreNum(Map<String, Object> requestMap) {
		return selectList("smallTitle.getScoreNum", requestMap);
	}


	public List<Double> getScoreList(Map<String, Object> requestMap) {
		return selectList("smallTitle.getScoreList", requestMap);
	}
	public List<HashMap<String, String>> getCourseExaminfoList(Map<String, Object> requestMap) {
		return selectList("smallTitle.getCourseExaminfoList", requestMap);
	}
	public List<String> getMaxNameList(Map<String, Object> mapForDistrict) {
		return selectList("smallTitle.getMaxNameList", mapForDistrict);
	}
	public List<String> getMinNameList(Map<String, Object> mapForDistrict) {
		return selectList("smallTitle.getMinNameList", mapForDistrict);
	}
	
}