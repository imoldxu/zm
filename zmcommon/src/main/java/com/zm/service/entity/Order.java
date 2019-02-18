package com.zm.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.type.JdbcType;

import tk.mybatis.mapper.annotation.ColumnType;

@Table(name="t_order")
public class Order {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private Long id;//订单id
	
	@Column(name = "sn")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String sn;//订单sn编号
	
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(name = "uid")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer uid;//用户id
	

	public static final int CODE_TRANSACTION = 1;
	public static final int CODE_RESERVE = 2;
	
	@Column(name = "code")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer code;//支付类型
	
	@Column(name = "payway")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String payway;//支付渠道
	
	@Column(name = "paysn")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String paysn;//支付渠道订单号
	
	@Column(name = "amount")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer amount;//金额
	
	@Column(name = "createtime")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date createtime;//创建时间
	
	@Column(name = "invalidtime")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date invalidtime;//创建时间
	
	public Date getInvalidtime() {
		return invalidtime;
	}

	public void setInvalidtime(Date invalidtime) {
		this.invalidtime = invalidtime;
	}

	@Column(name = "payovertime")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date payovertime;//支付成功事件
	
	@Column(name = "info")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String info;//交易内容
	
	public static final int STATE_NEW = 1;
	public static final int STATE_PAYING = 2;
	public static final int STATE_COMPELTE = 3;
	public static final int STATE_INVALID = 4;
	
	@Column(name = "state")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer state;//状态

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

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getPayway() {
		return payway;
	}

	public void setPayway(String payway) {
		this.payway = payway;
	}

	public String getPaysn() {
		return paysn;
	}

	public void setPaysn(String paysn) {
		this.paysn = paysn;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getPayovertime() {
		return payovertime;
	}

	public void setPayovertime(Date payovertime) {
		this.payovertime = payovertime;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
}
