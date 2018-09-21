package data.academic.taskCenter.entity;

import data.framework.support.AbstractEntity;

/**
 * OA-任务中心-任务接收人处理任务情况实体类。
 * 
 * @author haochaochao
 * 
 */
public class EntityTaskReceiverInfo extends AbstractEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1917525374794754169L;
	/**
     * 获取 外键，任务标识
     * 
     * @return 外键，任务标识
     */
    public String getTaskId() {
		return taskId;
	}

    /**
     * 设置 外键，任务标识
     * 
     * @param taskId 外键，任务标识
     */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	/**
     * 获取 任务接受人类型
     * 
     * @return 任务接受人类型
     */

	public String getReceiverAccountType() {
		return receiverAccountType;
	}
	/**
     * 设置 任务接受人类型
     * 
     * @param receiverAccountType 任务接受人类型
     */

	public void setReceiverAccountType(String receiverAccountType) {
		this.receiverAccountType = receiverAccountType;
	}
	/**
     * 获取 任务接受人帐号
     * 
     * @return 任务接受人帐号
     */

	public String getReceiverAccount() {
		return receiverAccount;
	}
	/**
     * 设置 任务接受人帐号
     * 
     * @param receiverAccount 任务接受人帐号
     */

	public void setReceiverAccount(String receiverAccount) {
		this.receiverAccount = receiverAccount;
	}
	/**
     * 获取 任务接受人姓名
     * 
     * @return 任务接受人姓名
     */

	public String getReceiver() {
		return receiver;
	}
	/**
     * 设置 任务接受人姓名
     * 
     * @param receiver 任务接受人姓名
     */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	/**
     * 获取 是否已读
     * 
     * @return 是否已读
     */

	public int getIsRead() {
		return isRead;
	}
	/**
     * 设置 是否已读
     * 
     * @param isRead 是否已读
     */
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	/**
     * 获取 是否已完成
     * 
     * @return 是否已完成
     */

	public int getIsDone() {
		return isDone;
	}
	/**
     * 设置 是否已完成
     * 
     * @param isDone 是否已完成
     */
	public void setIsDone(int isDone) {
		this.isDone = isDone;
	}
	
	private String taskId;
	private String receiverAccountType;
	private String receiverAccount;
	private String receiver;
	private int isRead;
	private int isDone;
	/**
	 * 辅导员映射表ID
	 */
	private String fdyId;
	/**
	 * 专业年级ID
	 */
	private String zynjId;
	
	public String getFdyId()
	{
		return fdyId;
	}

	public void setFdyId(String fdyId)
	{
		this.fdyId = fdyId;
	}

	public String getZynjId()
	{
		return zynjId;
	}

	public void setZynjId(String zynjId)
	{
		this.zynjId = zynjId;
	}

}
