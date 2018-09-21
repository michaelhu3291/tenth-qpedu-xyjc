package data.platform.entity ;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import data.framework.data.LongDateSerializer;
import data.framework.support.AbstractEntity;
import data.platform.entity.EntityPlatformFile;

/**
 * 公告基本信息实体类。
 * 
 * @author wangcheng
 * 
 */
public class EntityAnnouncementBasicInfo extends AbstractEntity
{

	/**
	 * 获取公告标题。
	 * 
	 * @return 公告标题
	 */
	public String getTitle() 
	{
		return title;
	}

	/**
	 * 设置公告标题。
	 * 
	 * @param title 公告标题
	 */
	public void setTitle(String title) 
	{
		this.title = title;
	}

	/**
	 * 获取公告内容。
	 * 
	 * @return 公告内容
	 */
	public String getContext() 
	{
		return context;
	}

	/**
	 * 设置公告内容。
	 * 
	 * @param context 公告内容
	 */
	public void setContext(String context) 
	{
		this.context = context;
	}

	/**
	 * 获取发布人员姓名
	 * 
	 * @return 发布人员姓名
	 */
	public String getPublishPerson() 
	{
		return publishPerson;
	}

	/**
	 * 设置发布人员姓名
	 * 
	 * @param publishPerson 发布人员姓名
	 */
	public void setPublishPerson(String publishPerson) 
	{
		this.publishPerson = publishPerson;
	}

	/**
	 * 获取发布日期
	 * 
	 * @return 发布日期
	 */
	@JsonSerialize( using = LongDateSerializer.class )
	public Date getPublishDate() 
	{
		return publishDate;
	}

	/**
	 * 设置发布日期
	 * 
	 * @param publishDate 发布日期
	 */
	public void setPublishDate(Date publishDate) 
	{
		this.publishDate = publishDate;
	}

	/**
	 * 获取排序（置顶）
	 * 
	 * @return 排序（置顶）
	 */
	public int getSeqNums() 
	{
		return seqNums;
	}

	/**
	 * 设置排序（置顶）
	 * 
	 * @param seqNum 排序（置顶）
	 */
	public void setSeqNums(int seqNums) 
	{
		this.seqNums = seqNums;
	}

	/**
	 * 获取公告状态
	 * 
	 * @return 公告状态
	 */
	public int getStatus() 
	{
		return status;
	}

	/**
	 * 设置公告状态
	 * 
	 * @param status 公告状态
	 */
	public void setStatus(int status) 
	{
		this.status = status;
	}


	/**
	 * 获取有效开始时间
	 * 
	 * @return 有效开始时间
	 */
	public Date getEnableTimeStart() {
		return enableTimeStart;
	}

	/**
	 * 设置有效开始时间
	 * 
	 * @param enableTimeStart 有效开始时间
	 */
	public void setEnableTimeStart(Date enableTimeStart) {
		this.enableTimeStart = enableTimeStart;
	}

	/**
	 * 获取有效结束时间
	 * 
	 * @return 有效结束时间
	 */
	public Date getEnableTimeEnd() {
		return EnableTimeEnd;
	}

	/**
	 * 设置有效结束时间
	 * 
	 * @param enableTimeEnd 有效结束时间
	 */
	public void setEnableTimeEnd(Date enableTimeEnd) {
		EnableTimeEnd = enableTimeEnd;
	}

	/**
	 * 获取附件文件信息。
	 * 
	 * @return 附件文件信息
	 */
	public List<EntityPlatformFile> getFiles() 
	{
		return files;
	}

	/**
	 * 设置附件文件信息。
	 * 
	 * @param files 附件文件信息
	 */
	public void setFiles(List<EntityPlatformFile> files)
	{
		this.files = files;
	}

	/**
	 * 获取发布人的Id。
	 * 
	 * @return 发布人的Id。
	 */
	public String getPublishPersonId() {
		return publishPersonId;
	}

	/**
	 * 设置发布人的Id。
	 * 
	 * @param publishPersonId 发布人的Id。
	 * 
	 */
	public void setPublishPersonId(String publishPersonId) {
		this.publishPersonId = publishPersonId;
	}

	/**
	 * 获取创建日期
	 * 
	 * @return 创建日期
	 */
	public Date getCreateDate() {
		return createDate;
	}




	/**
	 * 设置创建日期
	 * 
	 * @param createTime 创建日期
	 * 
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;

	}

	/**
	 * 获取创建人id
	 * @return
	 */
	public String getCreatePerson() {
		return createPerson;
	}

