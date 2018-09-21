package data.platform.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import data.framework.httpClient.CommonInterFace;
import data.framework.httpClient.HttpUtil;
import data.framework.httpClient.RestResponse;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.framework.utility.Util;
import data.platform.authority.security.SecurityContext;
import data.platform.entity.EntityPlatformMenu;
import data.platform.entity.EntityPlatformRole;
import data.platform.service.PlatformMenuService;
import data.platform.service.PlatformRoleService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 平台-角色控制器类。
 * @author wanggq
 */
@Controller
@RequestMapping( "authority/authority" )
public class PlatformRoleController extends AbstractBaseController
{
    private final static int MAX_INSERT = 500 ;
    @Override
    protected void init( ModelMap model, HttpServletRequest request )
    {
    }
    
    /**
     * 分页查询角色信息的方法。
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     * @throws UnsupportedEncodingException 
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=search" )
    public void search( @RequestParam("data") String data, java.io.PrintWriter out ) throws UnsupportedEncodingException
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        boolean isFast = parseBoolean( paramMap.get( "isFast" ) ) ;
        String roleName = "" ;
        Integer status = null ;
        if( isFast )
            roleName = trimString( paramMap.get( "q" ) ) ;
        else
        {
            roleName = trimString( paramMap.get( "roleName" ) ) ;
            Map<String,Object> statusData = (Map<String,Object>)paramMap.get( "status" );
            status = parseInteger( statusData == null ? null : statusData.get( "value" ) ) ;
        }
        int currentPage = parseInteger( paramMap.get( "page" ) ) ;
        int pageSize = parseInteger( paramMap.get( "rows" ) ) ;
        String sortField = trimString( paramMap.get( "sidx" ) ) ;
        String sort = trimString( paramMap.get( "sord" ) ) ;
        Object newPagingResult = roleService.searchBUARoles( roleName, status, sortField, sort, currentPage, pageSize ) ;
        
         out.print( getSerializer().formatObject( newPagingResult ) ) ;
    }
    
    /**
     * 根据一个实体 ID 来加载角色详细信息的 Web 方法。<br /><br />
     * 命令: "load" ；<br/><br/>
     * @param id 输入参数(由 Browser 端 POST 回来的角色编号)
     * @param out 响应输出对象
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping( params = "command=load" )
    public void load( @RequestParam( "id" ) String id, java.io.PrintWriter out ) throws UnsupportedEncodingException
    {
    	EntityPlatformRole role = roleService.load( id ) ;
    	System.out.println("======"+role);
        out.print( getSerializer().formatObject( role ) ) ;
    }
    
    /**
     * 获取teacher的名字
     * 4.20
     */
   /* @RequestMapping( params = "command=loadTeacher" )
    public void loadTeacher( @RequestParam( "name" ) String name, java.io.PrintWriter out )throws UnsupportedEncodingException
    {
        Map<String,Object> param = new HashMap<String, Object>();
        param.put("roleName", "辅导员");
        param.put("name",name);
    	List<Map<String, Object>> result = roleService.selectRoleUser(param);
        out.print( getSerializer().formatObject( result) );
    }*/
    @RequestMapping( params = "command=loadTeacher" )
    public void loadTeacher( @RequestParam( "term" ) String term, java.io.PrintWriter out ) throws UnsupportedEncodingException
    {
        /*term = new String( term.getBytes( "iso-8859-1" ),"UTF-8" ) ;//解析前台传过来的参数
        PagingResult<Map<String,String>> pagingResult = roleService.searchRolesName(term, null, null, null,1, 10);      
        List<Map<String,String>> resultList = pagingResult.getRows() ;*/
        Map<String,Object> param = new HashMap<String, Object>();
        param.put("roleName", "辅导员");
        param.put("name",term);
    	List<Map<String, Object>> result = roleService.selectRoleUser(param);
    	
        out.print( getSerializer().formatList( result ) ) ;
      
    }
    
    
    /**
     * 保存角色信息的Web方法。<br /><br />
     * 命令: "submit" ；<br/><br/>
     * @param data 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=submit" )
    public void submit( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
    	System.out.println("===========进入保存");
    	System.out.println("=========data打印"+data);
    	Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
    	paramMap.put("appId", "63594C80-63AA-4725-BFF5-1FDB61380D11");
    	
        System.out.println("=========="+paramMap);
        EntityPlatformRole entity = null ;
        String roleId = trimString( paramMap.get( "roleId" ) ) ;
        /* if( StringUtils.isNotBlank( id ) )
        {
            entity = roleService.load( id ) ;
            entity.setUpdateTime( new Date() );
        }
        else
        {
            entity = new EntityPlatformRole() ;
            entity.setCreateTime( new Date() );
        }
        entity.setRoleName( parseString( paramMap.get( "roleName" ) ) ) ;
        entity.setRoleCode(parseString(paramMap.get( "roleCode" )));
        entity.setRemark( parseString( paramMap.get( "remark" ) ) ) ;
        entity.setRoleType( "业务角色" ) ;
        System.out.println("=========="+entity);*/
        try {
			roleService.saveOrUpdate( paramMap,roleId ) ;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        out.print( getSerializer().message( "" ) ) ;
    }
    
