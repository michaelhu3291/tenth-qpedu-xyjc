/**
 * 2016年10月21日
 */
package data.academic.examNumberManagement.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import data.academic.examNumberManagement.service.AdminExamNumberService;
import data.framework.support.AbstractBaseController;

/**
 * @Title: AdminExamNumberManageController
 * @Description:青浦管理员考号管理控制层（区级）
 * @author zhaohuanhuan
 * @date 2016年10月21日 上午11:24:48
 */

@Controller
@RequestMapping("examNumberManagement/examNumberManage_admin")
public class AdminExamNumberManageController  extends AbstractBaseController{

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		
	}
	
	/**
	 * @Title: getSchoolByExamCode
	 * @Description:  得到青浦管理员发布的考试下的学校
	 * @author zhaohuanhuan
	 * @date 2016年10月21日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params="command=getSchoolByExamCode")
	public void getSchoolByExamCode(@RequestParam("data") String data,java.io.PrintWriter out)
	{
		Map<String, Object> paramMap=getSerializer().parseMap(data);
		List<Map<String,Object>> list=adminExamNumverService.getSchoolByExamCode(paramMap.get("examNumber").toString());
    	out.print(getSerializer().formatList(list));
	}
	
	@Autowired 
	private AdminExamNumberService adminExamNumverService;
}
