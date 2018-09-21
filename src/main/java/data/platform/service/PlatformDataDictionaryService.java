package data.platform.service;

import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;

import org.apache.commons.lang.StringUtils ;
import org.springframework.stereotype.Service ;

import data.framework.data.DataTree ;
import data.framework.pagination.model.PagingResult ;
import data.framework.support.AbstractService ;
import data.platform.entity.EntityPlatformDataDictionary ;
/**
 * 平台-数据字典服务类。
 * @author wanggq
 *
 */
@Service
public class PlatformDataDictionaryService extends AbstractService
{
    /**
     * 保存或更新数据字典信息。
     * @param entity 数据字典实体
     * @throws Exception
     */
    public void saveOrUpdate( EntityPlatformDataDictionary entity )
    {
        if( StringUtils.isNotBlank( entity.getId() ) )
            update( "platformDataDictionary.updateDataDictionary", entity ) ;
        else
            insert( "platformDataDictionary.insertDataDictionary", entity ) ;
    }
    
    /**
     * 根据数据字典ID获取数据字典信息。
     * @param id 数据字典ID
     * @return 数据字典实体
     */
    public EntityPlatformDataDictionary load( String id )
    {
        Map<String,String> param = new HashMap<String,String>() ;
        param.put( "id", id ) ;
        return selectOne( "platformDataDictionary.loadDataDictionary", param ) ;
    }
    
    /**
     * 根据数据字典ID删除数据字典信息。
     * @param id 数据字典集合
     * @return 删除记录数
     */
    public int remove( List<String> idAry )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "idAry", idAry ) ;
        return delete( "platformDataDictionary.deleteDataDictionary", param ) ;
    }
    
    /**
     * 分页查询数据。
     * @param dictionaryName 字典名称
     * @param dictionaryCode 字典代码
     * @param sortField 数据库排序字段
     * @param sort 排序方式（ASC|DESC）
     * @param currentPage 当前页数
     * @param pageSize 页大小
     * @return 分页查询集合
     */
    public PagingResult<EntityPlatformDataDictionary> searchDataDictionary( String dictionaryName, String dictionaryCode, String sortField, String sort, int currentPage, int pageSize )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "dictionaryName", dictionaryName ) ;
        param.put( "dictionaryCode", dictionaryCode ) ;
        if( StringUtils.isBlank( sortField ) )
            sortField = "serial" ;
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging( "platformDataDictionary.selectPaging", param, sortField, sort, currentPage, pageSize ) ;
    }
    
    /**
     * 获取数据字典树形结构数据。
     * @return 树形结构数据集合
     */
    public List<DataTree> getDataDictionaryTree()
    {
        return selectList( "platformDataDictionary.selectTreeDataDictionary" ) ;
    }
    
    /**
     * 获取数据字典树形结构数据。
     * @return 树形结构数据集合
     */
    public List<DataTree> selectChildDataDictionary(String id)
    {
        return selectList( "platformDataDictionary.selectChildDataDictionary" ,id) ;
    }
    
    /**
     *
     * @param parentCode 父节点的code值
     * @return 树形结构数据集合
     */
    public List<DataTree> getDataDictionaryTreeByParentCode(String parentCode){
      return selectList("platformDataDictionary.selectTreeDataDictionaryByParentCode",parentCode);
    }
    
    
    /**
    *
    * @param dictionaryCode 数据字典的code值
    * @return 对应的数据字典数据
    */
   public EntityPlatformDataDictionary selectDictionaryValueByCode(String dictionaryCode){
       return selectOne("platformDataDictionary.selectDictionaryValueByCode",dictionaryCode);
   }

   
	
	/**
	 * 根据父节点code加载子节点
	 * @param code 父字典code
	 * @return 
	 */
	public List<Map<String,Object>> loadChildCode(String codes){
		return selectList("platformDataDictionary.selectByParentCode",codes);
	}
	
   
	/**
	 * 根据父节点code加载子节点
	 * @param code 父字典code
	 * @return 
	 */
	public List<Map<String,Object>> loadChiadNode(Map<String,Object> param){
		return selectList("platformDataDictionary.loadChiadNode1",param);
	}
	
	/**
	 * @Title: getCoursesBySchoolType
	 * @Description: 通过学校类型code关联科目
	 * @author xiahuajun
	 * @date 2016年8月9日 
	 * @param schoolType
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getCoursesBySchoolType(String schoolType) {
		return selectList("platformDataDictionary.selectCoursesBySchoolCode", schoolType);
	}
	
	/**
	 * @Title: getCoursesByGrade
	 * @Description: 通过年级code关联科目
	 * @author xiahuajun
	 * @date 2016年8月25日 
	 * @param schoolType
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getCoursesByGrade(String schoolType) {
		return selectList("platformDataDictionary.selectCoursesByGradeCode", schoolType);
	}
	
	/**
	 * @Title: getGradesBySchoolType
	 * @Description: 通过学校类型code关联年级
	 * @author xiahuajun
	 * @date 2016年8月24日 
	 * @param schoolType
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getGradesBySchoolType(String schoolType) {
		return selectList("platformDataDictionary.selectGradessBySchoolCode", schoolType);
	}
	
	/**
	 * @Title: getSchoolYear
	 * @Description: 加载数据字典
	 * @author xiahuajun
	 * @date 2016年8月9日 
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getDataDictionary(List<String> idAry) {
		 Map<String,Object> param = new HashMap<String,Object>() ;
	     param.put( "idAry", idAry ) ;
		 return selectList("platformDataDictionary.selectDataDictionary",param);
	}
	
	/**
	 * @Title: getSchoolsBySchoolType
	 * @Description: 通过学校类型code关联学校
	 * @author xiahuajun
	 * @date 2016年8月26日 
	 * @param schoolTypeSequence
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getSchoolsBySchoolType(String schoolTypeSequence) {
		return selectList("platformDataDictionary.selectSchoolsBySchoolCode", schoolTypeSequence);
	}
	
	/**
	 * @Title: selectSchoolsBySchoolType
	 * @Description: 根据学校类型集合得到学校
	 * @author zhaohuanhuan
	 * @date 2016年11月8日 
	 * @param params
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> selectSchoolsBySchoolType(Map<String, Object> params) {
		return selectList("platformDataDictionary.selectSchoolsBySchoolType", params);
	}
	
}