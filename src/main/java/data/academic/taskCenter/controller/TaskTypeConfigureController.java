package data.academic.taskCenter.controller ;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractValidatorController;
import data.framework.validation.Errors;
import data.platform.authority.security.SecurityContext;
import data.academic.taskCenter.entity.EntityTaskKindInfo;
import data.academic.taskCenter.service.TaskKindInfoService;

/**
 * OA-任务中心-任务类型配置控制器类。
 * 
 * @author JohnXU
 * 
 */
@Controller       
@RequestMapping( "taskManagement/taskTypeConfigure" )
public class TaskTypeConfigureController extends AbstractValidatorController
{

	@Override
	protected void validate(Object obj, Errors errors) {
		
	}

	@Override
	protected void init(ModelMap model, HttpServletRequest request)
	{
		
	}
	 /**
     * 分页查询任务类型配置
     * 
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
	@RequestMapping( params = "command=search")
	public void queryall(@RequestParam( "data" ) String data,java.io.PrintWriter out)
	{
	 Map<String, Object> paramMap=getSerializer().parseMap(data);
	 int currentPage = parseInteger( paramMap.get( "page" ) ) ;
     int pageSize = parseInteger( paramMap.get( "rows" ) ) ;
     String sortField = trimString( paramMap.get( "sidx" ) ) ;
     String sort = trimString( paramMap.get( "sord" ) ) ;
     boolean isFast = parseBoolean( paramMap.get( "isFast" ) ) ;
     String appCode = "" ;
     if( isFast )
    	 appCode = trimString( paramMap.get( "q" ) ) ;
     else
     {
    	 appCode = trimString( paramMap.get( "appCode" ) ) ;
     }
   	 PagingResult<EntityTaskKindInfo> pagingResult=infoService.search(appCode,sortField, sort, currentPage, pageSize);
   	 List<EntityTaskKindInfo> list=pagingResult.getRows();
   	 List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
   	 for (EntityTaskKindInfo entity : list)
   	 {
		Map<String,String> map = new HashMap<String,String>() ;
 		map.put("id", formatString(entity.getId()));
 		map.put("appcode", formatString(entity.getAppCode()));
 		map.put("taskKindCode", formatString(entity.getTaskKindCode()));
 		map.put("taskKideName", formatString(entity.getTaskKideName()));
 		map.put("createtime", formatDate(entity.getCreateTime()));
 		newList.add(map) ;
     }
   	PagingResult<Map<String,String>> newPagingResult = 
		   	   new PagingResult<Map<String,String>>( newList, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords() ) ;
		          out.print( getSerializer().formatObject( newPagingResult ) ) ;	
	}
	
	/**
     * 保存任务类型配置的Web方法。<br /><br />
     * 命令: "submit" ；<br/><br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
	@SuppressWarnings("unchecked")
	@RequestMapping( params = "command=submit")
	 public void submit(@RequestParam( "data" ) String data, java.io.PrintWriter out )
	  {
	    	Map<String, Object> paramMap=getSerializer().parseMap(data);
	    	EntityTaskKindInfo entity=null;	
	    	String id=parseString(paramMap.get("id"));
	    	String appCode=parseString(paramMap.get("appCode"));  
	    	String taskKideName=parseString(paramMap.get("taskKideName"));
	    	String taskKideCode=parseString(paramMap.get("taskKindCode"));	    	
	    	Map<String,Object> maptaskType=(Map<String,Object>)paramMap.get("taskType");
	    	String taskTypetext=parseString(maptaskType.get("value"));	    	
        	Map<String,Object> mapFinisMode=(Map<String,Object>)paramMap.get("taskFinisMode");
	    	String FinisModetext=parseString(mapFinisMode.get("value"));
	    	String remindMsgType=parseString(paramMap.get("remindMsgType"));
	    	if(StringUtils.isNotBlank(id))
			{
	    		entity = infoService.load(id);
				entity.setLastUpdateUserName(parseString(SecurityContext.getPrincipal().getChineseName()));  
			    entity.setLastUpdateTime(new Date()) ; 
			}
	    	else
	    	{
	    		  entity=new EntityTaskKindInfo();
	    		  entity.setCreateUserName(parseString(SecurityContext.getPrincipal().getChineseName()));
	    		  entity.setCreateTime(new Date());	  
	    	}	
	    	entity.setAppCode(appCode);	
	    	entity.setTaskKideName(taskKideName);
	    	entity.setTaskKindCode(taskKideCode);
	    	entity.setTaskType(taskTypetext);
	    	entity.setTaskFinisMode(FinisModetext);
	    	entity.setRemindMsgType(remindMsgType);
	    	infoService.saveOrupdate(entity);
    	    Map<String, Object> resultMap=new HashMap<String, Object>();
		    resultMap.put( "message", "保存成功" ) ;
	        out.print( getSerializer().formatMap( resultMap ) ) ;
	  }
	
	 /**
     * 根据一个实体 ID 来加载任务类型配置详细信息的 Web 方法。<br />
     * 命令: "load" ；<br/>
     * @param id 输入参数
     * @param out 相应输出对象
     */
	 @RequestMapping( params = "command=load" )
    public void load(HttpServletRequest request,java.io.PrintWriter out) throws Exception
	{
    	String id=request.getParameter("id");
    	EntityTaskKindInfo entity = infoService.load( id ) ;
    	HashMap<String,Object> resultMap=new HashMap<String,Object>();
    	resultMap.put("entity", entity);
        out.print( getSerializer().formatMap(resultMap)) ;
	}
	 /**
     * 根据一个实体 ID 来删除任务类型配置信息的 Web 方法。<br /><br />
     * 命令: "delete" ；<br/><br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
	@SuppressWarnings("unchecked")
	@RequestMapping( params = "command=delete" )
	 public void delete( @RequestParam( "data" ) String data, java.io.PrintWriter out )
	    {
	        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
	        infoService.remove( (List<String>)paramMap.get( "id" ) ) ;
	        out.print( getSerializer().message( "" ) ) ;
	    }
	    
	@Autowired
	private TaskKindInfoService infoService;
}