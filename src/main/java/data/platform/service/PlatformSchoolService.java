package data.platform.service;

import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;
import org.apache.commons.lang.StringUtils ;
import org.springframework.stereotype.Service ;
import data.framework.data.DataTree ;
import data.framework.pagination.model.PagingResult ;
import data.framework.support.AbstractService ;
import data.platform.entity.PlatformSchool ;

@Service
public class PlatformSchoolService extends AbstractService
{ 
	
    /*
     * 初始化
     * 4.20(修改)
     * 
     *    
     */
	public void initialize(PlatformSchool entity){
            insert( "platformSchool.initialize", entity ) ;
	}
	/*更新 用于升级功能
	 * 4.24
	 * 
	 * 
	 * 
	 * 
	 * */
	public void update( PlatformSchool entity )
    {
        if( StringUtils.isNotBlank( entity.getId() ) )
            update( "platformSchool.update", entity ) ;
        else
            insert( "platformSchool.insert", entity ) ;
    }
    
    public PlatformSchool load( String id )
    {
        Map<String,String> param = new HashMap<String,String>() ;
        param.put( "id", id ) ;
        return selectOne( "platformSchool.load", param ) ;
    }
    
    public int remove( List<String> idAry )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "idAry", idAry ) ;
        return delete( "platformSchool.delete", param ) ;
    }
    
    public int deleteByYear( String year )
    {
    	Map<String,Object> param = new HashMap<String,Object>() ;
    	param.put( "year", year ) ;
    	return delete( "platformSchool.deleteByYear", param ) ;
    }
    
    
    public PagingResult<PlatformSchool> searchDataDictionary( String dictionaryName, String dictionaryCode,String year, String sortField, String sort, int currentPage, int pageSize )
    {
        Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "name", dictionaryName ) ;
        param.put( "code", dictionaryCode ) ;
        param.put( "year", year ) ;
        if( StringUtils.isBlank( sortField ) )
            sortField = "serial" ;
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging( "platformSchool.selectPaging", param, sortField, sort, currentPage, pageSize ) ;
    }
    
    
    public List<DataTree> getDataDictionaryTree()
    {
        return selectList( "platformSchool.selectTreeDataDictionary" ) ;
    }
    
    
    public List<DataTree> selectChildDataDictionary(String id)
    {
        return selectList( "platformSchool.selectChildDataDictionary" ,id) ;
    }
    
    
    public List<DataTree> getDataDictionaryTreeByParentCode(String parentCode){
      return selectList("platformSchool.selectTreeDataDictionaryByParentCode",parentCode);
    }
    
    
    
   public PlatformSchool selectDictionaryValueByCode(String dictionaryCode){
       return selectOne("platformSchool.selectDictionaryValueByCode",dictionaryCode);
   }

/**
 * 根据name获取year的学院、专业、班级对应的code
 * @param code 班级，专业，学院 code
 * @param year 入学级
 * @return String
 * @author 刘国典
 * */ 
 public String getNameByCode(String code,String year){
	   Map<String,String> param = new HashMap<String, String>();
	   param.put("code", code);
	   param.put("year", year);
	   PlatformSchool school = selectOne("platformSchool.loadBycode",param);
	   return school.getName();
 }

	//   根据name获取year的学院、专业、班级对应的pId   ——lbq
	   public String getpIdByName(String name,String year){
		   Map<String,String> param = new HashMap<String, String>();
		   param.put("name", name);
		   param.put("year", year);
		   PlatformSchool school = selectOne("platformSchool.loadByName",param);
		   return school.getpId();
	   }

	 //根据BJID获取专业
	 public PlatformSchool getschool(String id){
		 return selectOne("platformSchool.load",id);
		   
	 }
	//根据当前year获取新生学院集合        ——lbq
	public List<PlatformSchool> getXyList(String year){
		  List<PlatformSchool> schoolList = selectList("platformSchool.loadXyList",year);
		   return schoolList;
	}
	//根据当前year和pId获取新生专业或班级集合        ——lbq
		public List<PlatformSchool> getZyBjList(String year,String pId){
			Map<String,String> param = new HashMap<String, String>();
			   param.put("year", year);
			   param.put("pId", pId);
			  List<PlatformSchool> schoolList = selectList("platformSchool.getZyBjList",param);
			   return schoolList;
		}
		
		/**
		 * 根据code和year取得专业信息
		 * 
		 * @param code
		 * @param year
		 * @return PlatformSchool
		 */
		public PlatformSchool getInfoByCodeAndYear(String code, String year) {
			Map<String, String> param = new HashMap<String, String>();
			param.put("code", code);
			param.put("year", year);
			PlatformSchool school = selectOne("platformSchool.loadBycode", param);
			return school;
		}
		
		// 根据code获取year的学院、专业、班级对应的name
		public String getCodeByName(String name, String year) {
			Map<String, String> param = new HashMap<String, String>();
			param.put("name", name);
			param.put("year", year);
			PlatformSchool school = selectOne("platformSchool.loadByName", param);
			return school.getCode();
		}
		
		
		
   /*
    * 查询学院集合 学生管理
    * 4.23
    * */		
	public List<PlatformSchool> getCollegeList(){
			  List<PlatformSchool> schoolList = selectList("platformSchool.loadCollegeListList");
			   return schoolList;
		}
   /*
    * 根据name获取学院、专业、班级对应的pId
    *  4.23
    * 
    * */  
	   public String getpIdByName(String name){
		   Map<String,String> param = new HashMap<String, String>();
		   param.put("name", name);
		   
		   List<PlatformSchool> schools = selectList("platformSchool.loadBZ",param);
		   if(schools!=null && schools.size()>0)
		   {
			   return schools.get(0).getpId();
		   }else{
			   return null;	
		   }
		   
	   }
	 /*
	    * 查询专业班级集合 学生管理
	    * 4.23
	    * */
	public List<PlatformSchool> getZBList(String pId){
		   Map<String,String> param = new HashMap<String, String>();  
		   param.put("pId", pId);
		   return selectList("platformSchool.getZBList",param);
	}		
 
 public List<PlatformSchool> searchZaiDuBan()
 {
     return selectList( "platformSchool.searchZaiDuBan") ;
 }
}