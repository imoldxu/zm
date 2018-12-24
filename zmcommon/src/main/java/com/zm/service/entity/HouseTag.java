package com.zm.service.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.type.JdbcType;

import tk.mybatis.mapper.annotation.ColumnType;

@Table(name="t_housetag")
public class HouseTag {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private Long id;
	
	@Column(name = "houseid")
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private Long houseid;
	
	@Column(name = "tagid")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer tagid;
	
	@Column(name = "tagname")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String tagname;
	
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


	public Integer getTagid() {
		return tagid;
	}


	public void setTagid(Integer tagid) {
		this.tagid = tagid;
	}


	public String getTagname() {
		return tagname;
	}


	public void setTagname(String tagname) {
		this.tagname = tagname;
	}


	public Integer getGood() {
		return good;
	}


	public void setGood(Integer good) {
		this.good = good;
	}


	public Integer getBad() {
		return bad;
	}


	public void setBad(Integer bad) {
		this.bad = bad;
	}


	@Column(name = "good")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer good;
	

	@Column(name = "bad")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer bad;
}
