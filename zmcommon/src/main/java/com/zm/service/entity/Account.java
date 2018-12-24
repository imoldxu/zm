package com.zm.service.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.type.JdbcType;

import tk.mybatis.mapper.annotation.ColumnType;

@Table(name="t_account")
public class Account {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer id;
	
	@Column(name = "uid")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer uid;

	@Column(name = "coin")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer coin;

	@Column(name = "cash")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer cash;

	@Column(name = "lockedcash")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer lockedcash;
	
	public Integer getLockedcash() {
		return lockedcash;
	}

	public void setLockedcash(Integer lockedcash) {
		this.lockedcash = lockedcash;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getCoin() {
		return coin;
	}

	public void setCoin(Integer coin) {
		this.coin = coin;
	}

	public Integer getCash() {
		return cash;
	}

	public void setCash(Integer cash) {
		this.cash = cash;
	}
	
	
}
