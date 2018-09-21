package data.academic.noticeManage.controller;

import java.util.ArrayList;
import java.util.Arrays;
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
import data.framework.support.AbstractBaseController;
import data.platform.entity.EntityAnnouncementBasicInfo;
import data.academic.noticeManage.service.NoticeBasicInfoService;
import data.platform.authority.security.SecurityContext;
import data.platform.service.PlatformAccessoryService;
/**
 * HQ-公告管理-公告列表类。
 * @author LiuGuo
 *
 */
@Controller       
@RequestMapping( "announcement/announcementPublishList" )
public class AnnouncementPublishListController extends AbstractBaseController {

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub

	}
	/**
	 * 获取公告信息
	 * 
	 * @param out 响应输出对象
	 */
    @SuppressWarnings( "unchecked" )
	@RequestMapping(params = "command=search")
	public void search(@RequestParam( "data" ) String data,java.io.PrintWriter out) 
	{	
		Map<String,Object> paramMap= getSerializer().parseMap(data) ;
		boolean isFast = parseBoolean( paramMap.get( "isFast" ) ) ;	
        String title = "" ;    	
        if( isFast )
        {
        	title = trimString( paramMap.get( "q" ) ) ;
        
        }
        String announcementType="";
    	Map<String,Object>  announcementTypeMap=(Map<String,Object>)paramMap.get("announcementType") ;
    	if(announcementTypeMap!=null && announcementTypeMap.size()>0)
		{
			announcementType=parseString(announcementTypeMap.get("value")) ;
		}
    	String  publishPerson = parseString(paramMap.get("publishPerson")) ;
    	String  context = parseString(paramMap.get("context")) ;	
		String enableTimeStart = parseString(paramMap.get("enableTimeStart"));
		String enableTimeEnd = parseString(paramMap.get("enableTimeEnd"));		
		int currentPage = parseInteger(paramMap.get("page"));
		int pageSize = parseInteger(paramMap.get("rows"));
		String sortField = trimString(paramMap.get("sidx"));
		String sort = trimString(paramMap.get("sord"));
		String publishDept = parseString(paramMap.get("publishDept"));
		boolean history_boolean = parseBoolean( paramMap.get( "history" ) ) ;	
		String currentTime=formatDate(new Date(), "yyyy-MM-dd hh:mm:ss");
		if(history_boolean){
			currentTime = "";//当前时间为空表示查询历史记录
		}
		
		PagingResult<Map<String,Object>> pagingResult = announcementBasicInfoService.searchAnnouncementInfo("",SecurityContext.getPrincipal().getId(),2,currentTime,enableTimeStart,enableTimeEnd,context,announcementType,publishPerson,title,sortField,sort,null,null,currentPage,pageSize,publishDept);
		List<Map<String,Object>> list = pagingResult.getRows();
		List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
		for (Map<String,Object> entity : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", formatString(entity.get("id")));
			map.put("announcementType", formatString(entity.get("dictionaryName")));
			map.put("title", formatString(entity.get("Title")));
			map.put("publishDate", formatDate(parseDate(entity.get("PublishDate")),"yyyy-MM-dd HH:mm:ss"));
			map.put("enableTime", convert( formatDate(parseDate(entity.get("EnableTimeStart")),"yyyy-MM-dd HH:mm:ss"), formatDate(parseDate(entity.get("EnableTimeEnd")),"yyyy-MM-dd HH:mm:ss")," 至 "));
			map.put("operator", formatString(entity.get("Operator")));
			map.put("publishPerson", formatString(entity.get("PublishPerson")));
			map.put("publishPersonId", formatString(entity.get("PublishPersonId")));
			map.put("seqNums", formatString(entity.get("SeqNums")));
			map.put("status", formatString(entity.get("Status")));
			newList.add(map);
		}
		PagingResult<Map<String,String>> newPagingResult = new PagingResult<Map<String,String>>( newList, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords() ) ;
	    out.print( getSerializer().formatObject( newPagingResult ) ) ;
	}
    	
	/**
	 * 删除公告信息
	 * 
	 * @param out 响应输出对象
	 */
	@RequestMapping(params = "command=delete")
	public void delete(@RequestParam( "data" ) String data,java.io.PrintWriter out) 
	{	
		Map<String,Object> paramMap= getSerializer().parseMap(data) ;
		String[] id= parseString(paramMap.get("id")).substring(1, parseString(paramMap.get("id")).length()-1).replace(" ", "").split(",");
		for(int i=0;i<id.length;i++){
			EntityAnnouncementBasicInfo entity = announcementBasicInfoService.load(id[i]);
			if(entity.getPublishPersonId()!=SecurityContext.getPrincipal().getId()){
				Map<String,Object> resultMap = new HashMap<String,Object>() ;
		        resultMap.put( "message", "你要删除的记录中,含有非本人发布的公告信息,请检查" ) ;
		        out.print( getSerializer().formatMap( resultMap ) ) ;
		        return;
			}
		}
		List<String> ids=Arrays.asList(id);
		announcementBasicInfoService.remove(ids);
        out.print( getSerializer().formatObject("") ) ;
	}
    	
	/**
     * 保存公告信息的Web方法。<br />
     * 命令: "submit" ；<br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=submit" )
    public void submit( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        EntityAnnouncementBasicInfo entity = null ;
        String id = trimString( paramMap.get( "id" ) ) ;
        if( StringUtils.isNotBlank( id ) )
        {
            entity = announcementBasicInfoService.load( id ) ;
        }
        else
        {
            entity = new EntityAnnouncementBasicInfo() ;
            entity.setStatus(1);
        }
        
        Map<String,Object> announcementTypeMap=(Map<String,Object>)paramMap.get("announcementType");
        if(announcementTypeMap!=null && announcementTypeMap.size()>0)
        {
        	entity.setAnnouncementType(parseString(announcementTypeMap.get("value")));
        }
        
        entity.setTitle(parseString(paramMap.get("title")));
        entity.setContext(parseString(paramMap.get("context")));
        String validStartDate=parseString(paramMap.get("enableTimeStartInput"));
        validStartDate=validStartDate+" "+parseString(paramMap.get("enableTimeStart_hm"));
        String validEndDate=parseString(paramMap.get("enableTimeEndInput"));
        validEndDate=validEndDate+" "+parseString(paramMap.get("enableTimeEnd_hm"));
        entity.setEnableTimeStart(parseDate(validStartDate, "yyyy-MM-dd HH:mm"));  
        entity.setEnableTimeEnd(parseDate(validEndDate,"yyyy-MM-dd HH:mm"));
        entity.setPublishPersonId(SecurityContext.getPrincipal().getId());
        entity.setPublishDept(parseString(paramMap.get("publishDept")));
        announcementBasicInfoService.saveOrUpdate( entity ) ;
        
        List<Map<String,Object>> fileList = (List<Map<String,Object>>)paramMap.get( "files" ) ;
        List<String> fileIds = new ArrayList<String>() ;
        if( fileList != null && !fileList.isEmpty() )
        {
            for( Map<String,Object> fileMap : fileList )
            {
                fileIds.add( parseString( fileMap.get( "id" ) ) ) ;
            }
            accessoryService.associated( entity.getId(), fileIds ) ;
        }
    	Map<String,Object> resultMap = new HashMap<String,Object>() ;
        resultMap.put( "message", "保存成功" ) ;
        out.print( getSerializer().formatMap( resultMap ) ) ;
    }
    
    
    /**
     * 根据一个实体 ID 来加载公告信息的 Web 方法。<br />
     * 命令: "load" ；<br/>
     * @param id 输入参数(由 Browser 端 POST 回来的税务局编号)
     * @param out 相应输出对象
     */
    @RequestMapping( params = "command=load" )
    public void load( @RequestParam( "id" ) String id, java.io.PrintWriter out )
    {
    	Map<String,Object> notcieInfoMap = announcementBasicInfoService.loadNoticeInfoById( id ) ;
    	HashMap<String,Object> resultMap=new HashMap<String,Object>();
    	if(notcieInfoMap!=null && notcieInfoMap.size()>0)
    	{
    		resultMap.put("id",notcieInfoMap.get("id"));
    		resultMap.put("announcementType", notcieInfoMap.get("AnnouncementType"));
    	    resultMap.put("context", notcieInfoMap.get("Context"));
    	    String enableTimeStart=formatDate((Date)notcieInfoMap.get("EnableTimeStart"),"yyyy-MM-dd");
    	    String enableTimeStarthm=formatDate((Date)notcieInfoMap.get("EnableTimeStart"),"HH:mm");
    	    String enableTimeEndInput=formatDate((Date)notcieInfoMap.get("EnableTimeEnd"),"yyyy-MM-dd");
    	    String enableTimeEndhm=formatDate((Date)notcieInfoMap.get("EnableTimeEnd"),"HH:mm");
    	    resultMap.put("enableTimeStartInput",enableTimeStart) ;
    	    resultMap.put("enableTimeStart_hm",enableTimeStarthm) ;
    	    resultMap.put("enableTimeEndInput",enableTimeEndInput);
    	    resultMap.put("enableTimeEnd_hm",enableTimeEndhm);
    	    resultMap.put("title",notcieInfoMap.get("Title"));
    	    resultMap.put("publishDept", notcieInfoMap.get("PublishDept"));
    	}
        out.print( getSerializer().formatMap(resultMap)) ;
    }
    

    /**
     * 公告置顶
     * 
     * @param out 相应输出对象
     */
    @RequestMapping( params = "command=updateSeqNums" )
    public void updateSeqNums( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
    	Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
    	Boolean top=parseBoolean(paramMap.get("top"));
    	if(top){
    		String[] ids= parseString(paramMap.get("id")).substring(1, parseString(paramMap.get("id")).length()-1).replace(" ", "").split(",");
    		for(int i=0;i<ids.length;i++){
    			EntityAnnouncementBasicInfo entity = announcementBasicInfoService.load(ids[i]);
    	    	entity.setSeqNums(0);
    	    	announcementBasicInfoService.saveOrUpdate(entity);
    		}
    	}else{
    		String[] ids= parseString(paramMap.get("id")).substring(1, parseString(paramMap.get("id")).length()-1).replace(" ", "").split(",");
    		for(int i=0;i<ids.length;i++){
    			EntityAnnouncementBasicInfo entity = announcementBasicInfoService.load(ids[i]);
    	    	entity.setSeqNums(1);
    	    	announcementBasicInfoService.saveOrUpdate(entity);
    		}
    	}
        out.print( getSerializer().formatObject("")) ;
    }

    
    /**
   	 * 根据id加载公告信息
   	 * 
   	 * @param out 响应输出对象 
   	 */
    @RequestMapping( params = "command=lookAnnouncement" )
   	public void lookTable(@RequestParam( "data" ) String data,java.io.PrintWriter out) 
   	{
    	
    	Map<String, Object> idMap=getSerializer().parseMap(data);
   		EntityAnnouncementBasicInfo announcementEntity=announcementBasicInfoService.loadNoticeInfo( parseString(idMap.get("id")) );
   		
   		HashMap<String,Object> resultMap=new HashMap<String,Object>();
   		resultMap.put("context",announcementEntity.getContext());
   		resultMap.put("announcementType",announcementEntity.getAnnouncementType());
   		resultMap.put("title",announcementEntity.getTitle());
   		resultMap.put("activeTime", convert( formatDate(announcementEntity.getEnableTimeStart(), "yyyy-MM-dd"),
   				formatDate(announcementEntity.getEnableTimeEnd(), "yyyy-MM-dd")," 至 "));
   		resultMap.put("publishPerson", announcementEntity.getPublishPerson());                                                              
   		resultMap.put("publishTime", formatDate(announcementEntity.getPublishDate(),"yyyy-MM-dd"));
   		resultMap.put("announcementEntity", announcementEntity);
   		resultMap.put("id", announcementEntity.getId());
   		resultMap.put("publishDept", announcementEntity.getPublishDept());
   		out.print( getSerializer().formatMap(resultMap)) ;
   	}
    
    
    /**
     * 查询所有已发布的公告信息的 Web 方法。<br />
     * 命令: "loadAllPublishedAnnouncements" ；<br/>
     * @param out 相应输出对象
     */
    @RequestMapping( params = "command=loadAllPublishedAnnouncements" )
    public void loadAllPublishedAnnouncements(java.io.PrintWriter out )
    {
    	List<EntityAnnouncementBasicInfo> announcementBasicInfoList=announcementBasicInfoService.searchAllAnnouncements();
        out.print( getSerializer().formatList(announcementBasicInfoList));
    }
    
   /*@Autowired
   private RoomApplyService roomApplyService;*/
   @Autowired
   private NoticeBasicInfoService announcementBasicInfoService;
   @Autowired
   private PlatformAccessoryService accessoryService ;
}
