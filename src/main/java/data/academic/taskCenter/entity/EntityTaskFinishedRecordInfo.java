package data.academic.taskCenter.entity;

import java.util.Date;
import data.framework.support.AbstractEntity;

/**
 * OA-任务中心-已完成任务信息表(任务中心)实体类。
 * 
 * @author haochaochao
 * 
 */
public class EntityTaskFinishedRecordInfo extends AbstractEntity
 {
	
	/**
     * 获取 外键，任务标识。
     * 
     * @return 外键，任务标识
     */
	public String getTaskId() 
	{
		return taskId;
	}
	
	/**
     * 设置 外键，任务标识
     * 
     * @param taskId 外键，任务标识
     */
	
	public void setTaskId(String taskId) 
	{
		this.taskId = taskId;
	}
	/**
     * 获取 接受人帐号。
     * 
     * @return 接受人帐号
     */
	
	public String getReceiverAccount() 
	{
		return receiverAccount;
	}
	
	/**
     * 设置 接受人帐号
     * 
     * @param receiverAccount 接受人帐号
     */
	public void setReceiverAccount(String receiverAccount) 
	{
		this.receiverAccount = receiverAccount;
	}
	
	/**
     * 获取 接受人姓名。
     * 
     * @return 接受人姓名
     */
	
	public String getReceiver() 
	{
		return receiver;
	}
	/**
     * 设置 接受人姓名
     * 
     * @param receiver 接受人姓名
     */
	
	public void setReceiver(String receiver) 
	{
		this.receiver = receiver;
	}
	
	/**
     * 获取 组织机构代码，对应统一授权组织机构Code。
     * 
     * @return 组织机构代码，对应统一授权组织机构Code
     */
	public String getOrgCode() 
	{
		return orgCode;
	}
	
	/**
     * 设置 组织机构代码，对应统一授权组织机构Code
     * 
     * @param orgCode 组织机构代码，对应统一授权组织机构Code
     */
	public void setOrgCode(String orgCode) 
	{
		this.orgCode = orgCode;
	}
	
	/**
     * 获取 用于显示任务状态图例，L,N,H。
     * 
     * @return 用于显示任务状态图例，L,N,H
     */
	
	public String getSignalLamp() 
	{
		return signalLamp;
	}
	
	/**
     * 设置 用于显示任务状态图例，L,N,H
     * 
     * @param signalLamp 用于显示任务状态图例，L,N,H
     */
	public void setSignalLamp(String signalLamp) 
	{
		this.signalLamp = signalLamp;
	}
	
	/**
     * 获取 任务完成时间
     * 
     * @return 任务完成时间
     */
	
	public Date getFinishedTime() 
	{
		return finishedTime;
	}
	
	/**
     * 设置 任务完成时间
     * 
     * @param finishedTime 任务完成时间
     */
	public void setFinishedTime(Date finishedTime) 
	{
		this.finishedTime = finishedTime;
	}
	
	/**
     * 获取 提醒发送时间。
     * 
     * @return 提醒发送时间
     */
	
	public Date getRemindSenderSendTime() 
	{
		return remindSenderSendTime;
	}
	
	/**
     * 设置 提醒发送时间
     * 
     * @param remindSenderSendTime 提醒发送时间
     */
	public void setRemindSenderSendTime(Date remindSenderSendTime) 
	{
		this.remindSenderSendTime = remindSenderSendTime;
	}
	
    private String taskId;
	private String receiverAccount;
	private String receiver;
	private String orgCode;
	private String signalLamp;
	private Date finishedTime;
	private Date remindSenderSendTime;
	private Date createTime;
	private static final long serialVersionUID = -7448795181584231117L;
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
