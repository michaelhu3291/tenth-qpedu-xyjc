package data.academic.link.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import data.academic.link.entity.EntityLinkNet;
import data.academic.link.service.LinkNetService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.platform.authority.security.SecurityContext;

@RestController
@RequestMapping("link/linkNet")
public class LinkNetController extends AbstractBaseController {
	@Autowired
	private LinkNetService linkNetService;

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub

	}

	/**
	 * 2016年7月16日 xiahuajun
	 */
	@RequestMapping(params = "command=addLink")
	public void addLink(@RequestParam("data") String data,
			java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		Map<String, Object> map = new HashMap<String, Object>();
		if (paramMap.get("id") == null) {
			// 添加操作
			// 创建人
			String create_person = SecurityContext.getPrincipal().getId();
			// 创建事间
			String create_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date());
			map.put("create_person", create_person);
			map.put("create_time", create_time);
			map.put("selectMenu", paramMap.get("selectMenu").toString());
			map.put("link_name", paramMap.get("link_name").toString());
			map.put("link_address", paramMap.get("link_address").toString());
			linkNetService.addLink(map);
		} else {
			// 修改操作
			// 修改人
			String update_person = SecurityContext.getPrincipal().getId();
			// 修改时间
			String update_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date());
			paramMap.put("update_person", update_person);
			paramMap.put("update_time", update_time);
			linkNetService.updateLinkById(paramMap);
		}
		// paramMap.put("mess", "success");
		out.print(getSerializer().formatObject(""));
	}

	/**
	 * 查询链接 2016年7月16日 xiahuajun
	 */
	@RequestMapping(params = "command=searchLink")
	public void searchLink(@RequestParam("data") String data,
			java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		List<String> list = linkNetService.searchLink(paramMap);
		out.print(getSerializer().formatList(list));
	}

	/**
	 * 分页查询所有链接 2016年7月21日 xiahuajun
	 */
	@RequestMapping(params = "command=searchPaging")
	public void searchPaging(@RequestParam("data") String data,
			java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		// Map<String, Object> paramMap = new HashMap<String, Object>();
		boolean isFast = parseBoolean(requestMap.get("isFast"));
		String linkName = "";
		if (isFast) {
			linkName = trimString(requestMap.get("q"));
		}
		requestMap.put("linkName", linkName);
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));

		if (sortField.trim().length() == 0) {
			sortField = "Update_Time desc,Create_Time";
		}
		String sort = trimString("desc");
		PagingResult<Map<String, Object>> pagingResult = linkNetService
				.serachLink(requestMap, sortField, sort, currentPage, pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(
				list, pagingResult.getPage(), pagingResult.getPageSize(),
				pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}

	/**
	 * 删除链接 2016年7月22日 xiahuajun
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "command=delete")
	public void delete(@RequestParam("data") String data,
			java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		linkNetService.remove((List<String>) paramMap.get("id"));
		out.print(getSerializer().message(""));
	}

	/**
	 * 通过id把数据查出来展现给用户以便修改 2016年7月22日 xiahuajun
	 */
	@RequestMapping(params = "command=selectLinkById")
	public void selectLinkById(@RequestParam("id") String id,
			java.io.PrintWriter out) {
		// Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
		EntityLinkNet entityLinkNet = linkNetService.selectLinkById(id);
		Map<String, Object> map = getSerializer().parseMap(
				getSerializer().formatObject(entityLinkNet));
		map.put("link_name", map.get("name"));
		map.put("link_address", map.get("url"));
		map.put("selectMenu", map.get("code"));
		out.print(getSerializer().formatMap(map));
	}

}
