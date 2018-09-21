/**
 * 2016年9月12日
 */
package data.academic.statisticsAnalysis.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import data.academic.statisticsAnalysis.service.ClassRoomTeacherService;
import data.academic.statisticsAnalysis.service.SubTeaScoreSearchService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.platform.authority.security.SecurityContext;

/**
 * @Title: ClassRoomTeacherController
 * @Description:班主任成绩查询控制层
 * @author zhaohuanhuan
 * @date 2016年9月12日 下午1:57:21
 */
@RestController
@RequestMapping("statisticsAnalysis/classRoomTeacher")
public class ClassRoomTeacherController extends AbstractBaseController {

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @Title: searchPaging
	 * @Description: 班主任分页查询各科成绩
	 * @author zhaohuanhuan
	 * @date 2016年9月12日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=searchScore")
	public void searchPaging(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		// 加载页面时显示当前学年记录
		String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		int year = parseInteger(time.substring(0, time.indexOf("-")));
		int month = parseInteger(time.substring(time.indexOf("-") + 1, time.lastIndexOf("-")));
		String schoolYear = "";
		if (month < 9) {
			schoolYear = (year - 1) + "-" + year;
		} else {
			schoolYear = year + "-" + (year + 1);
		}
		if (null == requestMap.get("schoolYear")) {
			requestMap.put("schoolYear", schoolYear);
		}
		if ("" .equals(requestMap.get("term")) ) {
			requestMap.put("term", "xxq");
		}
		if ("".equals(requestMap.get("examType")) ) {
			requestMap.put("examType", "qm");
		}
	    //加载当前页面是显示当前班级记录
		if(null==requestMap.get("classs")){
			 List<Map<String, Object>> list = subTeaScoreSearchService.getGradeAndClassByLoginName(SecurityContext.getPrincipal().getUsername());
			 List<String> classIdList=new ArrayList<>();
			 for (Map<String, Object> map : list) {
				String classId= map.get("Class_Name").toString();
				classIdList.add(classId);
			   }
			requestMap.put("classs",classIdList);
			
			
		}
		
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		if (sortField.trim().length() == 0) {
			sortField = "Exam_Number";
		}
		String sort = trimString(requestMap.get("sord"));
		PagingResult<Map<String, Object>> pagingResult = classToomTeacherService.ScoreSearch(requestMap, sortField, sort,
				currentPage, pageSize);
		
		List<Map<String, Object>> list = pagingResult.getRows();

		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();

		for (Map<String, Object> map : list) {
			String className="";
			String classId=formatString(map.get("Class_Id"));
			if(StringUtils.isNotBlank(classId) ){
				//处理班级
				className=classId.substring(classId.length()-2);
				String classIdOneStr=className.substring(0, 1);
				if(classIdOneStr.equals("0")){
					className=classId.substring(classId.length()-1);
				}
				map.put("Class_Id", className);
			}
			
			
			
			// 动态得到学科类型，得到学科总分
						String courseStr = trimString(requestMap.get("course"));
						String courseVal = courseStr.substring(1, courseStr.length() - 1);
						String[] courseType = courseVal.split(", ");
						Integer yw = 0;
						for (int i = 0; i < courseType.length; i++) {
							if (map.get(courseType[i]) != null) {
								yw = yw + parseInteger(map.get(courseType[i]));
							} else {
								map.put("total", yw);
							}
							map.put("total", yw);
						}
						
						
			
			paramList.add(map);
		}
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(paramList,
				pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords());
		
		out.print(getSerializer().formatObject(newPagingResult));
	}
	@Autowired
	private ClassRoomTeacherService classToomTeacherService;
	
	@Autowired
    private SubTeaScoreSearchService subTeaScoreSearchService;
}


