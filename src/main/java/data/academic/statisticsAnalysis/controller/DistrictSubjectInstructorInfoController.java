/**
 * 2016年9月14日
 */
package data.academic.statisticsAnalysis.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import data.academic.schoolManage.service.PoliticalInstructorService;
import data.academic.statisticsAnalysis.service.DistrictSubjectInstructorInfoService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.platform.authority.security.SecurityContext;
import data.platform.service.PlatformDataDictionaryService;

/**
 * @Title: DistrictSubjectInstructorInfoController
 * @Description: 全区学科教研员信息控制层
 * @author zhaohuanhuan
 * @date 2016年9月14日 下午2:12:00
 */

@RestController
@RequestMapping("dataManage/districtSubjectInstructorInfo")
public class DistrictSubjectInstructorInfoController extends AbstractBaseController {
	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub

	}

	/**
	 * @Title: searchPaging
	 * @Description: 教研员分页查询
	 * @author zhaohuanhuan
	 * @date 2016年9月19日
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=selectDistrictSubInstructorPaging")
	public void searchPaging(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		boolean isFast = parseBoolean(requestMap.get("isFast"));
		String loginName = "";
		if (isFast) {
			loginName = trimString(requestMap.get("q"));
		}
		requestMap.put("loginName", loginName);
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		String sort = trimString(requestMap.get("sord"));
		PagingResult<Map<String, Object>> pagingResult = districtSubjectInstructorInfoService
				.selectDistrictSubInstructorPaging(requestMap, sortField, sort, currentPage, pageSize);

		List<Map<String, Object>> list = pagingResult.getRows();
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			String position = map.get("POSITION_NO").toString();
			if (position.equals("1")) {
				position = "教研员";
				map.put("POSITION_NO", position);
			}
			paramList.add(map);
		}
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(paramList,
				pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}

	/**
	 * @Title: addUser
	 * @Description: 添加学科教研员
	 * @author zhaohuanhuan
	 * @date 2016年9月19日
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=addUser")
	public void addUser(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		// paramMap.put("", value)
		//{userId=, loginName=eee, name=一年级,二年级教研员, course=, gradeValArr=[11, 12]}
		//int a = 3/0;
		//String periods = paramMap.get("period").toString();
		//String period = periods.substring(1, periods.length() - 1);
		//paramMap.put("period", period);
		List<String> paramList = (List<String>) paramMap.get("gradeValArr");
		List<Map<String,Object>> list = new ArrayList<>();
		// 添加学科教研员
		if ("".equals(paramMap.get("userId").toString())) {
			districtSubjectInstructorInfoService.addUser(paramMap);
			//添加教研员年级关系表
			for(String str : paramList){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("uuid", UUID.randomUUID().toString());
				map.put("loginName", paramMap.get("loginName").toString());
				map.put("grade", str);
				list.add(map);
			}
			districtSubjectInstructorInfoService.addRefInstractorGrade(list);
			String userId = paramMap.get("id").toString();
			//int a = 3/0;
			if (paramMap.containsKey("course") && !"".equals(paramMap.get("course"))) {
				districtSubjectInstructorInfoService.addDistrictSubjectInstructorRole(userId);
			} else {
				districtSubjectInstructorInfoService.addDistrictInstructorRole(userId);
			}
		} else {
			//{userId=2B4BFF68-B5C4-430B-A319-C7EB24A15EFE, loginName=czadmin3, positionName=六年级,七年级,八年级教研员, name=六年级,七年级教研员, gradeValArr=[16, 17, 18]}
			districtSubjectInstructorInfoService.updateSubjectInstructor(paramMap);
			//清除教研员和年级关系表
			districtSubjectInstructorInfoService.deleteRefInstructorGrade(paramMap);
			//添加教研员年级关系表
			for(String str : paramList){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("uuid", UUID.randomUUID().toString());
				map.put("loginName", paramMap.get("loginName").toString());
				map.put("grade", str);
				list.add(map);
			}
			districtSubjectInstructorInfoService.addRefInstractorGrade(list);
		}

		paramMap.put("mess", "success");
		out.print(getSerializer().formatMap(paramMap));
	}

	/**
	 * @Title: getPeriodByLoginName
	 * @Description: 根据登录名得到学段
	 * @author zhaohuanhuan
	 * @date 2016年9月19日
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getPeriodByLoginName")
	public void getPeriodByLoginName(@RequestParam("data") String data, java.io.PrintWriter out) {
		List<Map<String, Object>> list = districtSubjectInstructorInfoService
				.getPeriodByLoginName(SecurityContext.getPrincipal().getUsername());
		out.print(getSerializer().formatList(list));
	}

	/**
	 * @Title: getCoursesByCode
	 * @Description: 根据学科code得到学科
	 * @author zhaohuanhuan
	 * @date 2016年9月21日
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getCoursesBySchoolType")
	public void getCoursesByCode(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		List<Map<String, Object>> list = new ArrayList<>();
		String orgId=politicalInstructorService.selectOrgIdByLoginName(SecurityContext.getPrincipal().getUsername());//根据登录名得到学校code
		String schoolType = requestMap.get("period").toString();

		String school = schoolType.substring(1, schoolType.length() - 1);
		String s = "";
		if (0 < school.length() && school.length() < 3) {
			if (school.equals("0")) {
				school = "xx";
			}
			if (school.equals("1")) {
				school = "cz";
			}
			if ( "2".equals(school)  || "3".equals(school) || "4".equals(school) ) {
				if("3062".equals(orgId)){
					school = "ygz";
				}
				else if("3004".equals(orgId)){
					school = "wz";
				}else{
					school = "gz";
				}
			}
			if ( "5".equals(school)) {
				school = "ygz";
			}
			list = dictionaryService.getCoursesBySchoolType(school);
		} else {
			if (school.equals("0, 1")) {
				school = "xx";
				s = "cz";
				list = dictionaryService.getCoursesBySchoolType(school);
				List<Map<String, Object>> xxList = dictionaryService.getCoursesBySchoolType(s);
				list.removeAll(xxList);
				list.addAll(xxList);
			}
			if (school.equals("0, 2")) {
				school = "xx";
				s = "gz";
				list = dictionaryService.getCoursesBySchoolType(school);
				List<Map<String, Object>> gzList = dictionaryService.getCoursesBySchoolType(s);
				list.removeAll(gzList);
				list.addAll(gzList);
			}

			if (school.equals("1, 2")) {
				school = "cz";
				s = "gz";
				list = dictionaryService.getCoursesBySchoolType(school);
				List<Map<String, Object>> xxList = dictionaryService.getCoursesBySchoolType(s);
				list.removeAll(xxList);
				list.addAll(xxList);
			}
			if (school.equals("0, 1, 2")) {
				school = "cz";
				s = "gz";
				String xx = "xx";
				list = dictionaryService.getCoursesBySchoolType(school);
				List<Map<String, Object>> gzList = dictionaryService.getCoursesBySchoolType(s);
				List<Map<String, Object>> xxList = dictionaryService.getCoursesBySchoolType(xx);
				list.removeAll(gzList);
				list.addAll(gzList);
				list.removeAll(xxList);
				list.addAll(xxList);
			}
		}
		out.print(getSerializer().formatList(list));
	}

	/**
	 * @Title: loadDistrictSubjectInstructor
	 * @Description: 根据id加载教研员
	 * @author zhaohuanhuan
	 * @date 2016年9月21日
	 * @param request
	 * @param out
	 * @throws Exception
	 * @return void
	 */
	@RequestMapping(params = "command=loadDistrictSubjectInstructor")
	public void loadDistrictSubjectInstructor(HttpServletRequest request, java.io.PrintWriter out) throws Exception {
		String id = request.getParameter("id");
		//[{Grade=16, USER_NAME=六年级,七年级教研员, USER_UID=czadmin3}, {Grade=17, USER_NAME=六年级,七年级教研员, USER_UID=czadmin3}]
		List<Map<String, Object>> list = districtSubjectInstructorInfoService.loadDistrictSubjectInstructor(id);
		out.print(getSerializer().formatList(list));
	}

	/**
	 * @Title: getInfoByLoginName
	 * @Description: 校验用户名是否存在
	 * @author zhaohuanhuan
	 * @date 2016年9月20日
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=selectUserUidByUserId")
	public void selectUserUidByUserId(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> map = getSerializer().parseMap(data);
		String userId = (String) map.get("userId");
		String loginName = districtSubjectInstructorInfoService.selectUserUidByUserId(userId);
		String userName = (String) map.get("loginName");
		boolean flag = true;
		if (userName.equals(loginName)) {
			flag = true;
		} else {
			List<Map<String, Object>> paramMapList = districtSubjectInstructorInfoService.getInfoByLoginName(userName);
			if (paramMapList.size() > 0) {
				flag = false;
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("flag", flag);
		out.print(getSerializer().formatMap(resultMap));
	}

	@RequestMapping(params = "command=getInfoByLoginName")
	public void getInfoByLoginName(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> map = getSerializer().parseMap(data);
		String loginName = (String) map.get("loginName");
		List<Map<String, Object>> paramMapList = districtSubjectInstructorInfoService.getInfoByLoginName(loginName);
		boolean flag = true;
		if (paramMapList.size() > 0) {
			flag = false;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("flag", flag);
		out.print(getSerializer().formatMap(resultMap));
	}

	/**
	 * @Title: delete
	 * @Description: 删除教研员
	 * @author zhaohuanhuan
	 * @date 2016年9月20日
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=delete")
	public void delete(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		districtSubjectInstructorInfoService.deleteDistrictSubjectInstructorRole((List<String>) paramMap.get("id"));
		districtSubjectInstructorInfoService.deleteDistrictSubjectInstructor((List<String>) paramMap.get("id"));
		out.print(getSerializer().message(""));
	}

	@Autowired
	private DistrictSubjectInstructorInfoService districtSubjectInstructorInfoService;
	@Autowired
	private PlatformDataDictionaryService dictionaryService;
	@Autowired
	private PoliticalInstructorService politicalInstructorService;
}
