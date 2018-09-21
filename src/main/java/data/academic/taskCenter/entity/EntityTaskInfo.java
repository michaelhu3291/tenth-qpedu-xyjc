package data.academic.taskCenter.entity;

import java.util.Date;
import data.framework.support.AbstractEntity;

/**
 * OA-任务中心-任务信息表(任务中心)实体类。
 * 
 * @author haochaochao
 * 
 */
public class EntityTaskInfo  extends AbstractEntity
{

	/**
     * 获取 任务来源
     * 
     * @return 任务来源
     */
	public String getTaskSource() {
		return taskSource;
	}
	/**
     * 设置 任务来源
     * 
     * @param taskSource 任务来源
     */
	public void setTaskSource(String taskSource)
	{
		this.taskSource = taskSource;
	}
	/**
     * 获取 应用系统代码，对应统一授权应用系统Code
     * 
     * @return 应用系统代码，对应统一授权应用系统Code
     */
	public String getAppCode()
	{
		return appCode;
	}
	/**
     * 设置 应用系统代码，对应统一授权应用系统Code
     * 
     * @param appCode 应用系统代码，对应统一授权应用系统Code
     */
	public void setAppCode(String appCode)
	{
		this.appCode = appCode;
	}
	/**
     * 获取 任务类型代码，配置在TASK_KIND_INFO表中
     * 
     * @return 任务类型代码，配置在TASK_KIND_INFO表中
     */
	public String getTaskKindCode()
	{
		return taskKindCode;
	}
	/**
     * 设置 任务类型代码，配置在TASK_KIND_INFO表中
     * 
     * @param taskKindCode 任务类型代码，配置在TASK_KIND_INFO表中
     */
	public void setTaskKindCode(String taskKindCode)
	{
		this.taskKindCode = taskKindCode;
	}
	/**
     * 获取 三方任务唯一标识
     * 
     * @return 三方任务唯一标识
     */
	public String getClientTxSeq() 
	{
		return clientTxSeq;
	}
	/**
     * 设置 三方任务唯一标识
     * 
     * @param clientTxSeq 三方任务唯一标识
     */
	public void setClientTxSeq(String clientTxSeq)
	{
		this.clientTxSeq = clientTxSeq;
	}
	/**
     * 获取 任务创建人帐号
     * 
     * @return 任务创建人帐号
     */
	public String getCreatorAccount() 
	{
		return creatorAccount;
	}
	/**
     * 设置 任务创建人帐号
     * 
     * @param creatorAccount 任务创建人帐号
     */
	public void setCreatorAccount(String creatorAccount) 
	{
		this.creatorAccount = creatorAccount;
	}
	/**
     * 获取 任务创建人姓名
     * 
     * @return 任务创建人姓名
     */
	public String getCreator() 
	{
		return creator;
	}
	/** 
     * 设置 任务创建人姓名
     * 
     * @param creator 任务创建人姓名
     */
	public void setCreator(String creator) 
	{
		this.creator = creator;
	}
	/**
     * 获取 所属组织代码，对应统一授权组织机构Code
     * 
     * @return 所属组织代码，对应统一授权组织机构Code
     */
	public String getOrgCode() 
	{
		return orgCode;
	}
	/**
     * 设置 所属组织代码，对应统一授权组织机构Code
     * 
     * @param orgCode 所属组织代码，对应统一授权组织机构Code
     */
	public void setOrgCode(String orgCode) 
	{
		this.orgCode = orgCode;
	}
	/**
     * 获取 任务状态
     * 
     * @return 任务状态
     */
	public int getStatus() 
	{
		return status;
	}
	/**
     * 设置 任务状态
     * 
     * @param status 任务状态
     */
	public void setStatus(int status) 
	{
		this.status = status;
	}
	/**
     * 获取 用于显示任务状态图例
     * 
     * @return 用于显示任务状态图例
     */
	public int getSignalLamp() 
	{
		return signalLamp;
	}
	/**
     * 设置 主用于显示任务状态图例
     * 
     * @param signalLamp 用于显示任务状态图例
     */
	public void setSignalLamp(int signalLamp) 
	{
		this.signalLamp = signalLamp;
	}
	/**
     * 获取 任务紧急程度，L，N，H，高中低
     * 
     * @return 任务紧急程度，L，N，H，高中低
     */
	public int getTskLevel() 
	{
		return tskLevel;
	}
	/**
     * 设置 任务紧急程度，L，N，H，高中低
     * 
     * @param tskLevel 任务紧急程度，L，N，H，高中低
     */
	public void setTskLevel(int tskLevel) 
	{
		this.tskLevel = tskLevel;
	}
	/**
     * 获取 任务操作模式
     * 
     * @return 任务操作模式
     */
	public String getTaskOperateMode()
	{
		return taskOperateMode;
	}
	/**
     * 设置 任务操作模式
     * 
     * @param taskOperateMode 任务操作模式
     */
	public void setTaskOperateMode(String taskOperateMode) 
	{
		this.taskOperateMode = taskOperateMode;
	}
	/**
     * 获取 任务标题
     * 
     * @return 任务标题
     */
	public String getTitle() 
	{
		return title;
	}
	/**
     * 设置 任务标题
     * 
     * @param title 任务标题
     */
	public void setTitle(String title) 
	{
		this.title = title;
	}
	/**
     * 获取 任务连接
     * 
     * @return 任务连接
     */
	public String getActionUrl()
	{
		return actionUrl;
	}
	/**
     * 设置 任务连接
     * 
     * @param actionUrl 任务连接
     */
	public void setActionUrl(String actionUrl)
	{
		this.actionUrl = actionUrl;
	}
	
