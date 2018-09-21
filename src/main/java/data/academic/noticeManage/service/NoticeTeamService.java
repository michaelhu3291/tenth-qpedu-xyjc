package data.academic.noticeManage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import data.framework.support.AbstractService;
import data.academic.noticeManage.entity.EntityAnnouncementSchoolCode;

@Service
public class NoticeTeamService extends AbstractService {
	/**
     * 保存或更新公告与学校关系
     * 
     * @param entity 公告实体
     */
    public void saveOrUpdate( EntityAnnouncementSchoolCode entity )
    {
            insert( "noticeTeam.insert", entity ) ;
    }

    /**
     * 根据公告id删除公告与学校关系
     * 
     * @param id 公告编号
     * @return 删除记录数
     */
    public int remove(List<String> id)
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "idAry", id ) ;
        return delete( "noticeTeam.delete", param ) ;
    }
    /**
     * 根据用户id查询对应的公告关系
     * 
     * @param id 用户编号
     * @return 公告ID
     */
    public List<EntityAnnouncementSchoolCode> search(String id){
    	Map<String,String> param = new HashMap<String,String>() ;
    	param.put("userId", id);
    	return selectList("noticeTeam.selectAnnouncementID", param);
    }
}
