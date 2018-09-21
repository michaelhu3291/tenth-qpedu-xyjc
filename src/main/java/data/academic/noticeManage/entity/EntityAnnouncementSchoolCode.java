package data.academic.noticeManage.entity;

import data.framework.support.AbstractEntity;

/**
 * OA-通知公告-公告发布参与角色信息关系实体类
 * 
 * @author JohnXU
 * 
 */
public class EntityAnnouncementSchoolCode extends AbstractEntity
{


	/**
	 * 获取公告id
	 * 
	 * @return 公告id
	 */
	public String getAnnouncementId()
	{
		return announcementId;
	}

	/**
	 * 设置公告id
	 * 
	 * @param announcementId 公告id
	 * 
	 */
	public void setAnnouncementId(String announcementId)
	{
		this.announcementId = announcementId;
	}

	/**
	 * 获取学校Code
	 * 
	 * @return 角色学校Code
	 */
	public String getSchoolCode() {
		return schoolCode;
	}

	/**
	 * 设置学校Code
	 * 
	 * @param schoolCode 学校Code
	 * 
	 */
	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}
	/**
	 * @return the schoolName
	 */
	public String getSchoolName() {
		return schoolName;
	}

	/**
	 * @param schoolName the schoolName to set
	 */
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	private String announcementId ;//公告ID
	private String schoolCode ;//学校Code
	private String schoolName;//学校名称


	private static final long serialVersionUID = -7870187208447818764L;
}
