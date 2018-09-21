package data.academic.taskCenter.service;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;
import data.platform.authority.security.SecurityContext;

import data.academic.taskCenter.entity.EntityTaskInfo;

/**
 *  OA-任务中心-任务(信息)服务类。
 * 
 * @author JohnXU
 * 
 */
@Service
public class TaskRecordService extends AbstractService
{
	@Autowired
	private TaskReceiverService taskReceiverService;
	@Autowired
	private TaskFinishedRecordService taskFinishedRecordService;

    /**
     * 根据id获取任务(信息)
     * 
     * @param id 任务(信息)id
     * @return 任务(信息)
     */
    public EntityTaskInfo load( String id )
    {
        Map<String,String> param = new HashMap<String,String>() ;
        param.put( "id", id ) ;
        return selectOne( "taskCenter.load", param ) ;
    }
    
	 /**
     * 分页查询数据（待办任务）。
     * @param sortField 数据库排序字段
     * @param sort 排序方式（ASC|DESC）
     * @param currentPage 当前页数
     * @param pageSize 页大小
     * @return 分页查询集合
     */
    public PagingResult<EntityTaskInfo> searchAgencyMission(String title,String dispatchTimeStart,String dispatchTimeEnd,String taskDistribute,String sortField, String sort, int currentPage, int pageSize )
    {
    	HashMap<String,Object> param=new HashMap<String,Object>();
    	param.put("currentUserId", SecurityContext.getPrincipal().getId());
    	param.put("taskDistribute",taskDistribute);
    	param.put("title",title);
    	param.put("dispatchTimeStart", dispatchTimeStart) ;
    	param.put("dispatchTimeEnd", dispatchTimeEnd) ;
    	
        if( StringUtils.isBlank( sortField ) )
            sortField = "BEGIN_TIME" ;
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging( "taskCenter.selectAgencyTaskPaging", param, sortField, sort, currentPage, pageSize ) ;
    }	
    
    
    /**
	 * 添加和修改任务信息
	 * @param entity
	 */
	public void saveOrUpdate(EntityTaskInfo entity)
	{
		 if(StringUtils.isNotBlank( entity.getId() ) )
	            update("taskCenter.updateTaskCenter", entity );
	        else
	           insert("taskCenter.insertTaskCenter", entity) ;
	}
	
	/**
     * 根据任务信息三方任务唯一标识获取任务信息。
     * @param id 三方任务唯一标识
     * @param status 状态
     * @return 工作完成信息表实体
     */
	 public EntityTaskInfo loadTaskInfoByMarkedFlag( String clientTxSeq,Integer status,String taskKindCode,String id)
	    {
	        Map<String,Object> param = new HashMap<String,Object>() ;
	        param.put( "clientTxSeq", clientTxSeq ) ;
	        param.put( "status", status );
	        param.put( "taskKindCode", taskKindCode );
	        param.put( "id", id );
	        return selectOne( "taskCenter.loadTaskInfoByMarkedFlag", param ) ;
	    }
    
    /**
     * 根据任务信息三方任务唯一标识集合获取任务信息集合。
     * @param id 三方任务唯一标识
     * @return 工作完成信息表实体
     * @author lidong
     */
    public List<EntityTaskInfo> loadTaskInfoListByMarkedFlagList( List<String> idAry, Integer status)
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "idAry", idAry );
        param.put("status", status);
        return selectList( "taskCenter.loadTaskInfoListByMarkedFlagList", param ) ;
    }
    
    
    /**
     * 根据任务信息三方任务唯一标识 删除任务信息。
     * @param id 三方任务唯一标识集合
     * @return 删除任务信息数量
     */
    public  int  remove( List<String> idAry ) throws Exception 
	{
		Map<String, Object> param=new HashMap<String, Object>();
		param.put( "idAry", idAry ) ;
		return delete("taskCenter.deleteTaskInfoByMarkedFlag",param);
		
	}
	
	/**
     * 根据任务类别 来做任务信息分类统计。
     * @return 任务信息分类统计信息
     */
    public List<Map<String,Object>> selectTaskCountsByTaskKindCode()
    {
    	Map<String, Object> param=new HashMap<String, Object>();
		 param.put( "currentUserId", SecurityContext.getPrincipal().getId() ) ;
        return selectList( "taskCenter.selectTaskCountsByTaskKindCode",param) ;
    }
    
    /**
     * 根据第三方id 和 所属用户id 来定位 taskId
     * @param currentUserId 当前登录用户Id
     * @param clientTxSeq 三方任务唯一标识
     * @return 代办任务信息表实体
     */
    public EntityTaskInfo selectTaskInfoByThirdInfoIdAndBelongUserId(String currentUserId,String clientTxSeq)
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "currentUserId", currentUserId );
        param.put( "clientTxSeq", clientTxSeq ) ;
        return selectOne( "taskCenter.selectTaskInfoByThirdInfoIdAndBelongUserId", param ) ;
    }
    
    /**
     *  查询所有代办任务数据
     * @return 代办任务数据信息集合
     */
    public List<EntityTaskInfo> selectAllAgencyMissions(String currentUserId)
    {
    	Map<String, Object> param=new HashMap<String, Object>();
		param.put( "currentUserId",currentUserId) ;
        return selectList( "taskCenter.selectAllAgencyMissions",param) ;
    }
    
    /**
     * 根据第三方唯一标示集合来先删除已完成信息表中的数据和删除任务接收人表中的数据，后删除任务信息表中的数据。
     * @param thirdIdList 第三方唯一标示集合
     */
    public  void  removeRelatedTaskDatasByThirdIdList(List<String> thirdIdList,Integer status) throws Exception 
   	{
    	//获取所有待删除的任务信息数据集合
    	List<EntityTaskInfo> taskInfoList=loadTaskInfoListByMarkedFlagList(thirdIdList,status);
    	List<String> taskIdList=new ArrayList<String>();
    	for(EntityTaskInfo entity : taskInfoList)
    	{
    		taskIdList.add(entity.getId());
    	}
    	
    	if(taskIdList!=null && taskIdList.size()>0)
    	{
    		//删除已完成信息表中的数据
        	taskFinishedRecordService.remove(taskIdList);
        	//删除任务接收人表中的数据
        	taskReceiverService.remove(taskIdList);	
    	}
    	
    	//删除任务信息表中的数据
    	remove(thirdIdList);
   	}
    
}
