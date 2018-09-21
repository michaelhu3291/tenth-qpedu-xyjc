package data.academic.taskCenter.controller;

import java.util.ArrayList;
import java.util.Date;
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
import data.academic.taskCenter.entity.EntityTaskFinishedRecordInfo;
import data.academic.taskCenter.entity.EntityTaskInfo;
import data.academic.taskCenter.service.TaskFinishedRecordService;
import data.academic.taskCenter.service.TaskKindInfoService;
import data.academic.taskCenter.service.TaskRecordService;

/**
 * OA-任务中心-代办任务信息控制器类。
 * @author JohnXU
 *
 */
@Controller
@RequestMapping("taskManagement/taskCenter/taskCenterAgencyMissionList")
public class TaskCenterAgencyMissionListController extends AbstractValidatorController 
{

	@Override
	protected void validate(Object obj, Errors errors) 
	{
		
	}

	@Override
	protected void init(ModelMap model, HttpServletRequest request) 
	{ 
	}
	
	@SuppressWarnings(value="unchecked")
	@RequestMapping( params = "command=search")
	public void queryAll(@RequestParam( "data" ) String data,java.io.PrintWriter out)
	{
		Map<String, Object> paramMap=getSerializer().parseMap(data);
		int currentPage = parseInteger( paramMap.get( "page" ) ) ;
   	    int pageSize = parseInteger( paramMap.get( "rows" ) ) ;
   	    String sortField = trimString( paramMap.get( "sidx" ) ) ;
   	    String sort = trimString( paramMap.get( "sord" ) ) ;
   	    boolean isFast = parseBoolean( paramMap.get( "isFast" ) ) ;
	   	String title = "";
	     if( isFast ){
	    	 title = trimString( paramMap.get( "q" ) ) ;
	     }
	    Map<String, String> taskKindMap = (Map<String, String>)(paramMap.get("taskDistribute"));
	    String taskDistribute = "";
		if (null != taskKindMap){
			taskDistribute = parseString(taskKindMap.get("value")); 
		}
	    // String taskDistribute = parseString(paramMap.get( "taskDistribute" ));
   	    String dispatchTimeStart = trimString( paramMap.get( "dispatchTimeStart" ) ) ;
	    String dispatchTimeEnd = trimString( paramMap.get( "dispatchTimeEnd" ) ) ;//发送时间
		PagingResult<EntityTaskInfo> pagingResult=taskRecordService.searchAgencyMission(title,dispatchTimeStart,dispatchTimeEnd,taskDistribute,sortField,sort,currentPage,pageSize);
		
		List<EntityTaskInfo> list = pagingResult.getRows();
   		List<Map<String, String>> newList = new ArrayList<Map<String, String>>();	
   		  
	 	for (EntityTaskInfo entity : list) {
	 		Map<String,String> map = new HashMap<String,String>();

	 		map.put("id", formatString(entity.getId()));
	 		map.put("title", formatString(entity.getTitle()));
	 		map.put("remindSender", formatString(entity.getCreator()));
	 		map.put("taskStep", formatString(entity.getTaskStep()));
	 		map.put("taskKindName", taskKindInfoService.loadTaskKindNameByCode(formatString(entity.getTaskKindCode())));
	 		map.put("remindNowSendTime", formatDate( entity.getBeginTime()));
	 		map.put("meetingId", formatString(entity.getClientTxSeq()));
	 		map.put("actionUrl", formatString(entity.getActionUrl()));
	 		map.put("taskKindCode", formatString(entity.getTaskKindCode()));
	 		map.put("creatorAccount", formatString(entity.getCreatorAccount()));
	 		map.put("applyId", formatString(entity.getApplyId()));
	 		newList.add(map);
	 	/*	
	 		if(formatString(entity.getTaskStep()).equals("归还")){
	 			if(nowTime.getTime()<= entity.getBeginTime().getTime()){	
	 				map.remove(entity); 				
	 				
	 		}else{
	 			
	 		}
	 		}*/
	    	}
	 		
	 	
		PagingResult<Map<String,String>> newPagingResult = new PagingResult<Map<String,String>>( newList, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords() ) ;
		out.print( getSerializer().formatObject( newPagingResult ) ) ;
	}