	/**
     * 获取 是否立即发送提醒
     * 
     * @return 是否立即发送提醒
     */
	public int getRemindNow() 
	{
		return remindNow;
	}
	
	/**
     * 设置 是否立即发送提醒
     * 
     * @param remindNow 是否立即发送提醒
     */
	public void setRemindNow(int remindNow)
    {
		this.remindNow = remindNow;
	}
	/**
     * 获取 本次发送时间
     * 
     * @return 本次发送时间
     */
	public Date getRemindNowSendTime()
    {
		return remindNowSendTime;
	}
	/**
     * 设置 本次发送时间
     * 
     * @param remindNowSendTime 本次发送时间
     */
	public void setRemindNowSendTime(Date remindNowSendTime) 
	{
		this.remindNowSendTime = remindNowSendTime;
	}
	/**
     * 获取 发送人
     * 
     * @return 发送人
     */
	public int getRemindSender() 
	{
		return remindSender;
	}
	/**
     * 设置 发送人
     * 
     * @param remindSender 发送人
     */
	public void setRemindSender(int remindSender)
	{
		this.remindSender = remindSender;
	}
	/**
     * 获取 任务开始时间
     * 
     * @return 任务开始时间
     */
	public Date getBeginTime()
	{
		return beginTime;
	}
	/**
     * 设置 任务开始时间
     * 
     * @param beginTime 任务开始时间
     */
	public void setBeginTime(Date beginTime)
	{
		this.beginTime = beginTime;
	}
	/**
     * 获取 任务预警时间
     * 
     * @return 任务预警时间
     */
	public Date getWarningTime() 
	{
		return warningTime;
	}
	/**
     * 设置 任务预警时间
     * 
     * @param warningTime 任务预警时间
     */
	public void setWarningTime(Date warningTime) 
	{
		this.warningTime = warningTime;
	}
	/**
     * 获取 任务结束时间
     * 
     * @return 任务结束时间
     */
	public Date getEndTime()
	{
		return endTime;
	}
	/**
     * 设置 任务结束时间
     * 
     * @param endTime 任务结束时间
     */
	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}
	/**
     * 获取 任务超期时间
     * 
     * @return 任务超期时间
     */
	public Date getExpireTime() 
	{
		return expireTime;
	}
	/**
     * 设置 任务超期时间
     * 
     * @param expireTime 任务超期时间
     */
	public void setExpireTime(Date expireTime)
	{
		this.expireTime = expireTime;
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
     * 获取 任务完成提醒时间
     * 
     * @return 任务完成提醒时间
     */
	public Date getFinishedRemindSendTime() 
	{
		return finishedRemindSendTime;
	}
	/**
     * 设置 任务完成提醒时间
     * 
     * @param finishedRemindSendTime 任务完成提醒时间
     */
	public void setFinishedRemindSendTime(Date finishedRemindSendTime) 
	{
		this.finishedRemindSendTime = finishedRemindSendTime;
	}
	/**
     * 获取 记录创建时间
     * 
     * @return 记录创建时间
     */
	public Date getCreateTime() 
	{
		return createTime;
	}
	/**
     * 设置 记录创建时间
     * 
     * @param createTime 记录创建时间
     */
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}
	/**
     * 获取 记录最后一次修改时间
     * 
     * @return 记录最后一次修改时间
     */
	public Date getLastUpdateTime()
	{
		return lastUpdateTime;
	}
	/**
     * 设置 记录最后一次修改时间
     * 
     * @param lastUpdateTime 记录最后一次修改时间
     */
	public void setLastUpdateTime(Date lastUpdateTime)
	{
		this.lastUpdateTime = lastUpdateTime;
	}
	/**
     * 获取 备注
     * 
     * @return 备注
     */
	public String getRemark() 
	{
		return remark;
	}
	/**
     * 设置 备注
     * 
     * @param remark 备注
     */
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	/**
     * 获取 扩展字段
     * 
     * @return 扩展字段
     */
	public String getExfield() 
	{
		return exfield;
	}
	/**
     * 设置 扩展字段
     * 
     * @param exfield 扩展字段
     */
	public void setExfield(String exfield)
	{
		this.exfield = exfield;
	}
	/**
     * 获取 任务查看连接
     * 
     * @return 任务查看连接
     */
	public String getViewUrl() 
	{
		return viewUrl;
	}
	/**
     * 设置 任务查看连接
     * 
     * @param viewUrl 任务查看连接
     */
	public void setViewUrl(String viewUrl) 
	{
		this.viewUrl = viewUrl;
	}
	
	/**
     * 获取 流转步骤
     * 
     * @return 流转步骤
     */
	public String getTaskStep() 
	{
		return taskStep;
	}
	
	/**
     * 设置 流转步骤
     * 
     * @param takeStep 流转步骤
     */
	public void setTaskStep(String taskStep) 
	{
		this.taskStep = taskStep;
	}
	
	private String taskSource;
	private String appCode;
	private String taskKindCode;
	private String clientTxSeq;
	private String creatorAccount;
	private String creator;
	private String orgCode;
	private int	status;
	private int	signalLamp;
	private int	tskLevel;
	private String taskOperateMode;
	private String title;
	private String actionUrl;
	private String taskStep;
	private int	remindNow;
	private Date remindNowSendTime;
	private int remindSender;
	private Date beginTime;
	private Date warningTime;
	private Date endTime;
	private Date expireTime;
	private Date finishedTime;
	private Date finishedRemindSendTime;
	private Date createTime;
	private Date lastUpdateTime;
	private String remark;
	private String exfield;
	private String viewUrl;
	private String applyId;
	public String getApplyId() {
		return applyId;
	}
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	private static final long serialVersionUID = 3921696438643058002L;
}
