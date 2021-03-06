package data.academic.announceManage.service ;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;


import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;
import data.platform.entity.EntityAnnouncementBasicInfo;

/**
 * OA-通知公告-公告基本信息服务类。
 * 
 * @author JohnXU
 * 
 */
@Service
public class AnnouncementBasicInfoService extends AbstractService
{
	
    /**
     * 根据id获取公告信息
     * 
     * @param id 公告id
     * @return 公告信息
     */
    public EntityAnnouncementBasicInfo load( String id )
    {
        Map<String,String> param = new HashMap<String,String>() ;
        param.put( "id", id ) ;
        return selectOne( "announcementBasicInfo.load", param ) ;
    }
    

    /**
     * 根据id获取公告组信息
     * 
     * @param id 公告id
     * @return 公告信息
     */
    public Map<String,Object> loadNoticeInfoById( String id )
    {
        Map<String,String> param = new HashMap<String,String>() ;
        param.put( "id", id ) ;
        return selectOne( "announcementBasicInfo.loadNoticeInfoById", param ) ;
    }
    
    /**
     * 根据id获取公告组信息（带有附件）
     * 
     * @param id 公告id
     * @return 公告信息
     */
    public EntityAnnouncementBasicInfo loadNoticeInfo( String id )
    {
        Map<String,String> param = new HashMap<String,String>() ;
        param.put( "id", id ) ;
        return selectOne( "announcementBasicInfo.loadNoticeInfo", param ) ;
    }
    
    /**
     * 保存或更新公告信息
     * 
     * @param entity 公告实体
     */
    public void saveOrUpdate( EntityAnnouncementBasicInfo entity )
    {
        if( StringUtils.isNotBlank( entity.getId() ) )
            update( "announcementBasicInfo.updateAnnouncement", entity ) ;
        else
            insert( "announcementBasicInfo.insertAnnouncement", entity ) ;
    }

    /**
     * 根据公告id删除公告信息
     * 
     * @param id 公告编号
     * @return 删除记录数
     */
    public int remove(List<String> idAry)
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "idAry", idAry ) ;
        return delete( "announcementBasicInfo.deleteAnnouncement", param ) ;
    }
    

    /**
     * 根据数据字典dictionaryCode,查询数据字典信息
     * 
     * @return 数据字典信息
     */
    public Map<String,Object> selectDictionaryInfo(String dictionaryCode)
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "dictionaryCode", dictionaryCode ) ;
        return selectOne( "announcementBasicInfo.selectDictionaryInfo", param ) ;
    }
    
    /**
     * 分页查询公告管理信息 列表(所有记录信息)
     * 
     * @param currentId 当前id ctiveStartTime, 发布状态 status, 有效期开始日期,activeEndTime 有效期日期, announcementType 公告类型 , issuer发布人,  title标题
	 *
     * @return 分页查询集合
     */                                                            
    public PagingResult<Map<String,Object>> searchAnnouncementInfo(String role,String currentId,int status,String currentTime, String enableTimeStart,String enableTimeEnd,String context,String announcementType,String publishPerson,String title,String sortField, String sort,String sortFieldTwo, String sortTwo, int currentPage, int pageSize )
    {
    	HashMap<String,Object> param=new HashMap<String,Object>();
    	param.put("title",title) ;
    	param.put("role",role) ;
    	param.put("currentTime",currentTime) ;
    	param.put("context",context) ;
    	param.put("announcementType",announcementType) ;
    	param.put("enableTimeStart", enableTimeStart) ;
    	param.put("enableTimeEnd", enableTimeEnd) ;
    	param.put("publishPerson", publishPerson) ;
    	param.put("currentId", currentId) ;
    	param.put("status", status) ;
    	 if( StringUtils.isBlank( sortField ) ){
    		 sortField = "\"SeqNums\"" ;
    	 }else{
    		 sortField = "\""+sortField+"\"";
    	 }
            
         if( StringUtils.isBlank( sort ) )
             sort = "ASC" ;
         if(StringUtils.isNotBlank(currentId)){
        	 if( StringUtils.isBlank( sortFieldTwo ) ){
        		 sortFieldTwo = "\"CreateDate\"" ;
        	 }else{
        		 sortFieldTwo = "\""+sortFieldTwo+"\"";
        	 }
        	 if( StringUtils.isBlank( sortTwo ) )
        		 sortTwo = "DESC" ;
         }else{
        	 if( StringUtils.isBlank( sortFieldTwo ) ){
        		 sortFieldTwo = "\"PublishDate\"" ;
        	 }else{
        		 sortFieldTwo = "\""+sortFieldTwo+"\"";
        	 }
        		
        	 if( StringUtils.isBlank( sortTwo ) )
        		 sortTwo = "DESC" ;
         }
        return selectComplexPaging( "announcementBasicInfo.selectPaging", param, sortField, sort, sortFieldTwo, sortTwo, currentPage, pageSize);
    }
     
    /**
     * 根据id查询发布给本人的公告且现在时间在有效期时间段
     * @param param
     * @return
     */
    public List<EntityAnnouncementBasicInfo> searchAnnouncementsById(Map<String, Object> param) {
    	return selectList("announcementBasicInfo.searchAnnouncementsById", param);
    }
    
    
    /**
     * 查询所有已发布的公告信息
     * 
     * @return 已发布的公告信息集合
     */
    public List<EntityAnnouncementBasicInfo> searchAllAnnouncements()
    {
    	return selectList("announcementBasicInfo.searchAllAnnouncements");
    }
    
    /**
     * 根据查询某用户是不是，该公告参与人(包括公告发布人)
     * 
     * @param noticeID 外键id
     * @return 回复信息
     */
    public List<Map<String,Object>> selectPartakePerson( String userId,String noticeID )
    {
        Map<String,String> param = new HashMap<String,String>() ;
        param.put( "noticeID", noticeID ) ;
        param.put( "userId", userId ) ;
        return selectList( "announcementBasicInfo.selectPartakePerson", param ) ;
    }
}