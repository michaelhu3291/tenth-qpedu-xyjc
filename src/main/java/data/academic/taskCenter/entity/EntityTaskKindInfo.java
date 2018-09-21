package data.academic.taskCenter.entity;

import java.util.Date;
import data.framework.support.AbstractEntity;

/**
 * OA-任务中心-任务分类信息表(任务中心)实体类。
 * 
 * @author haochaochao
 * 
 */
public class EntityTaskKindInfo extends AbstractEntity
{
	
	/**
     * 获取 应用系统标识
     * 
     * @return 应用系统标识
     */
    public String getAppCode() 
    {
		return appCode;
	}
    
    /**
     * 设置 应用系统标识
     * 
     * @param appCode 应用系统标识
     */
	public void setAppCode(String appCode) 
	{
		this.appCode = appCode;
	}
	
	/**
     * 获取 任务分类标识
     * 
     * @return 任务分类标识
     */
	public String getTaskKindCode() 
	{
		return taskKindCode;
	}
	
	/**
     * 设置 任务分类标识
     * 
     * @param taskKindCode 任务分类标识
     */
	public void setTaskKindCode(String taskKindCode) 
	{
		this.taskKindCode = taskKindCode;
	}

	/**
     * 获取 任务分类名称
     * 
     * @return 任务分类名称
     */
	public String getTaskKideName() 
	{
		return taskKideName;
	}
	
	/**
     * 设置 任务分类名称
     * 
     * @param taskKideName 任务分类名称
     */

	public void setTaskKideName(String taskKideName) 
	{
		this.taskKideName = taskKideName;
	}
	
	/**
     * 获取 任务类型
     * 
     * @return 任务类型
     */
	public String getTaskType() 
	{
		return taskType;
	}
	
	/**
     * 设置 任务类型
     * 
     * @param taskType 任务类型
     */
	public void setTaskType(String taskType) 
	{
		this.taskType = taskType;
	}
	
	/**
     * 获取 任务完成模式
     * 
     * @return 任务完成模式
     */

	public String getTaskFinisMode() 
	{
		return taskFinisMode;
	}
	
	/**
     * 设置 任务完成模式
     * 
     * @param taskFinisMode 任务完成模式
     */

	public void setTaskFinisMode(String taskFinisMode)
	{
		this.taskFinisMode = taskFinisMode;
	}

	/**
     * 获取 任务类型状态
     * 
     * @return 
     */
	public int getTaskKindStatus() 
	{
		return taskKindStatus;
	}
	
	/**
     * 设置 任务类型状态
     * 
     * @param taskKindStatus 任务类型状态
     */

	public void setTaskKindStatus(int taskKindStatus) 
	{
		this.taskKindStatus = taskKindStatus;
	}

	/**
     * 获取 消息类型任务完成时发送给统一消息的消息类型; 任务在完成时，将判断有无该值，若则发送消息

     * 
     * @return 消息类型任务完成时发送给统一消息的消息类型;任务在完成时，将判断有无该值，若则发送消息
     */
	public String getRemindMsgType() 
	{
		return remindMsgType;
	}
	/**
     * 设置 消息类型任务完成时发送给统一消息的消息类型;任务在完成时，将判断有无该值，若则发送消息

     * 
     * @param remindMsgType 消息类型任务完成时发送给统一消息的消息类型;任务在完成时，将判断有无该值，若则发送消息
     */

	public void setRemindMsgType(String remindMsgType) 
	{
		this.remindMsgType = remindMsgType;
	}
	
	/**
     * 获取 任务排序
     * 
     * @return 任务排序
     */

	public int getTaskSeq() 
	{
		return taskSeq;
	}
	
	/**
     * 设置 任务排序
     * 
     * @param taskSeq 任务排序
     */

	public void setTaskSeq(int taskSeq) 
	{
		this.taskSeq = taskSeq;
	}
	
	/**
     * 获取 创建者
     * 
     * @return 创建者
     */

	public String getCreateUserName() 
	{
		return createUserName;
	}
	
	/**
     * 设置 创建者
     * 
     * @param createUserName 创建者
     */

	public void setCreateUserName(String createUserName) 
	{
		this.createUserName = createUserName;
	}
	
	/**
     * 获取 创建时间
     * 
     * @return 创建时间
     */
	public Date getCreateTime()
	{
		return createTime;
	}
	
	/**
     * 设置 创建时间
     * 
     * @param createTime 创建时间
     */

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
	
	/**
     * 获取 最后修改者
     * 
     * @return 最后修改者
     */

	public String getLastUpdateUserName() 
	{
		return lastUpdateUserName;
	}
	
	/**
     * 设置 最后修改者
     * 
     * @param lastUpdateUserName 最后修改者
     */

	public void setLastUpdateUserName(String lastUpdateUserName)
	{
		this.lastUpdateUserName = lastUpdateUserName;
	}
	
	/**
     * 获取 最后修改时间
     * 
     * @return 最后修改时间
     */

	public Date getLastUpdateTime() 
	{
		return lastUpdateTime;
	}

	/**
     * 设置 最后修改时间
     * 
     * @param lastUpdateTime 最后修改时间
     */
	public void setLastUpdateTime(Date lastUpdateTime) 
	{
		this.lastUpdateTime = lastUpdateTime;
	}

	private String appCode;
	private String taskKindCode;
	private String taskKideName;
	private String taskType;
	private String taskFinisMode;
	private int taskKindStatus;
	private String remindMsgType;
	private int taskSeq;
	private String createUserName;
	private Date createTime;
	private String lastUpdateUserName;
	private Date lastUpdateTime;
	private static final long serialVersionUID = 2098807167016090517L;
}
