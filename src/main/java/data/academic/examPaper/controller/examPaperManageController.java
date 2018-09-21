package data.academic.examPaper.controller;
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

import data.academic.examPaper.service.ExamPaperManageService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.platform.authority.security.SecurityContext;
@RestController
@RequestMapping("examPaper/examPaperManage")
public class examPaperManageController extends AbstractBaseController {

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
	@Autowired
	private ExamPaperManageService examPaperManageService;
	
	/**
	 * @Title: addExam
	 * @Description: 添加试卷
	 * @author xiahuajun
	 * @date 2016年8月14日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=addExamPaper")
	public void addExamPaper(@RequestParam("data") String data,java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		//System.out.println(paramMap.get("examPaperName").toString());
		if (paramMap.get("id").toString() == null || paramMap.get("id").toString() == "") {
			if("".equals(paramMap.get("examPaperName").toString()) || null == paramMap.get("examPaperName")){
				StringBuffer str = new StringBuffer();
				str.append(paramMap.get("schoolYear").toString());
				if(paramMap.get("term").toString().equals("sxq")){
					str.append("上学期");
				} else if(paramMap.get("term").toString().equals("xxq")){
					str.append("下学期");
				}
				if(paramMap.get("grade").toString().equals("11")){
					str.append("一年级");
				}
				else if(paramMap.get("grade").toString().equals("12")){
					str.append("二年级");
				}
				else if(paramMap.get("grade").toString().equals("13")){
					str.append("三年级");
				}
				else if(paramMap.get("grade").toString().equals("14")){
					str.append("四年级");
				}
				else if(paramMap.get("grade").toString().equals("15")){
					str.append("五年级");
				}
				else if(paramMap.get("grade").toString().equals("16")){
					str.append("六年级");
				}
				else if(paramMap.get("grade").toString().equals("17")){
					str.append("初一");
				}
				else if(paramMap.get("grade").toString().equals("18")){
					str.append("初二");
				}
				else if(paramMap.get("grade").toString().equals("19")){
					str.append("初三");
				}
				else if(paramMap.get("grade").toString().equals("31")){
					str.append("高一");
				}
				else if(paramMap.get("grade").toString().equals("32")){
					str.append("高二");
				}
				else if(paramMap.get("grade").toString().equals("33")){
					str.append("高三");
				}
				if(paramMap.get("examType").toString().equals("qz")){
					str.append("期中");
				}
				else if(paramMap.get("examType").toString().equals("qm")){
					str.append("期末");
				}
				if(paramMap.get("examPaperType").toString().equals("aj")){
					str.append("A卷");
				}
				else if(paramMap.get("examPaperType").toString().equals("bj")){
					str.append("B卷");
				}
				else if(paramMap.get("examPaperType").toString().equals("lk")){
					str.append("理科");
				}
				else if(paramMap.get("examPaperType").toString().equals("wk")){
					str.append("文科");
				}
				if(paramMap.get("course").toString().equals("sx")){
					str.append("数学");
				}
				else if(paramMap.get("course").toString().equals("yw")){
					str.append("语文");
				}
				else if(paramMap.get("course").toString().equals("yy")){
					str.append("英语");
				}
				else if(paramMap.get("course").toString().equals("wl")){
					str.append("物理");
				}
				else if(paramMap.get("course").toString().equals("hx")){
					str.append("化学");
				}
				else if(paramMap.get("course").toString().equals("ty")){
					str.append("体育");
				}
				else if(paramMap.get("course").toString().equals("sxzz")){
					str.append("思想政治");
				}
				else if(paramMap.get("course").toString().equals("ls")){
					str.append("历史");
				}
				else if(paramMap.get("course").toString().equals("dl")){
					str.append("地理");
				}
				
				else if(paramMap.get("course").toString().equals("ms")){
					str.append("美术");
				}
				else if(paramMap.get("course").toString().equals("zr")){
					str.append("自然");
				}
				else if(paramMap.get("course").toString().equals("xxkj")){
					str.append("信息科技");
				}
				
				else if(paramMap.get("course").toString().equals("sw")){
					str.append("生物");
				}
				else if(paramMap.get("course").toString().equals("yjxkc")){
					str.append("研究型课程");
				}
				else if(paramMap.get("course").toString().equals("tzxkc")){
					str.append("拓展型课程");
				}
				
				else if(paramMap.get("course").toString().equals("yyue")){
					str.append("音乐");
				}
				else if(paramMap.get("course").toString().equals("njyy")){
					str.append("牛津英语");
				}
				else if(paramMap.get("course").toString().equals("xsjyy")){
					str.append("高中新世纪英语");
				}
				paramMap.put("examPaperName", str.toString());
			}
			// 创建人
			String create_person = SecurityContext.getPrincipal().getId();
			// 创建事间
			String create_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			paramMap.put("create_person", create_person);
			paramMap.put("create_time", create_time);
			examPaperManageService.addExamPaper(paramMap);
			paramMap.put("mess", "addSuccess");
		} else {
			// 修改人
			String update_person = SecurityContext.getPrincipal().getId();
			// 修改时间
			String update_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date());
			paramMap.put("update_person", update_person);
			paramMap.put("update_time", update_time);
			examPaperManageService.UpdateExamPaperById(paramMap);
			paramMap.put("mess", "updateSuccess");
		}
		out.print(getSerializer().formatMap(paramMap));
	}
	
	@RequestMapping(params = "command=searchPaging")
	public void searchPaging(@RequestParam("data") String data,
			java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		// Map<String, Object> paramMap = new HashMap<String, Object>();
		boolean isFast = parseBoolean(requestMap.get("isFast"));
		String examPaperName = "";
		if (isFast) {
			examPaperName = trimString(requestMap.get("q"));
		}
		requestMap.put("examPaperName", examPaperName);
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));

		if (sortField.trim().length() == 0) {
			sortField = "Create_Time";
		}
		String sort = trimString("desc");
		PagingResult<Map<String, Object>> pagingResult = examPaperManageService.serachExamPaperPaging(requestMap, sortField, sort, currentPage,pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
		//[{rownum=1, Course=hx, School_Year=2014-2015, Grade=7, Exampaper_Name=, Create_Time=2016-08-18 15:21:18.0, Id=9038F43C-DB2C-44A4-B315-F9FA0D9279A4, Exam_Type=qm, Exampaper_Type=lk, Create_Person=238F62E4-AFBE-4976-AF3B-B1F1417CD13D, Term=sxq},
		//List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		//[{Question_Number=1, School_Year=2015-2016, Create_Time=2016-08-11 10:03:07.0, School_Type=xx, Exam_Type=qz, Create_Person=238F62E4-AFBE-4976-AF3B-B1F1417CD13D, Term=sxq, Name=吴琰, Course=sx, Id=EE346EFF-196E-418F-A950-6D09300A38A6, Exam_Number=270601008, Score=2, Stu_Code=1001}
		/*for(Map<String, Object> map:list){
			//System.out.println(map.get("Exampaper_Name").toString());
			
			if(map.get("Term").toString().equals("sxq")){
				map.put("Term", "上学期");
			} else if(map.get("Term").toString().equals("xxq")){
				map.put("Term", "下学期");
			}
			if(map.get("Exam_Type").toString().equals("qz")){
				map.put("Exam_Type", "期中");
			} else if(map.get("Exam_Type").toString().equals("qm")){
				map.put("Exam_Type", "期末");
			}
			if(map.get("Course").toString().equals("sx")){
				map.put("Course", "数学");
			}
			else if(map.get("Course").toString().equals("yw")){
				map.put("Course", "语文");
			}
			else if(map.get("Course").toString().equals("yy")){
				map.put("Course", "英语");
			}
			else if(map.get("Course").toString().equals("wl")){
				map.put("Course", "物理");
			}
			else if(map.get("Course").toString().equals("hx")){
				map.put("Course", "化学");
			}
			else if(map.get("Course").toString().equals("ty")){
				map.put("Course", "体育");
			}
			else if(map.get("Course").toString().equals("zz")){
				map.put("Course", "政治");
			}
			else if(map.get("Course").toString().equals("ls")){
				map.put("Course", "历史");
			}
			else if(map.get("Course").toString().equals("dl")){
				map.put("Course", "地理");
			}
			
			if(map.get("Grade").toString().equals("1")){
				map.put("Grade", "一年级");
			}
			else if(map.get("Grade").toString().equals("2")){
				map.put("Grade", "二年级");
			}
			else if(map.get("Grade").toString().equals("3")){
				map.put("Grade", "三年级");
			}
			else if(map.get("Grade").toString().equals("4")){
				map.put("Grade", "四年级");
			}
			else if(map.get("Grade").toString().equals("5")){
				map.put("Grade", "五年级");
			}
			else if(map.get("Grade").toString().equals("6")){
				map.put("Grade", "六年级");
			}
			else if(map.get("Grade").toString().equals("7")){
				map.put("Grade", "七年级");
			}
			else if(map.get("Grade").toString().equals("8")){
				map.put("Grade", "八年级");
			}
			else if(map.get("Grade").toString().equals("9")){
				map.put("Grade", "九年级");
			}
			if(map.get("Exampaper_Type").toString().equals("aj")){
				map.put("Exampaper_Type", "A卷");
			}
			else if(map.get("Exampaper_Type").toString().equals("bj")){
				map.put("Exampaper_Type", "B卷");
			}
			else if(map.get("Exampaper_Type").toString().equals("wk")){
				map.put("Exampaper_Type", "文科");
			}
			else if(map.get("Exampaper_Type").toString().equals("lk")){
				map.put("Exampaper_Type", "理科");
			}
			paramList.add(map);
		}*/
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(
				list, pagingResult.getPage(), pagingResult.getPageSize(),
				pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}
	
	/**
	 * @Title: delete
	 * @Description: 删除试卷
	 * @author xiahuajun
	 * @date 2016年8月14日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "command=deleteExamPaper")
	public void deleteExamPaper(@RequestParam("data") String data,java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		List<String> list = (List<String>) paramMap.get("selArr");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("selArr", list);
		examPaperManageService.remove(map);
		paramMap.put("mess", "success");
		out.print(getSerializer().formatMap(paramMap));
	}
	
	/**
	 * @Title: selectLinkById
	 * @Description: 修改回显数据
	 * @author xiahuajun
	 * @date 2016年8月14日 
	 * @param id
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=SelectExamPaperById")
	public void SelectExamPaperById(@RequestParam("data") String data,
			java.io.PrintWriter out) {
		Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
		Map<String, Object> map = examPaperManageService.selectExamPaperById(paramMap);
		map.put("mess", map.get("success"));
		out.print(getSerializer().formatMap(map));
	}

}

