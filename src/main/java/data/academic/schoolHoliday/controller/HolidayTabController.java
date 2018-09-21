package data.academic.schoolHoliday.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.platform.authority.security.SecurityContext;
import data.academic.schoolHoliday.service.HolidayTabService;

/**
 * 节假日维护控制器类
 * 
 */
@RestController
@RequestMapping("schoolHoliday/schoolHolidaySet/holidayTab")
public class HolidayTabController extends AbstractBaseController {

	@Autowired
	private HolidayTabService holidayTabService;

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		Map<String, Object> map = holidayTabService.selectIdByCode("JJRLX");
		// 加载数据字典节假日类型
		List<Map<String, String>> holidayType = holidayTabService.loadChildDictionary(map.get("Id").toString());
		model.addAttribute("holidayType", holidayType);
	}

	@SuppressWarnings(value = "unchecked")
	@RequestMapping(params = "command=search")
	public void queryAll(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		//String timeSpan = parseString(paramMap.get("q"));
		int currentPage = parseInteger(paramMap.get("page"));
		int pageSize = parseInteger(paramMap.get("rows"));
		String sort = trimString(paramMap.get("sord"));
		String sortField = trimString(paramMap.get("sidx"));
		paramMap.put("fastSearch", paramMap.get("q"));

		PagingResult<Map<String, Object>> pagingResult = holidayTabService.serachPaging(paramMap, sortField, sort,
				currentPage, pageSize);

		List<Map<String, Object>> list = pagingResult.getRows();
		PagingResult<Map<String, Object>> newPagingResult = new

		PagingResult<Map<String, Object>>(list, pagingResult.getPage(), pagingResult.getPageSize(),
				pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}

	/**
	 * 新增或修改节假日维护信息 *
	 * 
	 * @param data
	 * @param out
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "command=submit")
	public void submit(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);

		Date time = new Date();
		String currentId = SecurityContext.getPrincipal().getId();
		if (formatString(paramMap.get("holiId")) != "") {

			paramMap.put("updatePerson", currentId);
			paramMap.put("updateTime", formatString(formatDate(time, "yyyyMMdd HHmmss")));

			holidayTabService.saveOrUpdate(paramMap);
		} else {
			paramMap.put("createPerson", currentId);
			paramMap.put("createTime", formatString(formatDate(time, "yyyyMMdd HHmmss")));
			holidayTabService.saveOrUpdate(paramMap);
		}
		out.print(getSerializer().message(""));
	}

	/**
	 * 根据一个实体 ID 来删除数据信息的 Web 方法。<br />
	 * <br />
	 * 命令: "delete" ；<br/>
	 * <br/>
	 * 
	 * @param data
	 *            输入参数(由 Browser 端 POST 回来的JSON数据)
	 * @param out
	 *            响应输出对象
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "command=delete")
	public void delete(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		holidayTabService.remove((List<String>) paramMap.get("id"));
		out.print(getSerializer().message(""));
	}

}
