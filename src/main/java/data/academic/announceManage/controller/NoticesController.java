package data.academic.announceManage.controller;
/**
 * @author wangchaofa
 * @CreateTime Oct 15,2016
 * @UpdateTime Oct 21,2016
 */
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import data.framework.support.AbstractBaseController;

@RestController
@RequestMapping("/announcement/announces")
public class NoticesController extends AbstractBaseController{
	
	@Override
	protected void init(ModelMap model, HttpServletRequest request) {

	}
}
