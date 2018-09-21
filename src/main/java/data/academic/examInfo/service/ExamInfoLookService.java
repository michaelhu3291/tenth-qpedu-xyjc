package data.academic.examInfo.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import data.framework.support.AbstractService;

@Service
public class ExamInfoLookService extends AbstractService{

	
	/**
	 * @Title showExam
	 * @Description 门户页面展示考试信息
	 * @author wangchaofa
	 * @CreateDate Nov 7,2016
	 * @param
	 * @return
	 */
	public List<Map<String,Object>> showExam(Map<String,Object> map){
		return selectList("examPaper.getExam", map);
	}
	
	/**
	 * @Title: getExamForSchool
	 * @Description:学校管理员  门户页面展示考试信息
	 * @author zhaohuanhuan
	 * @date 2017年8月31日 
	 * @return List<Map<String,Object>>
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getExamForSchool(Map<String, Object> map){
		return selectList("examPaper.getExamForSchool", map );
	}
	
}
