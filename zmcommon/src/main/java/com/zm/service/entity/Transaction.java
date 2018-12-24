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

@Table(name="t_transaction")
public class Transaction {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private Long id;
	
	@Column(name = "houseid")
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private Long houseid;
	
	@Column(name = "iuid")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer iuid;
	
	@Column(name = "ruid")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer ruid;
	
	public static final int STATE_NEW = 1;
	public static final int STATE_R_CONFIRM = 2;
	public static final int STATE_I_CONFIRM = 3;
	public static final int STATE_COMPLETE = 4;
	public static final int STATE_CANCEL = 5;
	public static final int STATE_INVALID = 6;
	
	@Column(name = "state")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer state;
	

	@Column(name = "createtime")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date createtime;

	@Column(name = "amount")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer amount;//交易金额
	
	@Transient
	private Order order;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}


	public Integer getAmount() {
		return amount;
	}


	public void setAmount(Integer amount) {
		this.amount = amount;
	}


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


	public Integer getIuid() {
		return iuid;
	}


	public void setIuid(Integer iuid) {
		this.iuid = iuid;
	}


	public Integer getRuid() {
		return ruid;
	}


	public void setRuid(Integer ruid) {
		this.ruid = ruid;
	}


	public Integer getState() {
		return state;
	}


	public void setState(Integer state) {
		this.state = state;
	}


	public Date getCreatetime() {
		return createtime;
	}


	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
}
