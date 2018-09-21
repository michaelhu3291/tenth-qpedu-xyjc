package data.academic.schoolHoliday.controller;

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
import data.academic.schoolHoliday.service.HolidayTabService;
import data.academic.schoolHoliday.service.SchoolHolidayService;

/**
 * @Title: AddHolidayTabController
 * @Description: 校历
 * @author xiahuajun
 * @date 2016年8月15日
 */
@Controller
@RequestMapping("schoolHoliday/schoolHolidaySet/addHolidayTab")
public class AddHolidayTabController extends AbstractValidatorController {

	@Override
	protected void validate(Object obj, Errors errors) {

	}

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// 加载数据字典节假日类型
		//List<Map<String, String>> holidayType = holidayTabService
		//		.loadChildDictionary("6F9AA879-8706-40E8-BDF0-00981A5E1598");
		Map<String, Object> map = holidayTabService.selectIdByCode("JJRLX");
		// 加载数据字典节假日类型
		List<Map<String, String>> holidayType = holidayTabService.loadChildDictionary(map.get("Id").toString());
		model.addAttribute("holidayType", holidayType);
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
	@RequestMapping(params = "command=loadHolidayTab")
	public void loadHoliday(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		List<Map<String, Object>> list = holidayTabService.loadHolidayTab(paramMap);
		out.print(getSerializer().formatObject(list));
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
