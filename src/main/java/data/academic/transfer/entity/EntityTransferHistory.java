package data.academic.transfer.entity;
/**
 * @Description 人员调动历史记录表（学生、老师）
 * @author wangchaofa
 * @CreateDate Dec 06,2016
 */

import java.util.Date;

import data.framework.support.AbstractEntity;

public class EntityTransferHistory extends AbstractEntity{

	private static final long serialVersionUID = -7448795181584231117L;
	
	private String name;             //姓名
	private String schoolCode;       //学校code
	private String proposer;         //申请人
	private String direction;        //去向
	private Date applyTime;          //申请时间
	private String createPerson;     //创建人
	private Date createTime;         //创建时间
	private String updatePerson;     //修改人
	private Date updateTime;         //修改时间
	private String roleState;        //角色 （0-学生，1-老师）
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSchoolCode() {
		return schoolCode;
	}
	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}
	public String getProposer() {
		return proposer;
	}
	public void setProposer(String proposer) {
		this.proposer = proposer;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public Date getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	public String getCreatePerson() {
		return createPerson;
	}
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdatePerson() {
		return updatePerson;
	}
	public void setUpdatePerson(String updatePerson) {
		this.updatePerson = updatePerson;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getRoleState() {
		return roleState;
	}
	public void setRoleState(String roleState) {
		this.roleState = roleState;
	}
	
}
