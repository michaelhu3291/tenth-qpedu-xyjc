package data.academic.taskCenter.controller;

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

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractValidatorController;
import data.framework.validation.Errors;
import data.platform.authority.security.SecurityContext;
import data.academic.taskCenter.service.TaskFinishedRecordService;
import data.academic.taskCenter.service.TaskKindInfoService;

/**
 * OA-任务中心-已完成任务信息控制器类。
 * @author JohnXU
 *
 */
@Controller
@RequestMapping("taskManagement/taskCenter/taskCenterHandledMissionList")
public class TaskCenterHandledMissionListController extends AbstractValidatorController 
{

	@Override
	protected void validate(Object obj, Errors errors) 
	{
		
	}

	@Override
	protected void init(ModelMap model, HttpServletRequest request) 
	{
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping( params = "command=search")
	public void queryAll(@RequestParam( "data" ) String data,java.io.PrintWriter out)
	{
		Map<String, Object> paramMap=getSerializer().parseMap(data);
		int currentPage = parseInteger( paramMap.get( "page" ) ) ;
   	    int pageSize = parseInteger( paramMap.get( "rows" ) ) ;
   	    String sortField = trimString( paramMap.get( "sidx" ) ) ;
   	    String sort = trimString( paramMap.get( "sord" ) ) ;
   	    Map<String, String> taskKindMap = (Map<String, String>)(paramMap.get("taskDistribute"));
	    String taskDistribute = "";
		if (null != taskKindMap){
			taskDistribute = parseString(taskKindMap.get("value")); 
		}
   	    String dispatchTimeStart = trimString( paramMap.get( "dispatchTimeStart" ) ) ;
   	    String dispatchTimeEnd = trimString( paramMap.get( "dispatchTimeEnd" ) ) ;//发送时间
   	    boolean isFast = parseBoolean( paramMap.get( "isFast" ) ) ;
   		String title = "";
	     if( isFast ){
	    	 title = trimString( paramMap.get( "q" ) ) ;
	     }
		PagingResult<Map<String,Object>> pagingResult=centerService.searchTaskFinishedRecordList(title,dispatchTimeStart,dispatchTimeEnd,taskDistribute,SecurityContext.getPrincipal().getId(),"","",sortField,sort,currentPage,pageSize);
		List<Map<String,Object>> list = pagingResult.getRows();
   		List<Map<String, String>> newList = new ArrayList<Map<String, String>>();		
	 	for (Map<String,Object> resultMap : list) {
	 		Map<String,String> map = new HashMap<String,String>() ;
	 		map.put("id", formatString(resultMap.get("id")));
	 		map.put("title", formatString(resultMap.get("titleName")));
	 		map.put("taskStep", formatString(resultMap.get("taskStep")));
	 		map.put("taskKindName", taskKindInfoService.loadTaskKindNameByCode(formatString(resultMap.get("TASK_KIND_CODE"))));
	 		map.put("remindSender", formatString(resultMap.get("creater")));
	 		map.put("remindNowSendTime", formatDate(parseDate(resultMap.get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
   			newList.add(map);
	    }
		PagingResult<Map<String,String>> newPagingResult = new PagingResult<Map<String,String>>( newList, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords() ) ;
		out.print( getSerializer().formatObject( newPagingResult ) ) ;
	}
	
	@RequestMapping( params = "command=counts")
	public void counts(@RequestParam( "data" ) String data,java.io.PrintWriter out)
	{
		List<Map<String, Object>> countsMap = centerService.selectCompletedTaskCountsByTaskKindCode();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		for(Map<String, Object> map : countsMap){
			if( parseString(map.get("TASK_KIND_CODE")).equals("TaskCenter_MeetingConflict") ){
				paramMap.put("TaskCenter_MeetingConflict", parseInteger(map.get("completeTaskCounts"))) ;
			}else if( parseString(map.get("TASK_KIND_CODE")).equals("TaskCenter_Workflow") ){
				paramMap.put("TaskCenter_Workflow", parseInteger(map.get("completeTaskCounts"))) ;
			}else if(parseString(map.get("TASK_KIND_CODE")).equals("TaskCenter_AssetMaintenance") ){
				paramMap.put("TaskCenter_AssetMaintenance", parseInteger(map.get("completeTaskCounts")));
			}
		}
		out.print(getSerializer().formatMap(paramMap));
	}
	
	@RequestMapping( params = "command=selectAllHandledMissions")
	public void selectAllHandledMissions(java.io.PrintWriter out)
	{
		List<Map<String,Object>> handledMissionList = centerService.selectAllHandledMissions(SecurityContext.getPrincipal().getId());
		out.print(getSerializer().formatList(handledMissionList));
	}
	
	@Autowired
	private TaskFinishedRecordService centerService;
	@Autowired
	private TaskKindInfoService taskKindInfoService;
}
