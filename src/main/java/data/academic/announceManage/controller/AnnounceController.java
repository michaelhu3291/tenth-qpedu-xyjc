package data.academic.announceManage.controller;

/**
 * @author wangchaofa
 * @CreateTime Oct 14,2016
 * @UpdateTime Nov 2,2016
 */
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

import data.academic.announceManage.service.AnnounceService;
import data.academic.noticeManage.service.NoticeBasicInfoService;
import data.academic.schoolManage.service.PoliticalInstructorService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;

@RestController
@RequestMapping("announce/announceManage")
public class AnnounceController extends AbstractBaseController{

	@Autowired
	private AnnounceService announceService;
	@Autowired
	private PoliticalInstructorService politicalInstructorService;
	@Autowired
	private NoticeBasicInfoService noticeBasicInfoService;
	@Override
	protected void init(ModelMap model, HttpServletRequest request) {

	}

	/**
	 * 获取前几条的通知  
	 * @param data
	 * @param out
	 */
	@RequestMapping(params = "command=getNotice")
	public void getNotice(@RequestParam("data") String data,java.io.PrintWriter out){
		//String userId = SecurityContext.getPrincipal().getId();                       //当前登录人的主键id
		String loginId = SecurityContext.getPrincipal().getUsername();                //当前登录人的USER_UID
		String orgId = politicalInstructorService.selectOrgIdByLoginName(loginId);    //根据USER_UID获取组织id
		Map<String,Object> param = new HashMap<>();
		param.put("orgId", orgId);
		String orgCode = noticeBasicInfoService.getOrgCode(param);    //组织code 
		//String roleCode = examInfoService.getRoleByUserId(userId);    //角色code
		param.put("user_uid", "qpAdmin");
		String qpAdminId = announceService.getAdminId(param);        //区级管理员主键id
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sdf.format(d);
		if(ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']").equals(orgCode)){               //超级管理员、教研员
			param.put("validate",parseDate(now,"yyyy-MM-dd"));
			param.put("publishPersonId", qpAdminId);
			List<Map<String,String>> noticeList = announceService.getNoticeByAdmin(param);
			out.print(getSerializer().formatList(noticeList));
		}else{               //学校管理员、学校老师
			param.put("announceType", "通知");
			param.put("validate",parseDate(now,"yyyy-MM-dd"));
			param.put("schoolCode", orgId);
		    List<Map<String,String>> noticeList = announceService.getTop(param);
		    out.print(getSerializer().formatList(noticeList));
		}
	}
	
	
    /**
     * 获取前几条的公告 
     * @param data
     * @param out
     */
	@RequestMapping(params = "command=getAnnounce")
	public void getAnnounce(@RequestParam("data") String data,java.io.PrintWriter out){
		//String userId = SecurityContext.getPrincipal().getId();                       //当前登录人的主键id
		String loginId = SecurityContext.getPrincipal().getUsername();                //当前登录人的USER_UID
		String orgId = politicalInstructorService.selectOrgIdByLoginName(loginId);    //根据USER_UID获取单位code
		Map<String,Object> param = new HashMap<>();
		param.put("orgId", orgId);
		String orgCode = noticeBasicInfoService.getOrgCode(param);    //组织code 
		//String roleCode = examInfoService.getRoleByUserId(userId);    //角色code
		param.put("user_uid", "qpAdmin");
		String qpAdminId = announceService.getAdminId(param);        //区级管理员主键id
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sdf.format(d);
		if(ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']").equals(orgCode)){            //超级管理员、教研员
			param.put("validate",parseDate(now,"yyyy-MM-dd"));
			param.put("publishPersonId", qpAdminId);
			List<Map<String,String>> announceList = announceService.getAnnounceByAdmin(param);
			out.print(getSerializer().formatList(announceList));
		}else{	           //学校管理员、学校老师	
			param.put("announceType", "公告");
			param.put("validate",parseDate(now,"yyyy-MM-dd"));
			param.put("schoolCode", orgId);
		    List<Map<String,String>> announceList = announceService.getTop(param);
		    out.print(getSerializer().formatList(announceList));
		}
	}
	
	
	
	/**
	 * 获取所有的通知/公告
	 * @param data
	 * @param out
	 */
	@RequestMapping(params = "command=selectNoticesAnnounces")
	public void selectNoticesAnnounces(@RequestParam("data") String data,java.io.PrintWriter out){
		String loginName = SecurityContext.getPrincipal().getUsername();              //当前登录人的名称
		String orgId = politicalInstructorService.selectOrgIdByLoginName(loginName);  //根据登录名获取单位code

		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sdf.format(d);

		Map<String,Object> requestMap = getSerializer().parseMap(data);
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		String sort = trimString("asc");
		
		requestMap.put("validate",parseDate(now,"yyyy-MM-dd"));
		requestMap.put("schoolCode", orgId);
		
		PagingResult<Map<String,Object>> pagingResult = announceService.selectNoticesAnnounces(requestMap, sortField, sort, currentPage,pageSize);
		
		List<Map<String, Object>> list = pagingResult.getRows();
	}
	
	

	
	
	
	
}
