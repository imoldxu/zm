package com.zm.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.type.JdbcType;

import com.alibaba.fastjson.JSONArray;
import com.zm.service.typehandler.JSONArrayHandler;

import tk.mybatis.mapper.annotation.ColumnType;

@Table(name="t_condition")
public class Condition {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private Long id;

	@Column(name = "uid")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer uid;
	
	@Column(name = "type")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer type;
	
	@Column(name = "min")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer min;
	
	@Column(name = "max")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer max;

	@Column(name = "tip")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer tip;
	
	@Column(name = "room")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer room;

	@Column(name = "place")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String place;
	
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	@Column(name = "longitude")
	@ColumnType(jdbcType = JdbcType.DOUBLE)
	private Double longitude;
	
	@Column(name = "latitude")
	@ColumnType(jdbcType = JdbcType.DOUBLE)
	private Double latitude;
	
	@Column(name = "indate")
	@ColumnType(jdbcType = JdbcType.DATE)
	private Date indate;
	
	@Column(name = "createtime")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date createtime;
	
	@Column(name = "isnotify")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer isnotify;
	
	@Column(name = "tags")
	@ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler=JSONArrayHandler.class)
	private JSONArray tags;

	public static final int STATE_VALID = 1;
	public static final int STATE_INVALID = 0;
	
	@Column(name = "state")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer state;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}
	
	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Condition(){
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}



	public Integer getTip() {
		return tip;
	}

	public void setTip(Integer tip) {
		this.tip = tip;
	}

	public Integer getRoom() {
		return room;
	}

	public void setRoom(Integer room) {
		this.room = room;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Date getIndate() {
		return indate;
	}

	public void setIndate(Date indate) {
		this.indate = indate;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getIsnotify() {
		return isnotify;
	}

	public void setIsnotify(Integer isnotify) {
		this.isnotify = isnotify;
	}

	public JSONArray getTags() {
		return tags;
	}

	public void setTags(JSONArray tags) {
		this.tags = tags;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
}
