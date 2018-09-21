 
package data.academic.statisticsAnalysis.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import data.framework.support.AbstractBaseController;

/**
 * @Title: SubjectInstructorScoreAnalysisContorller
 * @Description: 教研员成绩分析服务层
 * @author zhaohuanhuan
 * @date 2016年9月27日 上午10:31:26
 */
	@Controller
	@RequestMapping("statisticsAnalysis/subjectInstructorScoreAnalysis")
public class SubjectInstructorScoreAnalysisContorller extends AbstractBaseController {

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}

}
