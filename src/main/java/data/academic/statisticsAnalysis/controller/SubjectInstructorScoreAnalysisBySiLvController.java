 
package data.academic.statisticsAnalysis.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import data.framework.support.AbstractBaseController;

/**
 * @Title: SubjectInstructorScoreAnalysisByAvgController
 * @Description: TODO
 * @author zhaohuanhuan
 * @date 2016年9月27日 下午2:21:53
 */ 
@RestController
@RequestMapping("statisticsAnalysis/subjectInstructorScoreAnalysisBySiLv")
public class SubjectInstructorScoreAnalysisBySiLvController  extends AbstractBaseController{

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}

}
