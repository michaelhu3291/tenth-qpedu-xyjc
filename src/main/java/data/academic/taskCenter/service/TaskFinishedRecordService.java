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
import data.academic.taskCenter.entity.EntityTaskFinishedRecordInfo;
import data.academic.taskCenter.entity.EntityTaskInfo;

/**
 *  OA-任务中心-已完成任务信息服务类。
 * 
 * @author JohnXU
 * 
 */
@Service
public class TaskFinishedRecordService extends AbstractService
{
	@Autowired
	private TaskRecordService taskRecordService;
	
	/**
	 * 保存或者更新已完成任务信息
	 * @param entity
	 */
	public void saveOrUpdate(EntityTaskFinishedRecordInfo entity)
	{
		 if(StringUtils.isNotBlank( entity.getId() ) )
	            update("taskFinishedRecord.updateTaskFinishedRecord", entity );
	     else
	            insert("taskFinishedRecord.insertTaskFinishedRecord", entity) ;
	}
    
    /**
     * 分页查询数据（已办任务）。
     * @param taskType 任务种类
     * @param taskStep 任务步骤
     * @param sortField 数据库排序字段
     * @param sort 排序方式（ASC|DESC）
     * @param currentPage 当前页数
     * @param pageSize 页大小
     * @return 分页查询集合
     */
    public PagingResult<Map<String,Object>> searchTaskFinishedRecordList(String title,String dispatchTimeStart,String dispatchTimeEnd,String taskDistribute,String currentUserId,String taskType,String taskStep,String sortField, String sort, int currentPage, int pageSize )
    {
    	HashMap<String,Object> param=new HashMap<String,Object>() ;
    	param.put("taskType", taskType) ;
    	param.put("currentUserId", currentUserId) ;
    	param.put("taskStep", taskStep) ;
    	param.put("title", title) ;
    	param.put("taskDistribute", taskDistribute) ;
    	param.put("dispatchTimeStart", dispatchTimeStart) ;
    	param.put("dispatchTimeEnd", dispatchTimeEnd) ;
        if( StringUtils.isBlank( sortField ) )
            sortField = "FINISHED_TIME";
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging( "taskFinishedRecord.selectHandledTaskPaging", param, sortField, sort, currentPage, pageSize ) ;
    }	
    
    /**
     * 根据任务信息id 来删除完成的任务信息。
     * @param id 任务信息id集合
     * @return 删除完成的任务信息数量
     */
    public  int  remove( List<String> idAry ) throws Exception 
	{
		Map<String, Object> param=new HashMap<String, Object>();
		 param.put( "idAry", idAry ) ;
		return delete("taskFinishedRecord.deleteCompletedTaskInfoByTaskId",param);
	}
    
    
    /**
     * 根据任务类别 来做已办任务分类统计。
     * @return 已办任务信息分类统计信息
     */
    public List<Map<String,Object>> selectCompletedTaskCountsByTaskKindCode()
    {
    	Map<String, Object> param=new HashMap<String, Object>();
		param.put( "currentUserId", SecurityContext.getPrincipal().getId() ) ;
        return selectList( "taskFinishedRecord.selectCompletedTaskCountsByTaskKindCode",param) ;
    }
    
    /**
     *  查询所有已办任务数据
     * @return 已办任务数据信息集合
     */
    public List<Map<String,Object>> selectAllHandledMissions(String currentUserId)
    {
    	Map<String, Object> param=new HashMap<String, Object>();
		param.put( "currentUserId",currentUserId) ;
        return selectList( "taskFinishedRecord.selectAllHandledMissions",param) ;
    }
    
    /**
     * 根据第三方唯一标示集合来先删除已完成信息表中的数据，后删除任务信息表中的数据。
     * @param thirdInfoIdList 第三方唯一标示集合
     * @return 删除信息数据的数量 
     */
    public  void  removeDatasByThirdInfoIdList(List<String> thirdInfoIdList,Integer status) throws Exception 
	{
    	//删除已完成信息表中的数据
    	List<EntityTaskInfo> taskInfoList=taskRecordService.loadTaskInfoListByMarkedFlagList(thirdInfoIdList,status);
    	List<String> taskInfoIdList=new ArrayList<String>();
    	for(EntityTaskInfo entity : taskInfoList)
    	{
    		taskInfoIdList.add(entity.getId());
    	}
    	if(taskInfoIdList!=null && taskInfoIdList.size()>0)
    	{
    		remove(taskInfoIdList);
    	}
    	//除任务信息表中的数据
    	taskRecordService.remove(thirdInfoIdList);
	}
}
