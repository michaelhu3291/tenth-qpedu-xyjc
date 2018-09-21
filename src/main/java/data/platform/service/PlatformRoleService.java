package data.platform.service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import data.framework.httpClient.CommonInterFace;
import data.framework.httpClient.HttpUtil;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;
import data.platform.entity.EntityPlatformRole;
import data.platform.entity.EntityPlatformUser;
import net.sf.json.JSONObject;

/**
 *平台-角色服务类。
 * @author wanggq
 *
 */
@Service
public class PlatformRoleService extends AbstractService
{
	/**
	 * 保存或更新角色信息。
	 * @param roleId 角色id
	 * @param data 保存角色信息的map
	 * @throws UnsupportedEncodingException 
	 */
	@Autowired
	public void saveOrUpdate( Map<String,Object> data ,String roleId) throws UnsupportedEncodingException
	{
		
		JSONObject param = new JSONObject().fromObject(data);
		String result="";
		if(StringUtils.isNotBlank( roleId )){
			result = HttpUtil.doPost(CommonInterFace.updateRole, param);
		}else {
			result = HttpUtil.doPost(CommonInterFace.saveRole, param);
		}
			System.out.println("======="+result);

	}

	/**
	 * 根据角色编号获取角色信息。
	 * @param id 角色编号
	 * @return 角色实体
	 * @throws UnsupportedEncodingException 
	 */
	public EntityPlatformRole load( String id ) throws UnsupportedEncodingException
	{
		JSONObject param = new JSONObject() ;
		param.put( "roleId", id ) ;
		String result="";

		result = HttpUtil.doPost(CommonInterFace.loadRole, param);
		JSONObject json=JSONObject.fromObject(result);
		JSONObject message=json.getJSONObject("meta");
		if("ok".equals(message.get("message"))){
			JSONObject jo=json.getJSONObject("data");
			EntityPlatformRole role=new EntityPlatformRole();
			role.setRoleName(jo.getString("roleName"));
			role.setRoleCode(jo.getString("roleCode"));
			role.setRoleType(jo.getString("roleType"));
			role.setRoleDescription(jo.getString("roleDescription"));
			role.setCreateUserName(jo.getString("createUserName"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String cr=jo.getString("createTime");
			String up=jo.getString("lastUpdateTime");
			String updateName=jo.getString("lastUpdateUserName");
			if(updateName.equals("null")){
			}else{
				System.out.println("===updateName==="+updateName);
				role.setUpdateName(updateName);
			}
			if(!up.equals("null")){
				long upd=Long.parseLong(up);
				Date date = new Date(upd);
				String dateStr = sdf.format(date);
				role.setUpdateTime(dateStr);
			}
			long cre=Long.parseLong(cr);
			Date date1 = new Date(cre);
			String dateStr1 = sdf.format(date1);
			role.setCreateTime(dateStr1);
			return role;
		}
		return  null;
		/* Map<String,String> param = new HashMap<String,String>() ;
        param.put( "id", id ) ;
        return selectOne( "platformRole.loadRole", param ) ;*/
	}

	/**
	 * 根据角色编号数组获取角色信息集合。
	 * @param id 角色编号
	 * @return 角色实体
	 * @author lidong
	 */
	public List<EntityPlatformRole> loadRoleByIDs( List<String> ids )
	{
		return selectList( "platformRole.loadRoleByIDs", ids ) ;
	}

	/**
	 * 根据角色编号删除角色信息。
	 * @param idAry 角色编号集合
	 * @return 删除记录数
	 */
	public int remove(List<String> idAry )
	{
		/*JSONObject param = new JSONObject().fromObject(data) ;
		System.out.println("=======param"+param);
		String result="";
		result=HttpUtil.doPost(CommonInterFace.deleteRole, param);
		System.out.println("======"+result);*/
		 Map<String,Object> param = new HashMap<String,Object>() ;
	        param.put( "idAry", idAry ) ;
	        return delete( "platformRole.deleteRole", param ) ;
	}
	
	/**
	 * 删除角色下的用户信息。
	 * @param idAry 用户编号集合
	 * @param roleId 角色编号
	 * @return 删除记录数
	 */
	public int removeRoleUser(List<String> idAry ,String roleId )
	{
		 Map<String,Object> param = new HashMap<String,Object>() ;
	        param.put( "roleId", roleId ) ;
	        param.put( "userIds", idAry ) ;
	        return delete( "platformRole.deleteRoleUser", param ) ;
	}
	
	/**
	 * 删除角色下的组织信息。
	 * @param idAry 组织编号集合
	 * @param roleId 角色编号
	 * @return 删除记录数
	 */
	public int removeRoleOrg(List<String> idAry ,String roleId )
	{
		Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "roleId", roleId ) ;
        param.put( "orgIds", idAry ) ;
        return delete( "platformRole.deleteRoleOrg", param ) ;
	}
	
	/**
	 * 删除角色下的岗位信息。
	 * @param idAry 岗位编号集合
	 * @param roleId 角色编号
	 * @return 删除记录数
	 */
	public int removeRolePost(List<String> idAry ,String roleId )
	{
		Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "roleId", roleId ) ;
        param.put( "postIds", idAry ) ;
        return delete( "platformRole.deleteRolePost", param ) ;
	}
	
