package data.academic.examInfo.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import data.framework.support.AbstractBaseController;

/**
 * @Title: MarkingArrangementAdmin
 * @Description: TODO
 * @author zhaohuanhuan
 * @date 2016年10月24日 下午3:03:00
 */
@RestController
@RequestMapping("examInfo/markingArrangement_admin")
public class MarkingArrangementAdminController  extends AbstractBaseController{
 
	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
	}

}
