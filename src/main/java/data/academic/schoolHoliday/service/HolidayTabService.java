package data.academic.schoolHoliday.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;

@Service
public class HolidayTabService extends AbstractService{
	
	/**
     * 分页查询未审批的数据
     * @param operatorType 操作类型
     * @param module 模式
     * @param sortField 数据库排序字段
     * @param sort 排序方式（ASC|DESC）
     * @param currentPage 当前页数
     * @param pageSize 页大小
     * @return 分页查询集合
     */
    public PagingResult<Map<String,Object>> serachPaging(Map<String,Object> param, String sortField, String sort, int currentPage, int pageSize )
    {
        if( StringUtils.isBlank( sortField ) )
            sortField = "CREATETIME" ;
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging( "holidayTab.selectPaging", param, sortField, sort, currentPage, pageSize ) ;
    }
    
    /**
	 * 新增或修改节假日维护内容
	 * @param 
	 */
	public void saveOrUpdate(Map<String,Object> map){
		if( StringUtils.isNotBlank( map.get("holiId").toString() ) )
            update( "holidayTab.updateHolidayTab", map ) ;
        else
            insert( "holidayTab.insertHolidayTab", map ) ;
	}
	
	
	/**
	 * @title 加载单个节日维护点修改
	 * @param 
	 * @return 节日点集合
	 */
    public List<Map<String,Object>> loadHolidayTab( Map<String,Object> paramMap){
  	   return selectList( "holidayTab.loadHolidayTab",paramMap );
   }
	
	
	/**
     * 根据ID批量删除节假日维护信息。
     * @param id 数据字典集合
     * @return 删除记录数
     */
    public int remove( List<String> idAry )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "idAry", idAry ) ;
        return delete( "holidayTab.deleteHolidayTab", param ) ;
    }
	
    /**
     *根据父节点加载子节点的数据字典信息
     *
     */
    public List<Map<String,String>> loadChildDictionary(String parentDictionary){
  	  HashMap<String,String> param=new HashMap<String,String>();
  	  param.put("parentDictionary",parentDictionary);
  	  return selectList( "schoolHoliday.loadChildDictionary",param);
   }
    
    /**
     * @Title: selectIdByCode
     * @Description: 通过父节点code查询父节点的id
     * @author xiahuajun
     * @date 2016年8月15日 
     * @param id
     * @return
     * @return String
     */
	public Map<String,Object>  selectIdByCode(String code) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("code",code);
		return selectOne( "schoolHoliday.selectParentIdByCode",map);
	}
    
}