	/**
	 * 根据角色编号添加用户信息。
	 * @param id 角色编号集合  
	 */
	public void insertRoleUser(List<String> idAry ,String roleId )
	{
		Map<String,Object> param = new HashMap<String,Object>() ;
        for(String userId :idAry){
        	param.put("userId", userId);
        	param.put("roleId", roleId);
        	insert( "platformRole.insertRoleUser", param ) ;
        }
	}
	
	/**
	 * 根据角色编号添加用户信息。
	 * @param id 角色编号集合  
	 */
	public void insertRoleOrg(List<String> idAry ,String roleId )
	{
		Map<String,Object> param = new HashMap<String,Object>() ;
        for(String orgId :idAry){
        	param.put("orgId", orgId);
        	param.put("roleId", roleId);
        	insert( "platformRole.insertRoleOrg", param ) ;
        }
	}
	
	/**
	 * @Title: selectAllUsersBySchoolCode
	 * @Description: 查询所有的学校
	 * @author xiahuajun
	 * @date 2016年9月20日 
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> selectAllUsersBySchoolCode(Map<String,Object> requestMap) {
		return selectList("platformRole.selectAllUsersBySchoolCode",requestMap);
	}
	
	/**
	 * @Title: selectAllSchools
	 * @Description: 查询所有的学校
	 * @author xiahuajun
	 * @date 2016年9月20日 
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> selectAllOrgsRole(Map<String,Object> requestMap) {
		return selectList("platformRole.selectAllOrgsRole",requestMap);
	}
	
	/**(用户，角色共有)
	 * 获取角色所属用户数据
	 * @param param
	 */
	public List<Map<String,Object>> getRoleUser(Map<String,Object> param){
	   return selectList("platformRole.getRoleUser",param);
	}
	
	/**
	 * 分页查询所有业务角色
	 * @param roleName 角色名称
	 * @param status 状态
	 * @return 角色的ID和名称信息集合(如Map：{text:"",value:""})
	 * @throws UnsupportedEncodingException 
	 */
	public Object searchBUARoles( String roleName, Integer status, String sortField, String sort,
			int currentPage, int pageSize) throws UnsupportedEncodingException
	{
		JSONObject param = new JSONObject() ;
		param.put( "q", roleName ) ;
		param.put( "sidx", sortField ) ;
		param.put( "sord", sort ) ;
		param.put( "page", currentPage ) ;
		param.put( "rows", pageSize ) ;
		param.put( "appId", "63594C80-63AA-4725-BFF5-1FDB61380D11" ) ;
		String result="";

		result = HttpUtil.doPost(CommonInterFace.searchAllRole, param);
		JSONObject json=JSONObject.fromObject(result);
		JSONObject message=json.getJSONObject("meta");
		if("ok".equals(message.get("message"))){
			return json.getJSONObject("data");
		}

		return  null;
	}

