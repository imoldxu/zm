package com.zm.service.entity;

import java.util.Date;

public class SimpleHouse {

	private Long id;
	
	private String name;
	
	private String imglist;
	
	private Date createtime;
	
	private int area;
	
	private Integer parlor;//厅
	
	private Integer room;
	
	private Integer toilet;
	
	private int amount;
	
	private int distance;
	
	private Integer uid;//发布者id
	
	private String phone;//发布者phone
	
	private String tags;

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

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getImglist() {
		return imglist;
	}

	public void setImglist(String imglist) {
		this.imglist = imglist;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public Integer getToilet() {
		return toilet;
	}

	public void setToilet(Integer toilet) {
		this.toilet = toilet;
	}
	
}
