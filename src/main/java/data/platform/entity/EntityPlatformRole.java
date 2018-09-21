package data.platform.entity ;

import java.util.Date ;
import java.util.List ;

import data.framework.support.AbstractEntity ;

/**
 * 平台－角色实体类。
 * 
 * @author wanggq
 */
public class EntityPlatformRole extends AbstractEntity
{
    /**
     * 获取角色名称。
     * @return 角色名称
     */
    public String getRoleName()
    {
        return roleName ;
    }

    /**
     * 设置角色名称。
     * @param roleName 角色名称
     */
    public void setRoleName( String roleName )
    {
        this.roleName = roleName ;
    }

    /**
     * 获取所属审批角色。
     * @return 所属审批角色
     */
    public String getApprovalRole()
    {
        return approvalRole ;
    }

    /**
     * 设置所属审批角色。
     * @param approvalRole 所属审批角色
     */
    public void setApprovalRole( String approvalRole )
    {
        this.approvalRole = approvalRole ;
    }
    
    /**
     * 获取角色描述。
     * @return 角色描述
     */
    public String getRoleDescription()
    {
        return roleDescription ;
    }

    /**
     * 设置角色描述。
     * @param remark 角色描述
     */
    public void setRoleDescription( String roleDescription )
    {
    	this.roleDescription = roleDescription ;
    }

    /**
     * 获取角色状态（0：停用；1：启用）。
     * @return 角色状态
     */
    public int getStatus()
    {
        return status ;
    }

    /**
     * 设置角色状态（0：停用；1：启用）。
     * @param status 角色状态
     */
    public void setStatus( int status )
    {
        this.status = status ;
    }

    /**
     * 获取用户创建时间。
     * @return 用户创建时间
     */
    public String getCreateTime()
    {
        return createTime ;
    }

    /**
     * 设置用户创建时间。
     * @param createTime 用户创建时间
     */
    public void setCreateTime( String createTime )
    {
        this.createTime = createTime ;
    }

    /**
     * 获取用户更新时间。
     * @return 用户更新时间
     */
    public String getUpdateTime()
    {
        return updateTime ;
    }

    /**
     * 设置用户更新时间。
     * @param updateTime 用户更新时间
     */
    public void setUpdateTime( String updateTime )
    {
        this.updateTime = updateTime ;
    }

    /**
     * 获取角色的功能权限集合。
     * @return 功能权限集合
     */
    public List<EntityPlatformRoleFunctionalAuthority> getFunctionalList()
    {
        return FunctionalList ;
    }

    /**
     * 设置角色的功能权限集合。
     * @param functionalList 功能权限集合
     */
    public void setFunctionalList( List<EntityPlatformRoleFunctionalAuthority> functionalList )
    {
        FunctionalList = functionalList ;
    }
    
    public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	

	@Override
	public String toString() {
		return "EntityPlatformRole [roleName=" + roleName + ", roleCode=" + roleCode + ", roleType=" + roleType
				+ ", approvalRole=" + approvalRole + ", roleDescription=" + roleDescription + ", status=" + status
				+ ", createTime=" + createTime + ", updateName=" + updateName + ", updateTime=" + updateTime
				+ ", createUserName=" + createUserName + "]";
	}



	private String roleName ;//角色名称
    private String roleCode;//角色标识
    private String roleType;//角色类型
    private String approvalRole ;//所属审批角色
    private String roleDescription ;//角色描述
    private int status ;//状态
    private String createTime ;//创建时间
    private String updateName ;//更新ren
    private String updateTime ;//更新时间
    private String createUserName;//创建人
    private List<EntityPlatformRoleFunctionalAuthority> FunctionalList ;//角色功能权限集合
    private static final long serialVersionUID = -5472676970400842459L ;
}