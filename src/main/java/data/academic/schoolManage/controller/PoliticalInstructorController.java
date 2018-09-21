package data.academic.schoolManage.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import data.academic.schoolManage.service.PoliticalInstructorService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.platform.authority.security.SecurityContext;

/**
 * @Title: PoliticalInstructorController
 * @Description: TODO
 * @author zhaohuanhuan
 * @date 2016年9月20日 下午5:45:38
 */
@RestController
@RequestMapping("schoolManage/politicalInstructor")
public class PoliticalInstructorController extends AbstractBaseController{

	@Autowired
	private PoliticalInstructorService politicalInstructorService;
	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
	
	@RequestMapping(params = "command=searchPaging")
	public void searchPaging(@RequestParam("data") String data,java.io.PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		//schoolType=1, school=3063
		boolean isFast = parseBoolean( requestMap.get( "isFast" ) ) ;
	    String loginName = "" ;
	    if( isFast ){
	    	loginName = trimString( requestMap.get( "q" ) ) ;
	    }
	    String orgId=politicalInstructorService.selectOrgIdByLoginName(SecurityContext.getPrincipal().getUsername());//根据登录名得到学校code
	    requestMap.put("loginName", loginName);   
	    requestMap.put("orgId", orgId); 
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		String sort = trimString(requestMap.get("sord"));
	
		//得到所有的教师集合
		PagingResult<Map<String, Object>> pagingResult = politicalInstructorService.searchPaging(requestMap, sortField, sort, currentPage, pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			String position = map.get("POSITION_NO").toString();
			if (position.equals("2")) {
				position = "教导员";
				map.put("POSITION_NO", position);
			}
			paramList.add(map);
		}
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(paramList,
				pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
		
		
	}
	
	@RequestMapping(params = "command=addPoliticalInstructor")
	public void addPoliticalInstructor(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		 String orgId=politicalInstructorService.selectOrgIdByLoginName(SecurityContext.getPrincipal().getUsername());//根据登录名得到学校code
		//String schoolShortName=politicalInstructorService.getSchoolShortNameBySchoolCode(orgId);
		paramMap.put("orgId", orgId);
	//	 paramMap.put("name",schoolShortName+"教导员");
		paramMap.put("name",paramMap.get("name"));
		 // 添加学科教研员
		   if("".equals(paramMap.get("userId").toString())){
			   politicalInstructorService.addPoliticalInstructor(paramMap);
			   String userId=paramMap.get("id").toString();
			   politicalInstructorService.addPoliticalInstructorRole(userId);
			   paramMap.put("mess", "addSuccess");
		   }else{
			   
			   politicalInstructorService.updatePoliticalInstructor(paramMap);
			   paramMap.put("mess", "updateSuccess");
		   }
	
		out.print(getSerializer().formatMap(paramMap));
	}
	
	
	/**
	 * @Title: delete
	 * @Description: 删除教导员所拥有的角色和教导员
	 * @author zhaohuanhuan
	 * @date 2016年9月21日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=delete")
	public void delete(@RequestParam("data") String data,java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		politicalInstructorService.deletePoliticalInstructorRole((List<String>) paramMap.get("id"));
		politicalInstructorService.deletePoliticalInstructor((List<String>) paramMap.get("id"));
		out.print(getSerializer().message(""));
	}
	
	
	/**
	 * @Title: loadPoliticalInstructor
	 * @Description: 根据id加载教导员
	 * @author zhaohuanhuan
	 * @date 2016年9月21日 
	 * @param request
	 * @param out
	 * @throws Exception
	 * @return void
	 */
	@RequestMapping( params = "command=loadPoliticalInstructor" )
    public void loadPoliticalInstructor(HttpServletRequest request,java.io.PrintWriter out) throws Exception
	{
	 String id=request.getParameter("id");
	List<Map<String,Object>> list=politicalInstructorService.loadPoliticalInstructor(id);
	 out.print(getSerializer().formatList(list));
	}
}