    /**
     * 根据一个角色 ID 来删除角色实体信息的 Web 方法。<br /><br />
     * 命令: "delete" ；<br/><br/>
     * @param date 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=delete" )
    public void delete( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        System.out.println("========="+paramMap);
        List<String> idAry = (List<String>)paramMap.get("ids");
        roleService.remove( idAry) ;
        out.print( getSerializer().message( "" ) ) ;
    }
    
    /**
     * 加载系统所有功能权限信息的 Web 方法。<br /><br />
     * 命令: "initFunctions" ；<br/><br/>
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=initFunctions" )
    public void initFunctions( java.io.PrintWriter out )
    {
        PagingResult<EntityPlatformMenu> menuResult = menuService.searchMenus( null, null, 1, null, null, 1, Integer.MAX_VALUE ) ;
        out.print( getSerializer().formatList( menuResult.getRows() ) ) ;
    }
    
    /**
     * 加载角色对应的功能权限信息的 Web 方法。<br /><br />
     * 命令: "getFunctions" ；<br/><br/>
     * @param roleId 输入参数(角色id)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=getFunctions" )
    public void getFunctions( @RequestParam( "roleId" ) String roleId, java.io.PrintWriter out )
    {
        List<Map<String,String>> list = roleService.selectRoleFunctions( roleId ) ;
        out.print( getSerializer().formatList( list ) ) ;
    }
    
    /**
     * 保存角色对应的功能权限信息的 Web 方法。<br /><br />
     * 命令: "saveFunctions" ；<br/><br/>
     * @param date 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=saveFunctions" )
    public void saveFunctions( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        String roleId = parseString( paramMap.get( "roleId" ) ) ;
        List<Map<String,String>> functionList = (List<Map<String,String>>)paramMap.get( "functions" ) ;
        roleService.saveRoleFunctions( roleId, functionList, "" ) ;
        out.print( getSerializer().message( "" ) ) ;
    }
    
    
    /**
     * 保存角色对应的数据权限信息的 Web 方法。<br /><br />
     * 命令: "saveDatas" ；<br/><br/>
     * @param date 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( params = "command=saveDatas" )
    public void saveDatas( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        String roleId = parseString( paramMap.get( "roleId" ) ) ;
        List<String> dataAuthorityList = (List<String>)paramMap.get( "datas" ) ;

        int totalCount = dataAuthorityList.size() ;
        int totalPackage = ( totalCount / MAX_INSERT ) + ( ( totalCount % MAX_INSERT == 0 ) ? 0 : 1 ) ;
        List<List<String>> packageList = new ArrayList<List<String>>( totalPackage ) ;
        
        int rowIndex = 0 ;
        int packageIndex = 0 ;
        for( int i = 0; i < dataAuthorityList.size(); ++i )
        {
            if( rowIndex == 0 )
            {
                packageList.add( new ArrayList<String>() ) ;
            }
            packageList.get( packageIndex ).add( dataAuthorityList.get( i ) ) ;
            rowIndex++ ;

            if( rowIndex > MAX_INSERT - 1 )
            {
                rowIndex = 0 ;
                packageIndex++ ;
            }
        }

        roleService.deleteRoleDataAuthoritys( roleId ) ;
        for( List<String> packageItem : packageList )
        {
            roleService.saveRoleDataAuthoritys( roleId, packageItem, "" ) ;
        }

        Map<String,Object> resultMap = new HashMap<String,Object>() ;
        resultMap.put( "message", "保存成功"  ) ;
        out.print( getSerializer().formatMap( resultMap ) ) ;
    }
    
    
    /**
     * 根据角色ID查找用户List
     */
    @RequestMapping( params = "command=selectUsersByRoleId" )
    public void selectUsersByRoleId(@RequestParam("data") String data,HttpServletRequest request,java.io.PrintWriter out )
    {
    	String roleId = request.getParameter("roleId"); 
    	Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        int currentPage = parseInteger( paramMap.get( "page" ) ) ;
        int pageSize = parseInteger( paramMap.get( "rows" ) ) ;
        String sortField = trimString( paramMap.get( "sidx" ) ) ;
        String sort = trimString( paramMap.get( "sord" ) ) ;
        paramMap.put("roleId", roleId);
        Object newPagingResult = null;
		try {
			newPagingResult = roleService.selectUsersByRoleId(roleId, sortField, sort, currentPage, pageSize );
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         out.print( getSerializer().formatObject( newPagingResult ) ) ;
    }
    
    
    /**
     * 根据角色ID查找组织List
     */
    @RequestMapping( params = "command=selectOrgsByRoleId" )
    public void selectOrgsByRoleId(@RequestParam("data") String data,HttpServletRequest request,java.io.PrintWriter out )
    {
    	String roleId = request.getParameter("roleId");
    	Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        int currentPage = parseInteger( paramMap.get( "page" ) ) ;
        int pageSize = parseInteger( paramMap.get( "rows" ) ) ;
        String sortField = trimString( paramMap.get( "sidx" ) ) ;
        String sort = trimString( paramMap.get( "sord" ) ) ;
        paramMap.put("roleId", roleId);
        Object newPagingResult = null;
		try {
			newPagingResult = roleService.selectOrgsByRoleId(roleId, sortField, sort, currentPage, pageSize );
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         out.print( getSerializer().formatObject( newPagingResult ) ) ;
    }
    
    
    /**
	 * @Title: searchPaging
	 * @Description: 查询角色下所有的组织code
	 * @author xiahuajun
	 * @date 2016年9月20日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=selectAllOrgsByRoleId")
	public void selectAllSchools(@RequestParam("data") String data,
			java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String school_sequence = "";
		String school_sequence1="";
		String schoolCode = "";
		if("xx".equals(requestMap.get("schoolType").toString())){
			school_sequence = "0";
		}
		else if("cz".equals(requestMap.get("schoolType").toString())){
			school_sequence = "1";
		}
		else if("ygz".equals(requestMap.get("schoolType").toString())){
			school_sequence = "5";
		}
		else if("wz".equals(requestMap.get("schoolType").toString())){
			school_sequence = "4";
			schoolCode = "3004";
		}
		else if("gz".equals(requestMap.get("schoolType").toString())){
			school_sequence1 = "gz";
			schoolCode = "3004";
		}
		
		requestMap.put("school_sequence", school_sequence);
		requestMap.put("school_sequence1", school_sequence1);
		requestMap.put("schoolCode", schoolCode);
		List<Map<String,Object>> list = roleService.selectAllOrgs(requestMap);
		out.print(getSerializer().formatList(list));
	}
	
    /**
     * 根据角色ID查找岗位List
     */
    @RequestMapping( params = "command=selectPostsByRoleId" )
    public void selectPostByRoleId(@RequestParam("data") String data,HttpServletRequest request,java.io.PrintWriter out )
    {
    	String roleId = request.getParameter("roleId");
    	Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        int currentPage = parseInteger( paramMap.get( "page" ) ) ;
        int pageSize = parseInteger( paramMap.get( "rows" ) ) ;
        String sortField = trimString( paramMap.get( "sidx" ) ) ;
        String sort = trimString( paramMap.get( "sord" ) ) ;
        paramMap.put("roleId", roleId);
        Object newPagingResult = null;
		try {
			newPagingResult = roleService.selectPostByRoleId(roleId, sortField, sort, currentPage, pageSize );
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         out.print( getSerializer().formatObject( newPagingResult ) ) ;
    }
    
    /**
     * 根据角色ID删除关联的用户映射
     * 添加新的映射
     */
    @RequestMapping( params = "command=makeRoleUserMapping" )
    public void makeRoleUserMapping( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
    	Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        String roleId = parseString( paramMap.get( "roleId" ) ) ;
        String userIds = parseString( paramMap.get( "userIds" ) ) ;
    	String[] s = userIds.split(",");
    	this.roleService.roleUserMapping(roleId,s);
    }
    
    /**
     * 根据一个角色 ID 来删除用户实体信息的 Web 方法。<br /><br />
     * 命令: "delete" ；<br/><br/>
     * @param date 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=deleteRoleUser" )
    public void deleteRoleUser( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
    	Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        System.out.println("========="+paramMap);
        List<String> idAry = (List<String>)paramMap.get("userIds");
        String roleId=trimString( paramMap.get( "roleId" ) ) ;
        roleService.removeRoleUser( idAry,roleId) ;
        out.print( getSerializer().message( "" ) ) ;
    }
    
    /**
     * 根据一个角色 ID 来删除组织实体信息的 Web 方法。<br /><br />
     * 命令: "delete" ；<br/><br/>
     * @param date 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=deleteRoleOrg" )
    public void deleteRoleOrg( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        
    	Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        System.out.println("========="+paramMap);
        List<String> idAry = (List<String>)paramMap.get("userIds");
        String roleId=trimString( paramMap.get( "roleId" ) ) ;
        roleService.removeRoleOrg( idAry,roleId) ;
        out.print( getSerializer().message( "" ) ) ;
    }
    
    /**
     * 根据一个角色 ID 来删除岗位实体信息的 Web 方法。<br /><br />
     * 命令: "delete" ；<br/><br/>
     * @param date 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=deleteRolePost" )
    public void deleteRolePost( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
    	Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        System.out.println("========="+paramMap);
        List<String> idAry = (List<String>)paramMap.get("userIds");
        String roleId=trimString( paramMap.get( "roleId" ) ) ;
        roleService.removeRolePost( idAry,roleId) ;
        out.print( getSerializer().message( "" ) ) ;
    }
    
    /**
     * 根据一个角色 ID 来添加用户实体信息的 Web 方法。<br /><br />
     * 命令: "insert" ；<br/><br/>
     * @param date 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=setUser" )
    public void insertRoleUser( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
    	Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        System.out.println("========="+paramMap);
        List<String> userList =(List<String>)paramMap.get("roleUserIds");
        String roleId=trimString( paramMap.get( "roleId" ) ) ;
        roleService.insertRoleUser( userList,roleId) ;
        out.print( getSerializer().message( "" ) ) ;
    }
    
    /**
     * 根据一个角色 ID 来添加用户实体信息的 Web 方法。<br /><br />
     * 命令: "insert" ；<br/><br/>
     * @param date 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=setOrg" )
    public void insertRoleOrg( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
    	Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        System.out.println("========="+paramMap);
        List<String> orgList =(List<String>)paramMap.get("roleOrgIds");
        String roleId=trimString( paramMap.get( "roleId" ) ) ;
        roleService.insertRoleOrg( orgList,roleId) ;
        out.print( getSerializer().message( "" ) ) ;
    }
    
    /**
     * 根据一个学校Code 来加载用户实体信息的 Web 方法。<br /><br />
     * 命令: "select" ；<br/><br/>
     * @param date 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=selectAllUsersBySchoolCode" )
    public void selectAllUsersBySchoolCode( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
    	Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
    	String schoolCode=paramMap.get("schoolCode").toString();
    	if(schoolCode!=null){
    		paramMap.put("schoolCode", schoolCode);
    	}
    	List<Map<String,Object>> list = roleService.selectAllUsersBySchoolCode(paramMap);
		out.print(getSerializer().formatList(list));
       /* System.out.println("========="+paramMap);
        List<String> idAry = (List<String>)paramMap.get("ids");
        String roleId=trimString( paramMap.get( "roleId" ) ) ;
        roleService.insertRoleUser( idAry,roleId) ;
        out.print( getSerializer().message( "" ) ) ;*/
    }
    
    
    /**
	 * @Title: searchPaging
	 * @Description: 查询所有的学校
	 * @author xiahuajun
	 * @date 2016年9月20日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=selectAllOrgsRole")
	public void selectAllOrgsRole(@RequestParam("data") String data,
			java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String school_sequence = "";
		String school_sequence1="";
		String schoolCode = "";
		if("xx".equals(requestMap.get("schoolType").toString())){
			school_sequence = "0";
		}
		else if("cz".equals(requestMap.get("schoolType").toString())){
			school_sequence = "1";
		}
		else if("ygz".equals(requestMap.get("schoolType").toString())){
			school_sequence = "5";
		}
		else if("wz".equals(requestMap.get("schoolType").toString())){
			school_sequence = "4";
			schoolCode = "3004";
		}
		else if("gz".equals(requestMap.get("schoolType").toString())){
			school_sequence1 = "gz";
			schoolCode = "3004";
		}
		
		requestMap.put("school_sequence", school_sequence);
		requestMap.put("school_sequence1", school_sequence1);
		requestMap.put("schoolCode", schoolCode);
		List<Map<String,Object>> list = roleService.selectAllOrgsRole(requestMap);
		out.print(getSerializer().formatList(list));
	}
    
    /**
     * 加载树形列表中的子节点。<br /><br />
     * 命令: "select" ；<br/><br/>
     * @param request 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=loadPerson" )
    public void treeLoadPerson(HttpServletRequest request, java.io.PrintWriter out )
    {
    	
    	System.out.println(request.getParameterMap().get("curNode"));
    	System.out.println(request.getParameter("curNode"));
    	String curNode = Util.returnNullValue(request.getParameter("curNode"));

        JSONObject param = new JSONObject();
        param.put("orgCheck",Util.returnNullValue(request.getParameter("orgCheck")) == "" ? true : Util.returnNullValue(request.getParameter("orgCheck")));
        param.put("personCheck",Util.returnNullValue(request.getParameter("personCheck")) == "" ? true : Util.returnNullValue(request.getParameter("personCheck")));
        param.put("curNode",Util.returnNullValue(request.getParameter("curNode")));
        param.put("isOpen",Util.returnNullValue(request.getParameter("isOpen")) == "" ? true : Util.returnNullValue(request.getParameter("isOpen")));
        param.put("isChecked",Util.returnNullValue(request.getParameter("isChecked")) == "" ? true : Util.returnNullValue(request.getParameter("isChecked")));
        param.put("ids", Util.returnNullValue(request.getParameter("ids")));
        param.put("orgIds", Util.returnNullValue(request.getParameter("orgIds")));
        /*if (curNode != null && !curNode.equals("")) {
            param.put("curNode",curNode);
        }*/
        String resultStr = null;
		try {
			resultStr = HttpUtil.doPost(CommonInterFace.loadPerson, param);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        JSONObject result=JSONObject.fromObject(resultStr);
        JSONArray resultJson = result.getJSONArray("data");
		out.print( getSerializer().formatObject( resultJson ) ) ;
    }
    
    /**
     * 查找用户List的 Web 方法。<br /><br />
     * 命令: "select" ；<br/><br/>
     * @param date 输入参数(由 Browser 端 POST 回来的JSON数据)
     * @param out 响应输出对象
     */
    @RequestMapping( params = "command=getRoleUser" )
    public @ResponseBody RestResponse getRoleUser(@RequestParam("data") String data,HttpServletRequest request,java.io.PrintWriter out )
    {
    	Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
    	List<Map<String,Object>> list = roleService.getRoleUser(paramMap);
		RestResponse resp=new RestResponse();
		resp.success(list);
		return resp;
    } 
    @Autowired
    private PlatformRoleService roleService ;
    @Autowired
    private PlatformMenuService menuService ;
}