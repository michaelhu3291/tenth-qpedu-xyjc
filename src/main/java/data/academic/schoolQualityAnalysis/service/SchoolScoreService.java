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
public class SchoolScoreService extends AbstractService{
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
		return selectList("schoolScore.getScoreDetailList", paramMap);
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
		return selectOne("schoolScore.getOneCourseRateList", paramMap);
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
		    	delete("schoolScore.deleteTotalScore", tMap);
				delete("schoolScore.deleteSmallTitleScore", tMap);
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
    	delete("schoolScore.deleteTotalScore", tMap);
		delete("schoolScore.deleteSmallTitleScore", tMap);
		
		batchNum = 0;
        temp = new ArrayList<HashMap<String,Object>>();
        
		for(int i=0;i<paramList.size();i++){
		    if(batchNum<100){
		        temp.add(paramList.get(i));
		        batchNum++;
		    }else{
		    	insert("schoolScore.insertSmallTitle",temp);
		        temp = new ArrayList<HashMap<String,Object>>();
		        temp.add(paramList.get(i));
		        batchNum=1;
		    }
		    paramListSize++;
		}
        for(int i=paramListSize;i<paramList.size();i++){
        	temp.add(paramList.get(i));
        }
        insert("schoolScore.insertSmallTitle",temp);

        batchNum = 0;
        temp = new ArrayList<HashMap<String,Object>>();
        for(int i=0;i<totalScoreList.size();i++){
		    if(batchNum<100){
		        temp.add(totalScoreList.get(i));
		        batchNum++;
		    }else{
		    	 insert("schoolScore.insertEveryStuEveryCourseTotalScore",temp);
		    	 batchNum = 1;
		         temp = new ArrayList<HashMap<String,Object>>();
		         temp.add(totalScoreList.get(i));
		    }
		    totalScoreListSize++;
		}
        for(int i=totalScoreListSize;i<totalScoreList.size();i++){
        	temp.add(totalScoreList.get(i));
        }
        insert("schoolScore.insertEveryStuEveryCourseTotalScore",temp);
		
		
		
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
		return selectList("schoolScore.getExamQuestion", requestMap);
	}
	
	/**
	 * 
	 * @Title: getDistrictRateMap
	 * @Description: 得到一个学校该年级总分及格率 优良率 平均分 排名
	 * @author chenteng
	 * @date 2017年8月10日 
	 * @return HashMap<String,Object>
	 * @param mapForDistrict
	 * @return
	 */
	public HashMap<String, Object> getDistrictRateMap(Map<String, Object> mapForDistrict) {
		return selectOne("schoolScore.getDistrictRateMap", mapForDistrict);
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
		return selectOne("schoolScore.getDistrictRateMap", mapForDistrict);
	}

	
	
	public List<Double> getScoreNum(Map<String, Object> requestMap) {
		return selectList("schoolScore.getScoreNum", requestMap);
	}


	public List<Double> getScoreList(Map<String, Object> requestMap) {
		return selectList("schoolScore.getScoreList", requestMap);
	}
	public List<HashMap<String, String>> getCourseExaminfoList(Map<String, Object> requestMap) {
		return selectList("schoolScore.getCourseExaminfoList", requestMap);
	}
	public List<String> getMaxNameList(Map<String, Object> mapForDistrict) {
		return selectList("schoolScore.getMaxNameList", mapForDistrict);
	}
	public List<String> getMinNameList(Map<String, Object> mapForDistrict) {
		return selectList("schoolScore.getMinNameList", mapForDistrict);
	}
	public List<Map<String, String>> getClassBySchoolCodeAndGrade(String schoolCode, String grade,String schoolCode2) {
		Map<String,String> paramMap = new HashMap<>();
		paramMap.put("schoolCode", schoolCode);
		paramMap.put("grade", grade);
		paramMap.put("schoolCode2", schoolCode2);
		return selectList("schoolScore.getClassBySchoolCodeAndGrade", paramMap);
	}

	public List<Map<String, String>> getClassByTeacher(String loginName, String grade) {
		Map<String,String> paramMap = new HashMap<>();
		paramMap.put("loginName", loginName);
		paramMap.put("grade", grade);
		return selectList("schoolScore.getClassByTeacher", paramMap);
	}
}