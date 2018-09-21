package data.academic.announceManage.controller;
/**
 * @author wangchaofa
 * @CreateTime Oct,15 2016
 * @UpdateTime Oct,21 2016
 */
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import data.academic.announceManage.service.AnnouncementBasicInfoService;
import data.framework.support.AbstractBaseController;
import data.platform.entity.EntityAnnouncementBasicInfo;

@RestController
@RequestMapping("announceDetail")
public class AnnounceDetailController extends AbstractBaseController{
	@Autowired
	private AnnouncementBasicInfoService announcementBasicInfoService;
	   
	@Override
	protected void init(ModelMap model, HttpServletRequest request) {

	}
	
	
	/**
	 * 根据id查看（通知/公告）的具体详情
	 * @param data
	 * @param out
	 */
	@RequestMapping(params = "command=getDetailbyId")
	public void getDetailById(@RequestParam("data") String data,java.io.PrintWriter out){
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		String id = (String) paramMap.get("id");
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.clear();
		Map<String,Object> notcieInfoMap = announcementBasicInfoService.loadNoticeInfoById( id );
		if(notcieInfoMap!=null && notcieInfoMap.size()>0)
    	{
    		resultMap.put("id",notcieInfoMap.get("id"));
    		resultMap.put("announcementType", notcieInfoMap.get("AnnouncementType"));
    		resultMap.put("publishDate",notcieInfoMap.get("PublishDate"));
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
    	    resultMap.put("partakePersons",notcieInfoMap.get("PartakePersons"));
    	    resultMap.put("partakePersonIDs",notcieInfoMap.get("PartakePersonIDs"));
    	    resultMap.put("replyStatus",notcieInfoMap.get("ReplyStatus"));  	    
    	    resultMap.put("teamNames", notcieInfoMap.get("TeamNames"));
    	}

    	EntityAnnouncementBasicInfo announcementBasicInfoEntity= announcementBasicInfoService.loadNoticeInfo( id ) ;
    	resultMap.put("announcementBasicInfoEntity", announcementBasicInfoEntity);
    	
        out.print( getSerializer().formatMap(resultMap)) ;
	}
	
	
 
	
}
