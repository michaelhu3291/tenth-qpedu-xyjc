package data.framework.httpClient;

import data.framework.support.ConfigContext;

/**
 * bua常用接口地址
 * 
 * @author qcxiao
 * @createTime 2016年3月30日-下午4:51:37
 *
 */
public class CommonInterFace {
	// 服务器地址
	private static String domain = ConfigContext.getStringSection("framework.web.http.path");
	// 系统登陆操作
	public static String sysLogin = domain + "sys/login";
	// 获取左侧菜单
	public static String getLeftMenu = domain + "privilege/getLeftMenu";

	// loadAppMenu导出系统菜单
	public static String loadAppMenu = domain + "user/loadAppMenu";
	// loadMenu导出下级菜单
	public static String loadMenu = domain + "user/loadMenu";
	// loadApp导出系统信息
	public static String loadApp = domain + "app/loadApp";
	//根据登录用户的id查询org_id(即schoolCode)
	public static String loadUserSchoolCode = domain + "refUserPrivilege/loadSchoolCodeByUserId";
	/**
	 * 获取用户所有权限
	 */
	public static String loadUserAllRole = domain + "refUserPrivilege/loadUserAllRole";
/*
 * 查询所有的角色信息
 * */
	public static String searchAllRole = domain + "role/serachRoleIsB";
	/*
	 * 新增角色
	 * */
	public static String saveRole=domain+"role/save";
	/*
	 * 角色修改
	 * */
	public static String updateRole=domain+"role/update";
	/*
	 * 角色删除
	 * */
	public static String deleteRole=domain+"role/remove";
	/*
	 * 角色查询
	 * */
	public static String loadRole=domain+"role/load";
	/*
	 * 根据角色ID查询用户列表
	 * */
	public static String getUsersByRoleId=domain+"refRoleUser/getUsersByRoleId";
	/*
	 * 根据角色ID删除用户信息
	 * */
	public static String deleteUsersByRoleId=domain+"refRoleUser/deleteRoleUser";
	/*
	 * 根据角色ID查询组织列表
	 * */
	public static String getOrgByRoleId=domain+"refRoleOrg/getOrgsByRoleId";
	/*
	 * 根据角色ID删除组织列表
	 * */
	public static String deleteOrgByRoleId=domain+"refRoleOrg/deleteOrgsByRoleId";
	/*
	 * 根据角色ID查询岗位列表
	 * */
	public static String getPostByRoleId=domain+"refRolePost/getPostsByRoleId";
	/*
	 * 根据角色ID删除岗位列表
	 * */
	public static String deletePostByRoleId=domain+"refRolePost/deletePostsByRoleId";
	
	public static String loadPerson=domain + "tree/loadPerson";
	
}
