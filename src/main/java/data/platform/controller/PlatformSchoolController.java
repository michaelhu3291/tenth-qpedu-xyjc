package data.platform.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import data.framework.data.DataTree;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.platform.entity.PlatformSchool;
import data.platform.service.PlatformSchoolService;


@Controller
@RequestMapping( "platform/school" )
public class PlatformSchoolController extends AbstractBaseController
{
    
    @Override
    protected void init( ModelMap model, HttpServletRequest request )
    {
    }

    /**
     * 分页查询
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=search" )
    public void search( @RequestParam("data") String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        boolean isFast = parseBoolean( paramMap.get( "isFast" ) ) ;
        String name = "" ;
        String code = "" ;
        String year = "" ;
        if( isFast ) {
        	name = trimString( paramMap.get( "q" ) ) ;
        }else {
            name = trimString( paramMap.get( "name" ) ) ;
            code = trimString( paramMap.get( "code" ) ) ;
        }
        year = trimString( paramMap.get( "year" ) ) ;
        
        String sortField = parseString( paramMap.get( "sidx" ) ) ;
        String sort = parseString( paramMap.get( "sord" ) ) ;
        PagingResult<PlatformSchool> pagingResult = schoolService.searchDataDictionary( name, code,year, sortField, sort, 1, Integer.MAX_VALUE ) ;
        List<PlatformSchool> list = pagingResult.getRows() ;
        PagingResult<PlatformSchool> newPagingResult = new PagingResult<PlatformSchool>( list, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords() ) ;
        out.print( getSerializer().formatObject( newPagingResult ) ) ;
    }
    
    /**
     * 根据一个实体 ID 来加载数据
     * 命令: "load" ；<br/><br/>
     * @param id 输入参数(由 Browser 端 POST 回来的数据字典编号)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=load" )
    public void load( @RequestParam( "id" ) String id, java.io.PrintWriter out )
    {
    	PlatformSchool entity = schoolService.load( id ) ;
        Map<String,Object> map = getSerializer().parseMap( getSerializer().formatObject( entity ) ) ;
        String parentId = parseString( map.get( "parent" ) ) ;
        List<Map<String,String>> parent = new ArrayList<Map<String,String>>() ;
        if( StringUtils.isNotBlank( parentId ) )
        {
        	PlatformSchool parentEntity = schoolService.load( parentId ) ;
            Map<String,String> parentMap = new HashMap<String,String>() ;
            parentMap.put( "text", parseString( parentEntity.getName() ) ) ;
            parentMap.put( "value", parseString( parentEntity.getId() ) ) ;
            parent.add( parentMap ) ;
        }
        map.put( "parent", parent ) ;
        out.print( getSerializer().formatMap( map ) ) ;
    }
    
    /**
     * 保存数据
     * 命令: "submit" ；<br/><br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=submit" )
    public void submit( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {

        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        PlatformSchool entity = null ;
        String id = trimString( paramMap.get( "id" ) ) ;
        if( StringUtils.isNotBlank( id ) )
        {
            entity = schoolService.load( id ) ;
            entity.setUpdateTime( new Date() );
        }
        else
        {
            entity = new PlatformSchool() ;
            entity.setCreateTime( new Date() );
        }
        entity.setName( parseString( paramMap.get( "name" ) ) ) ;
        entity.setCode( parseString( paramMap.get( "code" ) ) ) ;
        
        Map<String,Object> t =  (Map<String,Object>)paramMap.get("teacherId");
        if(t!=null)
        {
        	entity.setTeacher( t.get("value").toString() );
        }
        
        Map<String,Object> yearMap = (Map<String,Object>)paramMap.get( "year" );
        String year = (yearMap == null || yearMap.isEmpty() ) ? null : parseString( yearMap.get( "value" ) ) ;
        entity.setYear(year) ;
        
        entity.setRemark( parseString( paramMap.get( "remark" ) ) ) ;
        entity.setTeacherAdAccount(parseString(paramMap.get("teacherAdAccount")));
        
        List<Map<String,Object>> parentList = (List<Map<String,Object>>)paramMap.get( "parent" );
        String parentId = ( parentList == null || parentList.isEmpty() ) ? null : parseString( parentList.get( 0 ).get( "value" ) ) ;
        entity.setParent( parentId ) ;
        entity.setGrade("0");
        entity.setTeacherAdAccount(parseString(paramMap.get("teacherAdAccount")));
        schoolService.update(entity);
        out.print( getSerializer().message( "" ) );
    }
    
    /**
     * 根据一个实体 ID 来删除数据
     * 命令: "delete" ；<br/><br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=delete" )
    public void delete( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        schoolService.remove( (List<String>)paramMap.get( "id" )) ;
        out.print( getSerializer().message( "" ) ) ;
    }
    
    /**
     * 获取树形数据的 Web 方法。<br /><br />
     * 命令: "getTree" ；<br/><br/>
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=getTree" )
    public void getTree( java.io.PrintWriter out )
    {
        //获取启用状态的数据字典
        List<DataTree> menuTree = schoolService.getDataDictionaryTree() ;
        out.print( getSerializer().formatList( menuTree ) ) ;
    }

    /**
     * 根据去年数据初始化今年数据
     * 修改4.20
     */
    @RequestMapping( params = "command=initialize" )
    public void init(java.io.PrintWriter out )
    {
    	Calendar myDate = Calendar.getInstance();
    	int currYear = myDate.get(Calendar.YEAR);
    	int lastYear = currYear - 1;
    	//1、删除今年数据
    	schoolService.deleteByYear( String.valueOf(currYear) ) ;
    	//2、查询去年数据
		 PagingResult<PlatformSchool> pagingResult = schoolService.searchDataDictionary( null, null,String.valueOf(lastYear), "", "", 1, Integer.MAX_VALUE ) ;
		 List<PlatformSchool> list = pagingResult.getRows() ;
		 System.out.println(list.size());
		 
		 for( PlatformSchool entity : list ) {
		     entity.setTeacher("");
		     entity.setYear(String.valueOf(currYear));
		     //3、插入生成今年数据
		     schoolService.initialize( entity ) ;
		 }
    }
    
