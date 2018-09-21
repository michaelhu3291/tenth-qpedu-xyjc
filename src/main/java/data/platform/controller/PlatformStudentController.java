package data.platform.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.platform.service.PlatformStudentService;

/*
 * 学生管理控制器
 * 
 * */
@Controller
@RequestMapping("platform/student")
public class PlatformStudentController extends AbstractBaseController {
	@Autowired
	private PlatformStudentService platformStudentService;

	/**
	 * 学生管理分页查询
	 * 
	 * @param data
	 * @param out
	 */
	@RequestMapping(params = ("command=search"))
	public void search(@RequestParam("data") String data, HttpServletRequest request, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		boolean isFast = parseBoolean(paramMap.get("isFast"));
		if (isFast) {
			paramMap.put("stuName", paramMap.get("q"));
			// 快速查询 只能输'q'
		}
		int currentPage = parseInteger(paramMap.get("page"));
		int pageSize = parseInteger(paramMap.get("rows"));
		String sortField = trimString(paramMap.get("sidx"));
		if (sortField.trim().length() == 0) {
			sortField = "year";
		}
		String sort = trimString(paramMap.get("sord"));

		PagingResult<Map<String, Object>> pagingResult = platformStudentService.serachStu(paramMap, sortField, sort, currentPage, pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();

		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(list, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}

	/*
	 * 查询专业 4.23
	 */
	@RequestMapping(params = ("command=searchCollege"))
	public void loadTeacher(@RequestParam("term") String term, java.io.PrintWriter out) throws UnsupportedEncodingException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("college", term);
		List<Map<String, Object>> result = platformStudentService.selectCollege(param);

		out.print(getSerializer().formatList(result));

	}

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub

	}

}
