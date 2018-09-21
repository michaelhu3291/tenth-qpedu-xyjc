package data.academic.schoolHoliday.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import data.framework.support.AbstractBaseController;
import data.platform.authority.security.SecurityContext;
import data.academic.schoolHoliday.entity.SchoolCalendar;
import data.academic.schoolHoliday.service.SchoolHolidayService;

/**
 * 辅导员日志管理
 * 
 */
@RestController
@RequestMapping("schoolHoliday/schoolHolidaySet")
public class SchoolHolidaySetController extends AbstractBaseController
{

	@Override
	protected void init(ModelMap model, HttpServletRequest request)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * 查询历史校历 * wuzide 2016年5月17日11:00:15
	 * 
	 * @param data
	 * @param out
	 */
	@RequestMapping(params = "command=searchHoliday")
	public void searchHoliday(@RequestParam("data") String data, java.io.PrintWriter out)
	{
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		paramMap.put("schoolId", "1");

		List<Map<String, Object>> list = schoolHolidayService.searchHoliday(paramMap);
		out.print(getSerializer().formatObject(list));
	}
	
	/**
	 * 添加校历
	 */
	@RequestMapping(params = "command=addHoliday")
	public void addHoliday(@RequestParam("data") String data, java.io.PrintWriter out)
	{
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		//当前用户id
		String createPerson = SecurityContext.getPrincipal().getId();
		//创建事间
		String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		//学年
		String schoolYear = paramMap.get("last_beginTime").toString().substring(0,4) + "-" + paramMap.get("next_endTime").toString().substring(0,4);
		paramMap.put("createPerson", createPerson);
		paramMap.put("createTime", createTime);
		paramMap.put("schoolYear", schoolYear);
		schoolHolidayService.addHoliday(paramMap);
		paramMap.put("mess", "success");
		out.print(getSerializer().formatMap(paramMap));
	}
	
	/**
	 * 修改前查询最新校历
	 */
	@RequestMapping(params = "command=selectHoliday")
	public void selectEarlyHoliday(java.io.PrintWriter out)
	{
		
		List<SchoolCalendar> list = schoolHolidayService.selectEarlyHoliday();
		out.print(getSerializer().formatList(list));
	}
	
	/**
	 * 修改最新校历
	 */
	@RequestMapping(params = "command=updateHoliday")
	public void updateHoliday(@RequestParam("data") String data,java.io.PrintWriter out)
	{
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		//修改用户id
		String updatePerson = SecurityContext.getPrincipal().getId();
		//修改事间
		String updateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		//学年
		String schoolYear = paramMap.get("last_beginTime").toString().substring(0,4) + "-" + paramMap.get("next_endTime").toString().substring(0,4);
		paramMap.put("updatePerson", updatePerson);
		paramMap.put("updateTime", updateTime);
		paramMap.put("schoolYear", schoolYear);
		//修改校历
		schoolHolidayService.updateHoliday(paramMap);
		paramMap.put("mess", "success");
		out.print(getSerializer().formatMap(paramMap));
	}
	
	/**删除校历
	 * 2016年7月22日
	 * xiahuajun
	 */
	@RequestMapping(params = "command=deleteHoliday")
	public void deleteHoliday(@RequestParam("data") String data,java.io.PrintWriter out)
	{
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		
		schoolHolidayService.deleteHoliday(paramMap);;
		paramMap.put("mess", "success");
		out.print(getSerializer().formatMap(paramMap));
	}
	
	/**
	 * 根据选择的学年，查询上一学年的结束时间，加一天作为下一学年的开始时间
	 */
	@RequestMapping(params = "command=addOneDay")
	public void addOneDay(@RequestParam("data") String data, java.io.PrintWriter out)
	{
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		String Time = formatString(paramMap.get("Time"));
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		if(Time==""){
			resultMap.put("addedTime", "");
		}else{
			Date d = parseDate(Time,"yyyy-MM-dd");
			Calendar c = new GregorianCalendar();
			c.setTime(d);
			c.add(c.DATE, 1);
			String addedTime = formatDate(c.getTime(),"yyyy-MM-dd");
			resultMap.put("addedTime", addedTime);
		}
		out.print(getSerializer().formatMap(resultMap));	
	}
	
	/**
	 * 根据选择的学年，查询上一学年的结束时间，加一天作为下一学年的开始时间
	 */
	@RequestMapping(params = "command=getStartTimeBySchoolYear")
	public void getStartTimeBySchoolYear(@RequestParam("data") String data, java.io.PrintWriter out)
	{
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		String schoolYear = formatString(paramMap.get("schoolYear"));
		String tempYear = schoolYear.substring(0, 4);
		
		List<SchoolCalendar> list = schoolHolidayService.checkBeforeInsert(schoolYear);
		Map<String,Object> resultMap = new HashMap<String, Object>();
		if(list.size()>0){
			//学年已存在，不允许添加
			resultMap.put("flag", "1");
		}else{
			//学年不存在，提示新学年开始时间
			resultMap.put("flag", "2");
			SchoolCalendar sc = schoolHolidayService.getStartTimeBySchoolYear(tempYear);
			
			
			if(sc==null||formatString(sc.getSummer_End_Time())==""){
				resultMap.put("startTime", "");
			}else{
				Date d = parseDate(formatString(sc.getSummer_End_Time()),"yyyy-MM-dd");
				Calendar c = new GregorianCalendar();
				c.setTime(d);
				c.add(c.DATE, 1);
				String starTime = formatDate(c.getTime(),"yyyy-MM-dd");
				resultMap.put("startTime", starTime);
			}
		}
		
		
		out.print(getSerializer().formatMap(resultMap));	
			
	}
	@Autowired
	private SchoolHolidayService schoolHolidayService;
}
