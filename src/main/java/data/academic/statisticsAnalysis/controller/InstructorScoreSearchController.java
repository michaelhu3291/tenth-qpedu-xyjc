/**
 * 2016年10月10日
 */
package data.academic.statisticsAnalysis.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import data.framework.support.AbstractBaseController;

/**
 * @Title: InstructorScoreSearchController
 * @Description: TODO
 * @author zhaohuanhuan
 * @date 2016年10月10日 下午2:28:02
 */

@RestController
@RequestMapping("statisticsAnalysis/instructorScoreSearch")
public class InstructorScoreSearchController extends AbstractBaseController {

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
	
}