	@RequestMapping( params = "command=dealData")
	public void dealData(@RequestParam( "data" ) String data,java.io.PrintWriter out)
	{
		Map<String, Object> paramMap=getSerializer().parseMap(data);
		String taskId=formatString(paramMap.get("taskId"));
		String taskKindCode=formatString(paramMap.get("taskKindCode"));
		EntityTaskInfo taskInfoEntity=taskRecordService.load(taskId);
		
		HashMap<String,Object> resultMap=new HashMap<String,Object>();
		
		if(taskInfoEntity!=null && taskInfoEntity.getStatus()==1)
		{
			taskInfoEntity.setStatus(2);
			taskRecordService.saveOrUpdate(taskInfoEntity);
			if("TaskCenter_MeetingApproval".equals(taskKindCode))
			{
				EntityTaskFinishedRecordInfo taskFinishedRecordInfoEntity=new EntityTaskFinishedRecordInfo();
				taskFinishedRecordInfoEntity.setTaskId(taskInfoEntity.getId());
				taskFinishedRecordInfoEntity.setReceiver(SecurityContext.getPrincipal().getChineseName());
				taskFinishedRecordInfoEntity.setReceiverAccount(SecurityContext.getPrincipal().getId());
				taskFinishedRecordInfoEntity.setFinishedTime(new Date());
				taskFinishedRecordService.saveOrUpdate(taskFinishedRecordInfoEntity);
			}
			resultMap.put("yes","yes");//该任务可以进行处理
		}else
		{
			resultMap.put("no","no");//该任务已经被处理
		}
		out.print( getSerializer().formatMap(resultMap)) ;
	}
	
	@RequestMapping( params = "command=counts")
	public void counts(@RequestParam( "data" ) String data,java.io.PrintWriter out)
	{
		List<Map<String, Object>> countsMap = taskRecordService.selectTaskCountsByTaskKindCode();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		for(Map<String, Object> map : countsMap){
			if( parseString(map.get("TASK_KIND_CODE")).equals("TaskCenter_MeetingConflict") ){
				paramMap.put("TaskCenter_MeetingConflict", parseInteger(map.get("taskCounts"))) ;
			}else if( parseString(map.get("TASK_KIND_CODE")).equals("TaskCenter_AssetMaintenance") ){
				
				paramMap.put("TaskCenter_AssetMaintenance", parseInteger(map.get("taskCounts"))) ;
			}
			else if( parseString(map.get("TASK_KIND_CODE")).equals("TaskCenter_Workflow") ){
				paramMap.put("TaskCenter_WorkFlow", parseInteger(map.get("taskCounts"))) ;
			}
		}
		out.print(getSerializer().formatMap(paramMap));
	}
	
	@RequestMapping( params = "command=selectAllAgencyMissions")
	public void selectAllAgencyMissions(java.io.PrintWriter out)
	{
		List<EntityTaskInfo> agencyMissionList = taskRecordService.selectAllAgencyMissions(SecurityContext.getPrincipal().getId());
		out.print(getSerializer().formatList(agencyMissionList));
	}
	
	
	
	@RequestMapping( params = "command=audit")
	public void saveaudit(@RequestParam( "data" ) String data,java.io.PrintWriter out)
	{
		String currentId =SecurityContext.getPrincipal().getId();//获取当前用户的id
		out.print( currentId ) ;
	}
	
	
	
	@Autowired
	private TaskRecordService taskRecordService;
	@Autowired
	private TaskFinishedRecordService taskFinishedRecordService;
	@Autowired
	private TaskKindInfoService taskKindInfoService;
	
}
