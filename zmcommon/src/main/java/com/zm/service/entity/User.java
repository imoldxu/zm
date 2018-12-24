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

@Table(name="t_user")
public class User {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer id;
	
	@Column(name = "nick")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String nick;
	

	@Column(name = "avatar")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String avatar;
	
	
	@Column(name = "name")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String name;
	
	@Column(name = "sex")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer sex;
	
	public static final int TYPE_IDCARD = 1;
	public static final int TYPE_JG = 2;
	
	@Column(name = "idcardtype")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer idcardtype;
	
	@Column(name = "idcardnum")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String idcardnum;
	
	public Integer getIdcardtype() {
		return idcardtype;
	}

	public void setIdcardtype(Integer idcardtype) {
		this.idcardtype = idcardtype;
	}

	public String getIdcardnum() {
		return idcardnum;
	}

	public void setIdcardnum(String idcardnum) {
		this.idcardnum = idcardnum;
	}

	@Column(name = "birthday")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String birthday;
	
	
	@Column(name = "phone")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String phone;
	

	@Column(name = "password")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String password;
	
	@Column(name = "wxunionid")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String wxunionid;
	
	@Column(name = "createtime")
	@ColumnType(jdbcType = JdbcType.TIME)
	private Date createtime;
	
	@Column(name = "lastlogintime")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date lastlogintime;

	@Transient
    private int age;//年龄，年龄是实时计算的
    
    @Transient
    private int subscribe;//是否关注微信公众号
	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(int subscribe) {
		this.subscribe = subscribe;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}
	
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getWxunionid() {
		return wxunionid;
	}

	public void setWxunionid(String wxunionid) {
		this.wxunionid = wxunionid;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(Date lastlogintime) {
		this.lastlogintime = lastlogintime;
	}
	
	
}
