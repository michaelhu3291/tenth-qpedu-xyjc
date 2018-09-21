package data.academic.schoolHoliday.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.academic.schoolHoliday.entity.SchoolCalendar;
import data.framework.support.AbstractService;

/**
 * 学生基本信息service
 * @author wangcheng
 *
 * 2016年4月13日
 */
@Service
public class SchoolHolidayService extends AbstractService{
	
    
    /**
	 * 新增校历信息
	 * @param 
	 */
	public void saveOrUpdate(Map<String,Object> paramMap){
		if( StringUtils.isNotBlank(paramMap.get("mainId").toString()))
        {
            update( "schoolHoliday.updateHoliday", paramMap ) ;
        }
		else{
			insert("schoolHoliday.insertHoliday",paramMap);
		}
	}
	
	
	/**
	 * @title
	 * @param 
	 * @return 校历集合
	 */
    public List<Map<String,Object>> searchHoliday( Map<String,Object> paramMap){
  	   return selectList( "schoolHoliday.searchHoliday",paramMap );
   }
	
    
	/**
	 * @title 加载单个校历修改
	 * @param 
	 * @return 校历集合
	 */
    public List<Map<String,Object>> loadHoliday( Map<String,Object> paramMap){
  	   return selectList( "schoolHoliday.loadHoliday",paramMap );
   }
    
    
    /**
	 * 删除校历节假日
	 * @param name
	 * @return
	 */
	 public int deleteHoliday(String id){
		return delete("schoolHoliday.deleteHoliday",id);
	}
    
	 /**
		 * @title  加载节假日下拉框
		 * @param 
		 * @return 校历节假日集合
		 */
	    public List<Map<String,Object>> getHoliType( Map<String,Object> paramMap){
	  	   return selectList( "schoolHoliday.getHoliType",paramMap );
	   }
	    
	 /**
	  * 添加校历   
	  */
	    public void addHoliday(Map<String,Object> paramMap){
		  	   insert("platformSchool.insertSchoolCalendar", paramMap);
		   }  
	 /**
	  * 修改前查询最新校历
	  */
	    public List<SchoolCalendar> selectEarlyHoliday(){
	    	return selectList("platformSchool.selectEarlyHoliday");
	    }
	    
    /**
	  * 修改校历
	  */
	    public void updateHoliday(Map<String,Object> map){
	    	update("platformSchool.updateSchoolCalendar", map);
	    }
	    
	    /**删除校历
	     * 2016年7月22日
	     * xiahuajun
	     */
	    public void deleteHoliday(Map<String,Object> map){
	    	delete("platformSchool.deleteSchoolCalendar", map);
	    }


	    /**判断学年是否已经存在，能否插入新学年
	     * 2016年8月18日
	     * LBQ
	     */
	    public List<SchoolCalendar> checkBeforeInsert(String schoolYear){
	    	return selectList("platformSchool.checkBeforeInsert", schoolYear);
	    }
	    /**根据选择学年，查询上一学年的寒假结束时间，提示该学年的开始时间
	     * 2016年8月18日
	     * LBQ
	     */
	    public SchoolCalendar getStartTimeBySchoolYear(String tempYear){
	    	return selectOne("platformSchool.getStartTimeBySchoolYear", tempYear);
	    }
	    /**根据选择学年，查询上一学年的寒假结束时间，提示该学年的开始时间
	     * 2016年8月18日
	     * LBQ
	     */
	    public SchoolCalendar loadCalendarById(String id){
	    	return selectOne("platformSchool.loadCalendarById", id);
	    }
	 
}
