package data.academic.schoolHoliday.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import data.framework.support.AbstractValidatorController;
import data.framework.validation.Errors;
import data.platform.authority.security.SecurityContext;
import data.academic.schoolHoliday.service.HolidayTabService;
import data.academic.schoolHoliday.service.SchoolHolidayService;

/**
 * OA-日程管理-个人日程控制器类。
 * 
 * @author LIUGUO
 * 
 */
@Controller
@RequestMapping("schoolHoliday/schoolHolidaySet/addSchoolHoliday")
public class AddSchoolHolidayController extends AbstractValidatorController {

	@Override
	protected void validate(Object obj, Errors errors) {

	}

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// 加载数据字典节假日类型
		Map<String, Object> map = holidayTabService.selectIdByCode("JJRLX");
		// 加载数据字典节假日类型
		List<Map<String, String>> holidayType = holidayTabService.loadChildDictionary(map.get("Id").toString());
		// List<Map<String, String>> holidayType =
		// holidayTabService.loadChildDictionary("5BB75C27-768F-46CD-BCFB-EE4798C45788");
		model.addAttribute("holidayType", holidayType);
	}

	/**
	 * @Title: submit
	 * @Description: 新增校历
	 * @author xiahuajun
	 * @date 2016年8月15日
	 * @param data
	 * @param out
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "command=saveHoliday")
	public void submit(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		Date time = new Date();
		String startTime = parseString(paramMap.get("startTime")).replace("-", "");
		String endTime = parseString(paramMap.get("endTime")).replace("-", "");
		String currentId = SecurityContext.getPrincipal().getId();
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		if (formatString(paramMap.get("mainId")) == "") {
			paramMap.put("createPerson", currentId);
			paramMap.put("createTime", parseString(formatDate(time, "yyyyMMdd")));
			schoolHolidayService.saveOrUpdate(paramMap);
		} else {
			paramMap.put("updatePerson", currentId);
			paramMap.put("updateTime", parseString(formatDate(time, "yyyyMMdd")));
			schoolHolidayService.saveOrUpdate(paramMap);
		}
		out.print(getSerializer().message("保存成功"));
	}

	/**
	 * @Title: loadHoliday
	 * @Description: 加载单一历史校历
	 * @author xiahuajun
	 * @date 2016年8月15日
	 * @param data
	 * @param out
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "command=loadHoliday")
	public void loadHoliday(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		List<Map<String, Object>> list = schoolHolidayService.loadHoliday(paramMap);
		out.print(getSerializer().formatObject(list));
	}

	/**
	 * @Title: deleteHoliday
	 * @Description: 删除单一历史校历
	 * @author xiahuajun
	 * @date 2016年8月15日
	 * @param data
	 * @param out
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "command=deleteHoliday")
	public void deleteHoliday(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		String id = formatString(paramMap.get("mainId"));
		schoolHolidayService.deleteHoliday(id);
		out.print(getSerializer().message("删除成功"));
	}

	/**
	 * @Title: getHoliType
	 * @Description: 加载校历节假日下拉框
	 * @author xiahuajun
	 * @date 2016年8月15日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "command=getHoliType")
	public void getHoliType(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		List<Map<String, Object>> list = schoolHolidayService.getHoliType(paramMap);
		out.print(getSerializer().formatObject(list));
	}

	@Autowired
	private SchoolHolidayService schoolHolidayService;

	@Autowired
	private HolidayTabService holidayTabService;

}
