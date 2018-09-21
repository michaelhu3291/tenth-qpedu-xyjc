package data.platform.entity;

import java.util.Date;

import data.framework.support.AbstractEntity;

//用户实体类bua
public class EntityPlatformUserBua extends AbstractEntity{
	String org_id;
	String user_uid;
	String user_name;
	String user_full_name;
	String user_email;
	String user_email_public;
	String user_passwd;
	String user_type;
	String user_sex;
	String user_locked;
	String create_user_name;
	Date create_time;
	String last_update_user_name;
	Date last_update_time;
	Date validity_date;
	String related_account;
	String user_sequence;
	String identifier;
	String position_no;
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
	public String getUser_uid() {
		return user_uid;
	}
	public void setUser_uid(String user_uid) {
		this.user_uid = user_uid;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_full_name() {
		return user_full_name;
	}
	public void setUser_full_name(String user_full_name) {
		this.user_full_name = user_full_name;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getUser_email_public() {
		return user_email_public;
	}
	public void setUser_email_public(String user_email_public) {
		this.user_email_public = user_email_public;
	}
	public String getUser_passwd() {
		return user_passwd;
	}
	public void setUser_passwd(String user_passwd) {
		this.user_passwd = user_passwd;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getUser_sex() {
		return user_sex;
	}
	public void setUser_sex(String user_sex) {
		this.user_sex = user_sex;
	}
	public String getUser_locked() {
		return user_locked;
	}
	public void setUser_locked(String user_locked) {
		this.user_locked = user_locked;
	}
	public String getCreate_user_name() {
		return create_user_name;
	}
	public void setCreate_user_name(String create_user_name) {
		this.create_user_name = create_user_name;
	}
	public String getLast_update_user_name() {
		return last_update_user_name;
	}
	public void setLast_update_user_name(String last_update_user_name) {
		this.last_update_user_name = last_update_user_name;
	}
	public String getRelated_account() {
		return related_account;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getLast_update_time() {
		return last_update_time;
	}
	public void setLast_update_time(Date last_update_time) {
		this.last_update_time = last_update_time;
	}
	public Date getValidity_date() {
		return validity_date;
	}
	public void setValidity_date(Date validity_date) {
		this.validity_date = validity_date;
	}
	public void setRelated_account(String related_account) {
		this.related_account = related_account;
	}
	public String getUser_sequence() {
		return user_sequence;
	}
	public void setUser_sequence(String user_sequence) {
		this.user_sequence = user_sequence;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getPosition_no() {
		return position_no;
	}
	public void setPosition_no(String position_no) {
		this.position_no = position_no;
	}
}
