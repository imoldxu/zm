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

@Table(name="t_reserve")
public class Reserve {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private Long id;
	
	@Column(name = "houseid")
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private Long houseid;
	
	@Transient
	private SimpleHouse house;
	
	@Transient
	private Order order;
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public SimpleHouse getHouse() {
		return house;
	}

	public void setHouse(SimpleHouse house) {
		this.house = house;
	}

	@Column(name = "time")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date time;
	
	@Column(name = "iuid")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer iuid; //栋数
	
	@Column(name = "ruid")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer ruid;//单元数

	public static final int USER_STATE_UNCONFIRM = 1;
	public static final int USER_STATE_CONFIRM = 2;
	
	@Column(name = "istate")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer istate;//房号

	@Column(name = "rstate")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer rstate;//房号
	
	public static final int STATE_NEW = 1;
	public static final int STATE_VALID = 2;
	public static final int STATE_COMPLETE = 3;
	public static final int STATE_CANCEL = 4;
	
	@Column(name = "state")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer state;//预约状态
	
	@Column(name = "iscomment")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer iscomment;//是否完成评论，需在看房后才可评论
	
	@Column(name = "createtime")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date createtime;//是否完成评论，需在看房后才可评论
	
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
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

	public Integer getIstate() {
		return istate;
	}

	public void setIstate(Integer istate) {
		this.istate = istate;
	}

	public Integer getRstate() {
		return rstate;
	}

	public void setRstate(Integer rstate) {
		this.rstate = rstate;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getIscomment() {
		return iscomment;
	}

	public void setIscomment(Integer iscomment) {
		this.iscomment = iscomment;
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


}
