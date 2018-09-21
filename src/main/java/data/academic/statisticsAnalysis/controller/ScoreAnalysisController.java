package data.academic.statisticsAnalysis.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import data.academic.statisticsAnalysis.service.ScoreAnalysisService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;

/**
 * @Title: ScoreAnalysisController
 * @Description: 成绩分析控制层
 * @author zhaohuanhuan
 * @date 2016年8月30日
 */
@RestController
@RequestMapping("statisticsAnalysis/scoreAnalysis")
public class ScoreAnalysisController extends AbstractBaseController {

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub

	}

	/**
	 * @Title: searchPaging
	 * @Description: 分页查询成绩
	 * @author zhaohuanhuan
	 * @date 2016年8月30日
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
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		if (sortField.trim().length() == 0) {
			sortField = "sAvg";
		}
		PagingResult<Map<String, Object>> pagingResult = null;
		String sort = trimString(requestMap.get("sord"));
		//得到科目
		String courseStr = trimString(requestMap.get("course"));
		String courseVal = courseStr.substring(1, courseStr.length() - 1);
		String[] courseType = courseVal.split(", ");
		//得到学校code
		String schoolStr = trimString(requestMap.get("school"));
		String schoolVal = schoolStr.substring(1, schoolStr.length() - 1);
		String[] schoolType = schoolVal.split(", ");
		
		List<Map<String, Object>> paramList = new ArrayList<>();
		
		Map<String, Object> map3 = new HashMap<>();
		//循环科目得到各科的四率（优秀率，优良率，及格率，平均分）
		for (int i = 0; i < courseType.length; i++) {
			//循环学校code，比较各学校年级的学科四率
			for (int j = 0; j < schoolType.length; j++) {
				requestMap.put("school", schoolType[j]);
				int minClassNo=0;
				int maxClassNo=0;
				if(ConfigContext.getStringSection("yczxSchoolCode").equals(schoolType[j])){
					requestMap.put("school", ConfigContext.getStringSection("syzxSchoolCode"));
					minClassNo=21;
					maxClassNo=27;
				}
				if(ConfigContext.getStringSection("syzxSchoolCode").equals(schoolType[j])){
					minClassNo=1;
					maxClassNo=12;
				}
				requestMap.put("minClassNo", minClassNo);
				requestMap.put("maxClassNo", maxClassNo);
				requestMap.put("course", courseType[i]);
				pagingResult = scoreAnalysisService.ScoreSearch(requestMap, sortField, sort, currentPage, pageSize);
				List<Map<String, Object>> list = pagingResult.getRows();
				if (list.size() > 0) {
						DecimalFormat format = new DecimalFormat("0.00%");
						List<Map<String, Object>> scoreList = scoreAnalysisService.getScoreByCourses(requestMap);
						for (Map<String, Object> map2 : scoreList) {
							map3.put(courseType[i] + "Yll", format.format(map2.get("Yll")));
							map3.put(courseType[i] + "sAvg",Math.round(parseFloat(map2.get("sAvg")) * 100) / 100.0);
							map3.put(courseType[i] + "Yl", format.format(map2.get("Yl")));
							map3.put(courseType[i] + "Ll", format.format(map2.get("Ll")));
							map3.put(courseType[i] + "Jgl", format.format(map2.get("Jgl")));
							map3.put("School_Name", map2.get("School_Name"));
						}
					}
			}
		}
		paramList.add(map3);
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(paramList,
				pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}

	@Autowired
	private ScoreAnalysisService scoreAnalysisService;

}
