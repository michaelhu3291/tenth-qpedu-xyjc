package data.academic.taskCenter.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;
import data.academic.taskCenter.entity.EntityTaskKindInfo;

@Service
public class TaskKindInfoService extends AbstractService 
{
	 /**
     * 分页查询数据。
     * @param sortField 数据库排序字段
     * @param sort 排序方式（ASC|DESC）
     * @param currentPage 当前页数
     * @param pageSize 页大小
     * @return 分页查询集合
     */
	 public PagingResult<EntityTaskKindInfo> search(String appCode, String sortField, String sort, int currentPage, int pageSize )
	 {
	    	HashMap<String,Object> param=new HashMap<String,Object>();
	    	param.put("appCode", appCode);
	        if( StringUtils.isBlank( sortField ) )
	            sortField = "CREATE_TIME";
	        if( StringUtils.isBlank( sort ) )
	            sort = "ASC" ;
	        return selectPaging( "taskKind.selectTaskKindPaging", param, sortField, sort, currentPage, pageSize ) ;
	 }	
	 /**
     * 添加和修改
     * @param data
     * @param out
     */
	public void saveOrupdate(EntityTaskKindInfo entity )
	{
		if(StringUtils.isNotBlank(entity.getId()))
			  update("taskKind.updateTaskKind", entity );
		else
	          insert("taskKind.insertTaskKind", entity) ;
	}
	  
    /**
     * 根据id查询
     * @param id 个人日程信息id
     * @return 个人日程信息
     */
	public EntityTaskKindInfo load(String id)
	{	
		Map<String, String> param=new HashMap<String, String>();
		param.put( "id", id ) ;
		return selectOne("taskKind.TaskKindload",param);
	}
	 /**
	 * 根据id删除信息
	 * @param id 
	 * @return 删除记录数
	 * @throws Exception
	 */
	public  int  remove( List<String> idAry ) 
	{
		 Map<String,Object> param = new HashMap<String,Object>() ;
	     param.put( "idAry", idAry ) ;
		 return delete("taskKind.deleteTaskKind",param);
	}
	
	public String loadTaskKindNameByCode(String taskKindCode)
	{	
		Map<String, String> param=new HashMap<String, String>();
		param.put( "taskKindCode", taskKindCode ) ;
		return selectOne("taskKind.loadTaskKindNameByCode",param);
	}

}
