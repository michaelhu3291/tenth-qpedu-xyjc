/**
 * 2016年10月10日
 */
package data.academic.schoolManage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import data.framework.support.AbstractBaseController;

/**
 * @Title: NoticeManageController
 * @Description: TODO
 * @author zhaohuanhuan
 * @date 2016年10月10日 下午2:11:57
 */
@RestController
@RequestMapping("schoolManage/noticeManage")
public class NoticeManageController extends AbstractBaseController{

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}

}
