package com.zm.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.type.JdbcType;

import tk.mybatis.mapper.annotation.ColumnType;

@Table(name="t_message")
public class Message {


	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private Long id;
	
	@Column(name = "uid")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer uid;//消息的用户

	public static final int TYPE_NORMAL_NOTICE = 1;//普通消息，不推送到微信
	public static final int TYPE_RESERVE_RENDER_NOTICE = 2;//预约求租者的消息
	public static final int TYPE_RESERVE_ISSUER_NOTICE = 3;//预约发布者的者消息
	public static final int TYPE_TRASACTION_RENDER_NOTICE = 4;
	public static final int TYPE_TRASACTION_ISSUER_NOTICE = 5;
	
	
	@Column(name = "type")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer type;

	@Column(name = "content")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String content;
	
	@Column(name = "createtime")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date createtime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}


}
