/**
 * 2016年10月21日
 */
package data.academic.examNumberManagement.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import data.academic.examNumberManagement.service.AdminExamNumberService;
import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;

/**
 * @Title: TestCaseGeController
 * @Description: TODO
 * @author zhaohuanhuan
 * @date 2016年10月21日 下午2:27:13
 */

@Controller
@RequestMapping("examNumberManagement/testCaseGeneration")
public class TestCaseGenerationController extends AbstractBaseController {

	String examNumber="";
	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		examNumber=parseString(request.getParameter("examNumber"));
	}

	
	
	/**
	 * @Title: testCastGenerationSearchPading
	 * @Description: 学校生成考号情况分页显示
	 * @author zhaohuanhuan
	 * @date 2016年10月24日 
	 * @param data
	 * @param out
	 * @param request
	 * @return void
	 */
	@RequestMapping(params = "command=testCastGenerationSearchPading")
	public void testCastGenerationSearchPading(@RequestParam("data") String data,
			java.io.PrintWriter out,HttpServletRequest request) {
		
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		String sort = trimString("asc");
		requestMap.put("examCode",examNumber);
		PagingResult<Map<String, Object>> pagingResult = adminExamNumber
				.testCastGenerationSearchPading(requestMap, sortField, sort, currentPage,
						pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
		List<Map<String, Object>> requestList = new ArrayList<>();
		for (Map<String, Object> map : list) {
			requestMap.put("schoolCode", map.get("School_Code").toString());
			List<String>candidateNumberType= examNumberManageService.getCanNumberStateBySchoolCode(requestMap);
			map.put("candidateNumberType", candidateNumberType);
			requestList.add(map);
		}
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(
				requestList, pagingResult.getPage(), pagingResult.getPageSize(),
				pagingResult.getRecords());
		        out.print(getSerializer().formatObject(newPagingResult));
	}
	
	
	/**
	 * @Title: countSchoolByExamCode
	 * @Description: 根据考试编号的到学校
	 * @author zhaohuanhuan
	 * @date 2016年11月16日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping( params = "command=countSchoolByExamCode" )
	   public void countSchoolByExamCode( @RequestParam( "data" ) String data, java.io.PrintWriter out )
	   {
	       Map<String,Object> requestMap=new HashMap<String,Object>();
	        requestMap.put("examCode",examNumber);
	        //得到该考试下面的所有学校总数
	        Integer schoolSum=adminExamNumber.countSchoolByExamCode(requestMap);
			requestMap.put("examNumberIsExist", "1");
			//得到已经生成考号的学校的总数
			Integer exitExamNumberSchoolNum=adminExamNumber.countSchoolByExamCode(requestMap);
			//未生成考号学校总数
			Integer notExitSchoolNum=schoolSum-exitExamNumberSchoolNum;
			requestMap.put("schoolSum", schoolSum);
			requestMap.put("notExitSchoolNum", notExitSchoolNum);
			requestMap.put("exitExamNumberSchoolNum", exitExamNumberSchoolNum);
	       out.print( getSerializer().formatMap(requestMap)) ;
	   }
	
	
	@Autowired
	private AdminExamNumberService adminExamNumber;
	@Autowired
	private ExamNumberManageService examNumberManageService;
	
}