    /**
     * 升级所有在校的学生数据
     * 4.24
     */
    @RequestMapping( params = "command=updateNJ" )
    public void updateNJ(java.io.PrintWriter out )
    {
		 List<PlatformSchool> list = schoolService.searchZaiDuBan() ;//查询在读班级
		 for( PlatformSchool entity : list ) {
			 if(parseInteger(entity.getGrade())>=4)
			 {
				 entity.setGrade("9");
				 String rq = formatDate(new Date(), "yyyy");
				 entity.setRaduationYear(rq);
			 }else{
				 int nj = StringUtils.isNotEmpty(entity.getGrade())?(parseInteger(entity.getGrade())+1):1;
				 entity.setGrade(nj+"");
			 }
			 schoolService.update(entity);
		 }
		 
		
    }
    /**
     * 查询学生学院 学生管理
     * 4.23
     */
	@RequestMapping(params=("command=getCollegeList"))
    public void loadXyList(java.io.PrintWriter out){
    	
    	List<PlatformSchool> schoolList = new ArrayList<PlatformSchool>();
    	schoolList = schoolService.getCollegeList();
    	out.print(getSerializer().formatObject(schoolList));
    	
    }

    /**
     * 下拉框的级联效果，根据选择学院加载专业
     * 4.24
     */
	@RequestMapping(params=("command=getZyBjList"))
    public void loadZy( @RequestParam( "data" ) String data,java.io.PrintWriter out){
    	//获取前台数据
    	Map<String,Object> paramMap=getSerializer().parseMap(data);
    	String Name=trimString(paramMap.get("Name"));
    	
    	
    	//先根据这个学院名称查出他的PID
    	String pId = schoolService.getpIdByName(Name);
    	
    	//根据year查询学院（parent属性为空）
    	 List<PlatformSchool> schoolList = schoolService.getZBList(pId);
    	
    	
    	out.print(getSerializer().formatObject(schoolList));
    }

     
    @Autowired
    private PlatformSchoolService schoolService ;
}