	/**
	 * 设置 创建人id
	 * @param createPerson
	 */
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	/**
	 * 获取更新时间
	 * @return
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置更新时间
	 * @param updateTime
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/*
	 * 获取更新人id
	 * 
	 */
	public String getUpdatePerson() {
		return updatePerson;
	}

	/**
	 * 设置更新人id
	 * @param updatePerson
	 */
	public void setUpdatePerson(String updatePerson) {
		this.updatePerson = updatePerson;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setPartakePersonIDs(String partakePersonIDs) {
		this.partakePersonIDs = partakePersonIDs;
	}

	/**
	 * 获取公告参与人员
	 * 
	 * @return 公告参与人员
	 */
	public String getPartakePersons()
	{
		return partakePersons;
	}

	/**
	 * 设置公告参与人员
	 * 
	 * @param partakePersons 公告参与人员
	 * 
	 */
	public void setPartakePersons(String partakePersons)
	{
		this.partakePersons = partakePersons;
	}

	/**
	 * 获取公告参与人员ID
	 * 
	 * @return 公告参与人员ID
	 */
	public String getPartakePersonIDs() 
	{
		return partakePersonIDs;
	}

	/**
	 * 设置公告参与人员ID
	 * 
	 * @param partakePersonIDs 公告参与人员ID
	 * 
	 */
	public void setPartakePersonIDS(String partakePersonIDs) 
	{
		this.partakePersonIDs = partakePersonIDs;
	}

	/**
	 * 获取组名称
	 * 
	 * @return 组名称
	 */
	public String getTeamNames() 
	{
		return teamNames;
	}

	/**
	 * 设置组名称
	 * 
	 * @param teamNames 组名称
	 * 
	 */
	public void setTeamNames(String teamNames) 
	{
		this.teamNames = teamNames;
	}

	/**
	 * 获取院系年级编码
	 * @return
	 */
	public String getTeamCodes() {
		return teamCodes;
	}
	public String getAnnouncementType() {
		return announcementType;
	}

	public void setAnnouncementType(String announcementType) {
		this.announcementType = announcementType;
	}

	/**
	 * 设置院系年级编码
	 * @param teamCodes
	 */
	public void setTeamCodes(String teamCodes) {
		this.teamCodes = teamCodes;
	}

	/**
	 * 获取年级
	 */
	public String getGrade() {
		return grade;
	}
	/**
	 * 设置年级
	 * @param grade
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}

	/**
	 * 设置年级
	 * @param grade
	 */
	public String getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}

	public String getPublishDept() {
		return publishDept;
	}

	public void setPublishDept(String publishDept) {
		this.publishDept = publishDept;
	}

	@Override
	public String toString() {
		return "EntityAnnouncementBasicInfo [announcementType=" + announcementType + ", title=" + title + ", context="
				+ context + ", status=" + status + ", seqNums=" + seqNums + ", publishPerson=" + publishPerson
				+ ", publishPersonId=" + publishPersonId + ", publishDate=" + publishDate + ", enableTimeStart="
				+ enableTimeStart + ", EnableTimeEnd=" + EnableTimeEnd + ", createDate=" + createDate
				+ ", createPerson=" + createPerson + ", updateTime=" + updateTime + ", updatePerson=" + updatePerson
				+ ", partakePersons=" + partakePersons + ", partakePersonIDs=" + partakePersonIDs + ", teamNames="
				+ teamNames + ", teamCodes=" + teamCodes + ", schoolType=" + schoolType + ", grade=" + grade
				+ ", publishDept=" + publishDept + ", files=" + files + "]";
	}
	private String announcementType;// 公告类型
	private String title ;// 公告标题
	private String context ;// 公告内容
	private int status ;// 公告状态
	private int seqNums ;// 排序
	private String publishPerson ;// 发布人员姓名
	private String publishPersonId ;// 发布人的ID
	private Date publishDate ;// 发布日期
	private Date enableTimeStart ;// 有效开始时间
	private Date EnableTimeEnd ;// 有效结束时间
	private Date createDate ;// 创建时间
	private String createPerson ;//创建人
	private Date updateTime;//最后更新时间
	private String updatePerson;//最后更新人员
	private String partakePersons ;// 公告参与人员
	private String partakePersonIDs ;// 公告参与人员ID
	private String teamNames ;
	private String teamCodes;
	private String schoolType;//学校类型
	private String grade;//年级
	private String publishDept;//发布单位
	private List<EntityPlatformFile> files=new ArrayList<EntityPlatformFile>();
	private static final long serialVersionUID = -7870187208447818764L;
}