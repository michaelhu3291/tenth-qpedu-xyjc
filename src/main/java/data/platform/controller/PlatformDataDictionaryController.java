package data.platform.controller;

import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import data.framework.data.DataTree;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.platform.entity.EntityPlatformDataDictionary;
import data.platform.service.PlatformDataDictionaryService;

/**
 * 平台-数据字典控制器类。
 * 
 * @author wanggq
 */
@Controller
@RequestMapping("platform/dictionary")
public class PlatformDataDictionaryController extends AbstractBaseController {

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
	}

	/**
	 * 分页查询数据字典信息的方法。
	 * 
	 * @param data
	 *            输入参数(由 Browser 端 POST 回来的JSON数据)
	 * @param out
	 *            响应输出对象
	 */
	@RequestMapping(params = "command=search")
	public void search(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		boolean isFast = parseBoolean(paramMap.get("isFast"));
		String dictionaryName = "";
		String dictionaryCode = "";
		if (isFast)
			dictionaryName = trimString(paramMap.get("q"));
		else {
			dictionaryName = trimString(paramMap.get("dictionaryName"));
			dictionaryCode = trimString(paramMap.get("dictionaryCode"));
		}
		int currentPage = parseInteger(paramMap.get("page"));
		int pageSize = parseInteger(paramMap.get("rows"));
		String sortField = parseString(paramMap.get("sidx"));
		String sort = parseString(paramMap.get("sord"));
		PagingResult<EntityPlatformDataDictionary> pagingResult = dictionaryService.searchDataDictionary(dictionaryName, dictionaryCode, sortField, sort, currentPage, pageSize);
		List<EntityPlatformDataDictionary> list = pagingResult.getRows();
		List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
		for (EntityPlatformDataDictionary entity : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", formatString(entity.getId()));
			map.put("dictionaryName", formatString(entity.getDictionaryName()));
			map.put("dictionaryCode", formatString(entity.getDictionaryCode()));
			map.put("dictionaryType", formatString(entity.getDictionaryType()));
			map.put("dictionaryModule", formatString(entity.getDictionaryModule()));
			map.put("dictionaryLevel", formatString(entity.getDictionaryLevel()));
			map.put("remark", formatString(entity.getRemark()));
			map.put("sortNumber", formatString(entity.getSortNumber()));
			newList.add(map);
		}
		PagingResult<Map<String, String>> newPagingResult = new PagingResult<Map<String, String>>(newList, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}

	/**
	 * 根据一个实体 ID 来加载数据字典详细信息的 Web 方法。<br />
	 * <br />
	 * 命令: "load" ；<br/>
	 * <br/>
	 * 
	 * @param id
	 *            输入参数(由 Browser 端 POST 回来的数据字典编号)
	 * @param out
	 *            响应输出对象
	 */
	@RequestMapping(params = "command=load")
	public void load(@RequestParam("id") String id, java.io.PrintWriter out) {
		EntityPlatformDataDictionary entity = dictionaryService.load(id);
		Map<String, Object> map = getSerializer().parseMap(getSerializer().formatObject(entity));
		String parentId = parseString(map.get("parentDictionary"));
		List<Map<String, String>> parentDictionary = new ArrayList<Map<String, String>>();
		if (StringUtils.isNotBlank(parentId)) {
			EntityPlatformDataDictionary parentEntity = dictionaryService.load(parentId);
			Map<String, String> parentMap = new HashMap<String, String>();
			parentMap.put("text", parseString(parentEntity.getDictionaryName()));
			parentMap.put("value", parseString(parentEntity.getId()));
			parentDictionary.add(parentMap);
		}
		map.put("parentDictionary", parentDictionary);
		out.print(getSerializer().formatMap(map));
	}

	/**
	 * 保存数据字典信息的Web方法。<br />
	 * <br />
	 * 命令: "submit" ；<br/>
	 * <br/>
	 * 
	 * @param data
	 *            输入参数(由 Browser 端 POST 回来的JSON数据)
	 * @param out
	 *            响应输出对象
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "command=submit")
	public void submit(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		EntityPlatformDataDictionary entity = null;
		String id = trimString(paramMap.get("id"));
		if (StringUtils.isNotBlank(id)) {
			entity = dictionaryService.load(id);
			entity.setUpdateTime(new Date());
		} else {
			entity = new EntityPlatformDataDictionary();
			entity.setCreateTime(new Date());
		}
		entity.setDictionaryName(parseString(paramMap.get("dictionaryName")));
		entity.setDictionaryCode(parseString(paramMap.get("dictionaryCode")));

		Map<String, Object> dictionaryTypeMap = (Map<String, Object>) paramMap.get("dictionaryType");
		String dictionaryType = (dictionaryTypeMap == null || dictionaryTypeMap.isEmpty()) ? null : parseString(dictionaryTypeMap.get("value"));
		entity.setDictionaryType(dictionaryType);

		Map<String, Object> dictionaryModuleMap = (Map<String, Object>) paramMap.get("dictionaryModule");
		String dictionaryModule = (dictionaryModuleMap == null || dictionaryModuleMap.isEmpty()) ? null : parseString(dictionaryModuleMap.get("value"));
		entity.setDictionaryModule(dictionaryModule);
		entity.setRemark(parseString(paramMap.get("remark")));
		entity.setSortNumber(parseInteger(paramMap.get("sortNumber")));

		List<Map<String, Object>> parentList = (List<Map<String, Object>>) paramMap.get("parentDictionary");
		String parentId = (parentList == null || parentList.isEmpty()) ? null : parseString(parentList.get(0).get("value"));
		entity.setParentDictionary(parentId);
		dictionaryService.saveOrUpdate(entity);
		out.print(getSerializer().message(""));
	}

	/**
	 * 根据一个实体 ID 来删除数据字典实体信息的 Web 方法。<br />
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
		dictionaryService.remove((List<String>) paramMap.get("id"));
		out.print(getSerializer().message(""));
	}

	/**
	 * 获取树形数据字典数据的 Web 方法。<br />
	 * <br />
	 * 命令: "getTree" ；<br/>
	 * <br/>
	 * 
	 * @param out
	 *            响应输出对象
	 */
	@RequestMapping(params = "command=getTree")
	public void getTree(java.io.PrintWriter out) {
		// 获取启用状态的数据字典
		List<DataTree> menuTree = dictionaryService.getDataDictionaryTree();
		out.print(getSerializer().formatList(menuTree));
	}

	/**
	 * 根据单个父字典code查询数据字典的所有子字典数据
	 * */
	@RequestMapping(params = "command=loadChildCode")
	public void loadChildCode(@RequestParam("data") String data, java.io.PrintWriter out, HttpServletRequest request) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		List<Map<String, Object>> resultList = dictionaryService.loadChildCode(formatString(paramMap.get("codes")));
		out.print(getSerializer().formatList(resultList));
	}

	/**
	 * 根据系统
	 * */
	@RequestMapping(params = "command=loadChiadNode")
	public void loadChiadNode(@RequestParam("data") String data, HttpServletRequest request, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
	    System.out.println(data);
		List<Map<String, Object>> resultList = dictionaryService.loadChiadNode(paramMap);
		out.print(getSerializer().formatList(resultList));
	}
	
	/**
	 * @Title: getCoursesByCode
	 * @Description: 通过学校类型code关联科目
	 * @author xiahuajun
	 * @date 2s016年8月8日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getCoursesByCode")
	public void getCoursesByCode(@RequestParam("data") String data,java.io.PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		List<Map<String,Object>> list = dictionaryService.getCoursesBySchoolType(requestMap.get("schoolType").toString());
		out.print(getSerializer().formatList(list));
	}
	
	/**
	 * @Title: getCoursesByGradeCode
	 * @Description:通过年级code关联科目
	 * @author xiahuajun
	 * @date 2016年8月25日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getCoursesByGradeCode")
	public void getCoursesByGradeCode(@RequestParam("data") String data,java.io.PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		//Map<String, Object> paramMap = new HashMap<String, Object>();
		List<Map<String,Object>> list = dictionaryService.getCoursesByGrade(requestMap.get("grade").toString());
		out.print(getSerializer().formatList(list));
	}
	
	/**
	 * @Title: getGradessByCode
	 * @Description: 通过学校类型code关联年级
	 * @author xiahuajun
	 * @date 2016年8月24日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getGradessByCode")
	public void getGradessByCode(@RequestParam("data") String data,java.io.PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		List<Map<String,Object>> list = dictionaryService.getGradesBySchoolType(requestMap.get("schoolType").toString());
		out.print(getSerializer().formatList(list));
	}
	
	/**
	 * 
	 * @Title: getDataByCode
	 * @Description: 根据传过来的学校类型传回对应的年纪 学校 科目
	 * @author chenteng
	 * @date 2017年8月3日 
	 * @return void
	 * @param data
	 * @param out
	 */
	@RequestMapping(params = "command=getDataByCode")
	public void getDataByCode(@RequestParam("data") String data,java.io.PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		List<Map<String,Object>> courseList = dictionaryService.getCoursesBySchoolType(requestMap.get("schoolType").toString());
		List<Map<String,Object>> gradeList = dictionaryService.getGradesBySchoolType(requestMap.get("schoolType").toString());
		
		String schoolTypeSequence = "";
		if("xx".equals(requestMap.get("schoolType").toString())){
			schoolTypeSequence = "0";
		} else if("cz".equals(requestMap.get("schoolType").toString())){
			schoolTypeSequence = "1";
		} else if("gz".equals(requestMap.get("schoolType").toString())){
			schoolTypeSequence = "4";
		}
		List<Map<String,Object>> schoolList = dictionaryService.getSchoolsBySchoolType(schoolTypeSequence);
		
		HashMap<String,Object> listMap  = new HashMap<>();
		listMap.put("courseList", courseList);
		listMap.put("gradeList", gradeList);
		listMap.put("schoolList", schoolList);
		out.print(getSerializer().formatMap(listMap));
	}
	
	/**
	 * @Title: getSchoolByCode
	 * @Description: 根据学校类型的得到学校
	 * @author zhaohuanhuan
	 * @date 2016年11月7日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getSchoolByCode")
	public void getSchoolByCode(@RequestParam("data") String data,java.io.PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String schoolTypeSequence = "";
		if("xx".equals(requestMap.get("schoolType").toString())){
			schoolTypeSequence = "0";
		} else if("cz".equals(requestMap.get("schoolType").toString())){
			schoolTypeSequence = "1";
		} else if("gz".equals(requestMap.get("schoolType").toString())){
			schoolTypeSequence = "4";
		}
		List<Map<String,Object>> list = dictionaryService.getSchoolsBySchoolType(schoolTypeSequence);
		out.print(getSerializer().formatList(list));
	}
	
	/**
	 * 
	 * @Title: getSchoolByCode
	 * @Description: TODO
	 * @author chenteng
	 * @date 2017年8月3日 
	 * @return void
	 * @param data
	 * @param out
	 */
	@RequestMapping(params = "command=getClassBySchoolAndGrade")
	public void getClassBySchoolAndGrade(@RequestParam("data") String data,java.io.PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		System.out.println(requestMap);
		//out.print(getSerializer().formatList(list));
	}
	/**
	 * @Title: selectSchoolsBySchoolType
	 * @Description: 根据学校类型集合得到学校
	 * @author zhaohuanhuan
	 * @date 2016年11月8日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=selectSchoolsBySchoolType")
	public void selectSchoolsBySchoolType(@RequestParam("data") String data,java.io.PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		List<String> schoolTypeSequenceList =new ArrayList<>();
		if("xx".equals(requestMap.get("schoolType").toString())){
			schoolTypeSequenceList.add("0");
			schoolTypeSequenceList.add("5");
		} else if("cz".equals(requestMap.get("schoolType").toString())){
			schoolTypeSequenceList.add("1") ;
			schoolTypeSequenceList.add("5") ;
		} else if("gz".equals(requestMap.get("schoolType").toString())){
			schoolTypeSequenceList.add("2") ;
			schoolTypeSequenceList.add("3") ;
			schoolTypeSequenceList.add("4") ;
		}
		Map<String,Object> paramMap=new HashMap<>();
		paramMap.put("schoolTypeSequenceList", schoolTypeSequenceList);
		List<Map<String,Object>> list = dictionaryService.selectSchoolsBySchoolType(paramMap);
		
		if("gz".equals(requestMap.get("schoolType").toString())){
				Iterator<Map<String,Object>> ite = list.iterator();
				while(ite.hasNext()){
					Map<String,Object> m = ite.next();
					//删除学校code为3062值
					if("3062".equals(m.get("School_Code").toString())){
						ite.remove();
				}
			}
		}	
		
		if("cz".equals(requestMap.get("schoolType").toString())){
				Iterator<Map<String,Object>> ite = list.iterator();
				while(ite.hasNext()){
					Map<String,Object> m = ite.next();
					//删除学校code为3062值
					if("3005".equals(m.get("School_Code").toString()) || "3007".equals(m.get("School_Code").toString())  ){
						ite.remove();
				}
			}
				Map<String,Object> paramMaps = new HashMap<String,Object>();
				paramMaps.put("School_Code", "3004");
				paramMaps.put("School_Name", "上海市青浦区第一中学");
				paramMaps.put("School_Short_Name", "青浦一中");
		    	list.add(paramMaps);
		}	
		out.print(getSerializer().formatList(list));
	}
	
	/**
	 * @Title: getCoursesByCode
	 * @Description: 加载数据字典
	 * @author xiahuajun
	 * @date 2016年8月9日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getDataDictionaryByCode")
	public void getDataDictionaryByCode(@RequestParam("data") String data,java.io.PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		List<String> paramList = new ArrayList<String>();
		paramList.add(requestMap.get("schoolYear").toString());
		paramList.add(requestMap.get("schoolType").toString());
		paramList.add(requestMap.get("term").toString());
		paramList.add(requestMap.get("examType").toString());
		List<Map<String,Object>> list = dictionaryService.getDataDictionary(paramList);
		out.print(getSerializer().formatList(list));
	}

	@Autowired
	private PlatformDataDictionaryService dictionaryService;
}