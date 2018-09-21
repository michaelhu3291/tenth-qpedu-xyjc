package data.academic.noticeManage.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



import data.academic.statisticsAnalysis.service.ScoreSearchService;
import data.academic.util.ExportUtil;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.platform.entity.EntityAnnouncementBasicInfo;
import data.academic.noticeManage.entity.EntityAnnouncementSchoolCode;
import data.academic.noticeManage.service.NoticeBasicInfoService;
import data.academic.noticeManage.service.NoticeTeamService;
import data.academic.schoolManage.service.PoliticalInstructorService;
import data.platform.authority.security.SecurityContext;
import data.platform.service.PlatformAccessoryService;
import data.platform.service.PlatformUserService;
/**
 * HQ-公告管理-公告列表管理类。
 * @author LiuGuo
 *
 */
@Controller
@RequestMapping( "announcement/noticementList" )
public class NoticeListController extends AbstractBaseController {
	@Autowired 
	private ScoreSearchService scoreSearchService;
	@Override
	protected void init(ModelMap model, HttpServletRequest request) {

		// 得到所有学校的集合
		List<Map<String, Object>> schoolList = scoreSearchService.getSchoolCode();
		model.put("schoolList", schoolList);


		//List<Map<String, String>> dictionaryList = announcementBasicInfoService.loadChildDictionary("8BE70AE2-313C-4F3E-BC07-897E11140A3B");
		List<Map<String,String>> timeList = announcementBasicInfoService.loadChildDictionary("23B8C637-CE9D-449D-A2F2-FA685A79C960");
		//model.addAttribute("announcementTypeList", dictionaryList);
		model.addAttribute("timeList", timeList);
		//System.out.println("我我我"+dictionaryList);

	}
	/**
	 * 获取公告信息
	 * 
	 * @param out 响应输出对象
	 */
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
		String announcementType="通知";
		String  publishPerson = parseString(paramMap.get("publishPerson")) ;
		String  context = parseString(paramMap.get("context")) ;	
		String enableTimeStart = parseString(paramMap.get("enableTimeStart"));
		String enableTimeEnd = parseString(paramMap.get("enableTimeEnd"));		
		int currentPage = parseInteger(paramMap.get("page"));
		int pageSize = parseInteger(paramMap.get("rows"));
		String sortField = trimString(paramMap.get("sidx"));
		String sort = trimString(paramMap.get("sord"));
		boolean history_boolean = parseBoolean( paramMap.get( "history" ) ) ;	
		String currentTime=formatDate(new Date(), "yyyy-MM-dd");
		if(history_boolean){
			currentTime = "";//当前时间为空表示查询历史记录
		}
		String role = parseString(paramMap.get("role")) ;
		String publishDept=parseString(paramMap.get("publishDept")) ;
		PagingResult<Map<String,Object>> pagingResult = announcementBasicInfoService.searchAnnouncementInfo(role,SecurityContext.getPrincipal().getId(),0,currentTime,enableTimeStart,enableTimeEnd,context,announcementType,publishPerson,title,sortField,sort,null,null,currentPage,pageSize,publishDept);
		List<Map<String,Object>> list = pagingResult.getRows();
		List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
		for (Map<String,Object> resultMap : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", formatString(resultMap.get("id")));
			map.put("announcementType", formatString(resultMap.get("AnnouncementType")));
			map.put("title", formatString(resultMap.get("Title")));
			map.put("publishDate", formatDate(parseDate(resultMap.get("PublishDate")),"yyyy-MM-dd"));
		    Date date=new Date();
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			String time=format.format(date);
			String enableEndTime= formatDate(parseDate(resultMap.get("EnableTimeEnd")),"yyyy-MM-dd");
			if((time.compareTo(enableEndTime)>0)&&("2".equals(formatString(resultMap.get("Status"))))){
				resultMap.put("Status", "3");
			}
			map.put("enableTime", convert( formatDate(parseDate(resultMap.get("EnableTimeStart")),"yyyy-MM-dd"), formatDate(parseDate(resultMap.get("EnableTimeEnd")),"yyyy-MM-dd")," 至 "));
			map.put("operator", formatString(resultMap.get("Operator")));
			map.put("publishPerson", formatString(resultMap.get("PublishPerson")));
			map.put("publishPersonId", formatString(resultMap.get("PublishPersonId")));
			map.put("seqNums", formatString(resultMap.get("SeqNums")));
			map.put("status", formatString(resultMap.get("Status")));
			map.put("edit", formatString(resultMap.get("PublishPersonId")).equals(SecurityContext.getPrincipal().getId())?"ok":"no");
			newList.add(map);
		}
		PagingResult<Map<String,String>> newPagingResult = new PagingResult<Map<String,String>>( newList, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords() ) ;
		out.print( getSerializer().formatObject( newPagingResult ) ) ;
	}
	/**
	 * 
	 * @Title: importExcel
	 * @Description: 导出excel
	 * @author jay zhong
	 * @date 2017年11月20日 下午5:06:07 
	 * @return void
	 *
	 * @param data
	 * @param out
	 */
	@RequestMapping(params = "command=importExcel")
	public void importExcel(@RequestParam( "data" ) String data,HttpServletResponse response) 
	{	
		Map<String,Object> paramMap= getSerializer().parseMap(data) ;
		String title = trimString( paramMap.get( "q" ) ) ;
		String  state = parseString(paramMap.get("state")) ;
		String announcementType="";
		if("1".equals(state)){
			announcementType="通知";
		}else{
			announcementType="公告";
		}
		String  publishPerson = parseString(paramMap.get("publishPerson")) ;
		String  context = parseString(paramMap.get("context")) ;	
		String enableTimeStart = parseString(paramMap.get("enableTimeStart"));
		String enableTimeEnd = parseString(paramMap.get("enableTimeEnd"));		
		boolean history_boolean = parseBoolean( paramMap.get( "history" ) ) ;	
		String currentTime=formatDate(new Date(), "yyyy-MM-dd");
		if(history_boolean){
			currentTime = "";//当前时间为空表示查询历史记录
		}
		String role = parseString(paramMap.get("role")) ;
		String publishDept=parseString(paramMap.get("publishDept")) ;
		List<Map<String,Object>> list = announcementBasicInfoService.importExcel(role,SecurityContext.getPrincipal().getId(),0,currentTime,enableTimeStart,enableTimeEnd,context,announcementType,publishPerson,title);
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		for (Map<String,Object> resultMap : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", formatString(resultMap.get("id")));
			map.put("announcementType", formatString(resultMap.get("AnnouncementType")));
			map.put("title", formatString(resultMap.get("Title")));
			map.put("publishDate", formatDate(parseDate(resultMap.get("PublishDate")),"yyyy-MM-dd"));
		    Date date=new Date();
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			String time=format.format(date);
			String enableEndTime= formatDate(parseDate(resultMap.get("EnableTimeEnd")),"yyyy-MM-dd");
			if((time.compareTo(enableEndTime)>0)&&("2".equals(formatString(resultMap.get("Status"))))){
				resultMap.put("Status", "3");
			}
			String status=formatString(resultMap.get("Status"));
			if("1".equals(status)){
				resultMap.put("Status","未发布");
			}else if("2".equals(status)){
				resultMap.put("Status","已发布");
			}else if("3".equals(status)){
				resultMap.put("Status","已过期");
			}else{
				resultMap.put("Status","");
			}
			
			map.put("enableTime", convert( formatDate(parseDate(resultMap.get("EnableTimeStart")),"yyyy-MM-dd"), formatDate(parseDate(resultMap.get("EnableTimeEnd")),"yyyy-MM-dd")," 至 "));
			map.put("operator", formatString(resultMap.get("Operator")));
			map.put("publishPerson", formatString(resultMap.get("PublishPerson")));
			map.put("publishPersonId", formatString(resultMap.get("PublishPersonId")));
			map.put("seqNums", formatString(resultMap.get("SeqNums")));
			map.put("status", formatString(resultMap.get("Status")));
			map.put("edit", formatString(resultMap.get("PublishPersonId")).equals(SecurityContext.getPrincipal().getId())?"ok":"no");
			newList.add(map);
		}
		String fileName="通知列表.xls";
		String sheetName="通知列表详情";
		String[] title1={"序号","标题","发布人","发布日期","有效期","状态"};
		String[] key={"xh","title","publishPerson","publishDate","enableTime","status"};
		ExportUtil.ExportExcel(response, title1, fileName, sheetName, newList, key);
				
		
	}
	/**
	 * 门户获取获取通知公告数据
	 * @return
	 */
	@RequestMapping(value = "/loadAannouncementListPortal")
	public @ResponseBody Object loadAannouncementListPortal(HttpServletRequest request) {
		String currentId = SecurityContext.getPrincipal().getId();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("currentId", currentId);
		param.put("pageSize", request.getParameter("pageSize"));
		List<EntityAnnouncementBasicInfo> basicInfoList = announcementBasicInfoService.searchAnnouncementsById(param);

		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for(EntityAnnouncementBasicInfo basicInfo:basicInfoList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", basicInfo.getId());
			map.put("announcementType", basicInfo.getAnnouncementType());
			map.put("title", basicInfo.getTitle());
			map.put("outTime", periodToString(basicInfo.getEnableTimeEnd().getTime()-(new Date()).getTime()));
			map.put("outHour", ((basicInfo.getEnableTimeEnd().getTime()-(new Date()).getTime())% 86400000) / 3600000);
			map.put("size", basicInfo.getEnableTimeEnd().getTime()-(new Date()).getTime());
			list.add(map);
		}
		list = compare(list);
		return list;
	}
	public List<Map<String, Object>> compare(List<Map<String, Object>> list) {
		for (int i = 0;i < list.size();i++) {
			int mark = 0;
			for (int j = 1;j < list.size();j++) {
				if (Long.valueOf(list.get(i).get("size").toString()) >= Long.valueOf(list.get(j).get("size").toString())) {
					mark = j;
				}
			}
			if (mark != 0) {
				Map<String, Object> tmp = list.get(i);
				list.set(i, list.get(mark));
				list.set(mark, tmp);
			}
		}
		return list;
	}

	private String periodToString(Long millisecond){
		String str = "";
		long day = millisecond / 86400000;
		long hour = (millisecond % 86400000) / 3600000;
		long minute = (millisecond % 86400000 % 3600000) / 60000;
		long second = (millisecond % 86400000 % 3600000 % 60000) / 1000;
		if(day > 0){ str = String.valueOf(day) + "天"; }
		if(hour > 0){ str += String.valueOf(hour) + "小时"; }
		//if(minute > 0){ str += String.valueOf(minute) + "分钟"; }
		//if(second>0){ str += String.valueOf(second) + "秒";}
		return str;
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
		List<String> ids = (List<String>)paramMap.get("id");

		/*String[] id= parseString(paramMap.get("id")).substring(1, parseString(paramMap.get("id")).length()-1).replace(" ", "").split(",");
		List<String> ids=Arrays.asList(id);*/
		for(String objId : ids){
			accessoryService.removeAccessoryByAssociatedObjectId(objId);//删除本公告的附件
		}
		announcementBasicInfoService.remove(ids);

		out.print( getSerializer().formatObject("") ) ;
	}


	/**
	 * 保存公告信息的Web方法。<br />
	 * 命令: "submit" ；<br/>
	 * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
	 * @param out 响应输出对象
	 */
	
	@RequestMapping( params = "command=submit" )
	public void submit( @RequestParam( "data" ) String data, java.io.PrintWriter out )
	{	
		Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
		EntityAnnouncementBasicInfo entity = null ;
		String id = trimString( paramMap.get( "id" ));

		if( StringUtils.isNotBlank( id ) )
		{
			entity = announcementBasicInfoService.load( id ) ;
		}
		else
		{
			entity = new EntityAnnouncementBasicInfo() ;
			
		}
		entity.setAnnouncementType("通知");
		List<Map<String,Object>> schoolList =(List<Map<String, Object>>)paramMap.get("partakeSchool");
		List<Map<String,Object>> schoolNameList =(List<Map<String, Object>>)paramMap.get("partakeSchoolName");
		if( schoolList != null && !schoolList.isEmpty() )
		{
			String partakeSchoolName;
			String partakeSchoolIDs;
			StringBuilder sb=new StringBuilder();
			StringBuilder sBuilder=new StringBuilder();
			for( Map<String,Object> fileMap : schoolList )
			{
				String schoolCode=fileMap.get("schoolVal").toString();
				sBuilder.append(schoolCode+",");
			}
			for( Map<String,Object> fileMap : schoolNameList )
			{
				String schoolName=fileMap.get("schoolText").toString();
				sb.append(schoolName+",");
			}
			partakeSchoolName=sb.deleteCharAt(sb.length()-1).toString();
			partakeSchoolIDs=sBuilder.deleteCharAt(sBuilder.length()-1).toString();
			entity.setPartakePersons(partakeSchoolName);
			entity.setPartakePersonIDs(partakeSchoolIDs);
		}else{
			entity.setPartakePersons("");
			entity.setPartakePersonIDs("");
		}
		entity.setStatus(1);
		entity.setSeqNums(1);
		entity.setTitle(parseString(paramMap.get("title")));
		entity.setContext(parseString(paramMap.get("context")));
		String validStartDate=parseString(paramMap.get("enableTimeStartInput"));
		String validEndDate=parseString(paramMap.get("enableTimeEndInput"));
		entity.setEnableTimeStart(parseDate(validStartDate, "yyyy-MM-dd")); 
		entity.setEnableTimeEnd(parseDate(validEndDate,"yyyy-MM-dd"));
		entity.setSchoolType(parseString(paramMap.get("schoolType")));
		entity.setPublishPersonId(SecurityContext.getPrincipal().getId());
		entity.setPublishPerson(SecurityContext.getPrincipal().getChineseName());
		entity.setCreateDate(new Date());
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
	public void load(HttpServletRequest request, java.io.PrintWriter out )
	{
		String id=request.getParameter("id");
		Map<String,Object> notcieInfoMap = announcementBasicInfoService.loadNoticeInfoById( id ) ;
		HashMap<String,Object> resultMap=new HashMap<String,Object>();
		resultMap.clear();
		if(notcieInfoMap!=null && notcieInfoMap.size()>0)
		{
			resultMap.put("id",notcieInfoMap.get("id"));
			resultMap.put("announcementType", notcieInfoMap.get("AnnouncementType"));

			resultMap.put("context", notcieInfoMap.get("Context"));
			String enableTimeStart=formatDate((Date)notcieInfoMap.get("EnableTimeStart"),"yyyy-MM-dd");
			String enableTimeEndInput=formatDate((Date)notcieInfoMap.get("EnableTimeEnd"),"yyyy-MM-dd");
			resultMap.put("enableTimeStartInput",enableTimeStart) ;
			resultMap.put("enableTimeEndInput",enableTimeEndInput);
			resultMap.put("title",notcieInfoMap.get("Title"));
			resultMap.put("schoolType",notcieInfoMap.get("SchoolType"));
			resultMap.put("partakeSchool",notcieInfoMap.get("PartakePersons"));
			resultMap.put("partakeSchoolIDs",notcieInfoMap.get("PartakePersonIDs"));
			resultMap.put("publishDept", notcieInfoMap.get("publishDept"));
			resultMap.put("teamNames", notcieInfoMap.get("TeamNames"));
		}
		EntityAnnouncementBasicInfo announcementBasicInfoEntity= announcementBasicInfoService.loadNoticeInfo( id ) ;
		resultMap.put("announcementBasicInfoEntity", announcementBasicInfoEntity);

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
			String ids= parseString(paramMap.get("id"));

			EntityAnnouncementBasicInfo entity = announcementBasicInfoService.load(ids);
			entity.setSeqNums(0);
			announcementBasicInfoService.saveOrUpdate(entity);
			
		}else{
			String ids= parseString(paramMap.get("id"));

			EntityAnnouncementBasicInfo entity = announcementBasicInfoService.load(ids);
			entity.setSeqNums(1);
			announcementBasicInfoService.saveOrUpdate(entity);
			
		}
		out.print( getSerializer().formatObject("")) ;
	}

	/**
	 * 根据发布公告信息的 Web 方法。<br />
	 * 命令: "publish" ；<br/>
	 * @param id 输入参数(由 Browser 端 POST 回来的税务局编号)
	 * @param out 相应输出对象
	 */
	@RequestMapping( params = "command=publish" )
	public void publish(@RequestParam( "data" ) String data, java.io.PrintWriter out )
	{	
		Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
		String partakeSchoolName1=null;
		String partakeSchoolIDs1=null;
		String loginName = SecurityContext.getPrincipal().getUsername();
		String orgId = politicalInstructorService.selectOrgIdByLoginName(loginName);    //根据USER_UID获取组织id
		Map<String,Object> map=new HashMap<>();
		map.put("orgId", orgId);
		String orgCode=announcementBasicInfoService.getOrgCode(map);
		EntityAnnouncementBasicInfo entity = null ;
		String id = trimString( paramMap.get( "id" ));

		if( StringUtils.isNotBlank( id ) )
		{
			entity = announcementBasicInfoService.load( id ) ;
		}
		else
		{
			entity = new EntityAnnouncementBasicInfo() ;
			
		}
		entity.setAnnouncementType("通知");
		List<Map<String,Object>> schoolList =(List<Map<String, Object>>)paramMap.get("partakeSchool");
		List<Map<String,Object>> schoolNameList =(List<Map<String, Object>>)paramMap.get("partakeSchoolName");
		if( schoolList != null && !schoolList.isEmpty() )
		{
			
			StringBuilder sb=new StringBuilder();
			StringBuilder sBuilder=new StringBuilder();
			for( Map<String,Object> fileMap : schoolList )
			{
				String schoolCode=fileMap.get("schoolVal").toString();
				sBuilder.append(schoolCode+",");
			}
			for( Map<String,Object> fileMap : schoolNameList )
			{
				String schoolName=fileMap.get("schoolText").toString();
				sb.append(schoolName+",");
			}
			partakeSchoolName1=sb.deleteCharAt(sb.length()-1).toString();
			partakeSchoolIDs1=sBuilder.deleteCharAt(sBuilder.length()-1).toString();
			entity.setPartakePersons(partakeSchoolName1);
			entity.setPartakePersonIDs(partakeSchoolIDs1);
		}else{
			entity.setPartakePersons("");
			entity.setPartakePersonIDs("");
		}
		entity.setStatus(2);
		entity.setSeqNums(1);
		entity.setTitle(parseString(paramMap.get("title")));
		entity.setContext(parseString(paramMap.get("context")));
		String validStartDate=parseString(paramMap.get("enableTimeStartInput"));
		String validEndDate=parseString(paramMap.get("enableTimeEndInput"));
		entity.setEnableTimeStart(parseDate(validStartDate, "yyyy-MM-dd")); 
		entity.setEnableTimeEnd(parseDate(validEndDate,"yyyy-MM-dd"));
		entity.setSchoolType(parseString(paramMap.get("schoolType")));
		entity.setPublishDept(orgCode);
		entity.setPublishPersonId(SecurityContext.getPrincipal().getId());
		entity.setPublishPerson(SecurityContext.getPrincipal().getChineseName());
		entity.setPublishDate(new Date());
		announcementBasicInfoService.saveOrUpdate( entity ) ;
		String announcementID=entity.getId();//获取公告ID
		EntityAnnouncementSchoolCode eas=new EntityAnnouncementSchoolCode();
		eas.setAnnouncementId(announcementID);
		if(partakeSchoolIDs1!=null&& !partakeSchoolIDs1.isEmpty()){
			String[] ary = partakeSchoolIDs1.split(",");
				for(String aa:ary){
					eas.setSchoolCode(aa);
					noticeTeamService.saveOrUpdate(eas);
				}
		}
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
		resultMap.put( "message", "发布成功" ) ;
		out.print( getSerializer().formatMap( resultMap ) ) ;

//		if( StringUtils.isNotBlank( id ) )
//		{
//			entity = announcementBasicInfoService.load( id ) ;
//			entity.setStatus(2);//2:代表发布
//			
//
//
//			//entity.setPartakePersonIDs(userIds);
//			//entity.setPartakePersons(userNames);
//
//			//entity.setTeamNames(TeamName);
//			//entity.setReplyStatus( parseBoolean(paramMap.get("replyStatus"))?1:0 );
//			announcementBasicInfoService.saveOrUpdate( entity ) ;
//		}
//		else
//		{
//			entity = new EntityAnnouncementBasicInfo() ;
//			entity.setStatus(2);//2:代表发布
//			Map<String,Object> announcementTypeMap=(Map<String,Object>)paramMap.get("announcementType");
//			if(announcementTypeMap!=null && announcementTypeMap.size()>0)
//			{
//				entity.setAnnouncementType(parseString(announcementTypeMap.get("value")));
//			}
//			entity.setTitle(parseString(paramMap.get("title")));
//			entity.setContext(parseString(paramMap.get("context")));
//			String validStartDate=parseString(paramMap.get("enableTimeStartInput"));
//			validStartDate=validStartDate+" "+parseString(paramMap.get("enableTimeStart_hm"));
//			String validEndDate=parseString(paramMap.get("enableTimeEndInput"));
//			validEndDate=validEndDate+" "+parseString(paramMap.get("enableTimeEnd_hm"));
//			entity.setEnableTimeStart(parseDate(validStartDate, "yyyy-MM-dd HH:mm"));  
//			entity.setEnableTimeEnd(parseDate(validEndDate,"yyyy-MM-dd HH:mm"));
//			entity.setPublishDate(new Date());
//			entity.setPublishPersonId(SecurityContext.getPrincipal().getId());
//			entity.setPublishPerson(SecurityContext.getPrincipal().getChineseName());
//			entity.setSeqNums(1);
//			//entity.setPartakePersonIDs(userIds);
//			//entity.setPartakePersons(userNames);
//			//entity.setTeamNames(TeamName);
//			//entity.setReplyStatus( parseBoolean(paramMap.get("replyStatus"))?1:0 );
//			announcementBasicInfoService.saveOrUpdate( entity ) ;
//			
//			String announcementSchoolID=entity.getPartakePersonIDs();//获取参与学校ID
//			//			String[] ary = announcementSchoolID.split(",");
//			//			EntityAnnouncementSchoolCode eas=new EntityAnnouncementSchoolCode();
//			//			 
//			//			System.out.println("wowowowo"+eas.getAnnouncementId()+"wowowowo是");
//			//			System.out.println("wow你你你你你你你你你"+entity.getPartakePersonIDs()+"wowowowo是");
//			//			for(String aa:ary){
//			//				eas.setSchoolCode(aa);
//			//				noticeTeamService.saveOrUpdate(eas);
//			//			}
//			
//			List<Map<String,Object>> fileList = (List<Map<String,Object>>)paramMap.get( "files" ) ;
//			List<String> fileIds = new ArrayList<String>() ;
//			if( fileList != null && !fileList.isEmpty() )
//			{
//				for( Map<String,Object> fileMap : fileList )
//				{
//					fileIds.add( parseString( fileMap.get( "id" ) ) ) ;
//				}
//				accessoryService.associated( entity.getId(), fileIds ) ;
//			}
//		}
//
//		/* List<String> idAry = new ArrayList<String>();
//      idAry.add(entity.getId());
//      noticeTeamService.remove(idAry);*/
//
//		/*for( Map<String,Object> map :teamList)
//      {
//    	EntityNoticeTeam teamEntity = new EntityNoticeTeam();
//      	teamEntity.setRoleId(parseString(map.get("id")));
//      	teamEntity.setRoleName(parseString(map.get("name")));
//      	teamEntity.setAnnouncementId(entity.getId());
//      	noticeTeamService.saveOrUpdate(teamEntity);
//      }*/
		
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
		/* 		Map<String, Object> paramMap = announcementBasicInfoService.loadNoticeInfoById( parseString(idMap.get("id")) ) ;
		
   		HashMap<String,Object> resultMap=new HashMap<String,Object>();
   		resultMap.put("context",paramMap.get("Context"));
   		Map<String, Object> announcementType = announcementBasicInfoService.selectDictionaryInfo(parseString(paramMap.get("AnnouncementType")));
   		resultMap.put("announcementType",parseString(announcementType==null ? "" : announcementType.get("DictionaryName")));

   		resultMap.put("title",paramMap.get("Title"));
   		resultMap.put("activeTime", convert( formatDate(parseDate(paramMap.get("EnableTimeStart")), "yyyy-MM-dd"),
   				formatDate(parseDate(paramMap.get("EnableTimeEnd")), "yyyy-MM-dd")," 至 "));
   		resultMap.put("publishPerson", paramMap.get("PublishPerson"));                                                              
   		resultMap.put("publishTime", formatDate(parseDate(paramMap.get("PublishDate")),"yyyy-MM-dd"));*/
		HashMap<String,Object> resultMap=new HashMap<String,Object>();
		EntityAnnouncementBasicInfo announcementEntity=announcementBasicInfoService.loadNoticeInfo( parseString(idMap.get("id")) );
		//List<Map<String, Object>> list = announcementBasicInfoService.selectPartakePerson(SecurityContext.getPrincipal().getId(),parseString(idMap.get("id")),"通知");

		resultMap.put("context",announcementEntity.getContext());
		//System.out.println("打印context"+announcementEntity.getContext());
		resultMap.put("announcementType",announcementEntity.getAnnouncementType());
		resultMap.put("title",announcementEntity.getTitle());
		resultMap.put("activeTime", convert( formatDate(announcementEntity.getEnableTimeStart(), "yyyy年MM月dd日"),
				formatDate(announcementEntity.getEnableTimeEnd(), "yyyy年MM月dd日")," 至 "));
		resultMap.put("publishPerson", announcementEntity.getPublishPerson());                                                              
		resultMap.put("publishTime", formatDate(announcementEntity.getPublishDate(),"yyyy年MM月dd日"));
		resultMap.put("announcementEntity", announcementEntity);
		resultMap.put("id", announcementEntity.getId());
		
		out.print( getSerializer().formatMap(resultMap)) ;
	}


	/**
	 * 获取当前用可以查看或者回复的公告信息
	 * 
	 * @param out 响应输出对象
	 */
	@RequestMapping(params = "command=loadAllPublishedAnnouncements")
	public void loadAllPublishedAnnouncements(java.io.PrintWriter out) 
	{	
		List<String> roleList = platformUserService.selectUserRoles(SecurityContext.getPrincipal().getId()) ;
		String role=""; 
		for(String id : roleList){
			if(id.equals("0BF49EE4-4942-4921-8F1D-486B8673E401")){//是否是系统管理员角色
				role="admin" ;
			}
		}

		PagingResult<Map<String,Object>> pagingResult = announcementBasicInfoService.searchAnnouncementInfo(role,SecurityContext.getPrincipal().getId(),0,formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"),null,null,null,null,null,null,null,null,null,null,1,16,null);
		List<Map<String,Object>> list = pagingResult.getRows();
		List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
		for (Map<String,Object> resultMap : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", formatString(resultMap.get("id")));
			map.put("announcementType", formatString(resultMap.get("dictionaryName")));
			map.put("title", formatString(resultMap.get("Title")));
			map.put("publishDate", formatDate(parseDate(resultMap.get("PublishDate")),"yyyy-MM-dd HH:mm:ss"));
			map.put("enableTime", convert( formatDate(parseDate(resultMap.get("EnableTimeStart")),"yyyy-MM-dd HH:mm:ss"), formatDate(parseDate(resultMap.get("EnableTimeEnd")),"yyyy-MM-dd HH:mm:ss")," 至 "));
			map.put("operator", formatString(resultMap.get("Operator")));
			map.put("publishPerson", formatString(resultMap.get("PublishPerson")));
			map.put("publishPersonId", formatString(resultMap.get("PublishPersonId")));
			map.put("seqNums", formatString(resultMap.get("SeqNums")));
			map.put("status", formatString(resultMap.get("Status")));
			map.put("edit", formatString(resultMap.get("PublishPersonId")).equals(SecurityContext.getPrincipal().getId())?"ok":"no");
			map.put("publishDept", formatString(resultMap.get("PublishDept")));
			newList.add(map);
		}
		PagingResult<Map<String,String>> newPagingResult = new PagingResult<Map<String,String>>( newList, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords() ) ;
		out.print( getSerializer().formatObject( newPagingResult ) ) ;
	}


	@Autowired
	private NoticeBasicInfoService announcementBasicInfoService;
	@Autowired
	private PlatformAccessoryService accessoryService ;
	@Autowired
	private PlatformUserService platformUserService ;
	@Autowired
	private NoticeTeamService noticeTeamService ;
	@Autowired
	private PoliticalInstructorService politicalInstructorService;

}