	/**
	 * 分页查询数据。
	 * @param roleName 角色名称
	 * @param status 角色状态
	 * @param sortField 数据库排序字段
	 * @param sort 排序方式（ASC|DESC）
	 * @param currentPage 当前页数
	 * @param pageSize 页大小
	 * @return 分页查询集合
	 */
	public PagingResult<EntityPlatformRole> searchRoles( String roleName, Integer status, String sortField, String sort, int currentPage, int pageSize )
	{
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put( "roleName", roleName ) ;
		param.put( "status", status ) ;
		if( StringUtils.isBlank( sortField ) )
			sortField = "ROLE_NAME" ;
		if( StringUtils.isBlank( sort ) )
			sort = "ASC" ;
		return selectPaging( "platformRole.selectPaging", param, sortField, sort, currentPage, pageSize ) ;
	}

	/**
	 * 根据条件查询角色的ID和名称信息。
	 * @param roleName 角色名称
	 * @param status 状态
	 * @return 角色的ID和名称信息集合(如Map：{text:"",value:""})
	 */
	public List<Map<String,String>> searchRoles( String roleName, Integer status )
	{
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put( "roleName", roleName ) ;
		param.put( "status", status ) ;
		return selectList( "platformRole.selectRole", param ) ;
	}

	/**
	 * 根据角色ID获取对应功能权限的集合。
	 * @param roleId 角色ID
	 * @return 功能权限集合
	 */
	public List<Map<String,String>> selectRoleFunctions( String roleId )
	{
		return selectList( "platformRole.selectRoleFunctions", roleId ) ;
	}

	/**
	 * 删除角色对应的功能权限。
	 * @param roleId 角色ID
	 * @return 删除的记录数
	 */
	public int deleteRoleFunctions( String roleId )
	{
		return delete( "platformRole.deleteRoleFunctions", roleId ) ;
	}

	/**
	 * 保存角色功能权限信息。
	 * @param roleId 角色ID
	 * @param functionList 功能权限集合
	 * @param operator 操作人
	 */
	public void saveRoleFunctions( String roleId, List<Map<String,String>> functionList, String operator )
	{
		deleteRoleFunctions( roleId ) ;
		if( functionList == null || functionList.isEmpty() )
			return ;
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put( "roleId", roleId ) ;
		param.put( "functions", functionList ) ;
		param.put( "createTime", new Date() ) ;
		param.put( "operator", operator ) ;
		insert( "platformRole.insertRoleFunctions", param ) ;
	}



	/**
	 * 删除角色对应的数据权限。
	 * @param roleId 角色ID
	 * @return 删除的记录数
	 */
	public int deleteRoleDataAuthoritys( String roleId )
	{
		return delete( "platformRole.deleteRoleDataAuthoritys", roleId ) ;
	}

	/**
	 * 保存角色数据权限信息。
	 * @param roleId 角色ID
	 * @param dataAuthorityList 数据权限集合
	 * @param operator 操作人
	 */
	public void saveRoleDataAuthoritys( String roleId, List<String> dataAuthorityList, String operator )
	{
		if( dataAuthorityList == null || dataAuthorityList.isEmpty() )
			return ;
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put( "roleId", roleId ) ;
		param.put( "dataAuthoritys", dataAuthorityList ) ;
		param.put( "createTime", new Date() ) ;
		param.put( "operator", operator ) ;
		insert( "platformRole.insertRoleDataAuthoritys", param ) ;
	}

	/**
	 * 根据角色ID查找用户List
	 * @throws UnsupportedEncodingException 
	 */
	public Object selectUsersByRoleId(String roleId, String sortField, String sort,int currentPage, int pageSize ) throws UnsupportedEncodingException
	{	
		JSONObject param = new JSONObject() ;
		param.put( "sidx", sortField ) ;
		param.put( "sord", sort ) ;
		param.put( "page", currentPage ) ;
		param.put( "rows", pageSize ) ;
		param.put("roleId", roleId);
		String result="";

		result = HttpUtil.doPost(CommonInterFace.getUsersByRoleId, param);
		JSONObject json=JSONObject.fromObject(result);
		System.out.println("=====json===="+json);
		JSONObject message=json.getJSONObject("meta");
		if("ok".equals(message.get("message"))){
			return json.getJSONObject("data");
		}

		return  null;
		
		
		
	}
	
