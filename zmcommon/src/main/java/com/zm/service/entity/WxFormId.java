package com.zm.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import org.apache.ibatis.type.JdbcType;

import tk.mybatis.mapper.annotation.ColumnType;

@Table(name="t_wx_form_id")
public class WxFormId {

	@Column(name = "uid")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer uid;
	
	@Column(name = "formid")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String formid;
	
	@Column(name = "expiretime")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date expiretime;

	public Date getExpiretime() {
		return expiretime;
	}

	public void setExpiretime(Date expiretime) {
		this.expiretime = expiretime;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getFormid() {
		return formid;
	}

	public void setFormid(String formid) {
		this.formid = formid;
	}

	
}
