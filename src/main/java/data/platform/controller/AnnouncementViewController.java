package data.platform.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import data.framework.support.AbstractBaseController;
import data.platform.entity.EntityAnnouncementBasicInfo;
import data.platform.service.PlatformAccessoryService;
import data.platform.service.PlatformUserService;

/**
 * 公告管理-公告查看类。
 * @author LiuGuo
 *
 */
@Controller       
@RequestMapping( "announcementView" )
public class AnnouncementViewController extends AbstractBaseController
{

    @Override
    protected void init( ModelMap model, HttpServletRequest request )
    {
    	String id = request.getParameter("id");
    	Map<String, Object> paramMap = userService.loadNoticeInfoById( id ) ;
    	EntityAnnouncementBasicInfo announcementEntity=userService.loadNoticeInfo(id);
    	model.addAttribute("announcementType",paramMap.get("AnnouncementType"));
    	model.addAttribute("context",paramMap.get("Context"));
    	model.addAttribute("title",paramMap.get("Title"));
    	model.addAttribute("activeTime", convert( formatDate(parseDate(paramMap.get("EnableTimeStart")), "yyyy-MM-dd"),formatDate(parseDate(paramMap.get("EnableTimeEnd")), "yyyy-MM-dd")," 至 "));
    	model.addAttribute("publishPerson", paramMap.get("PublishPerson"));                                                              
    	model.addAttribute("publishTime", formatDate(parseDate(paramMap.get("PublishDate")),"yyyy-MM-dd"));
    	model.addAttribute("announcementEntity", announcementEntity);
    	model.addAttribute("announcementId", announcementEntity.getId());
    //	model.addAttribute("currentUserId", SecurityContext.getPrincipal().getId());
    	
    	
    }
    
    /**
  	 * 查看当前公告信息
  	 * 
  	 * @param out 响应输出对象
  	 */
  	@RequestMapping(params = "command=loadAnnouncement")
  	public void loadAnnouncement(@RequestParam( "data" )String data,java.io.PrintWriter out) 
  	{	
  		Map<String, Object> idMap=getSerializer().parseMap(data);
  		EntityAnnouncementBasicInfo announcementEntity =userService.loadNoticeInfo( parseString(idMap.get("id")) );
   		
   		HashMap<String,Object> resultMap=new HashMap<String,Object>();
   		resultMap.put("context",announcementEntity.getContext());
   		resultMap.put("title",announcementEntity.getTitle());
   		resultMap.put("activeTime", convert( formatDate(announcementEntity.getEnableTimeStart(), "yyyy-MM-dd"),
   				formatDate(announcementEntity.getEnableTimeEnd(), "yyyy-MM-dd")," 至 "));
   		resultMap.put("publishPerson", announcementEntity.getPublishPerson());                                                              
   		resultMap.put("publishTime", formatDate(announcementEntity.getPublishDate(),"yyyy-MM-dd"));
   		resultMap.put("announcementEntity", announcementEntity);
   		resultMap.put("id", announcementEntity.getId());
   		out.print( getSerializer().formatMap(resultMap)) ;
  	}
  	
    
   @Autowired
   private PlatformUserService userService;
   @Autowired
   private PlatformAccessoryService accessoryService ;
}
