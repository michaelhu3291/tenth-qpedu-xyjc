package data.academic.announceManage.service;
/**
 * @author wangchaofa
 * @CreateTime Oct 15,2016
 * @UpdateTime Oct 21,2016
 */
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;

@Service
public class AnnounceService extends AbstractService{

	
	/**
	 * 根据单位code获取前几条通知/公告
	 * 
	 * @param schoolCode
	 * @return 前几条通知/公告
	 */
	public List<Map<String,String>> getTop(Map<String,Object> param){
	    return selectList("announce.getTop",param);
	}
	
	
	
	/**
	 * 根据单位coce获取所有的通知/公告
	 * 
	 * @param schoolCode
	 * @return 所有通知/公告
	 */
	public PagingResult<Map<String, Object>> selectNoticesAnnounces(Map<String,Object> param,String sortField, String sort,int currentPage, int pageSize){
		if( StringUtils.isBlank(sortField)){
			sortField = "Course" ;
		}
        if( StringUtils.isBlank( sort )){
        	sort = "desc" ;
        }
		return selectPaging("announce.selectNoticesAnnounces",param, sortField, sort, currentPage, pageSize);
	}
	
	
	/**
	 * (超级管理员)获取前几条的通知
	 * 
	 * @param publishPersonId
	 * @return 前几条通知
	 */
	public List<Map<String,String>> getNoticeByAdmin(Map<String,Object> param){
		return selectList("announce.getNoticeByAdmin",param);
	}
	
	
	
	/**
	 * (超级管理员)获取前几条的公告
	 * 
	 * @param publishPersonId
	 * @return 前几条公告
	 */
	public List<Map<String,String>> getAnnounceByAdmin(Map<String,Object> param){
		return selectList("announce.getAnnounceByAdmin",param);
	}
	
	
	
	/**
	 * (超级管理员、教研员)获取所有的通知信息
	 * 
	 * @param publishPersonId
	 * @return 所有通知信息
	 */
	public PagingResult<Map<String, Object>> selectNoticesByAdmin(Map<String,Object> param,String sortField, String sort,int currentPage, int pageSize){
		if( StringUtils.isBlank(sortField)){
			sortField = "Course" ;
		}
        if( StringUtils.isBlank( sort )){
        	sort = "desc" ;
        }
		return selectPaging("announce.selectNoticesByAdmin",param, sortField, sort, currentPage, pageSize);
	}
	
	
	
	/**
	 * (超级管理员、教研员)获取所有的公告信息
	 * 
	 * @param publishPersonId
	 * @return 所有公告信息
	 */
	public PagingResult<Map<String, Object>> selectAnnouncesByAdmin(Map<String,Object> param,String sortField, String sort,int currentPage, int pageSize){
		if( StringUtils.isBlank(sortField)){
			sortField = "Course" ;
		}
        if( StringUtils.isBlank( sort )){
        	sort = "desc" ;
        }
		return selectPaging("announce.selectAnnouncesByAdmin",param, sortField, sort, currentPage, pageSize);
	}
	
	
	/**
	 * 得到区级管理员主键id
	 * @param map
	 * @return
	 */
	public String getAdminId(Map<String,Object> map){
		return selectOne("announce.getAdminId", map);
	}
	
	
}