	/**
	 * 根据角色ID查找组织List
	 * @throws UnsupportedEncodingException 
	 */
	public Object selectOrgsByRoleId(String roleId, String sortField, String sort,int currentPage, int pageSize ) throws UnsupportedEncodingException
	{	
		JSONObject param = new JSONObject() ;
		param.put( "sidx", sortField ) ;
		param.put( "sord", sort ) ;
		param.put( "page", currentPage ) ;
		param.put( "rows", pageSize ) ;
		param.put("roleId", roleId);
		String result="";

		result = HttpUtil.doPost(CommonInterFace.getOrgByRoleId, param);
		JSONObject json=JSONObject.fromObject(result);
		System.out.println("=====json===="+json);
		JSONObject message=json.getJSONObject("meta");
		if("ok".equals(message.get("message"))){
			return json.getJSONObject("data");
		}

		return  null;
		
		
		
	}
	
	
	/**
	 * 根据角色ID查找岗位List
	 * @throws UnsupportedEncodingException 
	 */
	public Object selectPostByRoleId(String roleId, String sortField, String sort,int currentPage, int pageSize ) throws UnsupportedEncodingException
	{	
		JSONObject param = new JSONObject() ;
		param.put( "sidx", sortField ) ;
		param.put( "sord", sort ) ;
		param.put( "page", currentPage ) ;
		param.put( "rows", pageSize ) ;
		param.put("roleId", roleId);
		String result="";

		result = HttpUtil.doPost(CommonInterFace.getPostByRoleId, param);
		JSONObject json=JSONObject.fromObject(result);
		System.out.println("=====json===="+json);
		JSONObject message=json.getJSONObject("meta");
		if("ok".equals(message.get("message"))){
			return json.getJSONObject("data");
		}

		return  null;
		
		
		
	}

	/**
	 * @Title: selectAllSchools
	 * @Description: 查询用户下所有的组织
	 * @author xiahuajun
	 * @date 2016年9月20日 
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> selectAllOrgs(Map<String,Object> requestMap) {
		return selectList("platformRole.selectAllOrgs",requestMap);
	}
	
	/**
	 * 根据角色ID删除关联的用户映射
	 * 添加新的映射
	 */
	public void roleUserMapping( String roleId,String[] userIds )
	{
		delete("platformRole.deleteMappingByRoleId",roleId);

		for (String userId : userIds) {
			Map<String,Object> param = new HashMap<String,Object>() ;
			param.put( "roleId", roleId ) ;
			param.put( "userId", userId ) ;

			insert("platformRole.insertMappingForRole",param);
		}
	}


	/**
	 * 删除角色对应的数据权限。
	 * @param roleId 角色ID
	 * @return 删除的记录数
	 */
	public int deleteUserRole( String userId )
	{
		return delete( "platformRole.deleteUserRole", userId ) ;
	}

	/**
	 * 根据角色的ApprovalName查询 相应的组(中心组，工会等等) List
	 */
	public List<EntityPlatformRole> selectRoleTeamsByApprovalName(String approvalName )
	{
		return selectList( "platformRole.selectRoleTeamsByApprovalName", approvalName ) ;
	}

	/**
	 * 根据角色的ApprovalName查询 相应的组(中心组，工会等等) List
	 */
	public List<Map<String,Object>> selectUsersByUserId(String userId )
	{
		return selectList( "platformRole.selectUsersByUserId", userId ) ;
	}

	/**
	 * 根据角色名，查询角色下用户
	 */
	public List<Map<String,Object>> selectRoleUser(Map<String,Object> param )
	{
		return selectList( "platformRole.selectRoleUser", param ) ;
	}
	
	/**
	 * 根据当前节点加载子节点
	 * @return
	 */
	public List<Map<String,Object>> loadChaildNode(Map<String,Object> param){
		return selectList("platformRole.loadChaildNode",param);
	}
	
	/**
	 * 根据当前节点加载子节点
	 * @return
	 */
	public List<Map<String,Object>> loadChaildNodess(Map<String,Object> param){
		return selectList("platformRole.loadChaildNodess",param);
	}

}