/**
 * 2016年9月12日
 */
package data.academic.statisticsAnalysis.controller;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import data.framework.support.AbstractBaseController;

/**
 * @Title: ClassRoomTeacherController
 * @Description:该控制层是用于学校超级管理员页面跳转
 * @author zhaohuanhuan
 * @date 2016年9月12日 下午1:57:21
 */
@RestController
@RequestMapping("statisticsAnalysis/schoolAdmin")
public class SchoolAdminController extends AbstractBaseController {

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
	
}


