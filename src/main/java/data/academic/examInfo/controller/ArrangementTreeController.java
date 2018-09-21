
package data.academic.examInfo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import data.framework.support.AbstractBaseController;

/**
 * @Title: ArrangementTreeController
 * @Description: 安排阅卷人控制层
 * @author zhaohuanhuan
 * @date 2016年10月24日 下午8:00:09
 */
@RestController
@RequestMapping("examInfo/arrangementTree")
public class ArrangementTreeController  extends AbstractBaseController{


	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		 String gradeId=request.getParameter("gradeId");
		 model.addAttribute("gradeId", gradeId);
	}

	
}
