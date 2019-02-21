package com.zm.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.ibatis.type.JdbcType;

import tk.mybatis.mapper.annotation.ColumnType;

@Table(name="t_comment")
public class HComment {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private Long id;
	
	@Column(name = "houseid")
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private Long houseid;
	
	@Column(name = "uid")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer uid;
	
	@Transient
	private String userNick;
	
	@Transient
	private String userAvatar;
	
	
	public String getUserNick() {
		return userNick;
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}


	public String getUserAvatar() {
		return userAvatar;
	}


	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}


	@Column(name = "content")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String content;
	
	@Column(name = "imglist")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String imglist;
	

	@Column(name = "createtime")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date createtime;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getHouseid() {
		return houseid;
	}


	public void setHouseid(Long houseid) {
		this.houseid = houseid;
	}


	public Integer getUid() {
		return uid;
	}


	public void setUid(Integer uid) {
		this.uid = uid;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getImglist() {
		return imglist;
	}


	public void setImglist(String imglist) {
		this.imglist = imglist;
	}


	public Date getCreatetime() {
		return createtime;
	}


	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
}
