package com.zm.service.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.ibatis.type.JdbcType;

import tk.mybatis.mapper.annotation.ColumnType;

@Table(name="t_house")
public class House {


	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private Long id;
	
	@Column(name = "uid")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer uid;//期
	
	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	@Column(name = "name")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String name;
	
	@Column(name = "building")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer building; //栋数
	
	@Column(name = "unit")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer unit;//单元数
	
	@Column(name = "num")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String num;//房号
	
	@Column(name = "issue")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer issue;//期
	
	@Column(name = "parlor")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer parlor;//厅
	
	@Column(name = "room")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer room;
	
	@Column(name = "area")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer area;
	
	@Column(name = "toilet")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer toilet;
	
	
	public Integer getToilet() {
		return toilet;
	}

	public void setToilet(Integer toilet) {
		this.toilet = toilet;
	}

	public static final int TYPE_HOLD_RENT = 1;
	public static final int TYPE_PART_RENT = 2;
	
	@Column(name = "type")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer type;
	
	@Column(name = "amount")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer amount;
	
	@Column(name = "tip")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer tip;
	
	@Column(name = "tenantlimit")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String tenantlimit;
	
	public String getTenantlimit() {
		return tenantlimit;
	}

	public void setTenantlimit(String tenantlimit) {
		this.tenantlimit = tenantlimit;
	}

	@Column(name = "outdate")
	@ColumnType(jdbcType = JdbcType.DATE)
	private Date outdate;
	
	@Column(name = "createtime")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date createtime;
	
	@Column(name = "reservedate")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String reservedate;
	
	@Column(name = "reservetime")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String reservetime;
	
	@Column(name = "imglist")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String imglist;

	public String getImglist() {
		return imglist;
	}

	public void setImglist(String imglist) {
		this.imglist = imglist;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	@Column(name = "des")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String des;
	
	
	@Column(name = "installation")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String installation;//设施

	public static final int STATE_VALID = 1;	//发布即有效
	public static final int STATE_LOCKED = 2;   //有人锁定
	public static final int STATE_COMPLETE = 3; //完成交易
	public static final int STATE_INVALID = 0;  //删除，则失效
	
	@Column(name = "state")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer state;//状态
	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Transient
	private List<HouseTag> tagList;//tag
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getTip() {
		return tip;
	}

	public void setTip(Integer tip) {
		this.tip = tip;
	}

	public Date getOutdate() {
		return outdate;
	}

	public void setOutdate(Date outdate) {
		this.outdate = outdate;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getReservedate() {
		return reservedate;
	}

	public void setReservedate(String reservedate) {
		this.reservedate = reservedate;
	}

	public String getReservetime() {
		return reservetime;
	}

	public void setReservetime(String reservetime) {
		this.reservetime = reservetime;
	}

	public String getInstallation() {
		return installation;
	}

	public void setInstallation(String installation) {
		this.installation = installation;
	}

	public List<HouseTag> getTagList() {
		return tagList;
	}

	public void setTagList(List<HouseTag> tagList) {
		this.tagList = tagList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getBuilding() {
		return building;
	}

	public void setBuilding(Integer building) {
		this.building = building;
	}

	public Integer getUnit() {
		return unit;
	}

	public void setUnit(Integer unit) {
		this.unit = unit;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public Integer getIssue() {
		return issue;
	}

	public void setIssue(Integer issue) {
		this.issue = issue;
	}

	public Integer getParlor() {
		return parlor;
	}

	public void setParlor(Integer parlor) {
		this.parlor = parlor;
	}

	public Integer getRoom() {
		return room;
	}

	public void setRoom(Integer room) {
		this.room = room;
	}

	public Integer getArea() {
		return area;
	}

	public void setArea(Integer area) {
		this.area = area;
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

	@Column(name = "longitude")
	@ColumnType(jdbcType = JdbcType.DOUBLE)
	private Double longitude;
	
	@Column(name = "latitude")
	@ColumnType(jdbcType = JdbcType.DOUBLE)
	private Double latitude;

	public House(){
	}
}
