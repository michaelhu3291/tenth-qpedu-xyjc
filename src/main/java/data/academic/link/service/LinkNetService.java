package data.academic.link.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.academic.link.entity.EntityLinkNet;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;
@Service
public class LinkNetService extends AbstractService {
	
	/**添加链接
	 * 2016年7月16日
	 * xiahuajun
	 */
	public void addLink(Map<String,Object> map){
		insert("linkNet.insertLink", map);
	}
	
	/**分页查询链接
	 * 2016年7月21日
	 * xiahuajun
	 */
	public PagingResult<Map<String,Object>> serachLink(Map<String,Object> param, String sortField, String sort, int currentPage, int pageSize ){
        if( StringUtils.isBlank( sortField ) )
            sortField = "Name" ;
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging( "linkNet.selectLinkPaging", param, sortField, sort, currentPage, pageSize ) ;
    }
	
	/**查询链接
	 * 2016年7月16日
	 * xiahuajun
	 */
	public List<String> searchLink(Map<String,Object> map){
		return selectList("linkNet.selectLink", map);
	}
	
	/**删除链接
	 * 2016年7月22日
	 * xiahuajun
	 */
	public int remove( List<String> idAry )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "idAry", idAry ) ;
        return delete("linkNet.deleteLink", param );
    }
	
	/**根据id把链接信息查出来
	 * 2016年7月22日
	 * xiahuajun
	 */
	public EntityLinkNet selectLinkById(String id)
	    {
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("id", id);
	        return selectOne( "linkNet.selectLinkById", param ) ;
	    }
	
	/**修改链接
	 * 2016年7月24日
	 * xiahuajun
	 */
	public void updateLinkById(Map<String,Object> map)
    {
        update("linkNet.updateLinkById", map);
    }

}
